package com.tb.billing.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Entidade de domínio — Fatura gerada pelo serviço de billing.
 * Pura, sem anotações de framework.
 */
public class Fatura {

    private final PedidoId pedidoId;
    private final ClienteId clienteId;
    private final BigDecimal valor;
    private final Instant dataProcessamento;
    private final StatusFatura status;

    private Fatura(PedidoId pedidoId, ClienteId clienteId, BigDecimal valor, Instant dataProcessamento, StatusFatura status) {
        this.pedidoId = pedidoId;
        this.clienteId = clienteId;
        this.valor = valor;
        this.dataProcessamento = dataProcessamento;
        this.status = status;
    }

    // --- Factory method ---

    public static Fatura faturar(PedidoId pedidoId, ClienteId clienteId, BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor da fatura deve ser positivo");
        }
        return new Fatura(pedidoId, clienteId, valor, Instant.now(), StatusFatura.FATURADO);
    }

    public static Fatura comErro(PedidoId pedidoId, ClienteId clienteId, BigDecimal valor) {
        return new Fatura(pedidoId, clienteId, valor != null ? valor : BigDecimal.ZERO, Instant.now(), StatusFatura.ERRO);
    }

    // --- Getters ---

    public PedidoId pedidoId() { return pedidoId; }
    public ClienteId clienteId() { return clienteId; }
    public BigDecimal valor() { return valor; }
    public Instant dataProcessamento() { return dataProcessamento; }
    public StatusFatura status() { return status; }
}
