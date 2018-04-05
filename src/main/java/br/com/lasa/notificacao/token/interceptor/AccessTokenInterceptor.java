package br.com.lasa.notificacao.token.interceptor;

import br.com.lasa.notificacao.token.domain.AccessToken;
import br.com.lasa.notificacao.token.domain.Endpoint;
import br.com.lasa.notificacao.token.service.AccessTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class AccessTokenInterceptor extends HandlerInterceptorAdapter {

	final Logger LOGGER = LoggerFactory.getLogger(AccessTokenInterceptor.class);
	
	@Autowired
	AccessTokenService accessTokenService;

	private String appName = null;
	 
	@Override
	public boolean preHandle(HttpServletRequest req,
			HttpServletResponse resp, Object handler) throws Exception {
		String reqMethod = req.getMethod();
		LOGGER.trace("RequestMethod="+reqMethod);

		/*String headerAppName = req.getHeader("x-application");*/

		if(RequestMethod.OPTIONS.toString().equals(reqMethod)){
			return true;
		}

		/*if (headerAppName == null) {
			LOGGER.warn("Propriedade appName não definida. Este interceptor será igorado!");
			return true;
		}*/
		
		//recuperar o token
		String token = getToken(req);
		if (token==null){
			throw new AccessDeniedException("Token de api não encontrado na requsição."
					+ " O mesmo deve ser passado pelo cabeçalho \"Authorization\" ou pelo parametro \"api_token\"");
		}
		
		//validar token
		validateToken(token,req);
		return true;
	}

	private String getToken(HttpServletRequest request){
		String token = null;
		//buscar o token na querystring
		token = request.getParameter("ApiToken");
		if (token != null)
			return token;
		
		//se não encontrar, buscar o token no cabeçalho Authorization
		token = request.getHeader("Authorization");
		return token;
	} 
	
	private void validateToken(String apiToken, HttpServletRequest req) throws AccessDeniedException {
		LOGGER.trace("apitoken="+apiToken);
		//recuperar o AccessToken
		AccessToken accesstoken = accessTokenService.getbyToken(apiToken);
		if(accesstoken==null){
			throw new AccessDeniedException("Token inválido");
		}
		
		//validar endpoints
		List<Endpoint> endpoints = accesstoken.getEndpoints();
		String servletPath = req.getServletPath();
		
		LOGGER.trace("servletPath:"+servletPath);
		//buscar match do servletpath com endpoints cadastrados
		for (Endpoint endpoint : endpoints) {
			LOGGER.trace("testing endpoint:" + endpoint.toString());
			if(wildcardMatch(servletPath,endpoint.getRoute())){
				LOGGER.trace("   -> Matched");
				return;
			}else{
				LOGGER.trace("   -> Not Matched");
			}
		}
		//se não encotrar
		throw new AccessDeniedException("Não é possível acessar a API solicitada com o token passado.");
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//super.postHandle(request, response, handler, modelAndView);
		response.setContentType("application/json;charset=UTF-8");
	}
	
	private static boolean wildcardMatch(String text, String pattern) {
	  return text.matches(pattern.replace("?", ".?").replace("*", ".*?"));
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	
}
