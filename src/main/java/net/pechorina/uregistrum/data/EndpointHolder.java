package net.pechorina.uregistrum.data;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.web.client.RestTemplate;

public class EndpointHolder implements Serializable {
	private static final long serialVersionUID = 1L;

	private String uuid;
	private Endpoint endpoint;
	private RestTemplate restTemplate;

	public EndpointHolder() {
		super();
		this.uuid = UUID.randomUUID().toString();
	}

	public EndpointHolder(Endpoint endpoint, RestTemplate restTemplate) {
		super();
		this.uuid = UUID.randomUUID().toString();
		this.endpoint = endpoint;
		this.restTemplate = restTemplate;
	}
	
	public String getURIStr() {
		if (endpoint == null) return null;
		return endpoint.getURIStr();
	}

	public Endpoint getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(Endpoint endpoint) {
		this.endpoint = endpoint;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((endpoint == null) ? 0 : endpoint.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EndpointHolder other = (EndpointHolder) obj;
		if (endpoint == null) {
			if (other.endpoint != null)
				return false;
		} else if (!endpoint.equals(other.endpoint))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EndpointHolder [endpoint=");
		builder.append(endpoint);
		builder.append("]");
		return builder.toString();
	}

}
