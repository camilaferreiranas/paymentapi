package br.com.camilaferreiranas.paymentapi.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentSummaryResponseDTO(
    @JsonProperty("default") ProcessorSummary defaultSummary,
    ProcessorSummary fallback
) {
    public record ProcessorSummary(Long totalRequests, Double totalAmount) {}
}
