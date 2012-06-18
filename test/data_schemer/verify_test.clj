(ns data-schemer.verify-test
  (:import [dataschemer Age Name])
  (:use clojure.test
        data-schemer.verify
        data-schemer.entities
        data-schemer.core
        data-schemer.predicates))

(deftest valid-tiny-type-verifies-true
  (with-no-defined-entities
    (defentity (:age
                (of-type Age)))
    (let [target (Age. (Integer. 10))
          result (verify target :age)]
      (is (= result true)))))

(deftest invalid-tiny-type-verifies-false
  (with-no-defined-entities
    (defentity (:age
                (of-type Age)))
    (let [target (Name. "Toby")
          result (verify target :age)]
      (is (= result false)))))

(deftest valid-tiny-type-with-nested-characteristics-verifies-true
  (with-no-defined-entities
    (defentity (:age
                (of-type Age)
                (has (:value
                      (of-type Integer)
                      (between 10 20)))))
    (let [target (Age. (Integer. 15))
          result (verify target :age)]
      (is (= result true)))))