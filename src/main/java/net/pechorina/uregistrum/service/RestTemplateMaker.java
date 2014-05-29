package net.pechorina.uregistrum.service;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class RestTemplateMaker {
	private static final Logger logger = LoggerFactory
			.getLogger(RestTemplateMaker.class);
	
	private static int instanceCount = 0;
	
	public static RestTemplate getAuthRestTemplate(String host, int port, String user,
			String password, int timeout) {
		instanceCount++;
		logger.debug("Preparing REST template #" + instanceCount + " [" + host + ":" + port + "],  user/password: [" + user + "/"
				+ password + "]");
		RequestConfig config = RequestConfig.custom()
				.setConnectTimeout(timeout * 1000)
				.setConnectionRequestTimeout(timeout * 1000)
				.setSocketTimeout(timeout * 1000).build();

		BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		
		AuthScope scope = new AuthScope(host, port, AuthScope.ANY_REALM);

		UsernamePasswordCredentials creds = new UsernamePasswordCredentials(
				user, password);
		
		credentialsProvider.setCredentials(scope, creds);
		
		CloseableHttpClient client = HttpClientBuilder.create()
				.setDefaultRequestConfig(config)
				.setDefaultCredentialsProvider(credentialsProvider).build();

		ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
				client);

		RestTemplate restTemplate = new RestTemplate(requestFactory);
		

		logger.debug(instanceCount + " restTemplate created");
		return restTemplate;
	}
}
