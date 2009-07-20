;; simple processing wrapper for Clojure
;; Roland Sadowski [szabla gmail com]

;; Copyright (c) 2008 Roland Sadowski. All rights reserved.  The use and
;; distribution terms for this software are covered by the Common
;; Public License 1.0 (http://www.opensource.org/licenses/cpl1.0.php)
;; which can be found in the file CPL.TXT at the root of this
;; distribution.  By using this software in any fashion, you are
;; agreeing to be bound by the terms of this license.  You must not
;; remove this notice, or any other, from this software. 

(ns rosado.processing
  (:import (processing.core PApplet PImage PGraphics PFont))
  (:load "constants"))

;; used by functions in this lib. Use binding to set it 
;; to an instance of processing.core.PApplet
(def #^PApplet *applet*)

(defn abs-int [n] (PApplet/abs (int n)))

(defn abs-float [n] (PApplet/abs (float n)))

(defn acos [n] (PApplet/acos n))

;; $$addListeners

(defn alpha
  [what] (.alpha *applet* (int what)))

(defn ambient
  ([gray] (.ambient *applet* (float gray)))
  ([x y z] (.ambient *applet* (float x) (float y) (float z))))

(defn ambient-int
  [rgb] (.ambient *applet* (int rgb)))

(defn ambient-light
  ([red green blue]
	 (.ambientLight *applet* (float red) (float green) (float blue)))
  ([red green blue x y z]
	 (.ambientLight *applet* (float red) (float green) (float blue) (float x) (float y) (float z))))

;; $$append

(defn apply-matrix
  ([n00 n01 n02 n10 n11 n12]
	 (.applyMatrix *applet* (float n00) (float n01) (float n02)
				   (float n10) (float n11) (float n12)))
  ([n00 n01 n02 n03
	n10 n11 n12 n13
	n20 n21 n22 n23
	n30 n31 n32 n33]
	 (.applyMatrix *applet* (float n00) (float n01) (float n02) (float 03)
				   (float n10) (float n11) (float n12) (float 13)
				   (float n20) (float n21) (float n22) (float 23)
				   (float n30) (float n31) (float n32) (float 33))))

(defn arc
  [a b c d start stop]
  (.arc *applet* (float a)(float b) (float c) (float d) (float start) (float stop)))

;; $$arraycopy

(defn asin [val] (PApplet/asin (float val)))

(defn atan [val] (PApplet/atan (float val)))

(defn atan2 [a b] (PApplet/atan2 (float a) (float b)))

(defn background-float
  ([gray] (.background *applet* (float gray)))
  ([gray alpha] (.background *applet* (float gray) (float alpha)))
  ([r g b] (.background *applet* (float r) (float g) (float b)))
  ([r g b a] (.background *applet* (float r) (float g) (float b) (float a))))

(defn background-int
  ([rgb] (.background *applet* (int rgb)))
  ([rgb alpha] (.background *applet* (int rgb) (float alpha))))

(defn background-image
  [#^PImage img] (.background *applet* img))

(defn begin-camera
  [] (.beginCamera *applet*))

(defn begin-raw
  ([#^PGraphics rawGfx] (.beginRaw *applet* rawGfx))
  ([#^java.lang.String renderer #^java.lang.String filename] 
	 (.beginRaw *applet* renderer filename)))

;; $$beginRecord

(defn begin-shape
  ([] (.beginShape *applet*))
  ([kind] (.beginShape *applet* (int kind))))

(defn bezier
  ([x1 y1 x2 y2 x3 y3 x4 y4]
	 (.bezier *applet* 
			  (float x1) (float y1) 
			  (float x2) (float y2) 
			  (float x3) (float y3) 
			  (float x4) (float y4)))
  ([x1 y1 z1 x2 y2 z2 x3 y3 z3 x4 y4 z4]
	 (.bezier *applet*
			  (float x1) (float y1) (float z1) 
			  (float x2) (float y2) (float z2)
			  (float x3) (float y3) (float z3)
			  (float x4) (float y4) (float z4))))

(defn bezier-detail
  [detail] (.bezierDetail *applet* (int detail)))

(defn bezier-point
  [a b c d t] (.bezierPoint *applet* (float a) (float b) (float c) (float d) (float t)))

(defn bezier-tangent
  [a b c d t] (.bezierTangent *applet* (float a) (float b) (float c) (float d) (float t)))

(defn bezier-vertex
  ([x2 y2 x3 y3 x4 y4]
	 (.bezierVertex *applet* 
			  (float x2) (float y2) 
			  (float x3) (float y3) 
			  (float x4) (float y4)))
  ([x1 y1 z1 x2 y2 z2 x3 y3 z3 x4 y4 z4]
	 (.bezierVertex *applet*
			  (float x2) (float y2) (float z2)
			  (float x3) (float y3) (float z3)
			  (float x4) (float y4) (float z4))))

;; $$binary

(defn blend
  ([sx1 sy1 sx2 sy2 dx1 dy1 dx2 dy2 mode]
	 (.blend *applet* (int sx1) (int sy1) (int sx2) (int sy2) (int dx1) (int dy1) (int dx2) (int dy2) (int mode)))
  ([#^PImage src sx1 sy1 sx2 sy2 dx1 dy1 dx2 dy2 mode]
	 (.blend *applet* src (int sx1) (int sy1) (int sx2) (int sy2) (int dx1) (int dy1) (int dx2) (int dy2) (int mode))))

(defn blend-color
  [c1 c2 mode] (PApplet/blendColor (int c1) (int c2) (int mode)))

(defn blue [what] (.blue *applet* (int what)))

(defn box
  ([size] (.box *applet* (int size)))
  ([w h d] (.box *applet* (float w) (float h) (float d))))

(defn brightness [what] (.brightness *applet* (int what)))

(defn camera
  ([] (.camera *applet*))
  ([eyeX eyeY eyeZ centerX centerY centerZ upX upY upZ]
	 (.camera *applet* (float eyeX) (float eyeY) (float eyeZ) (float centerX) (float centerY) (float centerZ) (float upX) (float upY) (float upZ))))

(defn can-draw? [] (.canDraw *applet*))

(defn ceil [n] (PApplet/ceil (float n)))

(defn color-float
  ([gray] (.background *applet* (float gray)))
  ([gray alpha] (.background *applet* (float gray) (float alpha)))
  ([r g b] (.background *applet* (float r) (float g) (float b)))
  ([r g b a] (.background *applet* (float r) (float g) (float b) (float a))))

(defn color-int
  ([gray] (.background *applet* (int gray)))
  ([gray alpha] (.background *applet* (int gray) (float alpha)))
  ([r g b] (.background *applet* (int r) (int g) (int b)))
  ([r g b a] (.background *applet* (int r) (int g) (int b) (int a))))

(defn color-mode
  ([mode] (.colorMode *applet* (int mode)))
  ([mode max] (.colorMode *applet* (int mode) (float max)))
  ([mode max-x max-y max-z] (.colorMode *applet* (int mode) (float max-x) (float max-y) (float max-z)))
  ([mode max-x max-y max-z max-a] (.colorMode *applet* (int mode) (float max-x) (float max-y) (float max-z) (float max-a))))

;; $$concat

(defn constrain-float
  [amt low high]
  (PApplet/constrain (float amt) (float low) (float high)))

(defn constrain-int
  [amt low high]
  (PApplet/constrain (int amt) (int low) (int high)))

(defn copy
  ([[sx1 sy1 sx2 sy2] [dx1 dy1 dx2 dy2]]
	 (.copy *applet* (int sx1) (int sy1) (int sx2) (int sy2)
			(int dx1) (int dy1) (int dx2) (int dy2)))
  ([#^PImage img [sx1 sy1 sx2 sy2] [dx1 dy1 dx2 dy2]]
	 (.copy *applet* img (int sx1) (int sy1) (int sx2) (int sy2)
			(int dx1) (int dy1) (int dx2) (int dy2))))

(defn cos [angle] (PApplet/cos (float angle)))

(defn create-font
  ([name size] (.createFont *applet* name (float size)))
  ([name size smooth] (.createFont *applet* name (float size) smooth))
  ([name size smooth #^chars charset]
	 (.createFont *applet* name (float size) smooth charset)))

(defn create-graphics
  ([w h renderer]
	 (.createGraphics *applet* (int w) (int h) renderer))
  ([w h renderer path]
	 (.createGraphics *applet* (int w) (int h) renderer path)))

(defn create-image [w h format] (.createImage *applet* (int w) (int h) (int format)))

(defn create-input [filename]
	(PApplet/createInput (java.io.File. filename)))

(defn create-input-raw
	"Call openStream() without automatic gzip decompression."
	[filename]
	(.createInputRaw *applet* filename))

(defn create-output [filename]
	(PApplet/createOutput (java.io.File. filename)))

(defn create-path [filename] (PApplet/createPath filename))

(defn create-reader [filename] (.createReader *applet* filename))

(defn create-writer [filename] (.createWriter *applet* filename))

(defn cursor
  ([] (.cursor *applet*))
  ([cur-type] (.cursor *applet* (int cur-type))))

(defn cursor-image
  ([#^PImage img] (.cursor *applet* img))
  ([#^PImage img hx hy] (.cursor *applet* img (int hx) (int hy))))

(defn curve
  ([x1 y1 x2 y2 x3 y3 x4 y4]
	 (.curve *applet* 
			  (float x1) (float y1) 
			  (float x2) (float y2) 
			  (float x3) (float y3) 
			  (float x4) (float y4)))
  ([x1 y1 z1 x2 y2 z2 x3 y3 z3 x4 y4 z4]
	 (.curve *applet*
			  (float x1) (float y1) (float z1) 
			  (float x2) (float y2) (float z2)
			  (float x3) (float y3) (float z3)
			  (float x4) (float y4) (float z4))))

(defn curve-detail [detail] (.curveDetail *applet* (int detail)))

(defn curve-point
  [a b c d t] (.bezierPoint *applet* (float a) (float b) (float c) (float d) (float t)))

(defn curve-tangent
  [a b c d t] (.curveTangent *applet* (float a) (float b) (float c) (float d) (float t)))

(defn curve-tightness [ti] (.curveTightness *applet* (float ti)))

(defn curve-vertex
  ([x y] (.curveVertex *applet* (float x) (float y)))
  ([x y z] (.curveVertex *applet* (float x) (float y) (float z))))

;; $$dataFile
;; $$dataPath

(defn day
  "Get the current day of the month (1 through 31)."
  []
  (PApplet/day))

(defn degrees [radians] (PApplet/degrees (float radians)))

(defn delay-frame [nap-time] (.delay *applet* (int nap-time)))

(defn destroy [] (.destroy *applet*))

;; $$die

(defn directional-light
  [r g b nx ny nz]
  (.directionalLight *applet* (float r) (float g) (float b) (float nx) (float ny) (float nz)))

(defn displayable? [] (.displayable *applet*))

(defn dist
  ([a b x y] (PApplet/dist (float a) (float b) (float x) (float y)))
  ([a b c x y z] (PApplet/dist (float a) (float b) (float c) (float x) (float y) (float z))))

;; $$draw

(defn ellipse
  [a b c d]
  (.ellipse *applet* (float a) (float b) (float c) (float d)))

(defn ellipse-mode [mode] (.ellipseMode *applet* (int mode)))

(defn emissive-float
  ([gray] (.emissive *applet* (float gray)))
  ([x y z] (.emissive *applet* (float x) (float y) (float z))))

(defn emissive-int
  [gray] (.emissive *applet* (int gray)))

(defn emissive [x y z] (emissive-float x y z))

(defn end-camera [] (.endCamera *applet*))

(defn end-raw [] (.endRaw *applet*))

(defn end-shape
  ([] (.endShape *applet*))
  ([mode] (.endShape *applet* (int mode))))

;; $$exec

(defn exit [] (.exit *applet*))

(defn exp [a] (PApplet/exp (float a)))

;; $$expand

(defn fill-float
  ([gray] (.fill *applet* (float gray)))
  ([gray alpha] (.fill *applet* (float gray) (float alpha)))
  ([x y z] (.fill *applet* (float x) (float y) (float z)))
  ([x y z a] (.fill *applet* (float x) (float y) (float z) (float a))))

(defn fill-int
  ([rgb] (.fill *applet* (int rgb)))
  ([rgb alpha] (.fill *applet* (int rgb) (float alpha))))

(defn fill
  ([rgb] (fill-int rgb))
  ([rgb alpha] (fill-int rgb alpha)))

(defn filter-kind
  ([kind] (.filter *applet* (int kind)))
  ([kind param] (.filter *applet* (int kind) (float param))))

;; $$focusGained
;; $$focusLost

(defn framerate [new-rate] (.frameRate *applet* (float new-rate)))

(defn frustum
  [l r b t near far]
  (.frustum *applet* (float l) (float r) (float b) (float t) (float near) (float far)))

(defn get-pixel
  ([] (.get *applet*))
  ([x y] (.get *applet* (int x) (int y)))
  ([x y w h] (.get *applet* (int x) (int y) (int w) (int h))))

(defn green [what] (.green *applet* (int what)))

;; $$handleDraw
;; $$hex

(defn hint [which] (.hint *applet* (int which)))

(defn hour [] (PApplet/hour))

(defn hue [what] (.hue *applet* (int what)))

(defn image
  ([#^PImage img x y] (.image *applet* img (float x) (float y)))
  ([#^PImage img x y c d] (.image *applet* img (float x) (float y) (float c) (float d)))
  ([#^PImage img x y c d u1 v1 u2 v2] (.image *applet* img (float x) (float y) (float c) (float d) (float u1) (float v1) (float u2) (float v2))))

(defn image-mode [mode] (.imageMode *applet* (int mode)))

;; $$init
;; $$insertFrame
;; $$join
;; $$keyPressed
;; $$keyReleased
;; $$keyTyped
;; $$lerp
;; $$lerpColor
;; $$lightFallof

(defn lights [] (.lights *applet*))

(defn light-specular
  [x y z]
  (.lightSpecular *applet* (float x) (float y) (float z)))

(defn line
  ([p1 p2] (apply line (concat p1 p2)))
  ([x1 y1 x2 y2] (.line *applet* (float x1) (float y1) (float x2) (float y2)))
  ([x1 y1 z1 x2 y2 z2] (.line *applet* (float x1) (float y1) (float z1) (float x2) (float y2) (float z2))))

;; $$link

(defn load-bytes [filename] (PApplet/loadBytes filename))

(defn load-font [filename] (.loadFont *applet* filename))

(defn load-image [filename] (.loadImage *applet* filename))

(defn load-matrix [] (.loadMatrix *applet*))

(defn load-pixels [] (.loadPixels *applet*))

(defn load-shape
	"Load a geometry from a file as a PShape."
	[filename]
	(.loadShape *applet* filename))

(defn load-strings 
	"Load data from a file and shove it into a String array."
	[filename]
	(.loadStrings *applet* filename))

;; $$log

(defn start-loop [] (.loop *applet*))

;; $$mag
;; $$main

(defn map-to [val istart istop ostart ostop]
  (PApplet/map val istart istop ostart ostop))

(defn mask
  ([#^ints alpha-array] (.mask *applet* alpha-array)))

(defn mask-image [#^PImage img] (.mask *applet* img))

;; $$match
;; $$max

(defn millis [] (.millis *applet*))

;; $$min

(defn minute [] (PApplet/minute))

(defn model-x [x y z] (.modelX *applet* (float x) (float y) (float z)))
(defn model-y [x y z] (.modelY *applet* (float x) (float y) (float z)))
(defn model-z [x y z] (.modelZ *applet* (float x) (float y) (float z)))

(defn month [] (PApplet/month))

;; $$mouseClicked
;; $$mouseDragged
;; $$mouseEntered
;; $$mouseExited
;; $$mouseMoved
;; $$mousePressed
;; $$mouseReleased
;; $$nf
;; $$nfc
;; $$nfp
;; $$nfs

(defn mouse-x [] (.mouseX *applet*))

(defn mouse-y [] (.mouseY *applet*))

(defn no-cursor [] (.noCursor *applet*))

(defn no-fill [] (.noFill *applet*))

(defn noise
  ([x] (.noise *applet* (float x)))
  ([x y] (.noise *applet* (float x) (float y)))
  ([x y z] (.noise *applet* (float x) (float y) (float z))))

(defn noise-detail
  ([int detail] (.noiseDetail *applet* (int detail)))
  ([int detail falloff] (.noiseDetail *applet* (int detail) (float falloff))))

(defn noise-seed [what] (.noiseSeed *applet* (int what)))

(defn no-lights [] (.noLights *applet*))

(defn no-loop [] (.noLoop *applet*))

(defn norm
	"Normalize a value to exist between 0 and 1 (inclusive)."
	[val start stop]
	(PApplet/norm (float val) (float start) (float stop)))

(defn normal [nx ny nz] (.normal *applet* (float nx) (float ny) (float nz)))

(defn no-smooth [] (.noSmooth *applet*))

(defn no-stroke [] (.noStroke *applet*))

(defn no-tint [] (.noTint *applet*))

(defn open [#^java.lang.String filename] (PApplet/open filename))

;; $$open -- overload

(defn ortho 
  ([] (.ortho *applet*))
  ([l r b t near far] (.ortho *applet* (float l) (float r) (float b) (float t) (float near) (float far))))

;; $$paint
;; $$param
;; $$parseBoolean
;; $$parseByte
;; $$parseChar
;; $$parseFloat
;; $$parseInt

(defn perspective
  ([] (.perspective *applet*))
  ([fovy aspect z-near z-far]
	 (.perspective *applet* (float fovy) (float aspect) (float z-near) (float z-far))))

(defn pmouse-x [] (.pmouseX *applet*))

(defn pmouse-y [] (.pmouseY *applet*))

(defn point
  ([x y] (.point *applet* (float x)(float y)))
  ([x y z] (.point *applet* (float x) (float y) (float z))))

(defn point-light
  [r g b x y z]
  (.pointLight *applet* r g b x y z))

(defn pop-matrix [] (.popMatrix *applet*))

(defn pow [a b] (PApplet/pow (float a) (float b)))

;; $$print

(defn print-camera [] (.printCamera *applet*))

;; $$println

(defn print-matrix [] (.printMatrix *applet*))

(defn print-projection [] (.printProjection *applet*))

(defn push-matrix [] (.pushMatrix *applet*))

(defn quad
  [x1 y1 x2 y2 x3 y3 x4 y4]
  (.quad *applet* x1 y1 x2 y2 x3 y3 x4 y4))

(defn radians [deg] (PApplet/radians (float deg)))

(defn random
  ([max] (.random *applet* (float max)))
  ([min max] (.random *applet* (float min) (float max))))


(defn random-seed [w] (.randomSeed *applet* (float w)))

(defn rect [x1 y1 x2 y2]
  (.rect *applet* (float x1) (float y1) (float x2) (float y2)))

(defn rect-mode [mode] (.rectMode *applet* (int mode)))

(defn red [what] (.red *applet* (int what)))

(defn redraw [] (.redraw *applet*))

;; $$registerDispose
;; $$registerDraw
;; $$reqisterKeyEvent
;; $$registerMouseEvent
;; $$registerPost
;; $$registerPre
;; $$registerSize
;; $$registerSize

(defn request-image
	([filename] (.requestImage *applet* filename))
	([filename extension] (.requestImage *applet* filename extension)))

(defn reset-matrix [] (.resetMatrix *applet*))

(defn reverse-array [arr] (PApplet/reverse arr))

(defn rotate
  ([angle] (.rotate *applet* (float angle)))
  ([angle vx vy vz] (.rotate *applet* (float angle) (float vx) (float vy) (float vz))))

(defn rotate-x [angle] (.rotateX *applet* (float angle)))

(defn rotate-y [angle] (.rotateY *applet* (float angle)))

(defn rotate-z [angle] (.rotateZ *applet* (float angle)))

(defn round [what] (PApplet/round (float what)))

;; $$run

(defn saturation [what] (.saturation *applet* (int what)))

(defn save [filename] (.save *applet* filename))

;; $$saveBytes
;; $$saveFile

(defn save-frame
  ([] (.saveFrame *applet*))
  ([what] (.saveFrame *applet*)))

;; $$savePath
;; $$saveStream
;; $$saveStrings

(defn scale
  ([s] (.scale *applet* (float s)))
  ([sx sy] (.scale *applet* (float sx) (float sy))))

(defn screen-x
  ([x y] (.screenX *applet* (float x) (float y)))
  ([x y y] (.screenX *applet* (float x) (float y))))

(defn screen-y
  ([x y] (.screenY *applet* (float x) (float y)))
  ([x y z] (.screenY *applet* (float x) (float y) (float z))))

(defn screen-z
  [x y z] (.screenX *applet* (float x) (float y) (float z)))

(defn seconds [] (PApplet/second))

;; $$selectFolder
;; $$selectInput
;; $$selectOutput

(defn set-pixel
  [x y c] (.set *applet* (int x) (int y) (int c)))

(defn set-image-at
  [dx dy #^PImage src] (.set *applet* (int dx) (int dy) src))

;; $$setup

;; $$setupExternalMessages
;; $$setupFrameListener

(defn shininess [shine] (.shininess *applet* (float shine)))

;; $$shorten

(defn sin [angle] (PApplet/sin (float angle)))

(defn size
  ([w h] (.size *applet* (int w) (int h)))
  ([w h #^java.lang.String renderer] (.size *applet* (int w) (int h) renderer)))

;; $$sketchFile
;; $$sketchPath

(defn smooth [] (.smooth *applet*))

;; $$sort

(defn specular
  ([gray] (.specular *applet* (float gray)))
  ([gray alpha] (.specular *applet* (float gray) (float alpha)))
  ([x y z] (.specular *applet* (float x) (float y) (float z)))
  ([x y z a] (.specular *applet* (float x) (float y) (float z) (float a))))

(defn sphere
  [r] (.sphere *applet* (float r)))

(defn sphere-detail
  ([res] (.sphereDetail *applet* (int res)))
  ([ures vres] (.sphereDetail *applet* (int ures) (int vres))))

;; $$splice
;; $$split
;; $$splitTokens

(defn spotlight
  ([r g b x y z nx ny nz angle concentration]
	 (.spotLight *applet* r g b x y z nx ny nz angle concentration))
  ([[r g b] [x y z] [nx ny nz] angle concentration]
	 (.spotLight *applet* r g b x y z nx ny nz angle concentration)))

(defn sq [a] (PApplet/sq (float a)))

(defn sqrt [a] (PApplet/sqrt (float a)))

;; $$start
;; $$status
;; $$stop
;; $$str

(defn stroke-float
  ([gray] (.stroke *applet* (float gray)))
  ([gray alpha] (.stroke *applet* (float gray) (float alpha)))
  ([x y z] (.stroke *applet* (float x) (float y) (float z)))
  ([x y z a] (.stroke *applet* (float x) (float y) (float z) (float a))))

(defn stroke-int
  ([rgb] (.stroke *applet* (int rgb)))
  ([rgb alpha] (.stroke *applet* (int rgb) (float alpha))))

(defn stroke-cap [cap] (.strokeCap *applet* (int cap)))

(defn stroke-join [jn] (.strokeJoin *applet* (int jn)))

(defn stroke-weight [weight] (.strokeWeight *applet* (float weight)))

;; $$subset

(defn tan [angle] (PApplet/tan (float angle)))

(defn char->text
  ([c] (.text *applet* (char c)))
  ([c x y] (.text *applet* (char c) (float x) (float y)))
  ([c x y z] (.text *applet* (char c) (float x) (float y) (float z))))

(defn num->text
  ([num x y] (.text *applet* (float num) (float x) (float y)))
  ([num x y z] (.text *applet* (float num) (float x) (float y) (float z))))

(defn string->text
  ([#^java.lang.String s] (.text *applet* s))
  ([#^java.lang.String s x y] (.text *applet* s (float x) (float y)))
  ([#^java.lang.String s x y z] (.text *applet* s (float x) (float y) (float z))))

(defn string->text-in
  ([#^java.lang.String s x1 y1 x2 y2]
	 (.text *applet* s (float x1) (float y1) (float x2) (float y2)))
  ([#^java.lang.String s x1 y1 x2 y2 z]
	 (.text *applet* s (float x1) (float y1) (float x2) (float y2) (float z))))

(defn text-align
  ([align] (.textAlign *applet* (int align)))
  ([align-x align-y] (.textAlign *applet* (int align-x) (int align-y))))

(defn text-ascent [] (.textAscent *applet*))

(defn text-descend [] (.textDescent *applet*))

(defn text-font
  ([#^PFont which] (.textFont *applet* which))
  ([#^PFont which size] (.textFont *applet* which (int size))))

(defn text-leading [leading] (.textLeading *applet* (float leading)))

(defn text-mode [mode] (.textMode *applet* (int mode)))

(defn text-size [size] (.textSize *applet* (float size)))

(defn texture [#^PImage img] (.texture *applet* img))

(defn texture-mode [mode] (.textureMode *applet* (int mode)))

(defmulti text-width #(= (class %) (class \a)))

(defmethod text-width true
  [c] (.textWidth *applet* (char c)))

(defmethod text-width false
  [#^java.lang.String s] (.textWidth *applet* s))

(defn tint-float
  ([gray] (.tint *applet* (float gray)))
  ([gray alpha] (.tint *applet* (float gray) (float alpha)))
  ([x y z] (.tint *applet* (float x)(float y) (float z)))
  ([x y z a] (.tint *applet* (float x)(float y) (float z) (float a))))

(defn tint-int
  ([rgb] (.tint *applet* (int rgb)))
  ([rgb alpha] (.tint *applet* (int rgb) (float alpha))))

(defn translate
	([v] (apply translate v))
  ([tx ty] (.translate *applet* (float tx) (float ty)))
  ([tx ty tz] (.translate *applet* (float tx) (float ty) (float tz))))

(defn triangle
  [x1 y1 x2 y2 x3 y3]
  (.triangle *applet* (float x1) (float y1) (float x2) (float y2) (float x3) (float y3)))

;; $$trim
;; $$unbinary
;; $$unhex
;; $$unint
;; $$unregisterDispose
;; $$unregisterDraw
;; $$unregiserKeyEvent
;; $$unregiserMouseEvent
;; $$unregiserKeyEvent
;; $$unregiserPost
;; $$unregisterPre
;; $$unregisterSize
;; $$update
;; $$updatePixels

(defn vertex
  ([x y] (.vertex *applet* (float x) (float y)))
  ([x y z] (.vertex *applet* (float x) (float y) (float z)))
  ([x y u v] (.vertex *applet* (float x) (float y) (float u) (float v)))
  ([x y z u v]
	 (.vertex *applet* (float x) (float y) (float z) (float u) (float v))))

(defn year [] (PApplet/year))

;; utility functions. clj-processing specific

(defmacro with-translation
	"Berforms body with translation, restores current transformation on exit."
	[translation-vector & body]
	`(let [tr# ~translation-vector]
		 (push-matrix)
		 (translate tr#)
		 ~@body
		 (pop-matrix)))

(defmacro with-rotation 
	"Berforms body with rotation, restores current transformation on exit.
  Accepts a vector [angle] or [angle x-axis y-axis z-axis].

  Example: 
    (with-rotation [angle] 
      (vertex 1 2))"
	[rotation & body]
	`(let [tr# ~rotation]
		 (push-matrix)
		 (apply rotate tr#)
		 ~@body
		 (pop-matrix)))
