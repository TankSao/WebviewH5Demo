var bot; //画布div
var X, Y, X1, Y1; //坐标
var flag = 0;
var time; //定时器ID
var color = 0; //记住所选颜色
var lineW = 2; //画笔粗细
var canvas; //创建画布
var context; //获取上下文
var isMouseDown = false; //记录鼠标是否按下
var WIN_WIDTH,
	WIN_HEIGHT,
	WIN_SRC; // 定义宽度，高度，图片路径

window.onresize = function (){
	var param = setImg()
	var img = new Image();
	img.src = param.src;
	img.onload = function() {
		context.drawImage(img, 0, 0,param.width, param.height);
	}
	canvas.width = param.width;
    canvas.height = param.height;
}
window.onload = function() {
	//创建画布
	canvas = document.getElementById("can");
	//获取上下文
	context = canvas.getContext("2d");
	bot = document.getElementById("canBox");

// 适配webview
	bot.ontouchstart = mouseDownAction;
	bot.ontouchmove = mouseMoveAction;
	bot.ontouchend = mouseUpAction;

// pc端
//	bot.onmousedown = mouseDownAction;
//	bot.onmousemove = mouseMoveAction;
//	document.onmouseup = mouseUpAction;
/**
 * 适配屏幕
 * */
setSize()

function setSize () { // 设置宽高 如果有图片根据图片宽高设置
	var param = setImg();
	insertImg(param);
	changeSize(param);
}

function changeSize (param){
    canvas.width = param.width;
    canvas.height = param.height;
}
}
/**
 *选中画笔颜色
 */
function pen_click(num, _this) {
	var chk = document.getElementsByTagName("span");
	var sp = document.getElementById('select_pen')
	sp.innerHTML = _this.innerHTML
	for(var i = 0; i < chk.length; i++) {
		if(i == num) {
			chk[i].checked = true;
			color = i;
		} else {
			chk[i].checked = "";
		}
	}
}

/**
 * 画笔粗细
 */
function line_wid(num, _this) {
	lineW = num;
	var sl = document.getElementById('select_line')
	sl.innerHTML = _this.innerHTML;
}

/**
 *鼠标按下
 */
function mouseDownAction(e) {
	isMouseDown = true;
	//记录下鼠标点击的时候的位置
	X = e.touches[0].clientX;
	Y = e.touches[0].clientY;
}

/**
 *鼠标移动
 */
function mouseMoveAction(e) {
	if(isMouseDown) {
		X1 = e.touches[0].clientX;
		Y1 = e.touches[0].clientY;
		drowline(X, Y, X1, Y1);
		flag++;
	}
}

/**
 *鼠标弹起来
 */
function mouseUpAction(e) {
	isMouseDown = false;
	//e.touches.clear();
	//flag = 0;
}

/**
 * 绘制
 */
function drowline(num1, num2, num3, num4) {
	//开启新的路径
	if(flag)
		context.beginPath();
	//移动画笔的初始位置
	context.moveTo(num1, num2);
	context.lineWidth = lineW;
	if(color == 0) {
		context.strokeStyle = "blue";
	} else if(color == 1) {
		context.strokeStyle = "green";
	} else if(color == 2) {
		context.strokeStyle = "red";
	}
	//移动画笔的结束位置
	context.lineTo(num3, num4);
	//开始绘制
	context.stroke();

	if(flag != 0) {
		X = X1;
		Y = Y1;
	}
}

function save_pic() { //保存画图
	var param = setImg();
	var str = canvas.toDataURL("image/png", 0.92);
	jsInterface.consoleE(str);
	var s = "<img src='" + str + "' style='width: "+param.width+";height: "+param.height+";clear: both;display: block;margin:auto;'>";
    str.crossOrigin = "Anonymous";/*开启 canvas 对 图片 的跨域权限才可以*/
    var w=window.open('about:blank','image from canvas');
    w.document.write(s);
}

function take_pic() { //拍照上传
	jsInterface.upTakenPic();
}

function clear_pic() { //清除画布
	var param = setImg();
	context.clearRect(0, 0, param.width, param.height);
	insertImg(param);
}
function insertImg (param){ // 插入题目背景
	var img = new Image();
	img.src = param.src;
	img.onload = function() {
		context.drawImage(img, 0, 0,param.width, param.height);
	}
}
function setImg () {
	/*WIN_WIDTH = 1024;
	WIN_HEIGHT = 768;
	WIN_SRC = 'img/testbg1.jpg';*/
	WIN_WIDTH = im.getWidth();
    WIN_HEIGHT = im.getHeight();
    WIN_SRC = im.getSourse();
	return WIN_OBJ = {
		width: WIN_WIDTH,
		height: WIN_HEIGHT,
		src:WIN_SRC
	}
}