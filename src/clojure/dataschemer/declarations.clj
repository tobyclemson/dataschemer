(ns dataschemer.declarations)

(def empty-declaration [[] {}])

(defn declaration-with [characteristics children]
  [characteristics children])

(defn characteristics-from [declaration]
  (first declaration))

(defn child-entities-from [declaration]
  (second declaration))

(defn set-characteristics [declaration characteristics]
  (assoc declaration 0 characteristics))

(defn add-characteristic [declaration characteristic]
  (let [updated-characteristics (conj (characteristics-from declaration) characteristic)]
    (set-characteristics declaration updated-characteristics)))

(defn set-child-entities [declaration child-entities]
  (assoc declaration 1 child-entities))

(defn add-child-entity [declaration child-entity]
  (let [updated-child-entities (merge (child-entities-from declaration)
                                          child-entity)]
    (set-child-entities declaration updated-child-entities)))

(defn merge-declarations [first-declaration second-declaration]
  (reduce add-child-entity
          (reduce add-characteristic
                  first-declaration
                  (characteristics-from second-declaration))
          (child-entities-from second-declaration)))