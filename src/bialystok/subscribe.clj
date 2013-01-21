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
