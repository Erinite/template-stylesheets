(ns template.stylesheet
  (:require
    [instaparse.core :as insta]))

;; Parser to skip whitespace between tokens
(def ^:private whitespace-parser
  (insta/parser
    "whitespace = #'\\s+'"))

;whitespace? xform whitespace? '}'
(def ^:private tss-parser
  (insta/parser
    "<rules>      = rule*
     rule         = selector <whitespace>? <'{'> <whitespace>? xform* <whitespace>? <'}'> <whitespace>?
     selector     = sel (<whitespace> sel)*
     sel          = s-el | s-cls | s-id | s-elid | s-elcls | s-idcls | s-elidcls
     <s-el>       = element
     <s-cls>      = class+
     <s-id>       = id
     <s-elid>     = s-el s-id
     <s-elcls>    = s-el s-cls
     <s-idcls>    = s-id s-cls
     <s-elidcls>  = s-el s-id s-cls
     <id>         = #'#[^\\s{}#\\.]+'
     <class>      = #'\\.[^\\s{}#\\.]+'
     <element>    = #'[^\\s{}#\\.]+'
     xform        = <whitespace>? command <whitespace>? <':'> <whitespace>? value <whitespace>? <';'>
     <command>    = #'[^\\s{}:]+'
     <value>      = keyword | vector | string
     string       = <'\"'> #'.*' <'\"'>
     keyword      = <':'> #'[^\\s;\\[\\]]+'
     vector       = <'['> item (<whitespace> item)* <']'>
     <item>       = string | keyword
     <whitespace> = #'\\s+'"))

(defn stylesheet 
  [tss]
  (apply
    merge
    (insta/transform
      {:keyword keyword
       :vector  (fn [& nodes] (into [] nodes))
       :sel     (fn [& nodes] (keyword (apply str nodes)))
       :xform   (fn [command value] [(keyword command) value])
       :rule    (fn [selector & xforms] {selector xforms})}
      (tss-parser tss))))

