(ns quil.typography.font-test
  (:require
   [clojure.test :as t :refer [deftest is] :include-macros true]
   [quil.core :as q :include-macros true]
   [quil.test-helpers :as qth]))

;; FIXME: sometimes this fails on re-run under CLJS, recommend running:
;;   rm -rf target/public && clj -Mfig:cljs-test

;; font-available? is clj only
#?(:clj
   ;; FIXME: macOS fonts are installed, but they are not showing up with
   ;; font-available. For now, this should fail on a local macOS test run since
   ;; RUNNER_OS is provided by github actions, but get skipped on github.
   (when-not (= (System/getenv "RUNNER_OS") "macOS")
     (deftest fonts-available?
       (is (q/font-available? "Inconsolata"))
       (is (q/font-available? "Roboto"))
       (is (q/font-available? "DejaVu Sans"))
       (is (q/font-available? "Tinos Regular")))))

;; text-width, ascent, and descent are dependent on platform and font, so this
;; varies wildly, so this is just verifying relative sizing
(deftest text-width
  (qth/with-test-sketch (qth/test-sketch)
    (q/text-size 16)
    (is (qth/delta= 80 (q/text-width "foo bar baz") 7.5))
    (q/text-size 12)
    (is (qth/delta= 60 (q/text-width "foo bar baz") 7.5))))

(deftest text-ascent
  (qth/with-test-sketch (qth/test-sketch)
    (q/text-size 16)
    (is (qth/delta= 14 (q/text-ascent) 3.5))
    (q/text-size 12)
    (is (qth/delta= 11 (q/text-ascent) 3.5))))

(deftest text-descent
  (qth/with-test-sketch (qth/test-sketch)
    (q/text-size 16)
    (is (qth/delta= 4 (q/text-descent) 1.5))
    (q/text-size 12)
    (is (qth/delta= 3 (q/text-descent) 1.5))))
