package br.com.lasa.notificacao;

import br.com.lasa.notificacao.domain.Canal;
import br.com.lasa.notificacao.domain.Notificacao;
import br.com.lasa.notificacao.domain.TimeUnit;
import br.com.lasa.notificacao.domain.lais.BotUser;
import br.com.lasa.notificacao.domain.lais.Conversation;
import br.com.lasa.notificacao.domain.lais.UserIdentification;
import br.com.lasa.notificacao.repository.ChannelRepository;
import br.com.lasa.notificacao.repository.NotificacaoRepository;
import br.com.lasa.notificacao.service.NotificacaoService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@SpringBootApplication
@Slf4j
@EnableScheduling
public class SistemaDeNotificaoPushApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaDeNotificaoPushApplication.class, args);
	}

	@Bean
	@Order(1)
	@Autowired
	CommandLineRunner initializeDatabase(final NotificacaoRepository notificacaoRepository, final ChannelRepository channelRepository){
		return (strings -> {
			log.info("Criando evento para teste");
			ObjectMapper objectMapper = new ObjectMapper();
			channelRepository.save(Canal.builder().channelId("without.sale.1min").users(Arrays.asList(objectMapper.writeValueAsString(usuarioJonatasLais()))).build());
			channelRepository.save(Canal.builder().channelId("without.sale.1min").users(Arrays.asList(objectMapper.writeValueAsString(usuarioJonatasLais()))).build());
			notificacaoRepository.save(new Notificacao("without.sale.1min", new Date(), "Check de loja sem venda entre 1 min", 1, TimeUnit.MINUTO, false ));

			log.info("Notificacao criado !!!!");
		});
	}

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
	CommandLineRunner clearScheduledByLocalhost(NotificacaoService notificacaoService) {
		return strings -> {
			InetAddress inetAddress = InetAddress.getLocalHost();
			notificacaoService.liberarTodosAgendamentoPorHostname(inetAddress.getHostAddress());
		};
	}

	@Bean
    UserIdentification usuarioJonatasLais() {
		return new UserIdentification("mid.$cAAA7URkk_Xxmi7uHeVgWnY_Fi0fm", "facebook", BotUser.builder().id("1696672097072999").name("Jônatas Ricardo").build(), BotUser.builder().id("107349120032554").name("LAIS-SAC-HML").build(), Conversation.builder().isGroup(false).id("1696672097072999-107349120032554").build(),"https://facebook.botframework.com/");
	}

	@Bean
    UserIdentification usuarioGustavoLais() {
		return new UserIdentification("mid.$cAAA7UQtt0cFmq7rohFgenWfiZhZL", "facebook", BotUser.builder().id("1652887001413594").name("Gustavo Gomes").build(), BotUser.builder().id("107349120032554").name("LAIS-SAC-HML").build(), Conversation.builder().isGroup(false).id("1652887001413594-107349120032554").build(),"https://facebook.botframework.com/");
	}


	@Bean
	RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

		/*
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
				.loadTrustMaterial(null, acceptingTrustStrategy)
				.build();
		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
		CloseableHttpClient httpClient = HttpClients.custom()
				.setSSLSocketFactory(csf)
				.build();
		HttpComponentsClientHttpRequestFactory requestFactory =
				new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);
		*/

		CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);

		return new RestTemplate(requestFactory);
	}


}
