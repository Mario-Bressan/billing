package com.tb.billing.adapter.in.kafka;

import com.tb.billing.application.port.in.FaturarPedidoCommand;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * DTO de evento Kafka — contrato externo da mensageria.
 * Desserializado pelo Jackson. Totalmente separado do domínio.
 */
record PedidoEventPayload(
    UUID id,
    String clienteId,
    List<ItemPayload> itens,
    Instant dataCriacao,
    String status,
    BigDecimal valorTotal
) {

    record ItemPayload(
        String produto,
        int quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal
    ) {}

    /**
     * Mapeia o DTO de evento para o Command do caso de uso.
     */
    FaturarPedidoCommand toCommand() {
        return new FaturarPedidoCommand(
            id.toString(),
            clienteId,
            valorTotal
        );
    }
}
