package br.com.camilaferreiranas.paymentapi.controller;

import br.com.camilaferreiranas.paymentapi.controller.dto.PaymentsRequestDTO;
import br.com.camilaferreiranas.paymentapi.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {


    private  final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }


    @PostMapping
    public ResponseEntity<Void> process(@Valid @RequestBody PaymentsRequestDTO dto) {
        service.process(dto);
        return ResponseEntity.ok().build();
    }
}
