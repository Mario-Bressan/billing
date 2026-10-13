package com.tb.billing.adapter.in.kafka;

import com.tb.billing.application.service.ProcessarFaturaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * Confirma o offset apenas depois do commit da transação PostgreSQL.
 */
@Component
public class FaturaKafkaConsumerAdapter {

    private static final Logger log = LoggerFactory.getLogger(FaturaKafkaConsumerAdapter.class);

    private final ProcessarFaturaService processarFaturaService;

    public FaturaKafkaConsumerAdapter(ProcessarFaturaService processarFaturaService) {
        this.processarFaturaService = processarFaturaService;
    }

    @KafkaListener(topics = "tb-pedidos-v1", groupId = "tb-billing-group")
    public void consumir(ConsumerRecord<String, PedidoEventPayload> record, Acknowledgment ack) {
        PedidoEventPayload payload = record.value();

        log.debug("Evento recebido: pedidoId={}, partition={}, offset={}",
            payload.id(), record.partition(), record.offset());

        processarFaturaService.processar(payload.id(), payload.toCommand(),
            record.partition(), record.offset());

        ack.acknowledge();
    }
}
