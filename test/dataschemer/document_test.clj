(ns dataschemer.document-test
  (:import [dataschemer Age])
  (:use [clojure.test]
        [dataschemer.core]
        [dataschemer.entities]
        [dataschemer.document]))

;; (defentity
;;   (:age
;;    (of-type Age)
;;    (has (:value
;;           (of-type Integer)
;;           (never :null :negative)
;;           (example 23)))))

;; (deftest document-returns-documentation-string-for-tiny-type-entity
;;     (is (= (document :age)
;;            (str
;;               "Age\n"
;;               "- has attributes:\n"
;;               "\t- 'value' which is:\n"
;;               "\t\t- of integer type\n"
;;               "\t\t- never null or negative\n"
;;               "\t\t- for example, '23'"))))

(deftest document-chararacteristics-returns-documentation-string-provided-characteristics
  (is (= (document-characteristics
          ['(of-type Integer) '(never :null :negative)]
          'dataschemer.document-test)
         (str
           "- of integer type\n"
           "- never null or negative"))))

(deftest document-chararacteristics-returns-documentation-string-provided-characteristic
  (is (= (document-characteristics
          ['(of-type Integer)]
          'dataschemer.document-test)
         (str "- of integer type"))))