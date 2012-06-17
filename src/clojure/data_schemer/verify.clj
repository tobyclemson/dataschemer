(ns data-schemer.verify
  (:use data-schemer.entities))

(defn verify [target schema]
  (let [predicates (schema @defined-entities)]
    (every? (fn [p] (p target))) predicates))