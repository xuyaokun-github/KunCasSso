/**
 *
 */
package com.sso.server.credential;

import org.jasig.cas.authentication.Credential;

import java.io.Serializable;


/**
 *
 * @project web-sso-cas
 * @description 通用登录凭证
 * @version 1.0
 * @history 修订历史（历次修订内容、修订人、修订时间等）
 *
 */
public class BaseCredential implements Credential, Serializable  {

	/**
	 *
	 */
	private static final long serialVersionUID = 1697559315412448257L;

	String authencationHandler;//校验器

	/** 以下参数是com.sso.server.authencation.UsernamePasswordAuthencation校验器需要的 **/
	String userName;
	String passWord;
	//除了最基本的用户名和密码之外，还有其他验证需要用到的信息都可以从前端传过来
	//假如需要扩展，就通过增加字段与之对应
	String captchCode;
	String single;
	String kunTgc;
	String nsrsbh;
	String qddm;

	/**滑块验证码需要的参数**/
	String csessionid;
	String sig;
	String token;
	String scene;

	/**
	 * 单点登录时，供webflow调用
	 * @param username 用户名
	 * @param password 密码
	 * @param handler 登录验证处理器
	 * @return
	 */
	public Credential setData(String username, String password, String single, String randNum, String signData, String enCert, String handler, String kunTgc, String nsrsbh, String qddm) {
		this.userName = username;
		this.passWord = password;
		this.single = single;
		this.randNum=randNum;
		this.signData=signData;
		this.enCert=enCert;
		this.authencationHandler = handler;
		this.kunTgc=kunTgc;
		this.nsrsbh=nsrsbh;
		this.qddm=qddm;
		return this;
	}


	/**
	 * 以下参数是以前的CaAuthencation校验器需要的
	 * 需要定义多少字段，可以自由发挥
	 * **/
	String randNum;
	String signData;
	String enCert;// 加密证书

	@Override
	public String getId() {
		return this.getUserName();
	}


	public String getAuthencationHandler() {
		return authencationHandler;
	}

	public void setAuthencationHandler(String authencationHandler) {
		this.authencationHandler = authencationHandler;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getCaptchCode() {
		return captchCode;
	}

	public void setCaptchCode(String captchCode) {
		this.captchCode = captchCode;
	}

	public String getSingle() {
		return single;
	}

	public void setSingle(String single) {
		this.single = single;
	}

	public String getRandNum() {
		return randNum;
	}

	public void setRandNum(String randNum) {
		this.randNum = randNum;
	}

	public String getSignData() {
		return signData;
	}

	public void setSignData(String signData) {
		this.signData = signData;
	}

	public String getEnCert() {
		return enCert;
	}

	public void setEnCert(String enCert) {
		this.enCert = enCert;
	}

	public String getKunTgc() {
		return kunTgc;
	}

	public void setKunTgc(String kunTgc) {
		this.kunTgc = kunTgc;
	}

	public String getNsrsbh() {
		return nsrsbh;
	}

	public void setNsrsbh(String nsrsbh) {
		this.nsrsbh = nsrsbh;
	}

	public String getQddm() {
		return qddm;
	}

	public void setQddm(String qddm) {
		this.qddm = qddm;
	}

	public String getCsessionid() {
		return csessionid;
	}

	public void setCsessionid(String csessionid) {
		this.csessionid = csessionid;
	}

	public String getSig() {
		return sig;
	}

	public void setSig(String sig) {
		this.sig = sig;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getScene() {
		return scene;
	}

	public void setScene(String scene) {
		this.scene = scene;
	}
}
