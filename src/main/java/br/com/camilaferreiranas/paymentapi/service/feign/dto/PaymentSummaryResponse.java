package br.com.camilaferreiranas.paymentapi.service.feign.dto;

public record PaymentSummaryResponse(Long totalRequests, Double totalAmount, Double totalFee, Double feePerTransaction) {
}
