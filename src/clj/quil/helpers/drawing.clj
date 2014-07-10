(ns quil.helpers.drawing)

(defn line-join-points
  "Takes either a seq of x y (and z) point coords tuples or two
  separate lists of x and y (and z) coords independently and creates a
  lazy list of line args (vectors of 4 (or 6) elements) suitable for
  use with the line fn.

  (line-join-points [1 2 3] [4 5 6])     ;=> ([1 4 2 5] [2 5 3 6])
  (line-join-points [[1 4] [2 5] [3 6]]) ;=> ([1 4 2 5] [2 5 3 6])
  (line-join-points [[1 4 0] [2 5 1] [3 6 2]]) ;=> ([1 4 0 2 5 1]
                                                    [2 5 1 3 6 2])
  (line-join-points [1 2 3] [4 5 6] [0 1 2])   ;=> ([1 4 0 2 5 1]
                                                    [2 5 1 3 6 2])"
  ([interleaved-points]
     (lazy-seq
       (let [head (take 2 interleaved-points)]
         (if (= 2 (count head))
           (cons (apply concat head) (line-join-points (drop 1 interleaved-points)))))))
  ([xs ys]
     (lazy-seq
      (if (and (next xs) (next ys))
        (cons [(first xs) (first ys) (second xs) (second ys)]
              (line-join-points (next xs) (next ys)))
        [])))
  ([xs ys zs]
     (lazy-seq
       (if (and (next xs) (next ys) (next zs))
         (cons [(first xs) (first ys) (first zs) (second xs) (second ys) (second zs)]
               (line-join-points (next xs) (next ys) (next zs)))
         []))))
