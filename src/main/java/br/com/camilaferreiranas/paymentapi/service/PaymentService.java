package br.com.camilaferreiranas.paymentapi.service;

import br.com.camilaferreiranas.paymentapi.controller.dto.PaymentSummaryResponseDTO;
import br.com.camilaferreiranas.paymentapi.controller.dto.PaymentsRequestDTO;
import br.com.camilaferreiranas.paymentapi.service.feign.dto.PaymentResponse;

public interface PaymentService {

    PaymentResponse process(PaymentsRequestDTO dto);
    PaymentSummaryResponseDTO summary(String from, String to);
}
