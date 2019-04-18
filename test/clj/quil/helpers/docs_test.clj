(ns quil.helpers.docs-test
  (:require [quil.helpers.docs :refer :all]
            [clojure.test :refer :all]))

(deftest link-to-p5js-reference-test []
  (are [p5js-name link] (= link (link-to-p5js-reference {:p5js-name p5js-name}))
    "Image.mask"    "https://p5js.org/reference/#/p5.Image/mask"
    "createImage()" "https://p5js.org/reference/#/p5/createImage"
    "key"           "https://p5js.org/reference/#/p5/key"))
