(ns quil.helpers.tools)

(defmacro with-shape [mode & body]
  (if (keyword? mode)
    `(do
       (quil.core/begin-shape ~mode)
       ~@body
       (quil.core/end-shape))

    `(do
       (quil.core/begin-shape)
       ~mode
       ~@body
       (quil.core/end-shape))))
