(ns data-schemer.core
  (:use data-schemer.entities
        data-schemer.declarations))

(defn- characteristic-level? [forms]
  (let [token (first forms)]
    (and (symbol? token)
         (not (= token 'has)))))

(defn- characteristics-level? [forms]
  (and (list? (first forms))
       (characteristic-level? (first forms))))

(defn- child-entities-level? [forms]
  (and (list? (first forms))
       (= (first (first forms)) 'has)))

(defn- entity-level? [forms]
  (let [token (first forms)]
    (keyword? token)))

(defn- entities-level? [forms]
  (and (list? (first forms))
       (entity-level? (first forms))))

(defmacro => [& forms]
  (cond
   (entity-level? forms) `(entity-with ~(first forms) (=> ~@(rest forms)))
   (characteristic-level? forms) `(declaration-with [(quote ~forms)] {})
   (child-entities-level? forms)
   `(merge-declarations
     (declaration-with [] (=> ~@(rest (first forms))))
     (=> ~@(rest forms)))
   (entities-level? forms)
   `(merge-entities
     (=> ~@(first forms))
     (=> ~@(rest forms)))
   (characteristics-level? forms)
   `(merge-declarations
     (=> ~@(first forms))
     (=> ~@(rest forms)))))
