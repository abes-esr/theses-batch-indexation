#!/bin/bash

LANG=fr_FR.UTF-8
if [[ $(pgrep -cf "theses-batch-indexation.jar --spring.batch.job.names=suppressionPersonnesDansES") < 1 ]];
then
    java -Xmx5120m -jar /scripts/theses-batch-indexation.jar --spring.batch.job.names=suppressionPersonnesDansES
fi