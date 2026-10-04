package com.tb.billing.application.port.in;

import java.math.BigDecimal;

/**
 * Command de entrada — dados necessários para faturar um pedido.
 * Record puro, sem validação de framework (validação fica no domínio).
 */
public record FaturarPedidoCommand(
    String pedidoId,
    String clienteId,
    BigDecimal valorTotal
) {}
