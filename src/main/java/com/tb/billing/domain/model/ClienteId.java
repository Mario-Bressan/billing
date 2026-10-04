package com.tb.billing.domain.model;

/**
 * Value Object — Identificador do cliente.
 * Puro, sem anotações de framework.
 */
public record ClienteId(String valor) {

    public ClienteId {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("clienteId não pode ser nulo ou vazio");
        }
    }
}
