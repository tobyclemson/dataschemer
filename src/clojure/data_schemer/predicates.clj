(ns data-schemer.predicates)

(defn of-type [klass]
  (partial instance? klass))

(defn one-of [& values]
  #(contains? (set values) %))

(defn between [lower upper]
  #(and (> % lower) (< % upper)))

(defn never [& keywords]
  (let [mappings {:null #(not= nil %)
                  :empty #(not= (count %) 0)
                  :whitespace #(not (nil? (re-find #"(?m)\S" %)))}]
    (fn [target]
      (every? (fn [keyword]
                ((mappings keyword) target))
              keywords))))

(defn exactly [value]
  (partial = value))

(defn of-length [predicate]
  (fn [target]
    (predicate (count target))))

(defn example [form])