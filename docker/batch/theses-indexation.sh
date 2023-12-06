#!/bin/bash

LANG=fr_FR.UTF-8
if [[ $(pgrep -cf "theses-indexation.jar --spring.batch.job.names=indexationThesesDansES") < 1 ]];
then
    java -jar /scripts/theses-indexation.jar --spring.batch.job.names=indexationThesesDansES
fi