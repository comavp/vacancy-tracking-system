package ru.comavp.vacancyscraper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import ru.comavp.vacancyscraper.client.HHClient;

@Configuration
public class WebConfig {

    @Bean
    public HHClient hhClient() {
        String baseUrl = "https://api.hh.ru";
        return HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(WebClient.builder()
                        .baseUrl(baseUrl)
                        .build()))
                .build()
                .createClient(HHClient.class);
    }
}
