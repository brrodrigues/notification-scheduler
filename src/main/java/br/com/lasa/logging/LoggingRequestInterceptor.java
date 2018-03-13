package br.com.lasa.logging;

import br.com.lasa.logging.repository.ApplicationWebRequestTraceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

    final static Logger LOGGER = LoggerFactory.getLogger(LoggingRequestInterceptor.class);

    @Autowired
    private ApplicationWebRequestTraceRepository applicationWebRequestTraceRepository;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        traceRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        traceResponse(response);
        return response;
    }

    private void traceRequest(HttpRequest request, byte[] body) throws IOException {

        Map<String, Object> maps = new HashMap<>();

        maps.put("uri", request.getURI());
        maps.put("method", request.getMethod());
        maps.put("headers", request.getHeaders());
        maps.put("body", new String(body, "UTF-8"));

        LOGGER.debug("Saving request {}....", maps.toString());

        applicationWebRequestTraceRepository.add(maps);


    }

    private void traceResponse(ClientHttpResponse response) throws IOException {
        StringBuilder inputStringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), "UTF-8"));
        String line = bufferedReader.readLine();
        while (line != null) {
            inputStringBuilder.append(line);
            inputStringBuilder.append('\n');
            line = bufferedReader.readLine();
        }

        Map<String, Object> maps = new HashMap<>();

        maps.put("statusCode", response.getStatusCode());
        maps.put("statusText", response.getStatusText());
        maps.put("headers", response.getHeaders());
        maps.put("body", inputStringBuilder.toString());

        LOGGER.debug("Saving response :: {} :: ....", maps.toString());
        applicationWebRequestTraceRepository.add(maps);
    }
}
