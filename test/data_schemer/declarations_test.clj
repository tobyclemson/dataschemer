(ns data-schemer.declarations-test
  (:use clojure.test
        data-schemer.declarations))

(deftest empty-declaration-has-no-characteristics
  (is (= (characteristics-from empty-declaration) [])))

(deftest empty-declaration-has-no-child-declarations
  (is (= (child-declarations-from empty-declaration)
         {})))

(deftest declaration-with-returns-declaration-containing-supplied-characteristics
  (let [characteristics ['(some-characteristic)]
        child-declarations {:child empty-declaration}
        declaration (declaration-with characteristics child-declarations)]
    (is (= (characteristics-from declaration)
           characteristics))))

(deftest declaration-with-returns-declaration-containing-supplied-child-declarations
  (let [characteristics ['(some-characteristic)]
        child-declarations {:child empty-declaration}
        declaration (declaration-with characteristics child-declarations)]
    (is (= (child-declarations-from declaration)
           child-declarations))))

(deftest set-characteristics-replaces-characteristics-in-declaration
  (let [old-characteristics ['(some-characteristic)]
        new-characteristics ['(some-other-characteristic)]
        child-declarations {:child empty-declaration}
        declaration (declaration-with old-characteristics child-declarations)]
    (is (= (characteristics-from (set-characteristics declaration new-characteristics))
           new-characteristics))))

(deftest add-characteristic-pushes-characteristic-into-declaration
  (is (= (characteristics-from (add-characteristic empty-declaration '(characteristic)))
         [(quote (characteristic))])))

(deftest set-child-declarations-replaces-child-declarations-in-declaration
  (let [characteristics ['(some-characteristic)]
        old-child-declarations {:old-child empty-declaration}
        new-child-declarations {:new-child (declaration-with ['(child-characteristic)] {})}
        declaration (declaration-with characteristics old-child-declarations)]
    (is (= (child-declarations-from
            (set-child-declarations declaration new-child-declarations))
           new-child-declarations))))

(deftest add-child-declaration-merges-child-declaration-into-declaration
  (let [existing-child-declarations {:first-child empty-declaration}
        child-declaration-to-add {:second-child empty-declaration}
        expected-child-declarations {:first-child empty-declaration
                                     :second-child empty-declaration}
        declaration (declaration-with [] existing-child-declarations)]
    (is (= (child-declarations-from
            (add-child-declaration declaration child-declaration-to-add))
           expected-child-declarations))))

(deftest merge-declarations-should-return-new-declaration-with-ordered-characteristics-of-both
  (let [first-declaration (declaration-with ['(characteristic1) '(characteristic2)] {})
        second-declaration (declaration-with ['(characteristic3) '(characteristic4)] {})
        expected-declaration (declaration-with ['(characteristic1)
                                                '(characteristic2)
                                                '(characteristic3)
                                                '(characteristic4)] {})]
    (is (= (merge-declarations first-declaration second-declaration)
           expected-declaration))))

(deftest merge-declarations-should-return-new-declaration-with-child-declarations-of-both
  (let [first-declaration (declaration-with [] {:child1 empty-declaration})
        second-declaration (declaration-with [] {:child2 empty-declaration
                                                 :child3 empty-declaration})
        expected-declaration (declaration-with [] {:child1 empty-declaration
                                                   :child2 empty-declaration
                                                   :child3 empty-declaration})]
    (is (= (merge-declarations first-declaration second-declaration)
           expected-declaration))))

(deftest merge-declarations-merges-both-characteristics-and-child-declarations
  (let [first-declaration (declaration-with ['(characteristic1)] {:child1 empty-declaration})
        second-declaration (declaration-with ['(characteristic2)] {:child2 empty-declaration})
        expected-declaration (declaration-with ['(characteristic1)
                                                '(characteristic2)]
                                               {:child1 empty-declaration
                                                :child2 empty-declaration})]
    (is (= (merge-declarations first-declaration second-declaration)
           expected-declaration))))