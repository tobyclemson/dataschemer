(ns data-schemer.declarations-test
  (:use clojure.test
        data-schemer.declarations))

(deftest empty-declaration-has-no-characteristics
  (is (= (characteristics-from empty-declaration) [])))

(deftest empty-declaration-has-no-child-entities
  (is (= (child-entities-from empty-declaration)
         {})))

(deftest declaration-with-returns-declaration-containing-supplied-characteristics
  (let [characteristics ['(some-characteristic)]
        child-entities {:child empty-declaration}
        declaration (declaration-with characteristics child-entities)]
    (is (= (characteristics-from declaration)
           characteristics))))

(deftest declaration-with-returns-declaration-containing-supplied-child-entities
  (let [characteristics ['(some-characteristic)]
        child-entities {:child empty-declaration}
        declaration (declaration-with characteristics child-entities)]
    (is (= (child-entities-from declaration)
           child-entities))))

(deftest set-characteristics-replaces-characteristics-in-declaration
  (let [old-characteristics ['(some-characteristic)]
        new-characteristics ['(some-other-characteristic)]
        child-entities {:child empty-declaration}
        declaration (declaration-with old-characteristics child-entities)]
    (is (= (characteristics-from (set-characteristics declaration new-characteristics))
           new-characteristics))))

(deftest add-characteristic-pushes-characteristic-into-declaration
  (is (= (characteristics-from (add-characteristic empty-declaration '(characteristic)))
         [(quote (characteristic))])))

(deftest set-child-entities-replaces-child-entities-in-declaration
  (let [characteristics ['(some-characteristic)]
        old-child-entities {:old-child empty-declaration}
        new-child-entities {:new-child (declaration-with ['(child-characteristic)] {})}
        declaration (declaration-with characteristics old-child-entities)]
    (is (= (child-entities-from
            (set-child-entities declaration new-child-entities))
           new-child-entities))))

(deftest add-child-entity-merges-child-entity-into-declaration
  (let [existing-child-entities {:first-child empty-declaration}
        child-entity-to-add {:second-child empty-declaration}
        expected-child-entities {:first-child empty-declaration
                                     :second-child empty-declaration}
        declaration (declaration-with [] existing-child-entities)]
    (is (= (child-entities-from
            (add-child-entity declaration child-entity-to-add))
           expected-child-entities))))

(deftest merge-declarations-should-return-new-declaration-with-ordered-characteristics-of-both
  (let [first-declaration (declaration-with ['(characteristic1) '(characteristic2)] {})
        second-declaration (declaration-with ['(characteristic3) '(characteristic4)] {})
        expected-declaration (declaration-with ['(characteristic1)
                                                '(characteristic2)
                                                '(characteristic3)
                                                '(characteristic4)] {})]
    (is (= (merge-declarations first-declaration second-declaration)
           expected-declaration))))

(deftest merge-declarations-should-return-new-declaration-with-child-entities-of-both
  (let [first-declaration (declaration-with [] {:child1 empty-declaration})
        second-declaration (declaration-with [] {:child2 empty-declaration
                                                 :child3 empty-declaration})
        expected-declaration (declaration-with [] {:child1 empty-declaration
                                                   :child2 empty-declaration
                                                   :child3 empty-declaration})]
    (is (= (merge-declarations first-declaration second-declaration)
           expected-declaration))))

(deftest merge-declarations-merges-both-characteristics-and-child-entities
  (let [first-declaration (declaration-with ['(characteristic1)] {:child1 empty-declaration})
        second-declaration (declaration-with ['(characteristic2)] {:child2 empty-declaration})
        expected-declaration (declaration-with ['(characteristic1)
                                                '(characteristic2)]
                                               {:child1 empty-declaration
                                                :child2 empty-declaration})]
    (is (= (merge-declarations first-declaration second-declaration)
           expected-declaration))))