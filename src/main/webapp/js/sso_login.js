/**
 * 单点登录界面js
 */
$(document).ready(function(){

    //初始化事件
    $(document).click(function(e) {
        var t = e.target;
        if (t.id && t.id === 'flushqr') {
            if (!$.Modernizr.websockets) {
                $(t).hide();
                $.loop();
            }
        }
    });

    window.setTimeout(function(){
        if ($("#hiddenFrame")[0].attachEvent){
            $("#hiddenFrame")[0].attachEvent("onload", function(){
                checkLoginState();
            });
        } else {
            //当无刷新表单提交之后，就会触发下面的代码执行
            $("#hiddenFrame")[0].onload = function(){
                checkLoginState();
            };
        }
    }, 1000);

});

function setTab(name, cursel, n) {
    //隐藏校验提示框
    try{
        $.formValidator.hideAutoTip("1");
    }catch(e){}
    for (var i = 1; i <= n; i++) {
        var menu = document.getElementById(name + i);
        var con = document.getElementById("con_" + name + "_" + i);
        menu.className = i == cursel ? "ved" : "";
        con.style.display = i == cursel ? "block" : "none";
    }
}

function upLogin() {
    if(!($("#userName").val().length>0)){
        layer.alert("用户名不能为空", {title:"提示",icon: 5});
        return;
    }

    if(!($("#passWord").val().length>0)){
        layer.alert("密码不能为空", {title:"提示",icon: 5});
        return;
    }

    var index = layer.load(0,{shade: 0.2});

    $("#upLoginForm").submit();
}

function checkQyCa(){

}

function regist(){
    layer.open({
        type: 2,
        title: '注册',
        fix: false,
        maxmin: false,
        area: ['1100px', '500px'],
        content: '/yhgl/service/um/user/init' //说要改成跳转到各自的地址
    });
}
function resetPwd(){
    layer.open({
        type: 2,
        title: '找回密码',
        fix: false,
        maxmin: false,
        area: ['1100px', '500px'],
        content: '/yhgl/service/um/user/init/zhmm' //保证同个域名下，不然会导致不会自动关闭对话框
    });
}


function checkLoginState(){

    //检查session中是否有登录信息
    $.post(contextPath+"/auth/checkLoginState.do",{},function(d){
        layer.closeAll();
        if(d.flag == "error"){
            //如果没有会话信息则表示登录失败，尝试从hiddenFrame中获取错误信息，并且把里面的lt和execution取出来替换当前form中对应的值
            var errMsg = $("#hiddenFrame").contents().find("#errMsg").html();
            if (errMsg&&errMsg != '') {
                layer.alert(errMsg, {title:"提示",icon: 5});
            } else if (d.errMsg !== '') {
                layer.alert(d.errMsg, {title:"提示",icon: 5});
            }

        }else{
            //检查session信息发现登录成功后，做跳转，调用下面这个函数跳转到单点登录客户端页面
            loginSuccess();
        }
    });
}

/**
 * 再次触发表单，目的就是做跳转
 */
function loginSuccess(){
    var $hiddenForm = $("#hiddenForm");
    $hiddenForm.attr("action",$hiddenForm.attr("action").replace("/login",""));
    $hiddenForm.submit();
}


