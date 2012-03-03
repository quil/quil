== Structure ==


delay() - delay-frame *
draw() :draw A
exit() - exit *
loop() - start-loop *
noLoop() - no-loop *
popStyle() - pop-style
pushStyle() - push-style
redraw() - redraw *
setup() :setup A
size() - Not supported - use :size A

== Data ==

Not directly supported - use Clojure's datatypes :-) @

== Control ==

Not directly supported. Use Clojure's control structures @


== Environment ==

cursor() - cursor, cursor-image *
focused - focused *
frameCount - frame-count *
frameRate() - frame-rate *
frameRate - current-frame-rate *
height - height *
noCursor() - no-cursor *
online - NOT SUPPORTED !
screen - screen-width screen-height +
width - width *

== Data ==

= Conversion =
binary() - binary +
boolean() - NOT SUPPORTED !
byte() - - NOT SUPPORTED !
char() - NOT SUPPORTED !
float() - NOT SUPPORTED !
hex() - hex +
int() - NOT SUPPORTED !
str() - NOT SUPPORTED !
unbinary() - unbinary +
unhex() - unhex +

== Shape ==

PShape - PShape

= 2D Primitives =

arc() - arc *
ellipse() - ellipse *
line() - line *
point() - point *
quad() quad *
rect() - rect *
triangle() - triangle *

= Curves =
bezier() - bezier *
bezierDetail() - bezier-detail *
bezierPoint() - bezier-point *
bezierTangent() - bezier-tangent *
curve() - curve *
curveDetail() - curve-detail *
curvePoint() - curve-point *
curveTangent() - curve-tangent *
curveTightness() - curve-tightness *

= 3D Primitives =
box() - box *
sphere() - sphere *
sphereDetail() - sphere-detail *

= Attributes =
ellipseMode() - ellipse-mode *
noSmooth() - no-smooth *
rectMode() - rect-mode *
smooth() - smooth *
strokeCap() - stroke-cap *
strokeJoin() - stroke-join *
strokeWeight() - stroke-weight *

= Vertex =
beginShape() - begin-shape *
bezierVertex() - bezier-vertex *
curveVertex() - curve-vertex *
endShape() - end-shape *
texture() - texture *
textureMode() - texture-mode *
vertex() - vertex

= Loading & Displaying =
loadShape() - load-shape *
shape()  - shape *
shapeMode() - shape-mode *

== Input ==

= Mouse =
mouseButton - mouse-button *
mouseClicked() - :mouse-clicked A
mouseDragged() - :mouse-dragged A
mouseMoved() - :mouse-moved A
mousePressed() - :mouse-pressed A
mousePressed - mouse-pressed *
mouseReleased() :mouse-released A
mouseX - mouse-x *
mouseY mouse-y *
pmouseX - pmouse-x *
pmouseY - pmouse-y *

= Keyboard =
key - raw-key *
keyCode - key-code *
keyPressed() - :key-pressed A
keyPressed - key-pressed *
keyReleased() :key-released A
keyTyped() :key-typed A

= Files =
BufferedReader - Use Java class directly if necessary @
createInput() - create-input *
createInputRaw() - create-input-raw *
createReader() - NOT SUPPORTED !
loadBytes() - load-bytes *
loadStrings()  - load-strings *
open() - NOT SUPPORTED !
selectFolder() - NOT SUPPORTED !
selectInput() - NOT SUPPORTED !

= Web =
link() - NOT SUPPORTED !
param() - NOT SUPPORTED !
status() - NOT SUPPORTED !

= Time & Date =
day() - day +
hour() - hour +
millis() - millis *
minute() - minute +
month() - month +
second() - seconds +
year() - year +

== Output ==

= Text Area =

print() - Use Clojure @
println() - Use Clojure @

= Image =
save() - save *
saveFrame() - save-frame *

= Files =

PrintWriter - Use Java class directly if necessary @
beginRaw() - begin-raw *
beginRecord() - begin-record *
createOutput() - create-output *
createWriter() - NOT SUPPORTED !
endRaw() - end-raw *
endRecord() - end-record *
saveBytes() - NOT SUPPORTED !
saveStream() - NOT SUPPORTED !
saveStrings() - NOT SUPPORTED !
selectOutput() - NOT SUPPORTED !

