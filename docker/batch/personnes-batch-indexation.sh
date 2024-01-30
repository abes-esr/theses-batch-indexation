#!/bin/bash

LANG=fr_FR.UTF-8
if [[ $(pgrep -cf "theses-batch-indexation.jar --spring.batch.job.names=ajoutPersonnesDansES") < 1 ]];
then
    java -jar /scripts/theses-batch-indexation.jar --spring.batch.job.names=ajoutPersonnesDansES
fi