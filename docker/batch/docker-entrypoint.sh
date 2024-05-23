#!/bin/bash

if [ "$THESES_BATCH_INDEXATION_THESES_CHOIX_JOB" = "theses" ]; then
  export THESES_BATCH_INDEXATION_THESES_CRON=${THESES_BATCH_INDEXATION_THESES_CRON:='* * * * *'}
  export THESES_BATCH_SUPPRESSION_THESES_CRON=${THESES_BATCH_SUPPRESSION_THESES_CRON:='* * * * *'}
  export THESES_BATCH_INDEXATION_THEMATIQUES_CRON=${THESES_BATCH_INDEXATION_THEMATIQUES_CRON:='* * * * *'}
  export THESES_BATCH_SUPPRESSION_THEMATIQUES_CRON=${THESES_BATCH_SUPPRESSION_THEMATIQUES_CRON:='* * * * *'}
  export THESES_BATCH_INDEXATION_AT_STARTUP=${THESES_BATCH_INDEXATION_AT_STARTUP:='1'}

  # Réglage de /etc/environment pour que les crontab s'exécutent avec les bonnes variables d'env
  echo "$(env)
  LANG=en_US.UTF-8" > /etc/environment

  # Charge la crontab depuis le template
  envsubst < /etc/cron.d/tasks-theses.tmpl > /etc/cron.d/tasks
  echo "-> Installation des crontab :"
  cat /etc/cron.d/tasks
  crontab /etc/cron.d/tasks

  # Force le démarrage du batch au démarrage du conteneur
  if [ "$THESES_BATCH_INDEXATION_AT_STARTUP" = "1" ]; then
    echo "-> Lancement de theses-batch-indexation.sh au démarrage du conteneur"
    /scripts/theses-batch-indexation.sh
    echo "-> Lancement de theses-batch-suppression.sh au démarrage du conteneur"
    /scripts/theses-batch-suppression.sh
    echo "-> Lancement de thematiques-batch-indexation.sh au démarrage du conteneur"
    /scripts/thematiques-batch-indexation.sh
    echo "-> Lancement de thematiques-batch-suppression.sh au démarrage du conteneur"
    /scripts/thematiques-batch-suppression.sh
  fi

  # execute CMD (crond)
  exec "$@"

fi

if [ "$THESES_BATCH_INDEXATION_THESES_CHOIX_JOB" = "personnes" ]; then
  export THESES_BATCH_INDEXATION_PERSONNES_CRON=${THESES_BATCH_INDEXATION_PERSONNES_CRON:='* * * * *'}
  export THESES_BATCH_SUPPRESSION_PERSONNES_CRON=${THESES_BATCH_SUPPRESSION_PERSONNES_CRON:='* * * * *'}
  export THESES_BATCH_INDEXATION_AT_STARTUP=${THESES_BATCH_INDEXATION_AT_STARTUP:='1'}

  # Réglage de /etc/environment pour que les crontab s'exécutent avec les bonnes variables d'env
  echo "$(env)
  LANG=en_US.UTF-8" > /etc/environment

  # Charge la crontab depuis le template
  envsubst < /etc/cron.d/tasks-personnes.tmpl > /etc/cron.d/tasks
  echo "-> Installation des crontab :"
  cat /etc/cron.d/tasks
  crontab /etc/cron.d/tasks

  # Force le démarrage du batch au démarrage du conteneur
  if [ "$THESES_BATCH_INDEXATION_AT_STARTUP" = "1" ]; then
    echo "-> Lancement de personnes-batch-indexation.sh au démarrage du conteneur"
    /scripts/personnes-batch-indexation.sh
    echo "-> Lancement de personnes-batch-suppression.sh au démarrage du conteneur"
    /scripts/personnes-batch-suppression.sh
  fi

  # execute CMD (crond)
  exec "$@"

fi


if [ "$THESES_BATCH_INDEXATION_THESES_CHOIX_JOB" = "recherche_personnes" ]; then
  export THESES_BATCH_INDEXATION_RECHERCHE_PERSONNES_CRON=${THESES_BATCH_INDEXATION_RECHERCHE_PERSONNES_CRON:='* * * * *'}
  export THESES_BATCH_SUPPRESSION_RECHERCHE_PERSONNES_CRON=${THESES_BATCH_SUPPRESSION_RECHERCHE_PERSONNES_CRON:='* * * * *'}
  export THESES_BATCH_INDEXATION_AT_STARTUP=${THESES_BATCH_INDEXATION_AT_STARTUP:='1'}

  # Réglage de /etc/environment pour que les crontab s'exécutent avec les bonnes variables d'env
  echo "$(env)
  LANG=en_US.UTF-8" > /etc/environment

  # Charge la crontab depuis le template
  envsubst < /etc/cron.d/tasks-recherche-personnes.tmpl > /etc/cron.d/tasks
  echo "-> Installation des crontab :"
  cat /etc/cron.d/tasks
  crontab /etc/cron.d/tasks

  # Force le démarrage du batch au démarrage du conteneur
  if [ "$THESES_BATCH_INDEXATION_AT_STARTUP" = "1" ]; then
    echo "-> Lancement de recherche-personnes-batch-indexation.sh au démarrage du conteneur"
    /scripts/recherche-personnes-batch-indexation.sh
    echo "-> Lancement de recherche-personnes-batch-suppression.sh au démarrage du conteneur"
    /scripts/recherche-personnes-batch-suppression.sh
  fi

  # execute CMD (crond)
  exec "$@"

fi

