(ns data-schemer.entities-test
  (:use clojure.test
        data-schemer.entities
        data-schemer.declarations))

(deftest empty-entity-has-no-declaration
  (is (= (declaration-from empty-entity)
         nil)))

(deftest empty-entity-has-no-entity-name
  (is (= (entity-name-from empty-entity)
         nil)))

(deftest entity-with-returns-entity-with-supplied-name
  (is (= (entity-name-from (entity-with :entity-name empty-declaration))
         :entity-name)))

(deftest entity-with-returns-entity-with-supplied-declaration
  (is (= (declaration-from (entity-with :entity-name empty-declaration))
         empty-declaration)))

(deftest merge-entities-returns-entities-containing-both-supplied-entity-names-when-different
  (let [first-declaration (declaration-with ['(characteristic)] {})
        second-declaration (declaration-with ['(other-characteristic)] {})
        first-entity (entity-with :first-entity first-declaration)
        second-entity (entity-with :second-entity second-declaration)]
    (is (= (entity-names-from (merge-entities first-entity second-entity))
           #{:first-entity :second-entity}))))

(deftest merge-entities-returns-entities-containing-both-supplied-declarations-when-different
  (let [first-declaration (declaration-with ['(characteristic)] {})
        second-declaration (declaration-with ['(other-characteristic)] {})
        first-entity (entity-with :first-entity first-declaration)
        second-entity (entity-with :second-entity second-declaration)]
    (is (= (declarations-from (merge-entities first-entity second-entity))
           #{first-declaration second-declaration}))))

(deftest merge-entities-returns-entities-containing-correctly-mapped-declarations-when-different
  (let [first-declaration (declaration-with ['(characteristic)] {})
        second-declaration (declaration-with ['(other-characteristic)] {})
        first-entity (entity-with :first-entity first-declaration)
        second-entity (entity-with :second-entity second-declaration)
        merged-entities (merge-entities first-entity second-entity)]
    (are [name declaration] (= (declaration-for name merged-entities) declaration)
         :first-entity first-declaration
         :second-entity second-declaration)))

(deftest merge-entities-merges-declarations-when-supplied-entities-have-same-name
  (let [first-declaration (declaration-with ['(first-characteristic)] {})
        second-declaration (declaration-with ['(second-characteristic)] {})
        first-entity (entity-with :entity first-declaration)
        second-entity (entity-with :entity second-declaration)
        merged-entities (merge-entities first-entity second-entity)
        expected-declaration (merge-declarations first-declaration second-declaration)]
    (is (= (declaration-for :entity merged-entities)
           expected-declaration))))
