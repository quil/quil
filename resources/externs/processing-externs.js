var Processing = {};
var Processing = function(aCanvas, aCode, aFunctions){};

Processing.name;
Processing.use3DContext;
Processing.focused;
Processing.breakShape;
Processing.glyphTable;
Processing.pmouseX;
Processing.pmouseY;
Processing.mouseX;
Processing.mouseY;
Processing.mouseButton;
Processing.mouseScroll;

// Undefined event handlers to be replaced by user when needed
Processing.mouseClicked;
Processing.mouseDragged;
Processing.mouseMoved;
Processing.mousePressed;
Processing.mouseReleased;
Processing.mouseScrolled;
Processing.mouseOver;
Processing.mouseOut;
Processing.touchStart;
Processing.touchEnd;
Processing.touchMove;
Processing.touchCancel;
Processing.key;
Processing.keyCode;
Processing.keyPressed;
Processing.keyReleased;
Processing.keyTyped;
Processing.draw;
Processing.setup;

// Remapped vars
Processing.__mousePressed;
Processing.__keyPressed;
Processing.__frameRate;

// The current animation frame
Processing.frameCount;

// The height/width of the canvas
Processing.width;
Processing.height;

Processing.prototype.size = function(x, y){};
Processing.prototype.size = function(x, y, z){};

// 2D Primitives
Processing.prototype.arc = function(x, y, width, height, start, stop){};
Processing.prototype.ellipse = function(x, y, width, height){};
Processing.prototype.line = function(x1, y1, x2, y2){};
Processing.prototype.line = function(x1, y1, z1, x2, y2, z2){};
Processing.prototype.point = function(x, y){};
Processing.prototype.point = function(x, y, z){};
Processing.prototype.quad = function(x1, y1, x2, y2, x3, y3, x4, y4){};
Processing.prototype.rect = function(x, y, width, height){};
Processing.prototype.rect = function(x, y, width, height, radius){};
Processing.prototype.rect = function(x, y, width, height, tlradius, trradius, brradius, blradius){};
Processing.prototype.triangle = function(x1, y1, x2, y2, x3, y3){};

// Color Setting
Processing.prototype.background = function(gray){};
Processing.prototype.background = function(gray, alpha){};
Processing.prototype.background = function(value1, value2, value3){};
Processing.prototype.background = function(value1, value2, value3, alpha){};
Processing.prototype.colorMode = function(mode){};
Processing.prototype.colorMode = function(mode, range1){};
Processing.prototype.colorMode = function(mode, range1, range2, range3){};
Processing.prototype.colorMode = function(mode, range1, range2, range3, range4){};
Processing.prototype.fill = function(arg1){};
Processing.prototype.fill = function(arg1, arg2){};
Processing.prototype.fill = function(arg1, arg2, arg3){};
Processing.prototype.fill = function(arg1, arg2, arg3, alpha){};
Processing.prototype.noFill = function(){};
Processing.prototype.noStroke = function(){};
Processing.prototype.stroke = function(arg1){};
Processing.prototype.stroke = function(arg1, arg2){};
Processing.prototype.stroke = function(arg1, arg2, arg3){};
Processing.prototype.stroke = function(arg1, arg2, arg3, alpha){};

// Color Creating & Reading
Processing.prototype.alpha = function(color){};
Processing.prototype.blendColor = function(c1, c2, mode){};
Processing.prototype.blue = function(color){};
Processing.prototype.brightness = function(color){};
Processing.prototype.color = function(arg1){};
Processing.prototype.color = function(arg1, arg2){};
Processing.prototype.color = function(arg1, arg2, arg3){};
Processing.prototype.color = function(arg1, arg2, arg3, alpha){};
Processing.prototype.green = function(color){};
Processing.prototype.hue = function(color){};
Processing.prototype.lerpColor = function(c1, c2, amt){};
Processing.prototype.red = function(color){};
Processing.prototype.saturation = function(color){};

// PImage
var PImage = {};
var PImage = function(){};
var PImage = function(width, height){};
var PImage = function(width, height, format){};
var PImage = function(img){};

PImage.prototype.blend = function(srcImg, x, y, width, height, dx, dy, dwidth, dheight, MODE){};
PImage.prototype.blend = function(x, y, width, height, dx, dy, dwidth, dheight, MODE){};
PImage.prototype.updatePixels = function(){};
PImage.prototype.get = function(x, y, w, h){};
PImage.prototype.set = function(x, y, c){};
PImage.prototype.copy = function(srcImg, sx, sy, swidth, sheight, dx, dy, dwidth, dheight){};
PImage.prototype.copy = function(sx, sy, swidth, sheight, dx, dy, dwidth, dheight){};
PImage.prototype.filter = function(mode, param){};
PImage.prototype.filter = function(mode){};
PImage.prototype.save = function(file){};
PImage.prototype.resize = function(w, h){};
PImage.prototype.mask = function(mask){};

Processing.prototype.createImage = function(w, h, mode){};

// PImage Loading & Displaying
Processing.prototype.image = function(img, x, y){};
Processing.prototype.image = function(img, x, y, width, height){};
Processing.prototype.imageMode = function(MODE){};
Processing.prototype.loadImage = function(filename){};
Processing.prototype.noTint = function(){};
Processing.prototype.requestImage = function(filename){};
Processing.prototype.requestImage = function(filename, extension){};
Processing.prototype.tint = function(arg1){};
Processing.prototype.tint = function(arg1, arg2){};
Processing.prototype.tint = function(arg1, arg2, arg3){};
Processing.prototype.tint = function(arg1, arg2, arg3, alpha){};

// PImage Pixels is a part of PImage class

// Rendering
var PGraphics = {};
var PGraphics = function(){};
var PGraphics = function(width, height){};
var PGraphics = function(width, height, applet){};

PGraphics.prototype.beginDraw = function(){};
PGraphics.prototype.endDraw = function(){};

Processing.prototype.createGraphics = function(width, height, renderer){};
Processing.prototype.createGraphics = function(width, height, renderer, filename){};

PGraphics.prototype.hint = function(item){};