;;;; Copyright 2013 Richard Fontana
;;;; License: EPL or copyleft-next

(ns bialystok.subscribe
  (:require [clj-yaml :as yaml])
  (:require [clojure.string :as string]))

(defn mod-subscriber 
  "Adds information about moderated subscriber to config.yaml."
  [list-dir sub-address sub-type]
  (let [yaml-file (str list-dir "/config.yaml")]
    (spit yaml-file 
      (yaml/generate-string 
        (update-in (yaml/parse-string (slurp yaml-file)) [:under-moderation] 
          #(conj % {:address sub-address, :subscription-type sub-type}))
        :dumper-options {:flow-style :block}))))

(defn list-delim
  "Returns delimiter given in config.yaml or default \"+\"."
  [list-dir]
  (let [yaml-file (str list-dir "/config.yaml") 
        delimv (get (yaml/parse-string (slurp yaml-file)) :delimiter)]
    (if-not delimv "+" delimv)))

(defn list-name-fqdn
  "Returns name and FQDN portions of list address."
  [list-dir]
  (let [yaml-file (str list-dir "/config.yaml")
        list-addr (get (yaml/parse-string (slurp yaml-file)) :list-address)]
    (string/split list-addr #"@")))

(defn list-name
  "Returns name portion of list address."
  [list-dir]
  (first (list-name-fqdn list-dir)))

(defn list-fqdn
  "Returns FQDN portion of list address."
  [list-dir]
  (second (list-name-fqdn list-dir)))

(defn moderators
  "Returns list of moderators (as addresses)."
  [list-dir]
  (let [yaml-file (str list-dir "/config.yaml")
        parsed-yaml (yaml/parse-string (slurp yaml-file))
        modsv (get parsed-yaml :moderators)]
    (if-not modsv (get parsed-yaml :owners) modsv)))

(defn cookie
  "Returns 16-digit random integer as string."
  []
  (str (rand-int 99999999) (rand-int 99999999)))
