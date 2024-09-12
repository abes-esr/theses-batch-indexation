# theses-batch-indexation

Programme qui permet l'indexation en masse et à l'unité des thèses et de leurs métadonnées.

Pour choisir le Job qu'on veut lancer : Ajouter dans la configuration (Override configuration properties):
(indexationPersonnesDansES, indexationRecherchePersonnesDansES, indexationThesesDansES ou indexationThematiquesDansES)
 ~~~
 spring.batch.job.names=nom_du_job
 ~~~

Le batch supprime l'index et le recrée si initialiseIndexTheses=true ou initialiseIndexPersonnes=true (reste à ajouter Thematiques) avec le fichier qui est dans resources/indexs :
- si spring.batch.job.names=indexationThesesDansES  => on utilise le fichier theses.json
- si spring.batch.job.names=indexationPersonnesDansES  => on utilise le fichier personnes.json
- si spring.batch.job.names=indexationRecherchePersonnesDansES  => on utilise le fichier recherche_personnes.json
- si spring.batch.job.names=indexationThematiquesDansES  => on utilise le fichier thematiques.json


Puis il est lancé via un crontab toutes les minutes et traite les lignes des tables indexation_es et suppression_es.

Pour le faire fonctionner :

- il faut compiler avec au moins jdk-11.0.2
- il faut ajouter un application.properties à placer dans src/main/resources: 

~~~~
#spring.batch.initialize-schema=always

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
initialiseIndexTheses=false
initialiseIndexPersonnes=false
~~~~

Dans la base de données, les lignes à indexer sont gérées via : 

~~~~
create table indexation_es_these (iddoc number not null, nnt nvarchar2(20) null, numsujet nvarchar2(20) null);
create table suppression_es_these (iddoc number not null, nnt nvarchar2(20) null, numsujet nvarchar2(20) null);
create table indexation_es_personne (iddoc number not null, nnt nvarchar2(20) null, numsujet nvarchar2(20) null);
create table suppression_es_personne (iddoc number not null, nnt nvarchar2(20) null, numsujet nvarchar2(20) null);
create table indexation_es_recherche_personne (iddoc number not null, nnt nvarchar2(20) null, numsujet nvarchar2(20) null);
create table suppression_es_recherche_personne (iddoc number not null, nnt nvarchar2(20) null, numsujet nvarchar2(20) null);
create table indexation_es_thematique (iddoc number not null, nnt nvarchar2(20) null, numsujet nvarchar2(20) null);
create table suppression_es_thematique (iddoc number not null, nnt nvarchar2(20) null, numsujet nvarchar2(20) null);
~~~~

Les tables précédentes sont remplies via les déclencheurs suivants : 

~~~~
create or replace TRIGGER SUPPRESSION_ES_TRIGGER
AFTER DELETE
   ON document
   FOR EACH ROW

BEGIN
    INSERT INTO suppression_es_these (iddoc, nnt, numsujet) VALUES (:old.iddoc, :old.nnt, :old.numsujet);
    INSERT INTO suppression_es_personne (iddoc, nnt, numsujet) VALUES (:old.iddoc, :old.nnt, :old.numsujet);
    INSERT INTO suppression_es_recherche_personne (iddoc, nnt, numsujet) VALUES (:old.iddoc, :old.nnt, :old.numsujet);
    INSERT INTO suppression_es_thematique (iddoc, nnt, numsujet) VALUES (:old.iddoc, :old.nnt, :old.numsujet);
END;

/

CREATE OR REPLACE TRIGGER INDEXATION_ES_TRIGGER
AFTER INSERT OR UPDATE
   ON document
   FOR EACH ROW

BEGIN
    INSERT INTO indexation_es_these (iddoc, nnt, numsujet) VALUES (:new.iddoc, :new.nnt, :new.numsujet);
    INSERT INTO indexation_es_personne (iddoc, nnt, numsujet) VALUES (:new.iddoc, :new.nnt, :new.numsujet);
    INSERT INTO indexation_es_recherche_personne (iddoc, nnt, numsujet) VALUES (:new.iddoc, :new.nnt, :new.numsujet);
    INSERT INTO indexation_es_thematique (iddoc, nnt, numsujet) VALUES (:new.iddoc, :new.nnt, :new.numsujet);
END;
~~~~

# theses-batch-indexation

Programme qui permet l'indexation en masse et à l'unité des thèses et de leurs métadonnées.

Pour choisir le Job qu'on veut lancer : Ajouter dans la configuration (Override configuration properties):
(indexationPersonnesDansES, indexationRecherchePersonnesDansES, indexationThesesDansES ou indexationThematiquesDansES)
 ~~~
 spring.batch.job.names=nom_du_job
 ~~~

Le batch supprime l'index et le recrée si initialiseIndexTheses=true ou initialiseIndexPersonnes=true avec le fichier qui est dans resources/indexs :
- si spring.batch.job.names=indexationThesesDansES  => on utilise le fichier theses.json
- si spring.batch.job.names=indexationPersonnesDansES  => on utilise le fichier personnes.json
- si spring.batch.job.names=indexationRecherchePersonnesDansES  => on utilise le fichier recherche_personnes.json
- si spring.batch.job.names=indexationThematiquesDansES  => on utilise le fichier thematiques.json


