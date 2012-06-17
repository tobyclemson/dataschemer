(ns data-schemer.core-test
  (:use clojure.test
        data-schemer.core
        data-schemer.entities
        data-schemer.declarations))

(deftest single-characteristic
  (is (= (=> characteristic)
         (declaration-with ['(characteristic)] {}))))

(deftest multiple-characteristics
  (is (= (=> (characteristic1) (characteristic2))
         (declaration-with ['(characteristic1) '(characteristic2)] {}))))

(deftest single-entity-with-single-characteristic
  (is (= (=> (:age (characteristic)))
         (entity-with :age (declaration-with ['(characteristic)] {})))))

(deftest single-entity-with-multiple-characteristics
  (is (= (=> (:age (characteristic1) (characteristic2)))
         (entity-with :age (declaration-with ['(characteristic1) '(characteristic2)] {})))))

(deftest multiple-entities-with-single-characteristic-each
  (is (= (=> (:age (characteristic1))
             (:name (characteristic2)))
         (merge-entities (entity-with :age (declaration-with ['(characteristic1)] {}))
                         (entity-with :name (declaration-with ['(characteristic2)] {}))))))

(deftest single-child-entity-definition-with-single-characteristic
  (is (= (=> (has (:age (characteristic))))
         (declaration-with [] (entity-with :age (declaration-with ['(characteristic)] {}))))))

(deftest multiple-child-entities-definition-each-with-single-characteristic
  (is (= (=> (has (:age (characteristic1)) (:name (characteristic2))))
         (declaration-with [] (merge-entities
                               (entity-with :age (declaration-with ['(characteristic1)] {}))
                               (entity-with :name (declaration-with ['(characteristic2)] {})))))))
(deftest single-child-entity-definitiion-with-multiple-characteristics
  (is (= (=> (has (:age (characteristic1) (characteristic2))))
         (declaration-with [] (entity-with :age
                                           (declaration-with
                                            ['(characteristic1) '(characteristic2)]
                                            {}))))))

(deftest multiple-child-entity-definitions-each-with-multiple-characterstics
  (is (= (=> (has (:age (characteristic1) (characteristic2))
                  (:name (characteristic3) (characteristic4))))
         (declaration-with [] (merge-entities
                               (entity-with :age
                                            (declaration-with
                                             ['(characteristic1) '(characteristic2)]
                                             {}))
                               (entity-with :name
                                            (declaration-with
                                             ['(characteristic3) '(characteristic4)]
                                             {})))))))

(deftest top-level-entity-with-characteristics-and-child-entity
  (is (= (=> (:entity
              (characteristic1)
              (has (:child
                    (child-characteristic)))
              (characteristic2)))
         (entity-with :entity (declaration-with
                               ['(characteristic1)
                                '(characteristic2)]
                               (entity-with :child
                                            (declaration-with ['(child-characteristic)]
                                                              {})))))))

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
         (merge-entities
          (entity-with
           :entity1
           (declaration-with
            ['(entity1-characteristic1)
             '(entity1-characteristic2)
             '(entity1-characteristic3)]
            (merge-entities
             (entity-with
              :entity1-child1
              (declaration-with
               ['(entity1-child1-characteristic1)
                '(entity1-child1-characteristic2)]
               {}))
             (entity-with
              :entity1-child2
              (declaration-with
               ['(entity1-child2-characteristic1)]
               {})))))
          (entity-with
           :entity2
           (declaration-with
            ['(entity2-characteristic1)
             '(entity2-characteristic2)]
            (entity-with
             :entity2-child1
             (declaration-with
              ['(entity2-child1-characteristic)]
              (entity-with
               :entity2-child1s-child
               (declaration-with
                ['(entity2-child1s-child-characteristic)]
                {}))))))))))