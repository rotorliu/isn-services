package com.isn.services.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cloopen")  
public class CloopenSettings {

	public enum SendType{
		SMS,
		Voice
	}
	
	private String restUrl;
	private String restPort;
	private String accountSid;
	private String authToken;
	private String appId;
	private String voicePlayTimes;
	private String lang;
	private SendType sendType;
	private String smsTemplateId;
	
	public String getRestUrl() {
		return restUrl;
	}
	
	public void setRestUrl(String restUrl) {
		this.restUrl = restUrl;
	}

	public String getRestPort() {
		return restPort;
	}

	public void setRestPort(String restPort) {
		this.restPort = restPort;
	}

	public String getAccountSid() {
		return accountSid;
	}

	public void setAccountSid(String accountSid) {
		this.accountSid = accountSid;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getVoicePlayTimes() {
		return voicePlayTimes;
	}

	public void setVoicePlayTimes(String voicePlayTimes) {
		this.voicePlayTimes = voicePlayTimes;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public SendType getSendType() {
		return sendType;
	}

	public void setSendType(SendType sendType) {
		this.sendType = sendType;
	}

	public String getSMSTemplateId() {
		return smsTemplateId;
	}

	public void setSMSTemplateId(String smsTemplateId) {
		this.smsTemplateId = smsTemplateId;
	}
	
}
