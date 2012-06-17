(ns data-schemer.predicates)

(defn of-type [klass]
  (partial instance? klass))

(defn between [lower upper]
  (fn [x] (and (> x lower) (< x upper))))
