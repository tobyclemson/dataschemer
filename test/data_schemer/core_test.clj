(ns data-schemer.core-test
  (:use clojure.test
        data-schemer.core)
  (:import [dataschemer Age
                        Name]))

#_(=> :age-type
    (of-type Age))

#_(=> :age
    (of-type Age)
    (has '(:value
           (between 0 130))))

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

#_(deftest generates-tree-with-characteristics-for-entity
  (is (= (=> (:age
              (of-type Age)
              (between 0 10)))
         {:age
          [['(of-type Age)
            '(between 0 10)]]})))

#_(deftest generates-tree-with-characteristics-for-multiple-entities
  (is (= (=> (:age
              (of-type Age))
             (:name
              (of-type Name)))
         {:age [['(of-type Age)]]
          :name [['(of-type Name)]]})))

(deftest generates-tree-with-nested-entity-and-its-characteristics
  (is (= (=> (:age
              (of-type Age)
              (has (:value
                    (between 0 10)))))
         {:age
          [['(of-type Age)]
           {:value
            [['(between 0 10)]]}]})))

#_(deftest effectively-ignores-has
  (is (= (=> (has (:value
                   (between 0 10))))
         {:value
          [['(between 0 10)]]})))

#_(deftest generates-tree-with-nested-entities-and-their-characteristics
  (is (= (=> (:person
              (of-type Person)
              (has (:age
                    (of-type Age))
                   (:name
                    (of-type Name)))))
         {:person
          ['(of-type Person)
           {:age ['(of-type Age)]}
           {:name ['(of-type Name)]}]})))

#_(deftest generates-tree-with-nested-entity-with-multiple-characteristics
  (is (= (=> (:person
              (of-type Person)
              (has (:age
                    (of-type Age)
                    (between 0 10)))))
         {:person
          ['(of-type Person)
           {:age ['(of-type Age)
                  '(between 0 10)]}]})))

#_(deftest generates-tree-with-nested-entities-with-multiple-characteristics-over-multiple-levels
  (is (= (=> (:person
              (of-type Person)
              (has (:age
                    (of-type Age)
                    (between 0 10)
                    (has (:value
                          (of-type Integer))))
                   (:name
                    (of-type Name)
                    (has (:value
                          (of-type String)))))))
         {:person
          ['(of-type Person)
           {:age ['(of-type Age)
                  '(between 0 10)
                  {:value ['(of-type Integer)]}]}
           {:name ['(of-type Name)
                   {:value ['(of-type String)]}]}]})))