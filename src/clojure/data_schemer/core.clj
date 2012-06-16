(ns data-schemer.core)

(def declarations (atom {}))

(defn update-declarations [identifier forms]
  (swap! declarations assoc identifier forms))

(defn- initialise
  [declaration form]
  (assoc declaration form []))

(defn- update
  [declaration key & forms]
  (reduce
   (fn [accumulator form]
     (update-in accumulator [key] #(conj % form)))
   declaration
   forms))

#_(defn => [key & forms]
  (loop [declaration {key []}
         current-key key
         forms forms]
    (cond
     (empty? forms) declaration
     (keyword? (first forms))
     (let [new-key (first forms)
           new-declaration (initialise declaration (first forms))
           remaining-forms (rest forms)]
       (recur new-declaration new-key remaining-forms))
     (= (first (first forms)) 'has)
     (let [nested-entities (rest (first forms))
           nested-declarations
           (map (fn [form]
                  (let [key (first (first (rest form)))
                        forms (rest (first (rest form)))]
                    (apply => key (map #(first (rest %)) forms))))
                nested-entities)
           new-key current-key
           new-declaration
           (apply update declaration current-key nested-declarations)
           remaining-forms (rest forms)]
       (recur new-declaration new-key remaining-forms))
     :else
     (let [new-key current-key
           new-declaration (update declaration current-key (first forms))
           remaining-forms (rest forms)]
       (recur new-declaration new-key remaining-forms)))))

#_(defmacro => [& forms]
  (loop [declaration {}
         current-key :root
         forms forms]
    (let [candidate-form (first forms)
          remaining-forms (rest forms)]
      (println (str "declaration: " declaration))
      (println (str "current-key: " current-key))
      (println (str "forms: " forms))
      (println (str "candidate-form: " candidate-form))
      (println (str "remaining-forms: " remaining-forms))
      (println (str "empty? forms: " (empty? forms)))
      (println (str "keyword? candidate-form: " (keyword? candidate-form)))
      (println (str "list? candidate-form: " (list? candidate-form)))
      (println (str "= candidate-form 'has: " (= candidate-form 'has)))
      (println "------------")
      (cond
       (empty? forms) declaration
       (keyword? candidate-form)
       (recur (initialise declaration candidate-form)
              candidate-form
              remaining-forms)
       (= candidate-form 'has)
       (update declaration current-key `(=> ~@remaining-forms))
       (list? candidate-form)
       (recur
        (update declaration current-key `(=> ~@candidate-form))
        current-key
        remaining-forms)
       :else
       `(quote ~forms)))))

#_(defmacro => [& forms]
  (loop [declaration {}
         current-key :root
         characteristics []
         forms forms]
    (let [candidate-form (first forms)
          remaining-forms (rest forms)]
      (println (str "declaration: " declaration))
      (println (str "current-key: " current-key))
      (println (str "characteristics: " characteristics))
      (println (str "forms: " forms))
      (println (str "candidate-form: " candidate-form))
      (println (str "remaining-forms: " remaining-forms))
      (println (str "empty? forms: " (empty? forms)))
      (println (str "keyword? candidate-form: " (keyword? candidate-form)))
      (println (str "list? candidate-form: " (list? candidate-form)))
      (println (str "= candidate-form 'has: " (= candidate-form 'has)))
      (println "------------")
      (cond
       (empty? forms) (assoc declaration current-key characteristics)
       (keyword? candidate-form) (recur (assoc declaration candidate-form characteristics)
                                        candidate-form
                                        characteristics
                                        remaining-forms)
       (= candidate-form 'has) `(=> ~@remaining-forms)
       (list? candidate-form) (recur declaration
                                     current-key
                                     (conj characteristics `(=> ~@candidate-form))
                                     remaining-forms)
       :else `(quote ~forms)))))

(defmacro => [& forms]
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
      (= (count forms) 1) `(=> ~@candidate-form)
      :else `(reduce merge (=> ~candidate-form) (=> ~@remaining-forms)))
     :else [`(quote ~forms)])))

#_(defn of-type [klass]
  (partial instance? klass))

#_(defn between [lower upper]
  (fn [x] (and (> x lower) (< x upper))))

(defn verify [target schema]
  (let [predicates (schema @declarations)]
    (println predicates)
    (every? (fn [p] (p target))) predicates))

;; :age
;;  -> :value
;;
;; :person
;;  -> :age
;;      -> :value --> predicates
;;  -> :name
;;      -> :value

;; public class Person
;;     private final Age age
;;     private final NAme name