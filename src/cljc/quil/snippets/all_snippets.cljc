(ns quil.snippets.all-snippets
  (:require
   [quil.snippets.all-snippets-internal :as asi]

   ;;; Require all snippets.
   [quil.snippets image environment input output rendering state structure transform]
   [quil.snippets.image rendering pixels loading-and-displaying]
   [quil.snippets.color creating-and-reading setting utility-macros]
   [quil.snippets.data conversion]
   [quil.snippets.lights-camera camera coordinates lights material-properties]
   [quil.snippets.math calculation random trigonometry]
   [quil.snippets.shape attributes curves loading-and-displaying primitives-2d primitives-3d vertex]
   [quil.snippets.transform utility-macros]
   [quil.snippets.typography attributes loading-and-displaying metrics]))

(def all-snippets @asi/all-snippets)

