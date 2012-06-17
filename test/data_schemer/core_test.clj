(ns data-schemer.core-test
  (:use clojure.test
        data-schemer.core)
  (:import [dataschemer Age
                        Name]))

(ns data-schemer.core-test
  (:use clojure.test
        data-schemer.core
        data-schemer.declarations))

(deftest single-characteristic-gets-quoted
  (is (= (=> characteristic)
         '(characteristic))))

(deftest many-characteristics-form-declaration
  (is (= (=> (characteristic1) (characteristic2))
         (declaration-with ['(characteristic1) '(characteristic2)] {}))))

#_(deftest single-child-declaration-gets-expanded
  (is (= (=> (:age (characteristic)))
         (declaration-with [] {:age (declaration-with ['(characteristic)] {})}))))