(ns quil.helpers.seqs
  (:use [quil.core :only [noise]]))

(defn range-incl
  "Returns a lazy seq of nums from start (inclusive) to end
  (inclusive), by step, where start defaults to 0, end to infinity and
  step to 1 or -1 depending on whether end is greater than or less
  than start respectively."
  ([] (range-incl 0 Double/POSITIVE_INFINITY))
  ([end] (range-incl 0 end))
  ([start end] (if (< start end)
                 (range-incl start end 1)
                 (range-incl start end -1)))
  ([start end step]
   (lazy-seq
    (let [b (chunk-buffer 32)
          comp (if (pos? step) <= >=)]
      (loop [i start]
        (if (and (< (count b) 32)
                 (comp i end))
          (do
            (chunk-append b i)
            (recur (+ i step)))
          (chunk-cons (chunk b)
                      (when (comp i end)
                        (range-incl i end step)))))))))

(defn indexed-range-incl
  "Returns a sequence of [idx val] pairs over the specified inclusive
  range"
  ([] (indexed-range-incl 0 Double/POSITIVE_INFINITY))
  ([end] (indexed-range-incl 0 end))
  ([start end]
     (if (< start end)
       (indexed-range-incl start end 1)
       (indexed-range-incl start end -1)))
  ([start end step]
     (map list (range) (range-incl start end step))))

(defn indexed-range
  "Returns a sequence of [idx val] pairs over the specified range"
  ([] (indexed-range 0 Double/POSITIVE_INFINITY))
  ([end] (indexed-range 0 end))
  ([start end]
     (if (< start end)
       (indexed-range start end 1)
       (indexed-range start end -1)))
  ([start end step]
     (map list (range) (range start end step))))

(defn steps
  "Returns a lazy sequence of numbers starting at
  start (default 0) with successive additions of step. step may be a
  sequence of steps to apply."
  ([] (steps 1))
  ([step] (steps 0 step))
  ([start step]
     (let [[step next-step] (if (sequential? step)
                              [(first step) (next step)]
                              [step step])]
       (lazy-seq (cons start (if next-step
                               (steps (+ step start) next-step)
                               [(+ step start)]))))))

(defn cycle-between
  "Cycle between min and max with inc-step and dec-step starting at
  start in direction :up"
  ([min max] (cycle-between min min max 1 1))
  ([min max inc-step] (cycle-between min min max inc-step inc-step))
  ([min max inc-step dec-step] (cycle-between min min max inc-step dec-step))
  ([start min max inc-step dec-step] (cycle-between start min max inc-step dec-step :up))
  ([start min max inc-step dec-step direction]
     (let [inc-step (if (neg? inc-step) (* -1 inc-step) inc-step)
           dec-step (if (neg? dec-step) (* -1 dec-step) dec-step)
           next (if (= :up direction)
                  (+ start inc-step)
                  (- start dec-step))
           [next dir] (if (= :up direction)
                        (if (> next max) [(- start dec-step) :down] [next :up])
                        (if (< next min) [(+ start inc-step) :up] [next :down]))]
       (lazy-seq (cons start (cycle-between next min max inc-step dec-step dir))))))

(defn tap
  "Debug tool for lazy sequences. Apply to a lazy-seq to print out
  current value when each element of the sequence is evaluated."
  ([s] (tap "-->" s))
  ([msg s]
     (map #(do (println (str msg " " %)) %) s)))


(defn- swap-returning-prev!
  "Similar to swap! except returns vector containing the previous and new values

  (def a (atom 0))
  (swap-returning-prev! a inc) ;=> [0 1]"
  [atom f & args]
  (loop []
    (let [old-val  @atom
          new-val  (apply f (cons old-val args))
          success? (compare-and-set! atom old-val new-val)]
      (if success?
        [old-val new-val]
        (recur)))))

(defn seq->stream
  "Converts a sequence to a stream - a stateful function which returns
  each subequent element each time it is called

  (def s (seq->stream [1 2 3]))
  (s) ;=> 1
  (s) ;=> 2
  (s) ;=> 3
  (s) ;=> nil"
  [s]
  (let [state (atom (seq s))]
    (fn []
      (let [[old new] (swap-returning-prev! state rest)]
        (first old)))))

(defn tally
  "Cumulative tally. Takes a sequence of numbers and returns a new
  sequence which is a cumulative tally of the successive additions of
  each element in the original seq.

  (take 5 (tally (range))) ;=> [0 1 3 6 10]"
  ([s] (tally s 0))
  ([s amount]
     (lazy-seq
      (let [nxt-amount (+ (first s) amount)
            nxt-s (next s)]
        (cons nxt-amount (if nxt-s
                           (tally nxt-s nxt-amount)
                           []))))))

(defn perlin-noise-seq
  "Generate a lazy infinite sequence of perlin noise values starting from
  the specified seed with incr added to the seed for each successive value."
  [seed incr]
  (lazy-seq (cons (noise seed) (perlin-noise-seq (+ seed incr) incr))))
