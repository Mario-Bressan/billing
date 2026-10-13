CREATE SCHEMA IF NOT EXISTS billing;

CREATE TABLE billing.processed_events (
    event_id UUID PRIMARY KEY,
    processed_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE billing.faturas (
    id UUID PRIMARY KEY,
    pedido_id UUID NOT NULL UNIQUE,
    cliente_id VARCHAR(100) NOT NULL,
    valor NUMERIC(19,2) NOT NULL CHECK (valor > 0),
    status VARCHAR(30) NOT NULL,
    criada_em TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_billing_faturas_cliente
    ON billing.faturas (cliente_id);
