#!/bin/bash

# Paramètres par défaut du conteneur
export THESES_BATCH_INDEXATION_CRON=${THESES_BATCH_INDEXATION_CRON:='0 * * * *'}
export THESES_BATCH_INDEXATION_AT_STARTUP=${THESES_BATCH_INDEXATION_AT_STARTUP:='1'}

# Réglage de /etc/environment pour que les crontab s'exécutent avec les bonnes variables d'env
echo "$(env)
LANG=en_US.UTF-8" > /etc/environment

# Charge la crontab depuis le template
envsubst < /etc/cron.d/tasks.tmpl > /etc/cron.d/tasks
echo "-> Installation des crontab :"
cat /etc/cron.d/tasks
crontab /etc/cron.d/tasks

# Force le démarrage du batch au démarrage du conteneur
if [ "$THESES_BATCH_INDEXATION_AT_STARTUP" = "1" ]; then
  echo "-> Lancement de theses-indexation.sh au démarrage du conteneur"
  /scripts/theses-indexation.sh
fi

# execute CMD (crond)
exec "$@"
