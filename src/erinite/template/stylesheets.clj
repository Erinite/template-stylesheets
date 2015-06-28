(ns erinite.template.stylesheets
  (:require
    [instaparse.core :as insta]))

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
     <value>      = (keyword | vector | string) (<whitespace> value)*
     string       = <'\"'> (!'\"' #'.')* <'\"'>
     keyword      = #'[^\\s;\\[\\]\\\"]+'
     vector       = <'['> item (<whitespace> item)* <']'>
     <item>       = string | keyword
     <whitespace> = (white | comment)+
     comment      = '//' #'.*' ('\n' | #'$')
     white        = #'\\s+'"))

(defn stylesheet 
  [tss]
  (apply
    merge
    (insta/transform
      {:keyword keyword
       :string  str
       :vector  (fn [& nodes] (into [] nodes))
       :sel     (fn [& nodes] (keyword (apply str nodes)))
       :xform   (fn [command & values] (into [(keyword command)] values))
       :rule    (fn [selector & xforms] {selector xforms})}
      (tss-parser tss))))

