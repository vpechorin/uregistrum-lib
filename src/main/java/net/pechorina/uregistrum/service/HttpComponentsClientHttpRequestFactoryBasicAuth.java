package net.pechorina.uregistrum.service;

import java.net.URI;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

public class HttpComponentsClientHttpRequestFactoryBasicAuth extends HttpComponentsClientHttpRequestFactory {
    private HttpHost host;
    private String username;
    private String password;
    private int timeout;
    
    public HttpComponentsClientHttpRequestFactoryBasicAuth(HttpHost host, String username, String password, int timeout) {
        super();
        this.host = host;
        this.username = username;
        this.password = password;
        this.timeout = timeout;
    }
 
    protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
        return createHttpContext();
    }
    
    private HttpContext createHttpContext() {
        // Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();
        // Generate BASIC scheme object and add it to the local auth cache
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(host, basicAuth);
        
        AuthScope scope = new AuthScope(host.getHostName(), host.getPort(), AuthScope.ANY_REALM);
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(
				username, password);
        
        RequestConfig config = RequestConfig.custom()
				.setConnectTimeout(timeout * 1000)
				.setConnectionRequestTimeout(timeout * 1000)
				.setSocketTimeout(timeout * 1000).build();
        
        // Add AuthCache to the execution context
        HttpClientContext context = HttpClientContext.create();
        context.setAuthCache(authCache);
        
        context.getCredentialsProvider().setCredentials(scope, creds);
        context.setRequestConfig(config);
        return context;
    }
}
