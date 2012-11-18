(defproject dataschemer "0.1.0-SNAPSHOT"
  :description "Data schema declaration, generation and verification framework."
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.4.0"]]
  :source-paths ["src/clojure"]
  :java-source-paths ["src/java"]
  :test-selectors { :functional :functional })