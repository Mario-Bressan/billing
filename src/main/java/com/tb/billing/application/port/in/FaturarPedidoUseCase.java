package com.tb.billing.application.port.in;

import com.tb.billing.domain.model.Fatura;

/**
 * Inbound Port — Caso de uso: Faturar Pedido.
 * Chamado pelo adaptador de entrada (Kafka Consumer).
 */
public interface FaturarPedidoUseCase {

    /**
     * Processa o faturamento de um pedido recebido via mensageria.
     *
     * @param command dados do pedido recebido
     * @param partition partição Kafka de origem
     * @param offset offset da mensagem
     * @return a fatura gerada
     */
    Fatura faturar(FaturarPedidoCommand command, int partition, long offset);
}
