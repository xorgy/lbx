# LBX — Langage de Balisage eXtensible

XML that might just work.

Emit clean XML with hiccup-style keyword s-expressions.

## Usage

```clojure
(require [je.suis.lbx :refer [emit str->cdata]])

(emit
  [:rss {:version "2.0" :xmlns:atom "http://www.w3.org/2005/Atom"}
   [:channel
    [:title "My totally sick RSS feed"]
    [:description (str->cdata "Only the illest <items> from this feed, yo. And no mess regarding this <![CDATA[CDATA thing lol]]]>")]]])
;; => <?xml version='1.0' encoding='UTF-8'?><rss version="2.0" xmlns:atom="http://www.w3.org/2005/Atom"><channel><title>My totally sick RSS feed</title><description><![CDATA[Only the illest <items> from this feed, yo. And no mess regarding this <![CDATA[CDATA thing lol]]]]]><![CDATA[>]]></description></channel></rss>
```

## License

Copyright © 2022 Aaron Muir Hamilton <aaron@correspondwith.me>

Permission to use, copy, modify, and distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
