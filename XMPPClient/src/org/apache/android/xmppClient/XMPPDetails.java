package org.apache.android.xmppClient;

public class XMPPDetails {

	private String host;
	private String port;
	private String service;
	private String user;
	private String password;
	
	public XMPPDetails(String host, String port, String service, String user,
			String password) {
		super();
		this.host = host;
		this.port = port;
		this.service = service;
		this.user = user;
		this.password = password;
	}
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public void setValues(String host2, String port2, String service2,
			String username, String password2) {
		host = host2;
		port= port2;
		service=service2;
		user=username;
		password=password2;
		
	}
	
	
	
}
