package br.com.camilaferreiranas.paymentapi.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID correlationId;

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProcessorType processor;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public PaymentEntity() {}

    public PaymentEntity(UUID correlationId, Double amount, ProcessorType processor, LocalDateTime createdAt) {
        this.correlationId = correlationId;
        this.amount = amount;
        this.processor = processor;
        this.createdAt = createdAt;
    }

}
