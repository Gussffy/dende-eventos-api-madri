package br.com.softhouse.dende.repositories.util.rowmapper;

import br.com.softhouse.dende.exceptions.MappingException;
import br.com.softhouse.dende.model.Ingresso;
import br.com.softhouse.dende.model.enums.StatusIngresso;
import br.com.softhouse.dende.repositories.util.RowMapper;

public class IngressoRowMapper implements RowMapper<Ingresso> {

    @Override
    public Ingresso mapRow(String[] row) {
        validateRow(row, 8);

        Ingresso ingresso = new Ingresso();
        ingresso.setId(RowMapperParsers.toLong(row[0]));
        ingresso.setUsuarioId(RowMapperParsers.toLong(row[1]));
        ingresso.setEventoId(RowMapperParsers.toLong(row[2]));
        ingresso.setValorPago(RowMapperParsers.toDouble(row[3]));
        ingresso.setStatus(mapStatus(row[4]));
        ingresso.setValorEstornado(RowMapperParsers.toDouble(row[5]));
        ingresso.setDataCompra(RowMapperParsers.toLocalDateTime(row[6]));
        ingresso.setDataCancelamento(RowMapperParsers.toLocalDateTime(row[7]));
        return ingresso;
    }

    private StatusIngresso mapStatus(String value) {
        String normalized = RowMapperParsers.text(value);
        if (normalized == null) {
            return null;
        }

        try {
            return StatusIngresso.valueOf(normalized.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new MappingException("Status de ingresso inválido: " + value, e);
        }
    }

    private void validateRow(String[] row, int expectedMinSize) {
        if (row == null || row.length < expectedMinSize) {
            throw new MappingException("Linha inválida para mapear Ingresso. Esperado ao menos " + expectedMinSize + " colunas.");
        }
    }
}

