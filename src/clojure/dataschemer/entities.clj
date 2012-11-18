(ns dataschemer.entities
  (:use dataschemer.declarations))

(def defined-entities (atom {}))

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

(defn store-entity [entity]
  (swap! defined-entities
         assoc
         (entity-name-from entity)
         (declaration-from entity)))

(defn store-entities [entities]
  (doall
   (map #(store-entity (select-keys entities [%]))
        (entity-names-from entities))))

(defmacro with-no-defined-entities [& body]
  `(with-redefs [defined-entities (atom {})]
     ~@body))
