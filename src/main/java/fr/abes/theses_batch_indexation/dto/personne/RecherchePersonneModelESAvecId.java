package fr.abes.theses_batch_indexation.dto.personne;

import lombok.Getter;

import java.util.*;

@Getter
public class RecherchePersonneModelESAvecId extends RecherchePersonneModelES{
    private String _id;

    public RecherchePersonneModelESAvecId(String id, RecherchePersonneModelES recherchePersonneModelES) {
        this._id = id;
        this.ppn = recherchePersonneModelES.ppn;
        this.has_idref = recherchePersonneModelES.has_idref;
        this.nom = recherchePersonneModelES.nom;
        this.prenom = recherchePersonneModelES.prenom;
        this.nom_complet = new ArrayList<>(recherchePersonneModelES.nom_complet);
        this.completion_nom = new ArrayList<>(recherchePersonneModelES.completion_nom);
        this.theses_id = new LinkedHashSet<>(recherchePersonneModelES.theses_id);
        this.nb_theses = recherchePersonneModelES.nb_theses;
        this.theses_date = new LinkedHashSet<>(recherchePersonneModelES.theses_date);
        this.roles = new ArrayList<>(recherchePersonneModelES.roles);
        this.etablissements = new ArrayList<>(recherchePersonneModelES.etablissements);
        this.disciplines = new ArrayList<>(recherchePersonneModelES.disciplines);
        this.thematiques = new HashMap<>(recherchePersonneModelES.thematiques);
        this.facette_roles = new LinkedHashSet<>(recherchePersonneModelES.facette_roles);
        this.facette_etablissements = new LinkedHashSet<>(recherchePersonneModelES.facette_etablissements);
        this.facette_domaines = new LinkedHashSet<>(recherchePersonneModelES.facette_domaines);
    }



}
