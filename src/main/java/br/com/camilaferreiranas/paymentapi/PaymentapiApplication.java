package br.com.camilaferreiranas.paymentapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PaymentapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentapiApplication.class, args);
    }

}