== Transform ==

applyMatrix() - apply-matrix *
popMatrix()  - pop-matrix *
printMatrix() - print-matrix *
pushMatrix()  - push-matrix *
resetMatrix() - reset-matrix *
rotate() - rotate *
rotateX() - rotate-x *
rotateY() - rotate-y *
rotateZ() - rotate-z *
scale()  - scale *
shearX() - shear-x *
shearY() - shear-y *
translate() - translate *


== Lights, Camera ==

= Lights =
ambientLight() - ambient-light *
directionalLight() - directional-light *
lightFalloff() - light-falloff *
lightSpecular() - light-specular *
lights() - lights *
noLights() - no-lights *
normal() - normal *
pointLight() - point-light *
spotLight() - spot-light *

= Camera =
beginCamera() - begin-camera *
camera() - camera *
endCamera() - end-camera *
frustum() - frustum *
ortho() - ortho *
perspective() - perspective *
printCamera() - print-camera *
printProjection() - print-projection *

= Coordinates =
modelX() - model-x *
modelY() - model-y *
modelZ() - model-z *
screenX() - screen-x *
screenY() - screen-y *
screenZ() - screen-z *

= Material Properties =
ambient() - ambient, ambient-int, ambient-float *
emissive() - emissive, emissive-int, emissive-float *
shininess()  - shininess *
specular() - specular *


== Color ==

= Setting =
background() - background, background-int, background-float, background-image *
colorMode() - color-mode *
fill() - fill, fill-int, fill-float *
noFill() - no-fill *
noStroke() - no-stroke *
stroke() - stroke, stroke-int, stroke-float *

 = Creating & Reading =
alpha() - alpha *
blendColor() - blend-color +
blue() - blue *
brightness() - brightness *
color() - color *
green() - green *
hue() - hue *
lerpColor() - lerp-color *
red() - red *
saturation() - saturation *

== Image  ==

PImage - Use directly from Clojure @
createImage() - create-image *

= Loading & Displaying =
image() - image *
imageMode() - image-mode *
loadImage() - load-image *
noTint() - no-tint *
requestImage() - request-image *
tint() - tint, tint-int, tint-float *

= Pixels =
blend() - blend *
copy() - copy *
filter() - display-filter *
get() - get-pixel *
loadPixels() - load-pixels *
pixels[] - pixels *
set() - set-pixel set-image *
updatePixels() - update-pixels *

== Rendering ==

PGraphics - Use directly from Clojure @
createGraphics() - create-graphics *
hint() - hint

== Typography ==

PFont - Use directly from Clojure @

= Loading & Displaying =
createFont() - create-font *
loadFont() - load-font *
text() - text, text-num, text-char *
textFont() - text-font *

= Attributes =
textAlign() - text-align *
textLeading() - text-leading *
textMode() - text-mode *
textSize() - text-size *
textWidth() - text-width *

= Metrics =
textAscent() - text-ascent *
textDescent() - text-descent *


== Math ==

PVector - use directly from Clojure @

= Operators =

Use Clojure's Math operators @

= Calculation =
abs() - abs, abs-int, abs-float +
ceil() - ceil +
constrain() - constrain, constrain-int, constrain-float +
dist() - dist +
exp() - exp +
floor() - Use Clojure's equivalent @
lerp() - lerp +
log() - log +
mag() - mag +
map() - map-range +
max() - Use Clojure equiv @
min() - Use Clojure equiv @
norm() - norm +
pow() - pow +
round() - round +
sq() - sq +
sqrt() - sqrt +

= Trigonometry =
acos() - acos +
asin() - asin +
atan() - atan +
atan2() - atan2 +
cos() - cos +
degrees()  - degrees +
radians() - radians +
sin() - sin +
tan() - tan +

= Random =
noise() - noise *
noiseDetail() - noise-detail *
noiseSeed() - noise-seed *
random() - random *
randomSeed() - random-seed *

Constants

HALF_PI (1.57079...) - HALF-PI %
PI (3.14159...)  - PI %
QUARTER_PI (0.78539...) - QUARTER-PI %
TWO_PI (6.28318...) - TWO-PI %
