package net.pechorina.uregistrum.service;

import net.pechorina.uregistrum.data.Endpoint;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class RestTemplateMaker {
	
	public static RestTemplate getAuthRestTemplate(String host, int port, String scheme, String username,
			String password, int timeout) {
		if (timeout == 0) timeout = 5;
		HttpHost httphost = new HttpHost(host, port, scheme);
        ClientHttpRequestFactory requestFactory = 
          new HttpComponentsClientHttpRequestFactoryBasicAuth(httphost, username, password, timeout);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
 
        return restTemplate;
	}
	
	public static RestTemplate getAuthRestTemplatePreAuth(String host, int port, String scheme, String username,
			String password, int timeout) {
		AuthScope scope = new AuthScope(host, port, AuthScope.ANY_REALM);
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(
				username, password);
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(scope, creds);
        
//        RequestConfig config = RequestConfig.custom()
//				.setConnectTimeout(timeout * 1000)
//				.setConnectionRequestTimeout(timeout * 1000)
//				.setSocketTimeout(timeout * 1000).build();
		
		CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider).build();
		HttpComponentsClientHttpRequestFactory requestFactory = new PreEmptiveAuthHttpRequestFactory( httpClient );
		
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		 
        return restTemplate;
	}
	
	public static RestTemplate getAuthRestTemplate(Endpoint endpoint) {
		return getAuthRestTemplatePreAuth(endpoint.getHost(), endpoint.getPort(), endpoint.getScheme(), endpoint.getUsername(), endpoint.getPassword(), 10);
	}
}
