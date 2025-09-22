package com.saju.saju_site.service.client;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Component
public class OpenAiClient {
    private final WebClient webClient;
    private final String model;

    public OpenAiClient(
            @Value("${openai.api-key}") String apiKey,
            @Value("${openai.url}") String url,
            @Value("${openai.model}") String model) {

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)   // 연결 시도 10초
                .responseTimeout(Duration.ofSeconds(30))               // 응답 대기 30초
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(30))   // 읽기 30초
                                .addHandlerLast(new WriteTimeoutHandler(30))); // 쓰기 30초

        this.webClient = WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        this.model = model;
    }

    public String getSajuResult(String prompt) {
        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(Map.of("role", "user", "content", prompt))
        );

        Map response = webClient.post()
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

        return (String) message.get("content");
    }
}
