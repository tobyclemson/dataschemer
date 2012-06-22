(ns data-schemer.document
  (:use [data-schemer.core]
        [data-schemer.entities]
        [clojure.string :only [join]]))

(defn document-entity)
(defn document-entity-name)
(defn document-characteristics [characteristics]
  (join
    "\n"
    (map
      (fn [characteristic]
        (str "- " (eval characteristic)))
      characteristics)))

(defn of-type [type]
  (str "of " (.. type (getSimpleName) (toLowerCase)) " type"))

(defn never [& keywords]
  (str "never " (join ", " (map name (butlast keywords))) " or " (name (first (take-last 1 keywords)))))

(defn document [schema]
  (let [entity (select-keys @defined-entities [schema])]
    (println entity)))
