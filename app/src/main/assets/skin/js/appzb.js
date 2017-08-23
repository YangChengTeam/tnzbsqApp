jQuery(document).ready(function($) {
	if(typeof PageClass == 'number'){
		if(PageClass>1 || PageClass<4){
			$('.m-foot').find('p').eq(PageClass-1).siblings().removeClass('f-hover');
			$('.m-foot').find('p').eq(PageClass-1).addClass('f-hover');
		}
	}
	if(typeof listid == 'number'){
		if(typeof $('list_'+listid) == 'object'){
			$('.list_'+listid).addClass('active');
		}
	}
	//通用点击返回顶部
	$(window).scroll(function(){
			if($(window).scrollTop()>0){
			$(".backtop").show();
			}
			else{
				$(".backtop").hide();}
	});
	$('.backtop').click(function(){
	$('html,body').animate({scrollTop:0},500);
	});

	//通用选项卡开始
	var tabbli = $(".tabnav i");
    tabbli.mouseover(function(){
    $(this).addClass("hover").siblings().removeClass("hover");
		var index = tabbli.index(this);
		$(".tabcon").eq(index).fadeIn(300).siblings(".tabcon").fadeOut(0);
		$("li").click(function(){
			})
	});
	//通用拉动把导航固顶
	var headerHeight = $('header.g-topindex').outerHeight(true);
	$(document).scroll(function () {
	    var $nav = $('nav');
	    if($(document).scrollTop() >= headerHeight) {
	        $nav.css({'position': 'fixed'});
	        $nav.next().css({'margin-top': $nav.outerHeight(true)});
	    } else {
	        $nav.css({'position': ''});
	        $nav.next().css({'margin-top': ''});
	    }
	});
	
	
	
});
function coleToggle(obj){
	$(obj).toggleClass("cole1");
}
//PageClass 判断页面专用 1 首页 2 下载列表 3 下载内容 4 文章列表 5 文章内容 6 库  7 专题 8厂商 9其他
if(PageClass==null)
{
	var PageClass=0;
}
//如果是首页
if( PageClass==1)
{
	jQuery(document).ready(function($){
		
	//首页换一换
	var numb = 1;
	var lenght = $(".m-hyhul").length;
	$(".m-hyh").click(function(){
		if(numb < lenght){
			numb++;
			$(".m-hyhul").eq(numb-1).show().siblings("ul").hide();
		}else if(numb >= lenght){
			numb=1;
			$(".m-hyhul").eq(numb-1).show().siblings("ul").hide();
		}
	});
//首页开始执行幻灯片
/*
	var self = $('#Hdatu');
	var defaults = {
	movebox:".slide",
	single:"li",
	ratio:6/2,
	dotwp:".slide_dot",
	trans:'trans',
	time:5000
	}
	var cur = 0;
	var options = $.extend(defaults, options);
	var movobj = self.find(options.movebox);
	var li;
	var width;
	var len = $('#Hdatu li').length;
	function resize(){
		width = self.width();
		li.css('width',width);
		self.height(width/defaults.ratio);
		li.css('height',width/defaults.ratio);
		to(cur);
	}
	$(window).resize(resize);
		var init = function(){
		for(var i=0;i<len;i++){
		    var dotclass = i==0?'selected':'dotItem';
		    self.find(options.dotwp).append('<span class="'+dotclass+'" title="'+i+'"></span>');
		}
		movobj.append(movobj.html());
		li = self.find(options.single);
		resize();
	};
	init();
	function to(n){
		  li.stop().animate({left:-n*width},200);
		  cur = n;
		  if(n==len) n-=len;
		  self.find(options.dotwp+' span').eq(n).addClass('selected').removeClass('dotItem').siblings('span').removeClass('selected');
	}
	function preto(n){
		li.css('left',-n*width);
	}
	self.find(options.dotwp+' span').click(function(){
		var n = $(this).index('span');
		$(this).addClass('selected').removeClass('dotItem').siblings('span').removeClass('selected');
		to(n);
	})
	var x;
	touch.on('#Hdatu li img','touchstart',function(ev){
	          x = ev.changedTouches[0].clientX;
	     });
	touch.on('#Hdatu li img','touchmove',function(ev){
		if(Math.abs(ev.changedTouches[0].clientX-x)>10){
	         ev.preventDefault();
	    }
	});
	touch.on('#Hdatu li img','touchend',function(ev){
	     if(ev.changedTouches[0].clientX-x>20){
	        if(cur-1<0){
	            preto(len);
	            cur =len;
	        }
	        cur--;
	        to(cur);
	    }else if(ev.changedTouches[0].clientX-x<-20){
	        if(cur==len){
	            preto(0);
	            cur =0;
	        }
	            cur++;
	            to(cur);
	    }
	});
	var h5indexgonggaocnt = $(".n-list li").length;
	var scroll_timer;
    function AutoScroll(){
	  $('.n-list').find("ul:first:not(:animated)").animate({
		  marginTop:"-30px"
	  },500,function(){
		  $(this).css({marginTop:"0px"}).find("li:first").appendTo(this);
		  return false;
	  });
    }
   if(h5indexgonggaocnt>1){
	   scroll_timer = setInterval(AutoScroll,3000);
   };
   */
	//幻灯片结束
	
	//获取特殊部分左侧高度
	/*
	var lheight = $(".m-left").height()/3;
	var bigheight = lheight + lheight;
	$(".m-ridiv p:eq(0)").find("a").height(lheight+20);
	$(".m-ridiv p:eq(1)").find("a").height(bigheight-20);
	*/
	});
}
//除了首页
if( PageClass!=1)
{
	jQuery(document).ready(function($){
      $(".m-listgame li").each(function(){
         var str = $(this).find("a").attr("href");  
         var newstr = str.replace(/\/pc/g,"");
	     $(this).find("a").attr("href",newstr);
      });
	});
}

//内容页
if( PageClass==3)
{
	jQuery(document).ready(function($){

		//判断当前设备
	if (/android/i.test(navigator.userAgent)){


	}
    // 微信浏览器
    if (/micromessenger/i.test(navigator.userAgent)) {
        
    }
	if (/iphone|ipad/i.test(navigator.userAgent)){

	}
	else {
        	
        }
		
        
		
		
        });
        window.onload=function(){//加载完后执行
        	//获取大图的宽高，
			var imgwidth = $("#myImage").width();
			var imgheight = $("#myImage").height();
			//console.log(imgwidth);
			//console.log(imgheight);
			if(imgwidth > imgheight){
				//获取高度让图片居中
	       // var divheight = $(".g-scqimg").height()/2+50;
	        //$(".g-scqimg").css("margin-top",-divheight).addClass("g-scqimgadd");
			}
	        //当获取输入框焦点时候
	        $(".m-input input").focus(function(){
	        	$(".g-btn").attr("style","position:relative;bottom：auto");
	        	
      
    		})
        }
        

}