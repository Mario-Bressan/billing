package com.tb.billing.config;

import com.tb.billing.application.port.in.FaturarPedidoUseCase;
import com.tb.billing.application.service.FaturarPedidoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração de beans da Arquitetura Hexagonal.
 * Único ponto de wiring — o resto do código não sabe que Spring existe.
 */
@Configuration
public class BillingBeanConfig {

    @Bean
    public FaturarPedidoUseCase faturarPedidoUseCase() {
        return new FaturarPedidoService();
    }
}
