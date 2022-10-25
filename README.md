# theses-batch-indexation

Programme qui permet l'indexation en masse des thèses et de leurs métadonnées.
(travail en cours)

A la date du 25/10/2022 ce dépôt contient un exemple d'indexation de 11 thèses. Cet exemple est amené à être enrichi pour tester les capacité d'elasticsearch.
A terme il sera remplacé par un batch d'indexation qui partira des 400 000 TEF pour les indexer dans un index d'elasticsearch.


Ce code indexe 11 thèses exemple dans elasticsearch, il lance le batch ``images/theses-sample-load.sh`` au démarrage du conteneur et va faire 3 choses :
- supprimer l'index ``theses-sample``
- créer l'index ``theses-sample`` avec son mapping elasticsearch
- charger 11 thèses exemple dans l'index ``theses-sample`` 
