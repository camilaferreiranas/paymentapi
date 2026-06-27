package br.com.camilaferreiranas.paymentapi.service.feign.dto;

public record ServiceHealthResponse(Boolean failing, Integer minResponseTime) {
}
