package br.com.camilaferreiranas.paymentapi.config;

import feign.Client;
import feign.Request;
import feign.Retryer;
import feign.httpclient.ApacheHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class FeignConfig {


    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(2, TimeUnit.SECONDS, 4, TimeUnit.SECONDS, true);
    }

    @Bean
    public Retryer retryer() {
        return Retryer.NEVER_RETRY;
    }


    @Bean
    public Client feignClient() {
        return new ApacheHttpClient(HttpClientBuilder.create()
                .setMaxConnTotal(200)
                .setMaxConnPerRoute(100)
                .setKeepAliveStrategy((response, context) -> 5000)
                .build());
    }
}
