(ns data-schemer.full)

(def declarations (atom {}))

(defn update-declarations [identifier forms]
  (swap! declarations assoc identifier forms))

#_(defmacro => [& forms]
  (let [candidate-form (first forms)
        remaining-forms (rest forms)]
    (cond
     (empty? forms) {}
     (list? candidate-form)
     (cond
      (keyword? (first candidate-form)) `(merge (assoc {}
                                                  ~(first candidate-form)
                                                  (=> ~@(rest candidate-form)))
                                                (=> ~@remaining-forms))
      (= (first candidate-form) 'has) `(merge (=> ~@(rest candidate-form))
                                              (=> ~@remaining-forms))
      :else `(conj (=> ~@candidate-form) (=> ~@remaining-forms)))
     :else [`(quote ~forms)])))

(defn verify [target schema]
  (let [predicates (schema @declarations)]
    (println predicates)
    (every? (fn [p] (p target))) predicates))