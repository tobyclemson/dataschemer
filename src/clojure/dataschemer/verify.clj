(ns dataschemer.verify
  (:use dataschemer.predicates
        dataschemer.entities
        dataschemer.declarations))

(defn- verify-against-declaration [target declaration ns-symbol]
  (and (every? (fn [characteristic]
                 ((binding [*ns* (find-ns ns-symbol)]
                    (eval characteristic)) target))
               (characteristics-from declaration))
       (let [child-entities (child-entities-from declaration)]
         (every? (fn [child-entity-name]
                   (let [field-for-child-entity
                         (.. target (getClass) (getDeclaredField (name child-entity-name)))]
                     (.. field-for-child-entity (setAccessible true))
                     (verify-against-declaration
                      (. field-for-child-entity (get target))
                      (child-entity-name child-entities)
                      ns-symbol)))
                 (entity-names-from child-entities)))))

(defn verify [target schema ns-symbol]
  (let [declaration (schema @defined-entities)]
    (verify-against-declaration target declaration ns-symbol)))