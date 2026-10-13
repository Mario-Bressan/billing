package com.tb.billing.application.service;

import com.tb.billing.adapter.out.persistence.FaturaJdbcRepository;
import com.tb.billing.adapter.out.persistence.ProcessedEventJdbcRepository;
import com.tb.billing.application.port.in.FaturarPedidoCommand;
import com.tb.billing.application.port.in.FaturarPedidoUseCase;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProcessarFaturaService {

    private static final Logger log = LoggerFactory.getLogger(ProcessarFaturaService.class);

    private final ProcessedEventJdbcRepository inbox;
    private final FaturarPedidoUseCase faturamento;
    private final FaturaJdbcRepository faturas;

    public ProcessarFaturaService(ProcessedEventJdbcRepository inbox,
                                 FaturarPedidoUseCase faturamento,
                                 FaturaJdbcRepository faturas) {
        this.inbox = inbox;
        this.faturamento = faturamento;
        this.faturas = faturas;
    }

    @Transactional
    public void processar(UUID eventId, FaturarPedidoCommand command, int partition, long offset) {
        if (!inbox.tryInsert(eventId)) {
            log.info("Evento já faturado, ignorando duplicação: eventId={}", eventId);
            return;
        }

        var fatura = faturamento.faturar(command, partition, offset);
        faturas.save(fatura);
        // O commit do PostgreSQL acontece ao sair do proxy transacional.
        // O listener só fará o ACK depois que este método retornar com sucesso.
    }
}
