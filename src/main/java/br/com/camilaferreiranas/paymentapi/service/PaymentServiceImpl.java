package br.com.camilaferreiranas.paymentapi.service;

import br.com.camilaferreiranas.paymentapi.controller.dto.PaymentSummaryResponseDTO;
import br.com.camilaferreiranas.paymentapi.controller.dto.PaymentsRequestDTO;
import br.com.camilaferreiranas.paymentapi.model.PaymentEntity;
import br.com.camilaferreiranas.paymentapi.model.ProcessorType;
import br.com.camilaferreiranas.paymentapi.repository.PaymentRepository;
import br.com.camilaferreiranas.paymentapi.service.feign.PaymentDefaultClient;
import br.com.camilaferreiranas.paymentapi.service.feign.PaymentFallbackClient;
import br.com.camilaferreiranas.paymentapi.service.feign.dto.PaymentRequest;
import br.com.camilaferreiranas.paymentapi.service.feign.dto.PaymentResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PaymentServiceImpl implements PaymentService {


    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentDefaultClient defaultClient;
    private final PaymentFallbackClient fallbackClient;
    private final HealthCheckService healthCheckService;
    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentDefaultClient defaultClient, PaymentFallbackClient fallbackClient, HealthCheckService healthCheckService, PaymentRepository paymentRepository) {
        this.defaultClient = defaultClient;
        this.fallbackClient = fallbackClient;
        this.healthCheckService = healthCheckService;
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public PaymentResponse process(PaymentsRequestDTO dto) {
        LocalDateTime now = LocalDateTime.now();
        var request = new PaymentRequest(dto.correlationId(), dto.amount(), now);
        ProcessorType processorType = chooseProcessor();


        return doProcess(processorType, request, now);
    }


    private ProcessorType chooseProcessor() {
        if (healthCheckService.isDefaultHealthy()) {
            return ProcessorType.DEFAULT;
        }
        log.info("Default unhealthy, checking fallback");
        if (healthCheckService.isFallbackHealthy()) {
            return ProcessorType.FALLBACK;
        }
        log.warn("Both unhealthy, trying default");
        return ProcessorType.DEFAULT;
    }

    @Override
    public PaymentSummaryResponseDTO summary(String from, String to) {
        LocalDateTime fromDate = from != null
                ? LocalDateTime.parse(from, DateTimeFormatter.ISO_DATE_TIME)
                : LocalDateTime.MIN;
        LocalDateTime toDate = to != null
                ? LocalDateTime.parse(to, DateTimeFormatter.ISO_DATE_TIME)
                : LocalDateTime.MAX;

        long defaultCount = paymentRepository.countByProcessorAndCreatedAtBetween(ProcessorType.DEFAULT, fromDate, toDate);
        double defaultAmount = paymentRepository.sumAmountByProcessorAndCreatedAtBetween(ProcessorType.DEFAULT, fromDate, toDate);
        long fallbackCount = paymentRepository.countByProcessorAndCreatedAtBetween(ProcessorType.FALLBACK, fromDate, toDate);
        double fallbackAmount = paymentRepository.sumAmountByProcessorAndCreatedAtBetween(ProcessorType.FALLBACK, fromDate, toDate);

        return new PaymentSummaryResponseDTO(
                new PaymentSummaryResponseDTO.ProcessorSummary(defaultCount, defaultAmount),
                new PaymentSummaryResponseDTO.ProcessorSummary(fallbackCount, fallbackAmount)
        );
    }


    private PaymentResponse doProcess(ProcessorType processorType, PaymentRequest dto, LocalDateTime now) {
        try {
            PaymentResponse response = (processorType.equals(ProcessorType.DEFAULT)) ? defaultClient.process(dto)
                    : fallbackClient.process(dto);

            PaymentEntity entity = new PaymentEntity(dto.correlationId(), dto.amount(), processorType, now);
            paymentRepository.save(entity);
            return response;
        } catch (Exception e) {
            log.error("Failed on {}: {}", processorType, e.getMessage());
            if (processorType.equals(ProcessorType.DEFAULT)) {
                log.info("Retrying on fallback");
                return doProcess(ProcessorType.FALLBACK, dto, now);
            }
            throw e;
        }
    }



}

