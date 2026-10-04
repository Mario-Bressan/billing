package com.tb.billing.domain.model;

import java.util.UUID;

/**
 * Value Object — Identificador de pedido recebido do evento Kafka.
 * Puro, sem anotações de framework.
 */
public record PedidoId(UUID valor) {

    public PedidoId {
        if (valor == null) {
            throw new IllegalArgumentException("pedidoId não pode ser nulo");
        }
    }

    public static PedidoId de(String id) {
        return new PedidoId(UUID.fromString(id));
    }
}
