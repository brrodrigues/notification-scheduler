package br.com.lasa.logging.web;

import org.springframework.boot.actuate.trace.TraceProperties;
import org.springframework.boot.actuate.trace.TraceRepository;
import org.springframework.boot.actuate.trace.WebRequestTraceFilter;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;

@Component
public class RequestTraceFilter extends WebRequestTraceFilter {

    private final String[] excludedEndpoints = new String[]{"/css/**", "/js/**", "/trace", "/error", "/favicon.ico"};

    RequestTraceFilter(TraceRepository repository, TraceProperties properties) {
        super(repository, properties);
    }

    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) throws ServletException {
        return Arrays.stream(excludedEndpoints)
                .anyMatch(e -> new AntPathMatcher().match(e, request.getServletPath()));
    }

    @Override
    protected void enhanceTrace(Map<String, Object> trace, HttpServletResponse response) {
        super.enhanceTrace(trace, response);
    }

    @Override
    protected void postProcessRequestHeaders(Map<String, Object> headers) {
        super.postProcessRequestHeaders(headers);
    }

    @Override
    public void setDumpRequests(boolean dumpRequests) {
        super.setDumpRequests(dumpRequests);
    }

    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        boolean shouldNotFilterAsyncDispatch = super.shouldNotFilterAsyncDispatch();
        return shouldNotFilterAsyncDispatch;
    }

    @Override
    protected Map<String, Object> getTrace(HttpServletRequest request) {
        return super.getTrace(request);
    }

    @Override
    public void setErrorAttributes(ErrorAttributes errorAttributes) {
        super.setErrorAttributes(errorAttributes);
    }
}
