(ns
 quil.snippets.all-snippets
  (:require
   [quil.snippets.all-snippets-internal :as asi]

   ;;; Require all snippets.
   quil.snippets.image
   quil.snippets.debugging
   quil.snippets.environment
   quil.snippets.input
   quil.snippets.output
   quil.snippets.rendering
   quil.snippets.state
   quil.snippets.structure
   quil.snippets.transform
   quil.snippets.image.rendering
   quil.snippets.image.pixels
   quil.snippets.image.loading-and-displaying
   quil.snippets.color.creating-and-reading
   quil.snippets.color.setting
   quil.snippets.color.utility-macros
   quil.snippets.data.conversion
   quil.snippets.lights-camera.camera
   quil.snippets.lights-camera.lights
   quil.snippets.lights-camera.material-properties
   quil.snippets.math.calculation
   quil.snippets.math.random
   quil.snippets.math.trigonometry
   quil.snippets.shape.attributes
   quil.snippets.shape.curves
   quil.snippets.shape.loading-and-displaying
   quil.snippets.shape.primitives-2d
   quil.snippets.shape.primitives-3d
   quil.snippets.shape.vertex
   quil.snippets.transform.utility-macros
   quil.snippets.typography.attributes
   quil.snippets.typography.loading-and-displaying
   quil.snippets.typography.metrics))

(def all-snippets @asi/all-snippets)

