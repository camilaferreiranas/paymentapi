package br.com.camilaferreiranas.paymentapi.repository;


import br.com.camilaferreiranas.paymentapi.model.PaymentEntity;
import br.com.camilaferreiranas.paymentapi.model.ProcessorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    List<PaymentEntity> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);

    long countByProcessorAndCreatedAtBetween(ProcessorType processor, LocalDateTime from, LocalDateTime to);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM PaymentEntity p WHERE p.processor = :processor AND p.createdAt BETWEEN :from AND :to")
    Double sumAmountByProcessorAndCreatedAtBetween(ProcessorType processor, LocalDateTime from, LocalDateTime to);
}
