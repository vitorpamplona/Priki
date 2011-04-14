package org.priki.bo;

import java.io.Serializable;

public class MailConfiguration  implements Serializable {
	public static final long serialVersionUID= 1L;
	private String smtpHost;
	private String smtpUser;
	private String smtpPassword;
	private String smtpPort;
	
	private String fromMail;
	private String fromName;
	
	public String getSmtpHost() { return smtpHost; }
	public void setSmtpHost(String smtpHost) { this.smtpHost = smtpHost; }
	public String getSmtpUser() { return smtpUser; }
	public void setSmtpUser(String smtpUser) { this.smtpUser = smtpUser; }
	public String getSmtpPassword() { return smtpPassword; }
	public void setSmtpPassword(String smtpPassword) { this.smtpPassword = smtpPassword; }
	public String getSmtpPort() { return smtpPort; }
	public void setSmtpPort(String smtpPort) { this.smtpPort = smtpPort; }
	public String getFromMail() { return fromMail; }
	public void setFromMail(String fromMail) {this.fromMail = fromMail; }
	public String getFromName() { return fromName; }
	public void setFromName(String fromName) { this.fromName = fromName; }
	
	
}
