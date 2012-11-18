(ns data-schemer.document
  (:use [data-schemer.core]
        [data-schemer.entities]
        [clojure.string :only [join]]))

(defn of-type [type]
  (str "of " (.. type (getSimpleName) (toLowerCase)) " type"))

(defn never [& keywords]
  (str "never "
       (join ", " (map name (butlast keywords)))
       " or "
       (name (first (take-last 1 keywords)))))

(defn document-entity)
(defn document-entity-name)
(defn document-characteristics [characteristics ns-symbol]
  (join
    "\n"
    (map
      (fn [characteristic]
        (str "- "
             (binding [*ns* (find-ns ns-symbol)]
               (eval characteristic))))
      characteristics)))

(defn document [schema ns-symbol]
  (let [entity (select-keys @defined-entities [schema])]
    (println entity)))
