(ns data-schemer.core
  (:use data-schemer.declarations))

(defmacro => [& forms]
  (cond
   (list? (first forms))
   `(merge-declarations
           (add-characteristic empty-declaration (=> ~@(first forms)))
           (=> ~@(rest forms)))
   (symbol? (first forms)) `(quote ~forms)))