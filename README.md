# Erinite/template-stylesheet

A companion library to [Erinite/template](https://github.com/Erinite/template)
which allows transformations to be written in a CSS-like DSL.

## Installation

Add the following to your `:dependencies`:

[![Clojars Project](http://clojars.org/erinite/template-stylesheet/latest-version.svg)](http://clojars.org/erinite/template)

If you do not use leiningen, click the above banner to get instructions for
maven.

## Usage

### Example

First, the stylesheet, `xforms.tss`:

```css
div#name .first.name {
    content: :first-name;
}
div#name .last.name {
    content: :last-name;
}
ul.details {
    clone-for: :details;
}
ul.details li.details span {
    content: :text;
}
```

Now the code:

```clj
(require '[erinite.template.stylesheet :as tss])
(require '[erinite.template.core :as t])

;; A hiccup template
(def hiccup [:div
              [:div#name
                [:div.first.name] 
                [:div.last.name]]
              [:ul.details
                [:li.details
                  [:span "Details"]
                  [:a.link {:href "http://example.com"} "link"]]]
              [:div#footer]])

;; Transformation rules
(def transformations (tss/stylesheet (slurp "xforms.tss")))

;; Compile the template and transformation rules to create a render function
(def render-template (t/compile-template hiccup transformations))

;; Render the template
(render-template {:first-name "Bob"
                  :last-name "Smith"
                  :details [{:text "Some text"}
                            {:text "Some more text"}]})

```

The call to render-template would output this transformed hiccup template:

```clj
[:div {}
  [:div {:id "name"}
    [:div {:class "first name"} "Bob"]
    [:div {:class "last name"} "Smith"]]
  [:ul {:class "details"}
    [:li {:class "details"}
      [:span {} "Some text"]
      [:a {:class "link" :href "http://example.com"} "link"]] 
    [:li {:class "details"}
      [:span {} "Some more text"]
      [:a {:class "link" :href "http://example.com"} "link"]]]
  [:div {:id "footer"}]])
```


## License

Copyright © 2015 Daniel Kersten

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.