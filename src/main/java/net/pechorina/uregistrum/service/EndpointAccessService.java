package net.pechorina.uregistrum.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.pechorina.uregistrum.exceptions.EnvironmentException;
import net.pechorina.uregistrum.model.Endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class EndpointAccessService {
	private static final Logger logger = LoggerFactory
			.getLogger(EndpointAccessService.class);

	private String uri;
	private RestTemplate restTemplate;
	private final static String endpointsPath = "/api/endpoints";

	public String getLocalAddressByName(String name) throws EnvironmentException {
		Endpoint e = getEndpointByName(name);
		String addr = null;
		if (e != null)
			addr = e.getLocalAddress();
		return addr;
	}
	
	public String getRemoteAddressByName(String name) throws EnvironmentException {
		Endpoint e = getEndpointByName(name);
		String addr = null;
		if (e != null)
			addr = e.getRemoteAddress();
		return addr;
	}

	private void selfCheck() throws EnvironmentException {
		if (restTemplate == null)
			throw new EnvironmentException(
					"restTemplate is not defined. Please set a RestTemplate upon bean init. Use setRestTemplate(RestTemplate restTemplate)");
		if (uri == null)
			throw new EnvironmentException(
					"uRegistrum service URI is not defined. Use setUri(String uri)");
	}

	public Endpoint getEndpointByName(String name) throws EnvironmentException {
		selfCheck();
		Endpoint e = null;
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("name", name);
		try {
			e = restTemplate.getForObject(uri + endpointsPath + "/{name}",
					Endpoint.class, vars);
			if (e != null) {
				logger.debug("Received endpoint: " + e);
			} else {
				logger.warn("Server responded but couldn't find endpoint by name: "
						+ name);
			}
		} catch (RestClientException ex) {
			logger.error("REST error:" + ex);
		}

		return e;
	}

	@SuppressWarnings("unchecked")
	public List<Endpoint> findEndpoints() throws EnvironmentException {
		selfCheck();
		List<Endpoint> list = new ArrayList<>();
		try {
			@SuppressWarnings("rawtypes")
			List l = restTemplate.getForObject(uri + endpointsPath, List.class);
			list = l;
		} catch (RestClientException ex) {
			logger.error("REST error:" + ex);
		}

		return list;
	}

	public void updateEndpoint(String name) throws EnvironmentException {
		Endpoint e = new Endpoint(name);
		updateEndpoint(e);
	}

	public void updateEndpoint(Endpoint e) throws EnvironmentException {
		selfCheck();
		try {
			restTemplate.put(uri + endpointsPath, e);
		} catch (RestClientException ex) {
			logger.error("REST error:" + ex);
		}
	}

	public void createEndpoint(Endpoint e) throws EnvironmentException {
		selfCheck();
		try {
			restTemplate.postForLocation(uri + endpointsPath, e);
		} catch (RestClientException ex) {
			logger.error("REST error:" + ex);
		}
	}
	
	public void createEndpoint(String name, String localAddress, String remoteAddress) throws EnvironmentException {
		selfCheck();
		Endpoint e = new Endpoint(name, localAddress, remoteAddress);
		try {
			restTemplate.postForLocation(uri + endpointsPath, e);
		} catch (RestClientException ex) {
			logger.error("REST error:" + ex);
		}
	}

	public void deleteEndpoint(String name) throws EnvironmentException {
		selfCheck();
		try {
			Map<String, String> vars = new HashMap<String, String>();
			vars.put("name", name);
			restTemplate.delete(uri + endpointsPath + "/{name}", vars);
		} catch (RestClientException ex) {
			logger.error("REST error:" + ex);
		}
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
}
