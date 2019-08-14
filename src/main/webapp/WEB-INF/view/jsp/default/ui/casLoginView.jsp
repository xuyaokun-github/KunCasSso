<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="now" value="<%=new java.util.Date().getTime() %>" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>单点登录服务端登录界面</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/plugs/formvalidator/formValidator-4.1.1.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/plugs/layer-v2.2/layer/layer.js"></script>
	<script type="text/javascript" src="https://g.alicdn.com/sd/ncpc/nc.js?t=${now}"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/sso_login.js"></script>
    <link href="${pageContext.request.contextPath }/style/sso-server.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript">
		contextPath = "${pageContext.request.contextPath}";
	</script>
</head>

<body class="body_login">
<form:form method="post" id="hiddenForm" commandName="${commandName}" htmlEscape="true" cssStyle="display:none;">
    <form:errors path="*" id="errMsg" cssClass="errors" element="div" htmlEscape="false" />
    <%--<form:errors path="errCode" id="errCode" cssClass="errors" element="div" htmlEscape="false" />--%>
</form:form>
<iframe name="hiddenFrame" id="hiddenFrame" style="display: none;"></iframe>
<div id="_umfp" style="display:inline;width:1px;height:1px;overflow:hidden"></div>

<div class="login">
    <h1>KunSSOWeb</h1>
    <div class="tabtit">
        <a href="javascript:setTab('one', 1, 3);" style="width: 32%;" class="ved" id="one1">用户名登录</a>
        <a href="javascript:setTab('one', 2, 3);checkQyCa();" style="width: 32%;" id="one2" style="border-left:none;">其他登录</a>
    </div>
    <!-- commandName -> modelAttribute -->
    <div class="conbox" id="con_one_1" style=" display: block;height:200px;">
        <form:form method="post" id="upLoginForm" commandName="${commandName}" htmlEscape="true" target="hiddenFrame">
            <input type="hidden" name="lt" value="${loginTicket}" />
            <input type="hidden" name="execution" value="${flowExecutionKey}" />
            <input type="hidden" name="_eventId" value="submit" />
            <input type="hidden" name="authencationHandler" value="UsernamePasswordAuthencationHandler" />

			<input type='hidden' id='csessionid' name='csessionid'/>
			<input type='hidden' id='sig' name='sig'/>
			<input type='hidden' id='token' name='token'/>
			<input type='hidden' id='scene' name='scene'/>

            <ul>
                <li><input id="userName" name="userName" type="text" class="txt bg1" placeholder="用户名"/></li>
                <li><input id="passWord" name="passWord" type="password" class="txt bg2" placeholder="密码"/></li>
                <div class="loginInBox">
					<div class="ln">
						<div id="dom_id"></div>
					</div>
				</div>
				<li style="display:none">
					<input onkeypress="if(event.keyCode=='13'){upLogin();}" id="captchCode" name="captchCode" type="text"
                           class="yzm bg3" placeholder="验证码"/><b>
                    <img id="yzmImg" style="height: 27px;" onclick="refreshYzm();" src=""></b>
				</li>
            </ul>
            <a href="javascript:upLogin();" class="tjbtn" >登录</a>
        </form:form>
    </div>

    <div class="psbox" >
      <a href="javascript:regist();">用户注册</a>
      <a href="javascript:resetPwd();">找回密码</a>
    </div>
    <div class="footer">版权所有&nbsp;&nbsp;许耀堃&nbsp;&nbsp;业务支持热线：<span>1353341XXXX</span></div>
    <div id="layerBox"></div>
</div>
</body>
</html>