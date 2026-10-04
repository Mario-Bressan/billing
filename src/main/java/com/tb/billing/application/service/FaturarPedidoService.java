package com.tb.billing.application.service;

import com.tb.billing.application.port.in.FaturarPedidoCommand;
import com.tb.billing.application.port.in.FaturarPedidoUseCase;
import com.tb.billing.domain.model.ClienteId;
import com.tb.billing.domain.model.Fatura;
import com.tb.billing.domain.model.PedidoId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Implementação do caso de uso FaturarPedido.
 * Regras de negócio puras, sem dependência de framework.
 * Mantém acumulador de faturamento em memória (thread-safe).
 */
public class FaturarPedidoService implements FaturarPedidoUseCase {

    private static final Logger log = LoggerFactory.getLogger(FaturarPedidoService.class);

    private final AtomicReference<BigDecimal> totalFaturado = new AtomicReference<>(BigDecimal.ZERO);

    @Override
    public Fatura faturar(FaturarPedidoCommand command, int partition, long offset) {
        // 1. Mapear command → value objects de domínio
        PedidoId pedidoId = PedidoId.de(command.pedidoId());
        ClienteId clienteId = new ClienteId(command.clienteId());

        // 2. Simular processamento de gateway de pagamento (50ms)
        simularGatewayPagamento();

        // 3. Criar entidade de domínio
        Fatura fatura = Fatura.faturar(pedidoId, clienteId, command.valorTotal());

        // 4. Acumular valor faturado (thread-safe)
        BigDecimal acumulado = totalFaturado.accumulateAndGet(
            fatura.valor(),
            BigDecimal::add
        );

        // 5. Log estruturado
        log.info("FATURA | pedidoId={} | cliente={} | valor={} | partition={} | offset={} | totalAcumulado={}",
            fatura.pedidoId().valor(),
            fatura.clienteId().valor(),
            fatura.valor(),
            partition,
            offset,
            acumulado
        );

        return fatura;
    }

    private void simularGatewayPagamento() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Simulação de gateway interrompida");
        }
    }
}
