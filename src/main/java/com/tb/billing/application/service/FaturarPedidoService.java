package com.tb.billing.application.service;

import com.tb.billing.application.port.in.FaturarPedidoCommand;
import com.tb.billing.application.port.in.FaturarPedidoUseCase;
import com.tb.billing.domain.model.ClienteId;
import com.tb.billing.domain.model.Fatura;
import com.tb.billing.domain.model.PedidoId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Regras de domínio sem efeitos colaterais em memória.
 * A persistência e a transação são orquestradas por ProcessarFaturaService.
 */
public class FaturarPedidoService implements FaturarPedidoUseCase {

    private static final Logger log = LoggerFactory.getLogger(FaturarPedidoService.class);

    @Override
    public Fatura faturar(FaturarPedidoCommand command, int partition, long offset) {
        PedidoId pedidoId = PedidoId.de(command.pedidoId());
        ClienteId clienteId = new ClienteId(command.clienteId());
        Fatura fatura = Fatura.faturar(pedidoId, clienteId, command.valorTotal());

        log.info("FATURA | pedidoId={} | cliente={} | valor={} | partition={} | offset={}",
            fatura.pedidoId().valor(), fatura.clienteId().valor(), fatura.valor(),
            partition, offset);
        return fatura;
    }
}
