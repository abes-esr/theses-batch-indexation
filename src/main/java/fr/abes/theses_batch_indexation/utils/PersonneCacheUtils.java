package fr.abes.theses_batch_indexation.utils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.JsonpMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.abes.theses_batch_indexation.configuration.ElasticClient;
import fr.abes.theses_batch_indexation.database.TheseModel;
import fr.abes.theses_batch_indexation.dto.personne.IModelES;
import fr.abes.theses_batch_indexation.dto.personne.PersonneModelES;
import fr.abes.theses_batch_indexation.dto.personne.RecherchePersonneModelES;
import fr.abes.theses_batch_indexation.model.bdd.PersonnesCacheModel;
import jakarta.json.spi.JsonProvider;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@NoArgsConstructor
public class PersonneCacheUtils {
    private String nomIndex;
    private Map<String, IModelES> personneCacheListPpn;
    private List<IModelES> personneCacheListSansPpn;

    public PersonneCacheUtils(String nomIndex,
                              Map<String, IModelES> personneCacheListPpn,
                              List<IModelES> personneCacheListSansPpn) {
        this.nomIndex = nomIndex;
        this.personneCacheListPpn = personneCacheListPpn;
        this.personneCacheListSansPpn = personneCacheListSansPpn;
    }

    public static String writeJson(Object personneModelES) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(personneModelES);
    }

    public static JsonData readJson(InputStream input, ElasticsearchClient esClient) {
        JsonpMapper jsonpMapper = esClient._transport().jsonpMapper();
        JsonProvider jsonProvider = jsonpMapper.jsonProvider();

        return JsonData.from(jsonProvider.createParser(input), jsonpMapper);
    }

    public void ajoutPersonneEnMemoire(IModelES personneModelES) {
        if (personneModelES.isHas_idref()) {
            personneCacheListPpn.put(personneModelES.getPpn(), personneModelES);
        } else {
            personneCacheListSansPpn.add(personneModelES);
        }
    }

    public boolean estPresentEnMemoire(String ppn) {
        if (ppn != null && !ppn.isEmpty()) {
            return personneCacheListPpn.containsKey(ppn);
        }
        return false;
    }

    public void updatePersonneEnMemoire(IModelES personneCourante) {

        if (personneCourante instanceof PersonneModelES) {
            PersonneModelES per2 = (PersonneModelES) personneCourante;
            PersonneModelES personnePresentEnMemoire =
                    (PersonneModelES) personneCacheListPpn.get(per2.getPpn());

            personnePresentEnMemoire.getTheses_id().addAll(per2.getTheses_id());
            personnePresentEnMemoire.getTheses().addAll(per2.getTheses());
            personnePresentEnMemoire.getRoles().addAll(per2.getRoles());
        }

        if (personneCourante instanceof RecherchePersonneModelES) {
            RecherchePersonneModelES per2 = (RecherchePersonneModelES) personneCourante;
            RecherchePersonneModelES recherchePersonneEnMemoire =
                    (RecherchePersonneModelES) personneCacheListPpn.get(per2.getPpn());

            recherchePersonneEnMemoire.getTheses_id().addAll(per2.getTheses_id());
            recherchePersonneEnMemoire.getTheses_date().addAll(per2.getTheses_date());
            recherchePersonneEnMemoire.setNb_theses(recherchePersonneEnMemoire.getTheses_id().size());

            recherchePersonneEnMemoire.getRoles().addAll((per2.getRoles()));
            recherchePersonneEnMemoire.getEtablissements().addAll(per2.getEtablissements());
            recherchePersonneEnMemoire.getDisciplines().addAll(per2.getDisciplines());

            // Facettes
            recherchePersonneEnMemoire.getFacette_roles().addAll(per2.getFacette_roles());
            recherchePersonneEnMemoire.getFacette_etablissements().addAll(per2.getFacette_etablissements());
            recherchePersonneEnMemoire.getFacette_domaines().addAll(per2.getFacette_domaines());
        }
    }

    public void indexerDansES(AtomicInteger page, int chunkPersonneES, List<IModelES> listIModel, ProxyRetry proxyRetry) throws IOException {
        while (true) {
            BulkRequest.Builder br = new BulkRequest.Builder();

            int pageCourante = page.getAndIncrement();

            log.debug("Indexation de la page " + pageCourante);

            List<PersonnesCacheModel> items = new ArrayList<>();

            for (int i = ((pageCourante) * chunkPersonneES);
                 (i < ((pageCourante) * chunkPersonneES) + chunkPersonneES) && (i < listIModel.size());
                 i++) {

                IModelES personneModelES = listIModel.get(i);
                items.add(new PersonnesCacheModel(personneModelES.getPpn(), nomIndex, writeJson(personneModelES)));
            }

            if (items.size() == 0) {
                log.debug("Fin de ce thread");
                break;
            }

            for (PersonnesCacheModel personnesCacheModel : items) {

                JsonData json = readJson(
                        new ByteArrayInputStream(
                                personnesCacheModel.getPersonne().getBytes()),
                        ElasticClient.getElasticsearchClient()
                );

                br.operations(op -> op
                        .index(idx -> idx
                                .index(nomIndex.toLowerCase())
                                .id(personnesCacheModel.getPpn())
                                .document(json)
                        )
                );
            }
            BulkResponse result = proxyRetry.executerDansES(br);

            if (result.errors()) {
                log.error("Erreurs dans le bulk : ");
                for (BulkResponseItem item : result.items()) {
                    if (item.error() != null) {
                        log.error(item.id() + item.error());
                    }
                }
            }
        }
    }

    private void logSiPasAssezDePersonnesDansLaThese(TheseModel theseModel) {
        if (theseModel.getRecherchePersonnes() != null && theseModel.getRecherchePersonnes().size() < 2) {
            log.warn("Moins de personnes que prévu dans cette theses " + theseModel.getId());
        }
        if (theseModel.getPersonnes() != null && theseModel.getPersonnes().size() < 2) {
            log.warn("Moins de personnes que prévu dans cette theses " + theseModel.getId());
        }
    }

    public void ecrireEnMemoire(List<? extends TheseModel> items, AtomicInteger nombreDeTheses, AtomicInteger nombreDePersonnes, AtomicInteger nombreDePersonnesUpdated) {
        AtomicInteger nombreDePersonnesUpdatedDansCeChunk = new AtomicInteger(0);

        for (TheseModel theseModel : items) {
            nombreDeTheses.incrementAndGet();
            logSiPasAssezDePersonnesDansLaThese(theseModel);

            List<IModelES> personnes = new ArrayList<>();
            if (nomIndex.contains("recherche")) {
                personnes.addAll(theseModel.getRecherchePersonnes());
            } else {
                personnes.addAll(theseModel.getPersonnes());
            }

            for (IModelES personneModelES : personnes) {
                nombreDePersonnes.incrementAndGet();
                log.debug("ppn : " + personneModelES.getPpn());
                if (estPresentEnMemoire(personneModelES.getPpn())) {
                    log.debug("update");
                    updatePersonneEnMemoire(personneModelES);
                    nombreDePersonnesUpdated.incrementAndGet();
                    nombreDePersonnesUpdatedDansCeChunk.incrementAndGet();
                } else {
                    log.debug("ajout");
                    ajoutPersonneEnMemoire(personneModelES);
                }
            }
        }
        log.info("Nombre de thèses traitées : " + nombreDeTheses.get());
        log.info("Nombre de personnes traitées : " + nombreDePersonnes.get());
        log.info("Nombre de personnes mis à jour dans ce chunk : " + nombreDePersonnesUpdatedDansCeChunk.get());
        log.info("Nombre de personnes mis à jour en tout : " + nombreDePersonnesUpdated.get());
        log.info("Nombre de personnes dans l'index : " + (nombreDePersonnes.intValue() - nombreDePersonnesUpdated.intValue()));
    }

}
