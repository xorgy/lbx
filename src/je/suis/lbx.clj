(ns je.suis.lbx
  (:require
   [clojure.string :as str]
   )
  )

(defn str->cdata [s]
  (str "<![CDATA[" (str/replace s "]]>" "]]]]><![CDATA[>") "]]>"))

(defn- escape-attr [s]
  (->
   (str s)
   (str/replace "&" "&amp;")
   (str/replace "\"" "&quot;")))

(defn emit-element [[tag & content-maybe]]
  (let [attrs (when (map? (first content-maybe)) (first content-maybe))
        content (if (some? attrs)
                  (rest content-maybe)
                  content-maybe)]
    (str
     \< (name tag)
     (reduce #(str %1 \space (name (key %2)) \= \" (escape-attr (val %2)) \") "" attrs)
     (if (empty? content)
       "/>"
       (str
        \>
        (reduce
         #(cond
            (string? %2) (str %1 %2)
            (vector? %2) (str %1 (emit-element %2))) "" content)
        \< \/ (name tag) \>)))))

(defn emit [root] (str "<?xml version='1.0' encoding='UTF-8'?>" (emit-element root)))
