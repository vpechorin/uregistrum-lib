package net.pechorina.uregistrum.service;

import net.pechorina.uregistrum.data.Endpoint;
import net.pechorina.uregistrum.data.ServiceDetails;
import net.pechorina.uregistrum.exceptions.EndpointNotFoundException;
import net.pechorina.uregistrum.exceptions.EnvironmentException;
import net.pechorina.uregistrum.exceptions.ServerErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

public class ServiceDetailsService {
	private static final Logger logger = LoggerFactory
			.getLogger(ServiceDetailsService.class);
	
	private EndpointAccessService endpointAccessService;
	
	private Environment env;
	
	public ServiceDetailsService() {
		super();
	}

	public ServiceDetailsService(EndpointAccessService endpointAccessService,
			Environment env) {
		super();
		this.endpointAccessService = endpointAccessService;
		this.env = env;
	}


	public ServiceDetails getServiceDetails(String name, String localRemote) throws EndpointNotFoundException,ServerErrorException  {
		
		Endpoint e = null;
		try {
			e = endpointAccessService.getEndpointByName(name);
		} catch (EnvironmentException ex) {
			logger.error("Error getting endpoint: " + name + " Exception:" + ex);
		}
		
		int timeout = 10;
		if (env.containsProperty("httpclient.timeout")) {
			Integer t = env.getProperty("httpclient.timeout", Integer.class);
			if (t != null) timeout = t;
		}
		String host = e.getLocalDomain();
		if (localRemote.equalsIgnoreCase("remote")) host = e.getRemoteDomain();
		int port = e.getPort();
		String user = e.getUsername();
		String password = e.getPassword();
		RestTemplate restTemplate = RestTemplateMaker.getAuthRestTemplate(host, port, user, password, timeout);
		
		return new ServiceDetails(restTemplate, e);
	}

	public EndpointAccessService getEndpointAccessService() {
		return endpointAccessService;
	}

	public void setEndpointAccessService(EndpointAccessService endpointAccessService) {
		this.endpointAccessService = endpointAccessService;
	}

	public Environment getEnv() {
		return env;
	}

	public void setEnv(Environment env) {
		this.env = env;
	}

}
