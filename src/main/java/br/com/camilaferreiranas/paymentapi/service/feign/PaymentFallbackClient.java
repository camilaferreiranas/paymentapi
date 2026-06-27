package br.com.camilaferreiranas.paymentapi.service.feign;


import br.com.camilaferreiranas.paymentapi.service.feign.dto.PaymentRequest;
import br.com.camilaferreiranas.paymentapi.service.feign.dto.PaymentResponse;
import br.com.camilaferreiranas.paymentapi.service.feign.dto.ServiceHealthResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-fallback", url = "${payment.fallback.url}")
public interface PaymentFallbackClient {

    @PostMapping("/payments")
    PaymentResponse process(@RequestBody PaymentRequest request);

    @GetMapping("/payments/service-health")
    ServiceHealthResponse serviceHealth();
}
