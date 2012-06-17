(ns data-schemer.declarations)

(def empty-declaration [[] {}])

(defn declaration-with [characteristics children]
  [characteristics children])

(defn characteristics-from [declaration]
  (first declaration))

(defn child-declarations-from [declaration]
  (second declaration))

(defn set-characteristics [declaration characteristics]
  (assoc declaration 0 characteristics))

(defn add-characteristic [declaration characteristic]
  (let [updated-characteristics (conj (characteristics-from declaration) characteristic)]
    (set-characteristics declaration updated-characteristics)))

(defn set-child-declarations [declaration child-declarations]
  (assoc declaration 1 child-declarations))

(defn add-child-declaration [declaration child-declaration]
  (let [updated-child-declarations (merge (child-declarations-from declaration)
                                          child-declaration)]
    (set-child-declarations declaration updated-child-declarations)))

(defn merge-declarations [first-declaration second-declaration]
  (reduce add-child-declaration
          (reduce add-characteristic
                  first-declaration
                  (characteristics-from second-declaration))
          (child-declarations-from second-declaration)))