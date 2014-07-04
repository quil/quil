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