package br.com.softhouse.dende.repositories.util.rowmapper;

import br.com.softhouse.dende.exceptions.MappingException;
import br.com.softhouse.dende.model.Evento;
import br.com.softhouse.dende.model.enums.ModalidadeEvento;
import br.com.softhouse.dende.model.enums.TipoEvento;
import br.com.softhouse.dende.repositories.util.RowMapper;

public class EventoRowMapper implements RowMapper<Evento> {

    @Override
    public Evento mapRow(String[] row) {
        validateRow(row, 17);

        Evento evento = new Evento();
        evento.setId(RowMapperParsers.toLong(row[0]));
        evento.setOrganizadorId(RowMapperParsers.toLong(row[1]));
        evento.setEventoPrincipalId(RowMapperParsers.toLong(row[2]));
        evento.setNome(RowMapperParsers.text(row[3]));
        evento.setDescricao(RowMapperParsers.text(row[4]));
        evento.setPagina(RowMapperParsers.text(row[5]));
        evento.setTipoEvento(mapTipoEvento(row[6]));
        evento.setModalidade(mapModalidade(row[7]));
        evento.setLocal(RowMapperParsers.text(row[8]));
        evento.setDataInicio(RowMapperParsers.toLocalDateTime(row[9]));
        evento.setDataFinal(RowMapperParsers.toLocalDateTime(row[10]));
        evento.setCapacidadeMaxima(RowMapperParsers.toInteger(row[11]));
        evento.setPrecoIngresso(RowMapperParsers.toDouble(row[12]));
        evento.setEstornaCancelamento(Boolean.TRUE.equals(RowMapperParsers.toBoolean(row[13])));
        evento.setTaxaEstorno(RowMapperParsers.toDouble(row[14]));
        evento.setAtivo(Boolean.TRUE.equals(RowMapperParsers.toBoolean(row[15])));
        return evento;
    }

    private TipoEvento mapTipoEvento(String value) {
        String normalized = RowMapperParsers.text(value);
        if (normalized == null) {
            return null;
        }

        try {
            return TipoEvento.valueOf(normalized.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new MappingException("Tipo de evento inválido: " + value, e);
        }
    }

    private ModalidadeEvento mapModalidade(String value) {
        String normalized = RowMapperParsers.text(value);
        if (normalized == null) {
            return null;
        }

        try {
            return ModalidadeEvento.valueOf(normalized.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new MappingException("Modalidade de evento inválida: " + value, e);
        }
    }

    private void validateRow(String[] row, int expectedMinSize) {
        if (row == null || row.length < expectedMinSize) {
            throw new MappingException("Linha inválida para mapear Evento. Esperado ao menos " + expectedMinSize + " colunas.");
        }
    }
}

