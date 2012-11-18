(ns dataschemer.entities-test
  (:use clojure.test
        dataschemer.entities
        dataschemer.declarations))

(deftest empty-entity-has-no-declaration
  (is (= (declaration-from empty-entity)
         nil)))

(deftest empty-entity-has-no-entity-name
  (is (= (entity-name-from empty-entity)
         nil)))

(deftest entity-with-returns-entity-with-supplied-name
  (let [characteristics ['(characteristic)]
        child-entities {:child empty-declaration}]
    (is (= (entity-name-from (entity-with :entity-name characteristics child-entities))
           :entity-name))))

(deftest entity-with-returns-entity-with-constructed-declaration
  (let [characteristics ['(characteristic)]
        child-entities {:child empty-declaration}]
    (is (= (declaration-from (entity-with :entity-name characteristics child-entities))
           (declaration-with characteristics child-entities)))))

(deftest entity-with-can-construct-from-prebuilt-declaration
  (let [characteristics ['(characteristic)]
        child-entities {:child empty-declaration}
        declaration (declaration-with characteristics child-entities)]
    (is (= (entity-with :name declaration)
           (entity-with :name characteristics child-entities)))))

(deftest entities-with-returns-entities-with-supplied-characteristics-and-child-entities
  (is (= (entities-with :first ['(first-characteristic)] empty-entity
                        :second ['(second-characteristic)] empty-entity)
         (merge-entities
          (entity-with :first ['(first-characteristic)] empty-entity)
          (entity-with :second ['(second-characteristic)] empty-entity)))))

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

(deftest store-entity-should-add-entity-to-defined-entities
  (with-no-defined-entities
    (let [entity (entity-with :entity ['(characteristic)] empty-entity)]
      (store-entity entity)
      (is (= @defined-entities entity)))))

(deftest store-entity-should-accumulate-entities-over-many-calls
  (with-no-defined-entities
    (let [first-entity (entity-with :first-entity ['(some-characteristic)] empty-entity)
          second-entity (entity-with :second-entity ['(other-characteristic)] empty-entity)]
      (store-entity first-entity)
      (store-entity second-entity)
      (is (= @defined-entities (merge-entities first-entity second-entity))))))

(deftest store-entities-should-store-all-supplied-entities
  (with-no-defined-entities
    (let [entities (entities-with :first-entity ['(some-characteristic)] empty-entity
                                  :second-entity ['(other-characteristic)] empty-entity)
          some-other-entity (entity-with :other-entity [] empty-entity)]
      (store-entity some-other-entity)
      (store-entities entities)
      (is (= @defined-entities
             (entities-with :first-entity ['(some-characteristic)] empty-entity
                            :second-entity ['(other-characteristic)] empty-entity
                            :other-entity [] empty-entity))))))