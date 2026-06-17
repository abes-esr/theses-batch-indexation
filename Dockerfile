###
# Image pour la compilation de theses batch indexation
FROM maven:3-eclipse-temurin-11 AS build-image
WORKDIR /build/

# On lance la compilation
# si on a un .m2 local on peut décommenter la ligne suivante pour
# éviter à maven de retélécharger toutes les dépendances
#COPY ./.m2/    /root/.m2/
COPY ./pom.xml /build/pom.xml
COPY ./src/   /build/src/

RUN mvn --batch-mode \
    -Dmaven.test.skip=false \
    -Duser.timezone=Europe/Paris \
    -Duser.language=fr \
    package

###
# Image pour le module batch d'insertion des thèses et personnes dans ES
# Remarque: l'image openjdk:11 n'est pas utilisée car nous avons besoin de cronie
#           qui n'est que disponible sous centos/rockylinux.
FROM rockylinux:8 AS batch-image
WORKDIR /scripts/
# systeme pour les crontab
# cronie: remplacant de crond qui support le CTRL+C dans docker (sans ce système c'est compliqué de stopper le conteneur)
# gettext: pour avoir envsubst qui permet de gérer le template tasks.tmpl
# Installation manuelle de pgrep suite à sa disparition dans l'image
RUN yum install -y procps
RUN dnf install -y cronie gettext && \
    crond -V && rm -rf /etc/cron.*/*
COPY ./docker/batch/*.tmpl /etc/cron.d/
# Le JAR et le script pour le batch d'insertion des thèses et personnes dans ES
RUN dnf install -y java-11-openjdk
COPY docker/batch/*-batch-*.sh /scripts/
RUN chmod +x /scripts/*-batch-*.sh

COPY --from=build-image /build/target/*.jar /scripts/theses-batch-indexation.jar
# Les fichiers de définition d'index et oaisets :
COPY ./src/main/resources/indexs/*.json /scripts/src/main/resources/indexs/
COPY ./src/main/resources/oaisets/listeOaiSets.xml   /scripts/src/main/resources/oaisets/listeOaiSets.xml
COPY ./src/main/resources/application.properties   /scripts/src/main/resources/application.properties
# Les locales fr_FR
RUN dnf install langpacks-fr glibc-all-langpacks -y
ENV LANG fr_FR.UTF-8
ENV LANGUAGE fr_FR:fr
ENV LC_ALL fr_FR.UTF-8
# Lancement de l'entrypoint et du démon crond
COPY ./docker/batch/docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

# Téléchargement d'une version fixe de l'agent OpenTelemetry pour la reproductibilité
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.3.0/opentelemetry-javaagent.jar /app/opentelemetry.jar

ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["crond", "-n"]
