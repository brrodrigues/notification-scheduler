package br.com.lasa.notificacao.auth.token.interceptor;

import br.com.lasa.notificacao.auth.token.domain.AccessToken;
import br.com.lasa.notificacao.auth.token.service.AccessTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class AccessTokenInterceptor extends HandlerInterceptorAdapter {

	private final Logger logger = LoggerFactory.getLogger(AccessTokenInterceptor.class);

	/*@Autowired
	@Lazy*/
	AccessTokenService accessTokenService;

	public AccessTokenInterceptor() {

		logger.info("Inciando AccessTokenInterceptor");

	}

	private String appName = null;

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
		String reqMethod = req.getMethod();
		logger.trace("RequestMethod=" + reqMethod);
		if(RequestMethod.OPTIONS.toString().equals(reqMethod)){
			return true;
		}
		if(appName==null){
			logger.warn("Propriedade appName não definida. Este interceptor será igorado!");
			return true;
		}

		//recuperar o token
		String token = getToken(req);
		if(token==null){
			throw new Exception("Token de api não encontrado na requsição."
					+ " O mesmo deve ser passado pelo cabeçalho \"Authorization\" ou pelo parametro \"api_token\"");
		}

		//validar token
		validateToken(token,req);
		return true;
	}

	private String getToken(HttpServletRequest request){
		String token = null;
		//buscar o token na querystring
		token = request.getParameter("api_token");
		if(token!=null) return token;

		//se não encontrar, buscar o token no cabeçalho Authorization
		token = request.getHeader("Authorization");
		return token;
	}

	private void validateToken(String apiToken, HttpServletRequest req) throws Exception {
		logger.trace("apitoken="+apiToken);
		//recuperar o AccessToken
		AccessToken accesstoken = accessTokenService.getByAccessKey(apiToken);
		if(accesstoken==null){
			throw new Exception("Token inválido");
		}

		//validar endpoints
		List<String> endpoints = accesstoken.getEndpoints();
		String servletPath = req.getServletPath();

		logger.trace("servletPath: {}", servletPath);
		//buscar match do servletpath com endpoints cadastrados
		for (String endpoint : endpoints) {
			logger.trace("testing endpoint: {}", endpoint);
			if(wildcardMatch(servletPath,endpoint)){
				logger.trace("   -> Matched");
				return;
			}else{
				logger.trace("   -> Not Matched");
			}
		}
		//se não encotrar
		throw new Exception("Não é possível acessar a API solicitada com o token passado.");
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//super.postHandle(request, response, handler, modelAndView);
		response.setContentType("application/json;charset=UTF-8");
	}

	private static boolean wildcardMatch(String text, String pattern){
	  return text.matches(pattern.replace("?", ".?").replace("*", ".*?"));
	}

}
