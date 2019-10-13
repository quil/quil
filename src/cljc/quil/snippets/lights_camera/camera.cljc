(ns quil.snippets.lights-camera.camera
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

(defsnippet camera
  "camera"
  {:renderer :p3d}

  (q/background 255)
  (comment "set camera in point [200, 200, 200]")
  (comment "the camera looks in direction of point [0, 0, 0]")
  (comment "\"up\" vector is [0, 0, -1]")
  (q/camera 200 200 200 0 0 0 0 0 -1)

  (comment "draw a box of size 100 at the [0, 0, 0] point")
  (q/stroke-weight 2)
  (q/stroke 0)
  (q/fill 127)
  (q/box 100)
  (comment "draw red X axis")
  (q/stroke 255 0 0)
  (q/line 0 0 0 100 0 0)
  (comment "draw green Y axis")
  (q/stroke 0 255 0)
  (q/line 0 0 0 0 100 0)
  (comment "draw blue Z axis")
  (q/stroke 0 0 255)
  (q/line 0 0 0 0 0 100))

#?(:clj
   (defsnippet frustum
     "frustum"
     {:renderer :p3d}

     (q/background 255)
     (comment "set camera in point [200, 200, 200]")
     (comment "the camera looks in direction of point [0, 0, 0]")
     (comment "\"up\" vector is [0, 0, -1]")
     (q/camera 200 200 200 0 0 0 0 0 -1)
     (comment "set camera to show only part of the scene according to following condition")
     (comment "between -100 and 100 left to right")
     (comment "between -100 and 100 top to bottm")
     (comment "between 300 and 350 points in front")
     (q/frustum -100 100 -100 100 300 350)

     (comment "draw box, it will appear cut as we frustum ")
     (comment "limits to 300-350 region in fron of camera")
     (q/stroke-weight 2)
     (q/stroke 0)
     (q/fill 127)
     (q/box 100)
     (comment "draw red X axis")
     (q/stroke 255 0 0)
     (q/line 0 0 0 100 0 0)
     (comment "draw green Y axis")
     (q/stroke 0 255 0)
     (q/line 0 0 0 0 100 0)
     (comment "draw blue Z axis")
     (q/stroke 0 0 255)
     (q/line 0 0 0 0 0 100)))

#?(:cljs
   (defsnippet orbit-control
     "orbit-control"
     {:renderer :p3d}
     (q/orbit-control)

     (q/background 255)
     (q/fill 0 127 127)
     (q/box 200)
     (comment "rotate camera around box using mouse")))

(defsnippet ortho
  ["ortho"]
  {:renderer :p3d}

  (q/background 240)

  (comment "set camera to look from [400, 100, 200] at point [100, 0, 0]")
  (q/camera 400 100 200 100 0 0 0 0 -1)

  (comment "in ortho all figures will look the same regardless distance")
  (q/ortho)
  (comment "draw 3 boxes with x coordinates 0, 100 and 200")
  (q/fill 0 127 127)
  (doseq [x [0 100 200]]
    (q/with-translation [x 0 0]
      (q/box 50))))

(defsnippet perspective
  ["perspective"]
  {:renderer :p3d}

  (q/background 240)

  (comment "set camera to look from [400, 100, 200] at point [100, 0, 0]")
  (q/camera 400 100 200 100 0 0 0 0 -1)

  (comment "in perspective (default) all figures will look smaller the farther they are")
  (q/perspective)
  (comment "draw 3  boxes with x coordinates 0, 100 and 200")
  (q/fill 0 127 127)
  (doseq [x [0 100 200]]
    (q/with-translation [x 0 0]
      (q/box 50))))

(defsnippet ortho-perspective
  ["ortho" "perspective"]
  {:renderer :p3d
   :skip-image-diff? true}

  (q/background 240)
  (comment "flip between ortho and perspective camera every frame")
  (comment "enable ortho camera")
  (comment "in ortho all figures will look the same regardless distance")
  (comment "in perspective (default) all figures will look smaller the farther they are")

  (if (even? (q/frame-count))
    (q/ortho)
    (q/perspective))

  (comment "set camera to look from [300, 0, 300] at point [100, 0, 0]")
  (q/camera 300 0 300 100 0 0 0 0 -1)
  (q/fill 0 127 127)
  (comment "draw 3 boxes with x coordinates 0, 100 and 200")
  (doseq [x [0 100 200]]
    (q/with-translation [x 0 0]
      (q/box 50))))

#?(:clj
   (defsnippet print-camera
     "print-camera"
     {:renderer :p3d}

     (q/print-camera)))

#?(:clj
   (defsnippet print-projection
     "print-projection"
     {:renderer :p3d
      :skip-image-diff? true}

     (q/print-projection)))
