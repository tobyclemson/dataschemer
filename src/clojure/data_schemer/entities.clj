(ns data-schemer.entities
  (:use data-schemer.declarations))

(def defined-entities (atom {}))

(defn store-entity [name declaration]
  (swap! defined-entities assoc name declaration))

(def empty-entity {})

(defn declarations-from [entities]
  (set (vals entities)))

(defn declaration-from [entity]
  (first (declarations-from entity)))

(defn declaration-for [name entities]
  (name entities))

(defn entity-names-from [entities]
  (set (keys entities)))

(defn entity-name-from [entity]
  (first (entity-names-from entity)))

(defn entity-with
  ([name declaration]
     {name declaration})
  ([name characteristics child-entities]
     (entity-with name (declaration-with characteristics child-entities))))

(defn merge-entities [first-entity second-entity]
  (merge-with merge-declarations first-entity second-entity))

(defn entities-with [name characteristics child-entities & rest]
  (let [current-entity (entity-with name characteristics child-entities)]
    (cond
     (empty? rest) current-entity
     :else (merge-entities
            current-entity
            (apply entities-with rest)))))