Puis il est lancé via un crontab toutes les minutes et traite les lignes des tables indexation_es et suppression_es.

Pour le faire fonctionner :

- il faut compiler avec au moins jdk-11.0.2
- il faut ajouter un application.properties à placer dans src/main/resources:

~~~~
#spring.batch.initialize-schema=always

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
initialiseIndexTheses=false
initialiseIndexPersonnes=false
~~~~

Dans la base de données, les lignes à indexer sont gérées via :

~~~~
create table indexation_es_these (iddoc number not null, nnt nvarchar2(20) null, numsujet nvarchar2(20) null);
create table suppression_es_these (iddoc number not null, nnt nvarchar2(20) null, numsujet nvarchar2(20) null);
create table indexation_es_personne (iddoc number not null, nnt nvarchar2(20) null, numsujet nvarchar2(20) null);
create table suppression_es_personne (iddoc number not null, nnt nvarchar2(20) null, numsujet nvarchar2(20) null);
create table indexation_es_recherche_personne (iddoc number not null, nnt nvarchar2(20) null, numsujet nvarchar2(20) null);
create table suppression_es_recherche_personne (iddoc number not null, nnt nvarchar2(20) null, numsujet nvarchar2(20) null);
create table indexation_es_thematique (iddoc number not null, nnt nvarchar2(20) null, numsujet nvarchar2(20) null);
create table suppression_es_thematique (iddoc number not null, nnt nvarchar2(20) null, numsujet nvarchar2(20) null);
~~~~

Les tables précédentes sont remplies via les déclencheurs suivants :

~~~~
create or replace TRIGGER SUPPRESSION_ES_TRIGGER
AFTER DELETE
   ON document
   FOR EACH ROW

BEGIN
    INSERT INTO suppression_es_these (iddoc, nnt, numsujet) VALUES (:old.iddoc, :old.nnt, :old.numsujet);
    INSERT INTO suppression_es_personne (iddoc, nnt, numsujet) VALUES (:old.iddoc, :old.nnt, :old.numsujet);
    INSERT INTO suppression_es_recherche_personne (iddoc, nnt, numsujet) VALUES (:old.iddoc, :old.nnt, :old.numsujet);
    INSERT INTO suppression_es_thematique (iddoc, nnt, numsujet) VALUES (:old.iddoc, :old.nnt, :old.numsujet);
END;

/

CREATE OR REPLACE TRIGGER INDEXATION_ES_TRIGGER
AFTER INSERT OR UPDATE
   ON document
   FOR EACH ROW

BEGIN
    INSERT INTO indexation_es_these (iddoc, nnt, numsujet) VALUES (:new.iddoc, :new.nnt, :new.numsujet);
    INSERT INTO indexation_es_personne (iddoc, nnt, numsujet) VALUES (:new.iddoc, :new.nnt, :new.numsujet);
    INSERT INTO indexation_es_recherche_personne (iddoc, nnt, numsujet) VALUES (:new.iddoc, :new.nnt, :new.numsujet);
    INSERT INTO indexation_es_thematique (iddoc, nnt, numsujet) VALUES (:new.iddoc, :new.nnt, :new.numsujet);
END;
~~~~

**_NOTE:_** Les tables Spring sont créés dans une base H2.

Pour relancer une indexation totale, on peut :
- remplir la table d'indexation depuis sql developer par exemple :
~~~~
insert into indexation_es_these (select iddoc, nnt, numsujet from document);commit;
~~~~

- lancer le batch avec les options suivantes, par exemple depuis un container de theses-batch-indexation :
~~~~
 java -Xmx5120m -jar /scripts/theses-batch-indexation.jar --spring.batch.job.names=indexationThesesDansES --initialiseIndexTheses=true
~~~~

Pour créer un nouvel index sans perturber le fonctionnement de theses.fr, on peut : 
- choisir un nouveau nom d'index via la variable THESES_INDEX_NAME_THESES_BATCH du .ENV dans la partie paramètrage de theses-batch-indexation (par exemple theses2)
- arreter (sudo docker compose down theses-batch-indexation-theses) et relancer (sudo docker compose up -d) le container pour qu'il prenne en compte le changement de nom d'index
- relancer le batch via la commande : java -Xmx5120m -jar /scripts/theses-batch-indexation.jar --spring.batch.job.names=indexationThesesDansES --initialiseIndexTheses=true pour qu'il crée le nouvel index (pendant ce temps, les mises à jour sont reportées dans le nouvel index et non plus dans celui visible dans theses.fr)
- lorsque l'index est plein, basculer l'alias dans dev tools de kibana sur le nouvel index via la commande :
~~~~
POST /_aliases
{
  "actions": [
    {
      "remove": {
        "index": "theses2",
        "alias": "theses_alias"
      }
    },
    {
      "add": {
        "index": "theses3",
        "alias": "theses_alias"
      }
    }
  ]
}
~~~~
