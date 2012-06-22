(ns data-schemer.predicates-test
  (:use clojure.test
        data-schemer.predicates))

(deftest of-type-returns-true-if-supplied-argument-is-an-instance-of-supplied-type
  (let [predicate (of-type BigDecimal)
        target (BigDecimal. "3.2")]
    (is (= (predicate target) true))))

(deftest of-type-returns-true-if-supplied-argument-is-an-instance-of-a-subtype-of-supplied-type
  (let [predicate (of-type Number)
        target (Integer. 10)]
    (is (= (predicate target) true))))

(deftest of-type-returns-false-if-supplied-argument-is-of-a-different-type-to-supplied-type
  (let [predicate (of-type Number)
        target (String. "some string")]
    (is (= (predicate target) false))))

(deftest one-of-returns-true-if-supplied-argument-is-one-of-specified-values
  (let [predicate (one-of "A" "B" "C")
        target "B"]
    (is (= (predicate target) true))))

(deftest one-of-returns-false-if-supplied-argument-is-not-one-of-specified-values
  (let [predicate (one-of "A" "B" "C")
        target "D"]
    (is (= (predicate target) false))))

(deftest never-null-returns-true-if-not-null
  (let [predicate (never :null)
        target "something"]
    (is (= (predicate target) true))))

(deftest never-null-returns-false-if-passed-null
  (let [predicate (never :null)
        target nil]
    (is (= (predicate target) false))))

(deftest never-empty-returns-true-if-not-empty-string
  (let [predicate (never :empty)
        target "hello"]
    (is (= (predicate target) true))))

(deftest never-empty-returns-false-if-empty-string
  (let [predicate (never :empty)
        target ""]
    (is (= (predicate target) false))))

(deftest never-whitespace-returns-true-if-not-only-whitespace-in-string
  (let [predicate (never :whitespace)
        target "   \t\ncontent\t    "]
    (is (= (predicate target) true))))

(deftest never-whitespace-returns-false-if-only-whitespace-in-string
  (let [predicate (never :whitespace)
        target "   \n\t\r \t  "]
    (is (= (predicate target) false))))

(deftest never-allows-multiple-modifier-keywords
  (let [predicate (never :null :empty :whitespace)]
    (are [target result] (= (predicate target) result)
         nil          false
         ""           false
         "  \t \n "   false
         "  hello \n" true
         "whatever"   true)))
(deftest exactly-returns-true-if-equal-to-supplied-value
  (let [predicate (exactly 5)
        target 5]
    (is (= (predicate target) true))))

(deftest exactly-returns-false-if-not-equal-to-supplied-value
  (let [predicate (exactly 5)
        target 10]
    (is (= (predicate target) false))))

(deftest length-finds-length-of-target-and-tests-against-supplied-predicate
  (let [predicate (of-length (exactly 5))]
    (are [target result] (= (predicate target) result)
         "hello" true
         "hey"   false)))

(deftest length-works-against-collections
  (let [predicate (of-length (between 2 5))]
    (are [target result] (= (predicate target) result)
         [1 2 3 4]                      true
         ["a"]                          false
         '("a" "b" "c" "d" "e" "f" "g") false)))