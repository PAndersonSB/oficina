(ns projeto.data.database
  (:require [datomic.api :as d]))

;bin/console -p 8080 dev datomic:dev://localhost:4334/
;bin/transactor -Ddatomic.printConnectionInfo=true config/dev-transactor-template.properties

(def db-uri "datomic:dev://localhost:4334/hello")
;(d/delete-database db-uri)
(d/create-database db-uri)
(def conn (d/connect db-uri))

; entidade :atributo                 valor
; 15       :project/title            projeto inovar     ID_TX     operacao
; 15       :project/people-quantity  30                 ID_TX     operacao
; 17       :project/title            projeto ESG        ID_TX     operacao
; 17       :project/people-quantity  10                 ID_TX     operacao
(def project-schema [{:db/ident       :project/title
                      :db/valueType   :db.type/string
                      :db/cardinality :db.cardinality/one
                      :db/doc         "The title of the project"}

                     {:db/ident       :project/people-quantity
                      :db/valueType   :db.type/long
                      :db/cardinality :db.cardinality/one
                      :db/doc         "The quantity of people involved in the project"}
                     ])

@(d/transact conn project-schema)


(defn all-projects []
  (let [query '[:find ?p ?title ?people-quantity
                :keys id titulo quantidade-pessoa
                :where
                [?p :project/title ?title]
                [?p :project/people-quantity ?people-quantity]]
        ]
    (d/q query (d/db conn))
    )
  )

(defn create-project [project]
  (let [new {:project/title           (:title project)
             :project/people-quantity (:quantity project)
             }
        ]
    @(d/transact conn [new])
    ))

(defn get-project-by-id [id]
  (let [query '[:find ?id ?title ?people-quantity
                :keys id titulo quantidade-pessoa
                :in $ ?id
                :where
                [?id :project/title ?title]
                [?id :project/people-quantity ?people-quantity]]
        ]
    (d/q query (d/db conn) id)))

(defn delete-project-by-id [id]
  @(d/transact conn [[:db/retractEntity id]])
  )

(defn update-project [id new-data]
  @(d/transact conn [[:db/add id :project/people-quantity (:quantity new-data)]
                     [:db/add id :project/title (:title new-data)]
                     ])
  )
