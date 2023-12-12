###
# Image pour la compilation de theses batch indexation
FROM maven:3-eclipse-temurin-11 as build-image
WORKDIR /build/
# Installation et configuration de la locale FR
RUN apt update && DEBIAN_FRONTEND=noninteractive apt -y install locales
RUN sed -i '/fr_FR.UTF-8/s/^# //g' /etc/locale.gen && \
    locale-gen
ENV LANG fr_FR.UTF-8
ENV LANGUAGE fr_FR:fr
ENV LC_ALL fr_FR.UTF-8
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
FROM rockylinux:8 as batch-image
WORKDIR /scripts/
# systeme pour les crontab
# cronie: remplacant de crond qui support le CTRL+C dans docker (sans ce système c'est compliqué de stopper le conteneur)
# gettext: pour avoir envsubst qui permet de gérer le template tasks.tmpl
RUN dnf install -y cronie gettext && \
    crond -V && rm -rf /etc/cron.*/*
COPY ./docker/batch/tasks.tmpl /etc/cron.d/tasks.tmpl
# Le JAR et le script pour le batch d'insertion des thèses et personnes dans ES
RUN dnf install -y java-11-openjdk
COPY docker/batch/theses-batch-indexation.sh /scripts/theses-batch-indexation.sh
RUN chmod +x /scripts/theses-batch-indexation.sh
COPY docker/batch/theses-batch-suppression.sh /scripts/theses-batch-suppression.sh
RUN chmod +x /scripts/theses-batch-suppression.sh
COPY docker/batch/thematiques-batch-indexation.sh /scripts/thematiques-batch-indexation.sh
RUN chmod +x /scripts/thematiques-batch-indexation.sh
COPY docker/batch/thematiques-batch-suppression.sh /scripts/thematiques-batch-suppression.sh
RUN chmod +x /scripts/thematiques-batch-suppression.sh
COPY --from=build-image /build/target/*.jar /scripts/theses-batch-indexation.jar
# Les fichiers de définition d'index et oaisets :
COPY ./src/main/resources/indexs/personnes.json   /scripts/src/main/resources/indexs/personnes.json
COPY ./src/main/resources/indexs/recherche_personnes.json   /scripts/src/main/resources/indexs/recherche_personnes.json
COPY ./src/main/resources/indexs/thematiques.json   /scripts/src/main/resources/indexs/thematiques.json
COPY ./src/main/resources/indexs/theses.json   /scripts/src/main/resources/indexs/theses.json
COPY ./src/main/resources/oaisets/listeOaiSets.xml   /scripts/src/main/resources/oaisets/listeOaiSets.xml
# Les locales fr_FR
RUN dnf install langpacks-fr glibc-all-langpacks -y
ENV LANG fr_FR.UTF-8
ENV LANGUAGE fr_FR:fr
ENV LC_ALL fr_FR.UTF-8
# Lancement de l'entrypoint et du démon crond
COPY ./docker/batch/docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["crond", "-n"]
