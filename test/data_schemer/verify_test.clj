(ns data-schemer.verify-test
  (:use clojure.test
        data-schemer.verify))

#_(deftest valid-tiny-type-verifies-true
  (let [target (Age. (Integer. 10))
        schema :age-type]
    (is (= (verify target schema) true))))

#_(deftest invalid-tiny-type-verifies-false
  (let [target (Name. "Toby")
        schema :age-type]
    (is (= (verify target schema) false))))

#_(deftest valid-tiny-type-with-nested-characteristics-verifies-true
  (let [target (Age. (Integer. 15))
        schema :age-in-range]
    (is (= (verify target schema) true))))