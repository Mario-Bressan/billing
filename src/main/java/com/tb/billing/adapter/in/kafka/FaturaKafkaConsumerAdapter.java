package com.tb.billing.adapter.in.kafka;

import com.tb.billing.application.port.in.FaturarPedidoCommand;
import com.tb.billing.application.port.in.FaturarPedidoUseCase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Adaptador Inbound — Kafka Consumer.
 * Recebe eventos do Kafka e os converte para chamadas ao caso de uso.
 * Único ponto da aplicação com anotações de framework (@KafkaListener, @Component).
 */
@Component
public class FaturaKafkaConsumerAdapter {

    private static final Logger log = LoggerFactory.getLogger(FaturaKafkaConsumerAdapter.class);

    private final FaturarPedidoUseCase faturarPedidoUseCase;

    public FaturaKafkaConsumerAdapter(FaturarPedidoUseCase faturarPedidoUseCase) {
        this.faturarPedidoUseCase = faturarPedidoUseCase;
    }

    @KafkaListener(topics = "tb-pedidos-v1", groupId = "tb-billing-group")
    public void consumir(ConsumerRecord<String, PedidoEventPayload> record) {
        PedidoEventPayload payload = record.value();

        log.debug("Evento recebido: pedidoId={}, partition={}, offset={}",
            payload.id(), record.partition(), record.offset());

        // Mapeia DTO Kafka → Command → Use Case
        FaturarPedidoCommand command = payload.toCommand();
        faturarPedidoUseCase.faturar(command, record.partition(), record.offset());
    }
}
