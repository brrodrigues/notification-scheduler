package br.com.lasa.notificacao;

import br.com.lasa.notificacao.domain.Notificacao;
import br.com.lasa.notificacao.domain.UnidadeTempo;
import br.com.lasa.notificacao.repository.NotificacaoRepository;
import br.com.lasa.notificacao.util.TempoRestanteUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@Slf4j
public class SistemaDeNotificaoPushApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaDeNotificaoPushApplication.class, args);
	}

	@Bean
	@Order(1)
	@Autowired
	CommandLineRunner initializeDatabase(final NotificacaoRepository notificacaoRepository){
		return (strings -> {
			log.info("Criando notificacao de exempo");
			notificacaoRepository.save(new Notificacao("bruno", "lais.bot", new Date(), "initial.sale", 1, UnidadeTempo.SEGUNDO));
			notificacaoRepository.save(new Notificacao("bruno", "lais.bot", new Date(), "final.sale", 15, UnidadeTempo.MINUTO));
			notificacaoRepository.save(new Notificacao("bruno", "lais.bot", new Date(), "close.store",1, UnidadeTempo.DIARIO));
			log.info("Feito!!!!");
		});
	}

	@Bean
	@Autowired
	@Order(2)
	CommandLineRunner carregarNotificao(final NotificacaoRepository notificacaoRepository, ScheduledExecutorService scheduledExecutorService) {
		return strings ->
		notificacaoRepository.findAll().parallelStream().forEach((Notificacao notificacao) -> scheduledExecutorService.scheduleAtFixedRate(() -> log.info(""), , notificacao.getDelay(), notificacao.getTimeUnit().getTimeUnit()));

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

	/**
	@Bean
	@Primary
	CommandLineRunner setTimeZone() {
		return strings -> TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));

	}**/

	@Bean(name = "jacksonObjectMapper")
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

	@Bean(name = "appJackson2HttpMessageConverter")
	public MappingJackson2HttpMessageConverter appJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
		jsonConverter.setObjectMapper(jacksonObjectMapper());
		// definir mediatypes
		List<MediaType> lstMedia = new ArrayList<MediaType>();
		lstMedia.add(MediaType.APPLICATION_JSON_UTF8);
		lstMedia.add(MediaType.TEXT_HTML);
		lstMedia.add(MediaType.TEXT_PLAIN);
		lstMedia.add(MediaType.ALL);
		jsonConverter.setSupportedMediaTypes(lstMedia);
		return jsonConverter;

	}

}
