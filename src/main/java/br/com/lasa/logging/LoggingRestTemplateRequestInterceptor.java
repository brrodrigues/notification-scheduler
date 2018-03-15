package br.com.lasa.logging;

import br.com.lasa.logging.repository.ApplicationWebRequestTraceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class LoggingRestTemplateRequestInterceptor implements ClientHttpRequestInterceptor {

    final static Logger LOGGER = LoggerFactory.getLogger(LoggingRestTemplateRequestInterceptor.class);

    @Autowired
    private ApplicationWebRequestTraceRepository applicationWebRequestTraceRepository;



    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        
        String ref = UUID.randomUUID().toString();
        traceRequest(ref, request);
        ClientHttpResponse response = execution.execute(request, body);
        traceResponse(ref, response);
        return response;
    }

    private void traceRequest(String ref, HttpRequest request) throws IOException {

        Map<String, Object> maps = new HashMap<>();

        maps.put("ref", ref);
        maps.put("uri", request.getURI());
        maps.put("method", request.getMethod());
        maps.put("request", request.getMethod());
        maps.put("headers", request.getHeaders());
        //maps.put("body", new String(body, "UTF-8"));

        LOGGER.debug("Intercepting  request and saving ...", maps.toString());
        applicationWebRequestTraceRepository.add(maps);
    }

    private void traceResponse(String ref, ClientHttpResponse response) throws IOException {
        StringBuilder inputStringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), "UTF-8"));
        String line ;

        while ((line = bufferedReader.readLine()) != null) {
            inputStringBuilder.append(line);
            inputStringBuilder.append('\n');
        }

        Map<String, Object> maps = new HashMap<>();

        maps.put("ref", ref);
        maps.put("statusCode", response.getStatusCode());
        maps.put("statusText", response.getStatusText());
        maps.put("headers", response.getHeaders());
        maps.put("body", inputStringBuilder.toString());

        LOGGER.debug("Intercepting response and saving ...", maps.toString());
        applicationWebRequestTraceRepository.add(maps);
    }
}
