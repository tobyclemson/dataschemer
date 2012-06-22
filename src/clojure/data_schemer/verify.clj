(ns data-schemer.verify
  (:use data-schemer.entities
        data-schemer.declarations))

(defn- verify-against-declaration [target declaration]
  (and (every? (fn [characteristic]
                 ((eval characteristic) target))
               (characteristics-from declaration))
       (let [child-entities (child-entities-from declaration)]
         (every? (fn [child-entity-name]
                   (let [field-for-child-entity
                         (.. target (getClass) (getDeclaredField (name child-entity-name)))]
                     (.. field-for-child-entity (setAccessible true))
                     (verify-against-declaration
                      (. field-for-child-entity (get target))
                      (child-entity-name child-entities))))
                 (entity-names-from child-entities)))))

(defn verify [target schema]
  (let [declaration (schema @defined-entities)]
    (verify-against-declaration target declaration)))