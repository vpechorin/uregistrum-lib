package net.pechorina.uregistrum.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.pechorina.uregistrum.data.Endpoint;
import net.pechorina.uregistrum.data.EndpointHolder;
import net.pechorina.uregistrum.exceptions.EndpointNotFoundException;
import net.pechorina.uregistrum.exceptions.EnvironmentException;
import net.pechorina.uregistrum.exceptions.ServerErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class EndpointAccessService {
	private static final Logger logger = LoggerFactory
			.getLogger(EndpointAccessService.class);

	private String addr;
	private RestTemplate restTemplate;
	private final static String endpointsPath = "/api/endpoints";
	
	public EndpointAccessService(String addr, String username, String password, int timeout) {
		super();
		this.addr = addr;
		URI uri = null;
		try {
			uri = new URI(addr);
		} catch (URISyntaxException e) {
			logger.error("Bad URI: " + addr + " Exception: " + e);
		}
		logger.debug("Ready to build authRestTemplate, host:" + uri);
		this.restTemplate = RestTemplateMaker.getAuthRestTemplatePreAuth(uri.getHost(), uri.getPort(), uri.getScheme(), username, password, timeout);
		logger.debug("authRestTemplate built");
	}
	
	public EndpointHolder getService(String serviceName, EndpointAccessService endpointService) {
		
		Endpoint e = null;
		RestTemplate restTemplate = null;

		try {
			e = endpointService.getEndpointByName(serviceName);
		} catch (EnvironmentException ex) {
			logger.warn("Environment exception: " + ex);
		} catch (EndpointNotFoundException ex) {
			logger.error("Endpoint not found: " + ex);
		} catch (ServerErrorException ex) {
			logger.error("Server error: " + ex);
		}
		if (e != null) {
			restTemplate = RestTemplateMaker.getAuthRestTemplate(e);
		}
		
		return new EndpointHolder(e, restTemplate);
	}

	public String getAddressByName(String name) throws EnvironmentException, EndpointNotFoundException, ServerErrorException {
		Endpoint e = getEndpointByName(name);
		if (e == null) return null;
		URI u = null;
		try {
			u = new URI(e.getScheme(), null, e.getHost(), e.getPort(), e.getPath(), null, null);
		} catch (URISyntaxException e1) {
			logger.error("Cannot construct URI from endpoint: " + e + " Exception: " + e1);
		}
		return u.toString();
	}

	private void selfCheck() throws EnvironmentException {
		if (restTemplate == null)
			throw new EnvironmentException(
					"restTemplate is not defined. Please set a RestTemplate upon bean init. Use setRestTemplate(RestTemplate restTemplate)");
		if (addr == null)
			throw new EnvironmentException(
					"uRegistrum service URI is not defined. Use setUri(String uri)");
	}

	public Endpoint getEndpointByName(String name) throws EnvironmentException, EndpointNotFoundException, ServerErrorException {
		selfCheck();
		Endpoint e = null;
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("name", name);
		try {
			ResponseEntity<Endpoint> entity = restTemplate.getForEntity(addr + endpointsPath + "/{name}",
					Endpoint.class, vars);
			if (entity.getStatusCode().is2xxSuccessful()) {
				e = entity.getBody();
			}
			else if (entity.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				throw new EndpointNotFoundException("Can't find endpoint: " + name);
			}
			else if (entity.getStatusCode().is5xxServerError()) {
				logger.error("Server error: " + entity);
				throw new ServerErrorException("Server error for endpoint /" + name + "/: " + entity.getStatusCode());
			}
			else {
				throw new ServerErrorException("Server error for endpoint /" + name + "/: " + entity.getStatusCode());				
			}
		} catch (RestClientException ex) {
			logger.error("REST error:" + ex);
			throw new ServerErrorException("Server error for endpoint /" + name + "/: " + ex);
		}

		return e;
	}

	@SuppressWarnings("unchecked")
	public List<Endpoint> findEndpoints() throws EnvironmentException {
		selfCheck();
		List<Endpoint> list = new ArrayList<>();
		try {
			@SuppressWarnings("rawtypes")
			ResponseEntity<List> entity = restTemplate.getForEntity(addr + endpointsPath, List.class);
			list = entity.getBody();
		} catch (RestClientException ex) {
			logger.error("REST error:" + ex);
		}

		return list;
	}

	public void updateEndpoint(String name) throws EnvironmentException,ServerErrorException {
		Endpoint e = new Endpoint(name);
		updateEndpoint(e);
	}

	public void updateEndpoint(Endpoint e) throws EnvironmentException,ServerErrorException {
		selfCheck();
		try {
			String s = addr + endpointsPath + "/" + e.getName();
			logger.debug("rest.put: " + s);
			restTemplate.put(s, e);
		} catch (RestClientException ex) {
			logger.error("REST error:" + ex);
			throw new ServerErrorException("REST error:" + ex);
		}
	}

	public void createEndpoint(Endpoint e) throws EnvironmentException {
		selfCheck();
		try {
			String s = addr + endpointsPath;
			logger.debug("rest.post: " + s);
			restTemplate.postForLocation(s, e);
		} catch (RestClientException ex) {
			logger.error("REST error:" + ex);
		}
	}
	
	public void createEndpoint(String name, String uriStr, String username, String password) throws EnvironmentException {
		Endpoint e = new Endpoint(name, uriStr, username, password);
		createEndpoint(e);
	}
	
	public void createEndpoint(String name, String uriStr) throws EnvironmentException {
		Endpoint e = new Endpoint(name, uriStr);
		createEndpoint(e);
	}

	public void deleteEndpoint(String name) throws EnvironmentException {
		selfCheck();
		try {
			Map<String, String> vars = new HashMap<String, String>();
			vars.put("name", name);
			String s = addr + endpointsPath + "/{name}";
			logger.debug("rest.delete:" + s + " vars:" + vars);
			restTemplate.delete(s, vars);
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

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

}
