(ns je.suis.lbx
  (:require
   [clojure.string :as str]
   )
  )

(defn- str->cdata [s]
  (str "<![CDATA[" (str/replace s "]]>" "]]]]><![CDATA[>") "]]>"))

(defn- escape-content [s]
  (->
   (str s)
   (str/replace "&" "&amp;")
   (str/replace "<" "&lt;")
   (str/replace  #"[\u0000-\u001F]" "\uFFFD")))

(defn- escape-attr [s]
  (->
   (str s)
   (str/replace "&" "&amp;")
   (str/replace "\"" "&quot;")
   (str/replace "\t", "&#x9;")
   (str/replace "\n", "&#xA;")
   (str/replace "\r", "&#xD;")
   (str/replace  #"[\u0000-\u001F]" "\uFFFD")))

(def CDATA CDATA)
(def noescape noescape)

(defn emit-element [[tag & content-maybe]]
  (cond
    (= tag CDATA) (str->cdata (first content-maybe))
    (= tag noescape) (first content-maybe)
    true
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
              (string? %2) (str %1 (escape-content %2))
              (seqable? %2) (str %1 (emit-element %2))
              true (str %1 (escape-content (str %2)))) "" content)
          \< \/ (name tag) \>))))))

(defn emit [root] (str "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" (emit-element root)))
