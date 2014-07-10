(ns quil.helpers.calc)

(defn mul-add
  "Generate a potential lazy sequence of values which is the result of
   multiplying each s by mul and then adding add. s mul and add may be
   seqs in which case the result will also be seq with the length
   being the same as the shortest input seq (similar to the behaviour
   of map). If all the seqs passed are infinite lazy seqs, the result
   will also be infinite and lazy..

   (mul-add 2 2 1)           ;=> 5
   (mul-add [2 2] 2 1)       ;=> [5 5]
   (mul-add [2 2] [2 4 6] 1) ;=> [5 9]
   (mul-add (range) 2 1)     ;=> [1 3 5 7 9 11 13...] ;; infinite seq
   (mul-add (range) [2 2] 1) ;=> [1 3]"
  [s mul add]
  (if (and (number? mul) (number? add) (number? s))
    (+ add (* mul s))
    (let [[mul nxt-mul] (if (sequential? mul)
                          [(first mul) (next mul)]
                          [mul mul])
          [add nxt-add] (if (sequential? add)
                          [(first add) (next add)]
                          [add add])
          [s nxt-s]     (if (sequential? s)
                          [(first s) (next s)]
                          [s s])]
      (lazy-seq
       (cons (+ add (* mul s)) (if (and nxt-mul nxt-add nxt-s)
                                 (mul-add  nxt-s nxt-mul nxt-add)
                                 []))))))

(defn mod-range
  "Similar to mod except allows min to be non-zero. Always returns a
  val within the range min (inclusive) to max (exclusive). Throws an
  exception if min is greater than max."
  [val min max]
  (when (> min max)
    (throw (Exception. (str "Error in mod-range: min is greater than max (> " min " " max ")"))))
  (if (< min 0)
    (let [abs-min (* -1 min)
          res (mod val (+ max abs-min))]
      (- res abs-min))
    (let [res (mod val (- max min))]
      (+ res min))))
