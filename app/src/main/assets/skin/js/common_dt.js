var n = 30;
var page = 1;

// 清除HTML5LocalStorage本地存储
function clearIds(){
	window.localStorage.removeItem("ids");
}

// 获取缓存中的ID
function getSCIds(){
	var ids = window.localStorage.getItem("ids");
	if(ids == null){
		return [];
	}
	return JSON.parse(ids);
}

function patchSCStyls(data){
	var ids =  getSCIds();
	var datas = data;
	for(var i = 0; i < datas.length ; i++){
		var id = datas[i]["id"];
		var coleObj = $("#cole"+ id) ;
		coleObj.removeClass("cole1");
		datas[i]["sc"] = "0";
		for(var j = 0; j < ids.length ; j++){
			var _id = ids[j];
			if(_id == id){
				datas[i]["sc"] = "1";
			}
		}
	}
	
	for(var i = 0; i < ids.length ; i++){
		var id = ids[i];
	
		var coleObj = $("#cole"+ id) ;
		if(coleObj.size() == 0){
			continue;
		}
		var clazz = coleObj.prop("class");
		
		if(clazz.indexOf("cole1") == -1){
			 coleObj.addClass("cole1");
		}
	}
}

function checkIdExist(_id){
	var tempI = -1;
	var ids =  getSCIds();

	for(i = 0; i < ids.length ; i++){
			if (ids[i] == _id){
				tempI = i;
				break;
			}
	}
	return tempI;
}


function putSCIds(arr){
	window.localStorage.setItem("ids", JSON.stringify(arr));
}


function patchData(data){
	var datas = data['data'];
	for(var i = 0;i<datas.length; i++){
		try{
			datas[i]["time"] = datas[i]["addtime"];
			datas[i]["info"] = datas[i]["content"];
			datas[i]["stitle"] = datas[i]["stitle"];
			datas[i]["smallimg"] = datas[i]["smallimg"];
			datas[i]["isbest"] = datas[i]["isbest"];
			datas[i]["isbesttitle"] = datas[i]["inputman"];
			datas[i]["isbestimg"] = datas[i]["bestimg"];
			datas[i]["catname"] = datas[i]["classtype"];
			datas[i]["frontimg"] = datas[i]["frontimg"];
			datas[i]["bigimg"] = datas[i]["img"];
			datas[i]["bigimgwidth"] = datas[i]["imgwidth"];
			datas[i]["bigimgheight"] = datas[i]["imgheight"];
		}catch(e){
			return false;
		}
	}
	return true;
}

function getDataById(id){
	var  rdata;
	var datas = data['data'];
	for(var i=0; i<datas.length ;i++){
		if(parseInt(datas[i]["id"]) === parseInt(id)){
			rdata = datas[i];
			break;
		}
	}
	return rdata;
}

function addKeep(id, obj){
	var tempdata = getDataById(id);
	var ids = getSCIds();
	if(tempdata['sc'] == "0"){
		if(checkIdExist(id) == -1){
			ids.push(id);
			tempdata['sc'] = "1";
			putSCIds(ids);
		}
	}else{
		var i = checkIdExist(id);
		if(i != -1){
			ids.splice(i, 1);
			tempdata['sc'] = "0"
			putSCIds(ids);
		}
	}
	
	coleToggle(obj);
	AndroidJSObject.addKeep(id);
	return false;
}


function quickSort(arr, prop, type) {
　　if (arr.length <= 1) { return arr; }
　　	var pivotIndex = Math.floor(arr.length / 2);
　　	var pivot = arr.splice(pivotIndex, 1)[0];
　　	var left = [];
　　	var right = [];
　　	for (var i = 0; i < arr.length; i++){
	  if(type == 1){
	     var date1 = new Date((arr[i][prop]+"").replace(/-/g,"/"));
	     var date2 = new Date((pivot[prop]+"").replace(/-/g,"/"));
	     if (date1.getTime() > date2.getTime()) {
　　　　　　	left.push(arr[i]);
　　　　 	 } else {
　　　　　　	right.push(arr[i]);
　　　　	 }
	  } else {
　　　　	 if (parseFloat(arr[i][prop]+"") > parseFloat(pivot[prop])+"") {
　　　　　　	left.push(arr[i]);
　　　　	 } else {
　　　　　　	right.push(arr[i]);
　　　　	 }
	  }
　　	}
　　return quickSort(left, prop, type).concat([pivot], quickSort(right, prop, type));
};



//执行滑动加载
$(window).scroll(function (e) {
    var bodyh = $("body").height();
    var scrtop = $("body").scrollTop();
    var winh = window.innerHeight;
    if (scrtop >= bodyh - winh) {
    	 page++;
    	 //暂时注释
    	 //AndroidJSObject.loadImageList(page);
    	 template3(_data);
         return false;
    }
});

function refresh(){
	template3(_data);
}