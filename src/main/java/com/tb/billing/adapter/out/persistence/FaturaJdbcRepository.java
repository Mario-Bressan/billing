package com.tb.billing.adapter.out.persistence;

import com.tb.billing.domain.model.Fatura;
import java.sql.Types;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FaturaJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public FaturaJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Fatura fatura) {
        jdbcTemplate.update(connection -> {
            var statement = connection.prepareStatement("""
                INSERT INTO billing.faturas
                  (id, pedido_id, cliente_id, valor, status, criada_em)
                VALUES (?, ?, ?, ?, ?, ?)
                """);
            statement.setObject(1, UUID.randomUUID(), Types.OTHER);
            statement.setObject(2, fatura.pedidoId().valor(), Types.OTHER);
            statement.setString(3, fatura.clienteId().valor());
            statement.setBigDecimal(4, fatura.valor());
            statement.setString(5, fatura.status().name());
            statement.setObject(6, fatura.dataProcessamento());
            return statement;
        });
    }
}
