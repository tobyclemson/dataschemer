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

(defn => [key & forms]
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

#_(defmacro => [key & forms]
  (loop [declaration {key []}
         current-key key
         forms forms]
    (let [candidate-form (first forms)
          remaining-forms (rest forms)]
      (println (str "candidate: " candidate-form))
      (println (str "remaining: " remaining-forms))
      (cond
       (empty? forms) declaration
       (keyword? candidate-form) (recur (initialise declaration candidate-form)
                                        candidate-form
                                        remaining-forms)
       (= (first candidate-form) 'has) (recur
                                        (update
                                         declaration
                                         current-key
                                         `(=>
                                           ~@(first (rest candidate-form))
                                           ~@()))
                                        current-key
                                        (conj remaining-forms (list 'has (rest (rest candidate-form)))))
       :else (recur (update
                     declaration
                     current-key
                     `(quote ~candidate-form))
                    current-key
                    remaining-forms)))))

#_{key (vec (map (fn [form] `(quote ~form)) forms))}

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