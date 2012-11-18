(ns data-schemer.functional-test
  (:import (dataschemer.plan
             Plan
             Assortment
             Demand
             DistributionGroup
             Fulfillment
             PlanningSeason
             ReceiptMonth
             Style)
           (java.util HashSet))
  (:use clojure.test
        data-schemer.core
        data-schemer.verify
        data-schemer.predicates))

(defentity
  (:plan
    (of-type Plan)
    (has (:assortment
           (of-type Assortment)
           (has (:style
                  (of-type Style)
                  (has (:id
                         (of-type String)
                         (of-length (exactly 9))
                         (example "000123399"))))
                #_(:demand
                  (has (:id
                         (of-type String)
                         (never :null :empty :whitespace))
                       (:quantity
                         (of-type Integer)
                         (between 0 1000000))))))
         (:fulfillment
           (of-type Fulfillment)
           (has (:planningSeason
                  (of-type PlanningSeason)
                  (has (:name
                         (of-type String)
                         (never :null)
                         (one-of "HOL" "SPR" "SUM" "FAL"))
                       (:year
                         (of-type String)
                         (of-length (exactly 4)))))
                (:distributionGroup (of-type DistributionGroup)
                  (has (:value
                         (of-type String))))
                (:receiptMonth
                  (of-type ReceiptMonth)
                  (has (:value
                         (of-type String)))))))))

(deftest ^:functional verifies-true-for-valid-plan
  (let [style              (Style. "111083809")
        demand             (HashSet. [(Demand. "RI123882" (Integer. 130))])
        assortment         (Assortment. style demand)
        planning-season    (PlanningSeason. "SUM" "2012")
        distribution-group (DistributionGroup. "Hot stores")
        receipt-month      (ReceiptMonth. "Aug")
        fulfillment        (Fulfillment. planning-season distribution-group receipt-month)
        plan               (Plan. assortment fulfillment)]
    (is (= (verify plan :plan 'data-schemer.functional-test) true))))

(deftest ^:functional verifies-false-for-invalid-plan
  (let [style              (Style. "083809")
        demand             (HashSet. [(Demand. "RI123882" (Integer. 130))])
        assortment         (Assortment. style demand)
        planning-season    (PlanningSeason. "SUM" "2012")
        distribution-group (DistributionGroup. "Hot stores")
        receipt-month      (ReceiptMonth. "Aug")
        fulfillment        (Fulfillment. planning-season distribution-group receipt-month)
        plan               (Plan. assortment fulfillment)]
    (is (= (verify plan :plan 'data-schemer.functional-test) false))))