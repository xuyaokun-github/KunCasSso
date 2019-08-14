$(document).ready(function(){
	
	$.formValidator.initConfig({
		formID:"form1",
		theme:'ArrowSolidBox',
		mode:'AutoTip',
		onError:function(msg){
			alert(msg)
		},
		inIframe:true
	});
		
	$("#us").formValidator({
		onShow:"请输入用户名,只有输入\"maodong\"才是对的",
		onFocus:"用户名至少6个字符,最多10个字符",
		onCorrect:"该用户名可以注册"
	}).inputValidator({
		min:6,
		max:10,
		onError:"你输入的用户名非法,请确认"
	}).regexValidator({
		regExp:"username",
		dataType:"enum",
		onError:"用户名格式不正确"
	}).ajaxValidator({
		dataType : "html",
		async : true,
		url : "http://www.51gh.net/chkuser.aspx?act=ok",
		success : function(data){
			if( data.indexOf("此用户名可以注册!") > 0 ) {
				return true
			};
			return data;
		},
		buttons: $("#button"),
		error: function(jqXHR, textStatus, errorThrown){
			alert("服务器没有返回数据，可能服务器忙，请重试"+errorThrown);
		},
		onError : "该用户名不可用，请更换用户名",
		onWait : "正在对用户名进行合法性校验，请稍候..."
	});
	
	$("#password1").formValidator({
		onShow:"请输入密码",
		onFocus:"至少1个长度",
		onCorrect:"密码合法"
	}).inputValidator({
		min:1,
		empty:{
			leftEmpty:false,
			rightEmpty:false,
			emptyError:"密码两边不能有空符号"
		},
		onError:"密码不能为空,请确认"
	});
	
	$("#password2").formValidator({
		onShow:"输再次输入密码",
		onFocus:"至少1个长度",
		onCorrect:"密码一致"
	}).inputValidator({
		min:1,
		empty:{
			leftEmpty:false,
			rightEmpty:false,
			emptyError:"重复密码两边不能有空符号"
		},
		onError:"重复密码不能为空,请确认"
	}).compareValidator({
		desID:"password1",
		operateor:"=",
		onError:"2次密码不一致,请确认"
	});
	
	$(":radio[name='xb_one']").formValidator({
		tipID:"sexTip",
		onShow:"请选择你的性别",
		onFocus:"没有第三种性别了，你选一个吧",
		onCorrect:"输入正确",
		defaultValue:["1"]
	}).inputValidator({
		min:1,
		max:1,
		onError:"性别忘记选了,请确认"
	});
	
	$("#nl").formValidator({
		onShow:"请输入的年龄（1-99岁之间）",
		onFocus:"只能输入1-99之间的数字哦",
		onCorrect:"恭喜你,你输对了"
	}).inputValidator({
		min:1,
		max:99,
		type:"value",
		onErrormin:"你输入的值必须大于等于1",
		onError:"年龄必须在1-99之间，请确认"
	}).defaultPassed();
	
	$("#csny").DateTimeMask({
		lawlessmessage:"你输入的出生日期格式错误"
	}).formValidator({
		onShow:"请输入的出生日期",
		onFocus:"请输入的出生日期，不能全部是0哦",
		onCorrect:"你输入的日期合法"
	}).inputValidator({
		min:"1900-01-01",
		max:"2000-01-01",
		type:"value",
		onError:"日期必须在\"1900-01-01\"和\"2000-01-01\"之间"
	}).defaultPassed();
	
	$("#sfzh").formValidator({
		onShow:"请输入15或18位的身份证",
		onFocus:"输入15或18位的身份证",
		onCorrect:"输入正确"
	}).functionValidator({
		fun:isCardID
	});
	
	$("#email").formValidator({
		onShow:"请输入邮箱",
		onFocus:"邮箱至少6个字符,最多100个字符",
		onCorrect:"恭喜你,你输对了",
		defaultValue:"@"
	}).inputValidator({
		min:6,
		max:100,
		onError:"你输入的邮箱长度非法,请确认"
	}).regexValidator({
		regExp:"^([\\w-.]+)@(([[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.)|(([\\w-]+.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$",
		onError:"你输入的邮箱格式不正确"
	});
	
	$("#xueli").formValidator({
		onShow:"请选择你的学历",
		onFocus:"学历必须选择",
		onCorrect:"谢谢你的配合",
		defaultValue:"a"
	}).inputValidator({
		min:1,
		onError: "你是不是忘记选择学历了!"
	}).defaultPassed();
	
	$("#ewjy").formValidator({
		onShow:"无论你输入什么都会提示你额外校验出错",
		onFocus:"如果你输入\"猫冬\"，额外校验就会成功",
		onCorrect:"额外校验成功"
	}).inputValidator({
		min:1,
		onError:"这里至少要一个字符,请确认"
	}).functionValidator({
	    fun:function(val,elem){
	        if(val=="猫冬"){
			    return true;
		    }else{
			    return "额外校验失败,想额外校验通过，请输入\"猫冬\""
		    }
		}
	});
	
	$("#Tel_country").formValidator({
		tipID:"teltip",
		onShow:"请输入国家区号",
		onFocus:"国家区号2位数字",
		onCorrect:"恭喜你,你输对了",
		defaultValue:"86"
	}).regexValidator({
		regExp:"^\\d{2}$",
		onError:"国家区号不正确"
	});
	
	$("#Tel_area").formValidator({
		tipID:"teltip",
		onShow:"请输入地区区号",
		onFocus:"地区区号3位或4位数字",
		onCorrect:"恭喜你,你输对了"
	}).regexValidator({
		regExp:"^\\d{3,4}$",
		onError:"地区区号不正确"
	});
	
	$("#Tel_number").formValidator({
		tipID:"teltip",
		onShow:"请输入电话号码",
		onFocus:"电话号码7到8位数字",
		onCorrect:"恭喜你,你输对了"
	}).regexValidator({
		regExp:"^\\d{7,8}$",
		onError:"电话号码不正确"
	});
	
	$("#Tel_ext").formValidator({
		tipID:"teltip",
		onShow:"请输入分机号码",
		onFocus:"分机号码1到5位数字",
		onCorrect:"恭喜你,你输对了"
	}).regexValidator({
		regExp:"^\\d{1,5}$",
		onError:"分机号码不正确"
	});
	
	$(":checkbox[name='xqah_one']").formValidator({
		tipID:"test3Tip",
		onShow:"请选择你的兴趣爱好（至少选一个）",
		onFocus:"你至少选择1个",
		onCorrect:"恭喜你,你选对了"
	}).inputValidator({
		min:1,
		onError:"你选的个数不对"
	});
	
	$(":checkbox[name='xqah_more']").formValidator({
		tipID:"test2Tip",
		onShow:"请选择你的兴趣爱好(至少选择2个,最多选择3个)",
		onFocus:"你至少选择2个,最多选择3个",
		onCorrect:"恭喜你,你选对了",
		defaultValue:["7","8"]
	}).inputValidator({
		min:2,
		max:3,
		onError:"你选的个数不对(至少选择2个,最多选择3个)"
	});
	
	$(":radio[name='aiguo']").formValidator({
		tipID:"aiguoTip",
		onShow:"爱国的人一定要选哦",
		onFocus:"你得认真思考哦",
		onCorrect:"不知道你爱不爱，反正你是选了",
		defaultValue:["4"]
	}).inputValidator({
		min:1,
		max:1,
		onError:"难道你不爱国？你给我选！！！！"
	}).defaultPassed();
	
	$("#shouji").formValidator({
		empty:true,
		onShow:"请输入你的手机号码，可以为空哦",
		onFocus:"你要是输入了，必须输入正确",
		onCorrect:"谢谢你的合作",
		onEmpty:"你真的不想留手机号码啊？"
	}).inputValidator({
		min:11,
		max:11,
		onError:"手机号码必须是11位的,请确认"
	}).regexValidator({
		regExp:"mobile",
		dataType:"enum",
		onError:"你输入的手机号码格式不正确"
	});
	
	$("#lxdh").formValidator({
		empty:true,
		onShow:"请输入你的联系电话，可以为空哦",
		onFocus:"格式例如：0577-88888888",
		onCorrect:"谢谢你的合作",
		onEmpty:"你真的不想留联系电话了吗？"
	}).regexValidator({
		regExp:"^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$",
		onError:"你输入的联系电话格式不正确"
	});
	
	$("#sjdh").formValidator({
		empty:true,
		onShow:"请输入你的手机或电话，可以为空哦",
		onFocus:"格式例如：0577-88888888或11位手机号码",
		onCorrect:"谢谢你的合作",
		onEmpty:"你真的不想留手机或电话了吗？"
	}).regexValidator({
		regExp:["tel","mobile"],
		dataType:"enum",
		onError:"你输入的手机或电话格式不正确"
	});
	
	$("#selectmore").formValidator({
		onShow:"按住CTRL可以多选",
		onFocus:"按住CTRL可以多选,至少选择2个",
		onCorrect:"谢谢你的合作",
		defaultValue:["0","1","2"]
	}).inputValidator({
		min:2,
		onError:"至少选择2个"
	});
	
	$("#ms").formValidator({
		onShowText:"这家伙很懒，什么都没有留下。",
		onShow:"请输入你的描述",
		onFocus:"描述至少要输入10个汉字或20个字符",
		onCorrect:"恭喜你,你输对了",
		defaultValue:"这家伙很懒，什么都没有留下。"
	}).inputValidator({
		min:20,
		onError:"你输入的描述长度不正确,请确认"
	});
	
});


function test(obj)
{
	if(obj.value=="不校验身份证")
	{
		$("#sfzh").attr("disabled",true).unFormValidator(true);
		obj.value = "校验身份证";
	}
	else
	{
		$("#sfzh").attr("disabled",false).unFormValidator(false);
		obj.value = "不校验身份证";
	}
}
function test1(obj)
{
	var initConfig = $.formValidator.getInitConfig("1");
	if(obj.value=="全角字符当做1个长度")
	{
		initConfig.wideword = false;
		obj.value = "全角字符当做2个长度";
	}
	else
	{
		initConfig.wideword = true;
		obj.value = "全角字符当做1个长度";
	}
	$('body').data(obj.validatorgroup,initConfig);
}