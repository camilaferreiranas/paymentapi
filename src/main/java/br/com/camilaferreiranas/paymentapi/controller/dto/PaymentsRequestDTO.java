package br.com.camilaferreiranas.paymentapi.controller.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PaymentsRequestDTO(@NotNull UUID correlationId, @NotNull Double amount) {
}
