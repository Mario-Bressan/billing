package com.tb.billing.adapter.out.persistence;

import java.sql.Types;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProcessedEventJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProcessedEventJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Inserção e demais operações de negócio participam da mesma transação.
     * Uma inserção conflitante retorna 0 e indica processamento anterior confirmado.
     */
    public boolean tryInsert(UUID eventId) {
        int affected = jdbcTemplate.update(connection -> {
            var statement = connection.prepareStatement("""
                INSERT INTO billing.processed_events (event_id)
                VALUES (?)
                ON CONFLICT (event_id) DO NOTHING
                """);
            statement.setObject(1, eventId, Types.OTHER);
            return statement;
        });
        return affected == 1;
    }
}
