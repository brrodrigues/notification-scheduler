package br.com.lasa.notificacao.aspect;

import br.com.lasa.notificacao.aspect.repository.ApplicationTraceRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class ApplicationErrorAspects {

    @Autowired
    private ApplicationTraceRepository applicationTraceRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(ApplicationErrorAspects.class);

    @Autowired
    private ApplicationContext context;

    @Autowired
    ZoneId zoneId;

    @Pointcut("within(br.com.lasa.notificacao.service..*)")
    public void comTodasAsClassesDaAplicacao() {}

    @AfterThrowing(pointcut = "comTodasAsClassesDaAplicacao()", throwing = "ex")
    public void capturarExcecao(JoinPoint joinPoint, Throwable ex) throws Throwable {

        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] parameters = joinPoint.getArgs();

        LocalDateTime bean = context.getBean(LocalDateTime.class);

        Map<String, Object> maps = new HashMap<>();

        maps.put("className", className);
        maps.put("methodName", methodName);
        maps.put("parameters", parameters);

        ApplicationErrorTrace applicationErrorTrace = new ApplicationErrorTrace();
        applicationErrorTrace.setInfo(maps);
        applicationErrorTrace.setTimestamp(Date.from(bean.atZone(zoneId).toInstant()));

        applicationTraceRepository.save(applicationErrorTrace);

    }


}
