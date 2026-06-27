package br.com.camilaferreiranas.paymentapi.service.feign;


import br.com.camilaferreiranas.paymentapi.service.feign.dto.PaymentRequest;
import br.com.camilaferreiranas.paymentapi.service.feign.dto.PaymentResponse;
import br.com.camilaferreiranas.paymentapi.service.feign.dto.PaymentSummaryResponse;
import br.com.camilaferreiranas.paymentapi.service.feign.dto.ServiceHealthResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "payment-default", url = "${payment.default.url}")
public interface PaymentDefaultClient {

    @PostMapping("/payments")
    PaymentResponse process(@RequestBody PaymentRequest request);

    @GetMapping("/payments/service-health")
    ServiceHealthResponse serviceHealth();

    @GetMapping("/admin/payments-summary")
    PaymentSummaryResponse summary(@RequestParam String from, @RequestParam String to);
}
