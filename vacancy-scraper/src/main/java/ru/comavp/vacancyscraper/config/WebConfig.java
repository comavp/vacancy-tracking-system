package ru.comavp.vacancyscraper.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.util.unit.DataSize;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import ru.comavp.vacancyscraper.client.HHClient;

@Configuration
@EnableConfigurationProperties(VacancyScraperProperties.class)
public class WebConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        return mapper;
    }

    @Bean
    public HHClient hhClient(ObjectMapper mapper) {
        String baseUrl = "https://api.hh.ru";
        int size = (int) DataSize.ofMegabytes(16).toBytes();
        return HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(WebClient.builder()
                        .baseUrl(baseUrl)
                        .codecs(configurer -> {
                            configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(mapper));
                            configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(mapper));
                        })
                        .exchangeStrategies(ExchangeStrategies.builder()
                                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                                .build())
                        .build()))
                .build()
                .createClient(HHClient.class);
    }
}
