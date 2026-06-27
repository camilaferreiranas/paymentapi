package br.com.camilaferreiranas.paymentapi.controller;


import br.com.camilaferreiranas.paymentapi.controller.dto.PaymentSummaryResponseDTO;
import br.com.camilaferreiranas.paymentapi.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/payments-summary")
public class PaymentSummaryController {

    private final PaymentService paymentService;

    public PaymentSummaryController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @GetMapping
    public ResponseEntity<PaymentSummaryResponseDTO> summary(
            @RequestParam Optional<String> from,
            @RequestParam Optional<String> to) {
        return ResponseEntity.ok(paymentService.summary(from.orElse(null), to.orElse(null)));
    }
}
