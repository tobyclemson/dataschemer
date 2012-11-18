(ns dataschemer.core-test
  (:use clojure.test
        dataschemer.core
        dataschemer.entities
        dataschemer.declarations))

(deftest single-characteristic
  (is (= (=> characteristic)
         (declaration-with ['(characteristic)] empty-entity))))

(deftest multiple-characteristics
  (is (= (=> (characteristic1) (characteristic2))
         (declaration-with ['(characteristic1) '(characteristic2)] empty-entity))))

(deftest single-entity-with-single-characteristic
  (is (= (=> (:age (characteristic)))
         (entity-with :age ['(characteristic)] empty-entity))))

(deftest single-entity-with-multiple-characteristics
  (is (= (=> (:age (characteristic1) (characteristic2)))
         (entity-with :age ['(characteristic1) '(characteristic2)] empty-entity))))

(deftest multiple-entities-with-single-characteristic-each
  (is (= (=> (:age (characteristic1))
             (:name (characteristic2)))
         (entities-with :age ['(characteristic1)] empty-entity
                        :name ['(characteristic2)] empty-entity))))

(deftest single-child-entity-definition-with-single-characteristic
  (is (= (=> (has (:age (characteristic))))
         (declaration-with [] (entity-with :age ['(characteristic)] empty-entity)))))

(deftest multiple-child-entities-definition-each-with-single-characteristic
  (is (= (=> (has (:age (characteristic1)) (:name (characteristic2))))
         (declaration-with [] (entities-with :age ['(characteristic1)] empty-entity
                                             :name ['(characteristic2)] empty-entity)))))

(deftest single-child-entity-definitiion-with-multiple-characteristics
  (is (= (=> (has (:age (characteristic1) (characteristic2))))
         (declaration-with [] (entity-with :age
                                           ['(characteristic1) '(characteristic2)]
                                           empty-entity)))))

(deftest multiple-child-entity-definitions-each-with-multiple-characterstics
  (is (= (=> (has (:age (characteristic1) (characteristic2))
                  (:name (characteristic3) (characteristic4))))
         (declaration-with [] (entities-with
                               :age ['(characteristic1) '(characteristic2)] empty-entity
                               :name ['(characteristic3) '(characteristic4)] empty-entity)))))

(deftest top-level-entity-with-characteristics-and-child-entity
  (is (= (=> (:entity
              (characteristic1)
              (has (:child
                    (child-characteristic)))
              (characteristic2)))
         (entity-with :entity
                      ['(characteristic1)
                       '(characteristic2)]
                      (entity-with :child
                                   ['(child-characteristic)]
                                   empty-entity)))))

(deftest multiple-top-level-entities-each-with-multiple-characteristics-and-child-entities
  (is (= (=> (:entity1
              (entity1-characteristic1)
              (has (:entity1-child1
                    (entity1-child1-characteristic1)
                    (entity1-child1-characteristic2))
                   (:entity1-child2
                    (entity1-child2-characteristic1)))
              (entity1-characteristic2)
              (entity1-characteristic3))
             (:entity2
              (entity2-characteristic1)
              (entity2-characteristic2)
              (has (:entity2-child1
                    (entity2-child1-characteristic)
                    (has (:entity2-child1s-child
                          (entity2-child1s-child-characteristic)))))))
         (entities-with
          :entity1
          ['(entity1-characteristic1)
           '(entity1-characteristic2)
           '(entity1-characteristic3)]
          (entities-with
           :entity1-child1
           ['(entity1-child1-characteristic1)
            '(entity1-child1-characteristic2)]
           empty-entity
           :entity1-child2
           ['(entity1-child2-characteristic1)]
           empty-entity)
          :entity2
          ['(entity2-characteristic1)
           '(entity2-characteristic2)]
          (entity-with
           :entity2-child1
           ['(entity2-child1-characteristic)]
           (entity-with
            :entity2-child1s-child
            ['(entity2-child1s-child-characteristic)]
            empty-entity))))))