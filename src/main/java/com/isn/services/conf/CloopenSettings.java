package com.isn.services.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cloopen")  
public class CloopenSettings {

	private String restUrl;
	private String restPort;
	private String accountSid;
	private String authToken;
	private String appId;
	private String voicePlayTimes;
	private String lang;
	
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
	
}
