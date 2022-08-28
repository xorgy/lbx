(ns je.suis.lbx
  (:require
   [clojure.string :as str]
   )
  )

(defn- str->cdata [s]
  (str "<![CDATA[" (str/replace s "]]>" "]]]]><![CDATA[>") "]]>"))

(defn- str->comment [s]
  (str "<!--" (str/replace s "-->" "--\\>") "-->"))

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
(def COMMENT COMMENT)
(def noescape noescape)

(defn write-element [w [tag & content-maybe]]
  (cond
    (= tag CDATA) (.write w (str->cdata (first content-maybe)))
    (= tag COMMENT) (.write w (str->comment (first content-maybe)))
    (= tag noescape) (.write w (str (first content-maybe)))
    true
    (let [attrs (when (map? (first content-maybe)) (first content-maybe))
          content (if (some? attrs)
                    (rest content-maybe)
                    content-maybe)]
      (.write w (str \< (name tag)))
      (doseq [[k v] attrs]
        (.write w (str \space (name k) \= \" (escape-attr v) \")))
      (if (empty? content)
        (.write w "/>")
        (do
          (.write w (str \>))
          (doseq [ce content]
            (cond
              (string? ce) (.write w (escape-content ce))
              (seqable? ce) (write-element w ce)
              true (.write w (escape-content (str ce)))))
          (.write w (str \< \/ (name tag) \>)))))))

(defn write-document [w root]
  (.write w "<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
  (write-element w root))

;; write-element StringWriter is always faster than recursive string appends
(defn emit-element [e]
  (let [s (java.io.StringWriter.)]
    (write-element s e)
    (str s)))

(defn emit [root]
  (let [s (java.io.StringWriter.)]
    (write-document s root)
    (str s)))
