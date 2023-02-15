# theses-batch-indexation

Programme qui permet l'indexation en masse des thèses et de leurs métadonnées.

Pour choisir le Job qu'on veut lancer : Ajouter dans la configuration (Override configuration properties):
 ~~~
 spring.batch.job.names=nom_du_job
 ~~~

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

table.name=DOCUMENT_TEST
index.name=
~~~~
Pour créer l'index dans ES:
~~~~
PUT https://serveur_url:port/nom_de_lindex
~~~~
ajouter l'authorization en basic (prendre login/password dans application.properties)

en body / json :
~~~~
{
  "settings": {
    "analysis": {
        "filter": {
          "french_stop": {
            "type": "stop",
            "stopwords": "_french_"
          },
          "french_stemmer": {
            "type": "stemmer",
            "language": "light_french"
          }
        },
        "analyzer": {
          "francais": {
            "type": "custom",
            "tokenizer": "standard",
            "filter": [
              "lowercase",
              "french_stop",
              "french_stemmer"
            ]
          }
        },
        "normalizer": {
        "tri": {
          "type": "custom",
          "filter": ["lowercase", "asciifolding"]
        }
      }
      }
    },
  "mappings": {
    "properties": {
      "cas": {
        "type": "keyword"
      },
      "accessible": {
        "type": "keyword"
      },
      "source": {
        "type": "keyword"
      },
      "status": {
        "type": "keyword"
      },
      "codeEtab": {
        "type": "keyword"
      },
      "nnt": {
        "type": "keyword"
      },
      "dateSoutenance": {
      "type": "date"
    },
    "dateFinEmbargo": {
      "type": "date"
    },
      "titrePrincipal": {
        "type": "text",
        "fields": {
          "exact": {
            "type": "text",
            "analyzer": "standard"
          }
        }
      },
      "titres": {
        "properties": {
              "fr" : {
                "type": "text",
                "analyzer": "francais"
                ,
                "fields": {
                  "exact": {
                    "type": "text",
                    "analyzer": "standard"
                  }
                }
          },
              "en" : {
                "type": "text",
                "analyzer": "english"
                ,
                "fields": {
                  "exact": {
                    "type": "text",
                    "analyzer": "standard"
                  }
                }
          },
              "es" : {
                "type": "text",
                "analyzer": "spanish"
                ,
                "fields": {
                  "exact": {
                    "type": "text",
                    "analyzer": "standard"
                  }
                }
          },
              "de" : {
                "type": "text",
                "analyzer": "german"
                ,
                "fields": {
                  "exact": {
                    "type": "text",
                    "analyzer": "standard"
                  }
                }
          },
              "it" : {
                "type": "text",
                "analyzer": "italian"
                ,
                "fields": {
                  "exact": {
                    "type": "text",
                    "analyzer": "standard"
                  }
                }
          }
          }
    },
      "resumes": {
        "properties": {
          "fr" : {
            "type": "text",
            "analyzer": "francais"
          },
          "en" : {
            "type": "text",
            "analyzer": "english"
          },
          "es" : {
            "type": "text",
            "analyzer": "spanish"
          },
          "de" : {
            "type": "text",
            "analyzer": "german"
          },
          "it" : {
            "type": "text",
            "analyzer": "italian"
          }
          }
    },
      "discipline": {
        "type": "text",
        "analyzer": "francais",
            "fields": {
              "tri": {
                "type": "keyword",
                "normalizer": "tri"
              }
            },
            "copy_to": "suggestion"
      },
      "langues": {
        "type": "keyword"
      },
      "auteurs" : {
        "type": "nested",
        "properties": {
        "ppn": {
          "type": "keyword"
        },
        "nom": {
          "type": "text"
        },
        "prenom": {
          "type": "text"
        }
        }
      },
      "auteursNP": {
        "type": "text",
        "fields": {
              "tri": {
                "type": "keyword",
                "normalizer": "tri"
              }
            }
      },
      "directeurs" : {
        "type": "nested",
        "properties": {
        "ppn": {
          "type": "keyword"
        },
        "nom": {
          "type": "text"
        },
        "prenom": {
          "type": "text"
        }
        }
      },
      "directeursNP": {
        "type": "text"
      },
      "presidentJury" : {
        "type": "nested",
        "properties": {
        "ppn": {
          "type": "keyword"
        },
        "nom": {
          "type": "text"
        },
        "prenom": {
          "type": "text"
        }
        }
      },
      "presidentJuryNP": {
        "type": "text"
      },
      "membresJury" : {
        "type": "nested",
        "properties": {
        "ppn": {
          "type": "keyword"
        },
        "nom": {
          "type": "text"
        },
        "prenom": {
          "type": "text"
        }
        }
      },
      "membresJuryNP": {
        "type": "text"
      },
      "rapporteurs" : {
        "type": "nested",
        "properties": {
        "ppn": {
          "type": "keyword"
        },
        "nom": {
          "type": "text"
        },
        "prenom": {
          "type": "text"
        }
        }
      },
      "rapporteursNP": {
        "type": "text"
      },
      "etabSoutenance": {
        "type": "nested",
        "properties": {
        "ppn": {
          "type": "keyword"
        },
        "nom": {
          "type": "text"
        }
        }
      },
      "etabSoutenanceN": {
        "type": "text"
      },
      "etabsCotutelle": {
        "type": "nested",
        "properties": {
        "ppn": {
          "type": "keyword"
        },
        "nom": {
          "type": "text"
        }
        }
      },
      "etabsCotutelleN": {
        "type": "text"
      },
      "ecolesDoctorales": {
        "type": "nested",
        "properties": {
        "ppn": {
          "type": "keyword"
        },
        "nom": {
          "type": "text"
        }
        }
      },
      "ecolesDoctoralesN": {
        "type": "text"
      },
    "partenairesRecherche" : {
      "type": "nested",
      "properties": {
        "ppn": {
          "type": "keyword"
        },
        "nom": {
          "type": "text"
        },
        "type": {
          "type": "text"
        }
        }
      },
      "partenairesRechercheN": {
        "type": "text"
      },
    "sujetsRameau": {
      "type": "text",
      "analyzer": "francais",
      "copy_to": "suggestion"
    },
    "sujetsFR": {
      "type": "text",
      "analyzer": "francais",
      "copy_to": "suggestion"
    },
    "sujetsEN": {
      "type": "text",
      "analyzer": "standard",
      "copy_to": "suggestion"
    },
    "oaiSets": {
        "type": "keyword"
      },
      "theseTravaux": {
        "type": "keyword"
      },
    "suggestion": {
        "type": "completion",
        "analyzer": "francais",
        "max_input_length" : 200
      }
    }
  }
}


~~~~

