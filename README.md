# theses-batch-indexation

Programme qui permet l'indexation en masse des thèses et de leurs métadonnées.

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

