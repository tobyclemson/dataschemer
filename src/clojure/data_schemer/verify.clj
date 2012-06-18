(ns data-schemer.verify
  (:use data-schemer.entities
        data-schemer.declarations))

(defn verify [target schema]
  (let [declaration (schema @defined-entities)]
    (every? (fn [characteristic] ((eval characteristic) target))
            (characteristics-from declaration))))