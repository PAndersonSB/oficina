(ns projeto.handlers
  (:require [projeto.data.database :as database]))

(defn create-project [{project :body-params}]
  (database/create-project project)
  {:status 201})

(defn get-projects [_]
  {:status 200
   :body   (database/all-projects)})


(defn get-project-by-id
  [{{id :id} :path-params}]
  {:status 200
   :body   (database/get-project-by-id (Long/parseLong id))})

(defn delete-project-by-id
  [{{id :id} :path-params}]
  (database/delete-project-by-id (Long/parseLong id))
  {:status 200}
  )

(defn update-project [{project :body-params}]
  (database/update-project project)
  {:status 200}
  )