# theses-batch-indexation

Programme qui permet l'indexation en masse et à l'unité des thèses et de leurs métadonnées.

Pour choisir le Job qu'on veut lancer : Ajouter dans la configuration (Override configuration properties):
(indexationPersonnesDansES, indexationRecherchePersonnesDansES, indexationThesesDansES ou indexationThematiquesDansES)
 ~~~
 spring.batch.job.names=nom_du_job
 ~~~

Le batch supprime l'index et le recrée si initialiseIndex=true avec le fichier qui est dans resources/indexs :
- si typeIndex=theses  => on utilise le fichier theses.json
- si typeIndex=personnes  => on utilise le fichier personnes.json
- si typeIndex=thematiques  => on utilise le fichier thematiques.json
- si typeIndex=recherche_personnes  => on utilise le fichier recherche_personnes.json

Puis il est lancé via un crontab toutes les minutes et traite les lignes des tables indexation_es et suppression_es.

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

table.name=DOCUMENT_TEST
table.personne.name=personne_cache

# Utilisés pour les lots de test
table.name=document
index.name=theses

oaiSets.path=src/main/resources/listeOaiSets.xml

# Crée ou recrée l'index si true
initialiseIndex=false
~~~~

Dans la base de données, les lignes à indexer sont gérées via : 

~~~~
create table indexation_es (iddoc number not null, nnt nvarchar2(20) null, numsujet nvarchar2(20) null);
create table suppression_es (iddoc number not null, nnt nvarchar2(20) null, numsujet nvarchar2(20) null);
~~~~

Les tables précédentes sont remplies via les déclencheurs suivants : 

~~~~
CREATE OR REPLACE TRIGGER SUPPRESSION_ES_TRIGGER
AFTER DELETE
   ON document
   FOR EACH ROW

BEGIN
    INSERT INTO suppression_es (iddoc, nnt, numsujet) VALUES (:new.iddoc, :new.nnt, :new.numsujet);
END;

/

CREATE OR REPLACE TRIGGER INDEXATION_ES_TRIGGER
AFTER INSERT OR UPDATE
   ON document
   FOR EACH ROW

BEGIN
    INSERT INTO indexation_es (iddoc, nnt, numsujet) VALUES (:new.iddoc, :new.nnt, :new.numsujet);
END;
~~~~
