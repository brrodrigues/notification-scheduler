package br.com.lasa.notificacao;

import br.com.lasa.notificacao.audit.AppAuditor;
import br.com.lasa.notificacao.domain.Loja;
import br.com.lasa.notificacao.domain.UsuarioNotificacao;
import br.com.lasa.notificacao.domain.lais.BotUser;
import br.com.lasa.notificacao.domain.lais.Conversation;
import br.com.lasa.notificacao.domain.lais.Recipient;
import br.com.lasa.notificacao.service.NotificacaoService;
import br.com.lasa.notificacao.util.AppConstants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.BiConsumer;

@Slf4j
@EnableScheduling
@EnableMongoAuditing
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class SistemaDeNotificaoPushApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaDeNotificaoPushApplication.class, args);

	}

	/*
	@Bean
	@Order(1)
	@Autowired
	CommandLineRunner initializeDatabase(final UsuarioNotificacaoRepository usuarioNotificacaoRepository, final NotificacaoRepository notificacaoRepository, final EventRepository eventRepository){
		return (strings -> {
			log.info("Criando evento para teste");
			usuarioNotificacaoRepository.deleteAll();
			usuarioNotificacaoRepository.save(usuarioGustavoLais());
			notificacaoRepository.deleteAll();
			//Execucao em horario especifico
			notificacaoRepository.save(new Notification( "Evento de 10 min", 10, Collections.singleton("1"), Behavior.INTERVAL_TIME));
			//Execucao em periodo de intervalo
			Instant localDate = LocalDateTime.now().plusMinutes(2).toInstant(ZoneOffset.UTC);
			Date date = Date.from(localDate);
			notificacaoRepository.save(new Notification(String.format("Evento de %s min", date), date, Collections.singleton("1"), Behavior.SPECIFIC_TIME_AFTER) );
			log.info("Notification criado !!!!");
		});
	}
	*/



	@Bean(destroyMethod="shutdown")
	@Order(1)
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(20);
		executor.setMaxPoolSize(350);
		executor.setQueueCapacity(150);
		executor.setThreadNamePrefix("notificacao-app");
		executor.initialize();
		return executor;
	}

	@Bean
	@Order(1)
	public ScheduledExecutorService taskScheduler() {
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(250);
		return scheduledExecutorService;
	}

	@Bean
	public ObjectMapper jacksonObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		// mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
		//Algumas exception que podem vir a ser serializadas podem conter atributos não serializáveis
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		// TODO desabilitar timestamp passa a usar uma notação
		// ISO8061-compilant, acho que pode ser um bom formato pode ser definido outro formato também, talvez com o timezone mais simples usando a letra Z
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		// habilitar loose/non-standard format
		// to allow C/C++ style comments in JSON (non-standard, disabled by
		// default)
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		mapper.setDateFormat(dateFormat);

		mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
		// to allow (non-standard) unquoted field names in JSON:
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		// to allow use of apostrophes (single quotes), non standard
		mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		// pretty print. Pode ser desabilitado para diminuir o tamanho das
		// requisições
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		return mapper;
	}


	@Bean
	TimeZone timeZone() {
		return TimeZone.getTimeZone("UTC");
	}


	@Bean
	public MappingJackson2HttpMessageConverter appJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
		jsonConverter.setObjectMapper(jacksonObjectMapper());
		// definir mediatypes
		List<MediaType> lstMedia = new ArrayList<MediaType>();
		lstMedia.add(MediaType.APPLICATION_JSON_UTF8);
		lstMedia.add(MediaType.APPLICATION_JSON);
		lstMedia.add(MediaType.TEXT_HTML);
		lstMedia.add(MediaType.TEXT_PLAIN);
		lstMedia.add(MediaType.ALL);
		jsonConverter.setSupportedMediaTypes(lstMedia);
		return jsonConverter;

	}

	@Bean
	@Autowired
	CommandLineRunner clearScheduledByLocalhost(final NotificacaoService notificacaoService) {
		return (String... strings) -> {
			InetAddress inetAddress = InetAddress.getLocalHost();
			notificacaoService.releaseAllByHostname(inetAddress.getHostAddress());
		};
	}

	@Bean
    UsuarioNotificacao usuarioJonatasLais() {
		//new Recipient("mid.$cAAA7URkk_Xxmi7uHeVgWnY_Fi0fm", "facebook", BotUser.builder().id("1696672097072999").name("Jônatas Ricardo").montarEstrutura(), BotUser.builder().id("107349120032554").name("LAIS-SAC-HML").montarEstrutura(), Conversacao.builder().isGroup(false).id("1696672097072999-107349120032554").montarEstrutura(), "https://facebook.botframework.com/")

		Recipient recipient = new Recipient("mid.$cAAA7URkk_Xxmi7uHeVgWnY_Fi0fm", "facebook", BotUser.builder().id("1696672097072999").name("Jônatas Ricardo").build(), BotUser.builder().id("107349120032554").name("LAIS-SAC-HML").build(), Conversation.builder().isGroup(false).id("1696672097072999-107349120032554").build(), "https://facebook.botframework.com/");

		return new UsuarioNotificacao(recipient.getUser().getId(), "Bruno Rodrigues","L0001","", recipient, true, null);
	}

	@Bean
    UsuarioNotificacao usuarioGustavoLais() {
		//new Recipient("mid.$cAAA7UQtt0cFmq7rohFgenWfiZhZL", "facebook", BotUser.builder().id("1652887001413594").name("Gustavo Gomes").montarEstrutura(), BotUser.builder().id("107349120032554").name("LAIS-SAC-HML").montarEstrutura(), Conversacao.builder().isGroup(false).id("1652887001413594-107349120032554").montarEstrutura(),"https://facebook.botframework.com/");
		return UsuarioNotificacao.builder().status(true).storeId("1").profile(new Recipient("mid.$cAAA7UQtt0cFmq7rohFgenWfiZhZL", "facebook", BotUser.builder().id("1652887001413594").name("Gustavo Gomes").build(), BotUser.builder().id("107349120032554").name("LAIS-SAC-HML").build(), Conversation.builder().isGroup(false).id("1652887001413594-107349120032554").build(),"https://facebook.botframework.com/")).build();
	}

	@Bean
	RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

		/*
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
				.loadTrustMaterial(null, acceptingTrustStrategy)
				.montarEstrutura();
		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
		CloseableHttpClient httpClient = HttpClients.custom()
				.setSSLSocketFactory(csf)
				.montarEstrutura();
		HttpComponentsClientHttpRequestFactory requestFactory =
				new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);
		*/

		CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);

		return new RestTemplate(requestFactory);
	}

	@Bean(name = AppConstants.APP_URL)
	String getUrl() throws UnknownHostException {
		InetAddress localHost = Inet4Address.getLocalHost();
		String urlFormatted = MessageFormat.format("http://{0}/api/do-notify", localHost.getHostAddress());
		return urlFormatted;
	}

	@Bean(name = AppConstants.BRAZILIAN_LOCALE)
	Locale getBrazilianLocale(){
		return new Locale("pt", "BR");
	}

	@Bean
	AuditorAware<String> auditorAware(){
		return new AppAuditor();
	}

	@Bean
	@Qualifier(AppConstants.UTC_ZONE)
	ZoneId utcZone() {
		return ZoneId.of("UTC");
	}

	@Bean
	@Primary
	ZoneId brazilZone() {
		return ZoneId.of("America/Sao_Paulo");
	}

	@Bean
	@Scope(proxyMode = ScopedProxyMode.INTERFACES, value = "prototype")
	LocalDateTime localDateTime() {
		LocalDateTime horario = LocalDateTime.now(brazilZone());
		return horario;
	}

	/**
	@Bean(name = AppConstants.FLASH_DATASOURCE_PROPERTIES)
	@ConfigurationProperties("spring.datasource")
	public DataSourceProperties flashDBDataSourceProperties() {
		DataSourceProperties dataSource = new DataSourceProperties();
		return dataSource;
	}

	@Bean(name = AppConstants.FLASH_DS)
	@ConfigurationProperties
	DataSource flashDBDataSource(@Qualifier(AppConstants.FLASH_DATASOURCE_PROPERTIES) @Autowired DataSourceProperties properties) {
		DataSource dataSource = properties.initializeDataSourceBuilder().montarEstrutura();
		log.info("Starting datasource {} with parameters {} {} ", AppConstants.FLASH_DS, properties.getUrl(), properties.getUsername());

		if (dataSource != null) {
			log.info("Initialized datasource {}", AppConstants.FLASH_DS );
		}

		return dataSource;
	}
	**/

	@Bean(name = AppConstants.FLASH_JDBC_TEMPLATE)
	JdbcTemplate jdbcTemplateFlash(@Autowired DataSource dataSource ){
		return new JdbcTemplate(dataSource);
	}

}