package br.com.camilaferreiranas.paymentapi.service.feign.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentRequest(UUID correlationId, Double amount, LocalDateTime requestedAt) {
}
