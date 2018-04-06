package br.com.lasa.notificacao;

import br.com.lasa.notificacao.audit.AppAuditor;
import br.com.lasa.notificacao.service.NotificacaoService;
import br.com.lasa.notificacao.util.AppConstants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
@EnableScheduling
@EnableMongoAuditing
@SpringBootApplication(scanBasePackages = "br.com.lasa")
@EnableAspectJAutoProxy
public class SistemaDeNotificaoPushApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaDeNotificaoPushApplication.class, args);

	}


	@Bean
	ForkJoinPool forkJoinPool(){
		return new ForkJoinPool(20);
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
	public ScheduledExecutorService scheduledExecutorService() {
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(250);
		return scheduledExecutorService;
	}

	@Bean
	@Primary
	public ObjectMapper jacksonObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		// mapper.configure(JsonGenerator.Route.ESCAPE_NON_ASCII, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
		//Algumas exception que podem vir a ser serializadas podem conter atributos não serializáveis
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		// TODO desabilitar timestamp passa a usar uma notação
		// ISO8061-compilant, acho que pode ser um bom formato pode ser definido outro formato também, talvez com o timezone mais simples usando a letra Z
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		mapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, false);
		mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, false);

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
	RestTemplate restTemplate(@Autowired ClientHttpRequestInterceptor clientHttpRequestInterceptor) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

		/*
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
				.loadTrustMaterial(null, acceptingTrustStrategy)
				.toLoja();
		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
		CloseableHttpClient httpClient = HttpClients.custom()
				.setSSLSocketFactory(csf)
				.toLoja();
		HttpComponentsClientHttpRequestFactory requestFactory =
				new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);
		*/

		CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);

		RestTemplate restTemplate = new RestTemplate(requestFactory);
		//restTemplate.setInterceptors(Arrays.asList(clientHttpRequestInterceptor));
		return restTemplate;
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

	@Bean(name = AppConstants.BRAZILIAN_DATETIME)
	@Scope(proxyMode = ScopedProxyMode.DEFAULT, value = "prototype")
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
		DataSource dataSource = properties.initializeDataSourceBuilder().toLoja();
		log.info("Starting datasource {} with parameters {} {} ", AppConstants.FLASH_DS, properties.getUrl(), properties.getUsername());

		if (dataSource != null) {
			log.info("Initialized datasource {}", AppConstants.FLASH_DS );
		}

		return dataSource;
	}
	**/

}