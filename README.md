# theses-batch-indexation

Programme qui permet l'indexation en masse des thèses et de leurs métadonnées.

Pour choisir le Job qu'on veut lancer : Ajouter dans la configuration (Override configuration properties):
(indexationPersonnesDansES ou indexationThesesDansES ou indexationThematiquesDansES)
 ~~~
 spring.batch.job.names=nom_du_job
 ~~~

Le batch va supprimer l'index et le recréer avec le fichier qui est dans resources/indexs :
- si indexType=theses  => on utilise le fichier theses.json
- si indexType=personnes  => on utilise le fichier personnes.json
- si indexType=thematiques  => on utilise le fichier thematiques.json

Pour le faire fonctionner :

- il faut compiler avec au moins jdk-11.0.2
- il faut ajouter un application.properties à placer dans src/main/resources: 

~~~~
spring.batch.initialize-schema=always

# oracle
spring.datasource.driverClassName=oracle.jdbc.pool.OracleDataSource
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=
spring.datasource.hikari.minimumIdle=10
spring.datasource.hikari.maximumPoolSize=15
spring.datasource.hikari.readOnly=true

# apres ajout des dependances xdb et xmlparser, erreur : "Unable to start ServletWebServerApplicationContext due to missing ServletWebServerFactory bean."
# resolu grace a : 
# https://stackoverflow.com/questions/50231736/applicationcontextexception-unable-to-start-servletwebserverapplicationcontext
spring.main.web-application-type=none

elastic.hostname=127.0.0.1
elastic.port=9200
elastic.scheme=http

elastic.userName=
elastic.password=
elastic.protocol=https

job.chunk=100
job.throttle=6
# Limite de la clause where, mettre 0 pour tout prendre
job.where-limite=2000

index.pathTheses=src/main/resources/indexs/theses.json
index.pathPersonnes=src/main/resources/indexs/personnes.json
index.pathThematiques=src/main/resources/indexs/thematiques.json
index.pathRecherchePersonnes=src/main/resources/indexs/recherche_personnes.json

index.name=
indexType=

table.name=DOCUMENT_TEST
table.personne.name=personne_cache

oaiSets.path=src/main/resources/listeOaiSets.xml


