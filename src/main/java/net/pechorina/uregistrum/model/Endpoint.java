package net.pechorina.uregistrum.model;

import java.io.Serializable;

import org.joda.time.DateTime;

public class Endpoint implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String description;
	private String localAddress;
	private String remoteAddress;
	private String version;
	private DateTime registered;
	private DateTime expires;

	public Endpoint(String name) {
		super();
		this.name = name;
	}

	public Endpoint(String name, String localAddress, String remoteAddress) {
		super();
		this.name = name;
		this.localAddress = localAddress;
		this.remoteAddress = remoteAddress;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocalAddress() {
		return localAddress;
	}

	public void setLocalAddress(String localAddress) {
		this.localAddress = localAddress;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public DateTime getRegistered() {
		return registered;
	}

	public void setRegistered(DateTime registered) {
		this.registered = registered;
	}

	public DateTime getExpires() {
		return expires;
	}

	public void setExpires(DateTime expires) {
		this.expires = expires;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Endpoint [name=");
		builder.append(name);
		builder.append(", description=");
		builder.append(description);
		builder.append(", localAddress=");
		builder.append(localAddress);
		builder.append(", remoteAddress=");
		builder.append(remoteAddress);
		builder.append(", version=");
		builder.append(version);
		builder.append(", registered=");
		builder.append(registered);
		builder.append(", expires=");
		builder.append(expires);
		builder.append("]");
		return builder.toString();
	}

}
