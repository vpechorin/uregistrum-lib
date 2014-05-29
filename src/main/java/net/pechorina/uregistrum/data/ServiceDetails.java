package net.pechorina.uregistrum.data;

import org.springframework.web.client.RestTemplate;

public class ServiceDetails {
	private RestTemplate restTemplate;
	private Endpoint endpoint;

	public ServiceDetails() {
		super();
	}

	public ServiceDetails(RestTemplate restTemplate, Endpoint endpoint) {
		super();
		this.restTemplate = restTemplate;
		this.endpoint = endpoint;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public Endpoint getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(Endpoint endpoint) {
		this.endpoint = endpoint;
	}

}
