;;;; Copyright 2013 Richard Fontana
;;;; License: EPL or copyleft-next

(ns bialystok.subscribe
  (:require [clj-yaml :as yaml]))

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

(defn list-fqdn
  "Returns FQDN portion of list address."
  [list-dir]
  (let [yaml-file (str list-dir "/config.yaml")
        list-addr (get (yaml/parse-string (slurp yaml-file)) :list-address)]
    (subs list-addr (inc (.indexOf list-addr "@")) (count list-addr))))

