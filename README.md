# theses-batch-indexation

Programme qui permet l'indexation en masse des thèses et de leurs métadonnées.
(travail en cours)

A la date du 25/10/2022 ce dépôt contient un exemple d'indexation de 11 thèses. Cet exemple est amené à être enrichi pour tester les capacité d'elasticsearch.
A terme il sera remplacé par un batch d'indexation qui partira des 500 000 TEF pour les indexer dans un index d'elasticsearch.


Ce code indexe 11 thèses exemple dans elasticsearch, il lance le batch ``images/theses-sample-load.sh`` au démarrage du conteneur et va faire 3 choses :
- supprimer l'index ``theses-sample``
- créer l'index ``theses-sample`` avec son mapping elasticsearch
- charger 11 thèses exemple dans l'index ``theses-sample`` 

09/02/2023 : ajout du batch d'indexation pour indexer en masse.

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

job.chunk=100
job.throttle=6
# Limite de la clause where, mettre 0 pour tout prendre
job.where-limite=2000
~~~~

