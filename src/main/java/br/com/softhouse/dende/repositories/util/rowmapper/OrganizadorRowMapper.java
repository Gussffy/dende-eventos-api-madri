package br.com.softhouse.dende.repositories.util.rowmapper;

import br.com.softhouse.dende.exceptions.MappingException;
import br.com.softhouse.dende.model.Organizador;
import br.com.softhouse.dende.model.enums.Sexo;
import br.com.softhouse.dende.repositories.util.RowMapper;

public class OrganizadorRowMapper implements RowMapper<Organizador> {

    @Override
    public Organizador mapRow(String[] row) {
        validateRow(row, 8);

        Organizador organizador = new Organizador();
        organizador.setId(RowMapperParsers.toLong(row[0]));
        organizador.setNome(RowMapperParsers.text(row[1]));
        organizador.setDataNascimento(RowMapperParsers.toLocalDate(row[2]));
        organizador.setSexo(mapSexo(row[3]));
        organizador.setEmail(RowMapperParsers.text(row[4]));
        organizador.setSenha(RowMapperParsers.text(row[5]));
        organizador.setAtivo(Boolean.TRUE.equals(RowMapperParsers.toBoolean(row[7])));
        return organizador;
    }

    private Sexo mapSexo(String value) {
        String normalized = RowMapperParsers.text(value);
        if (normalized == null) {
            return null;
        }

        try {
            switch (normalized.toUpperCase()) {
                case "M":
                    return Sexo.MASCULINO;
                case "F":
                    return Sexo.FEMININO;
                case "O":
                    return Sexo.OUTRO;
                default:
                    return Sexo.valueOf(normalized.toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            throw new MappingException("Sexo inválido: " + value, e);
        }
    }

    private void validateRow(String[] row, int expectedMinSize) {
        if (row == null || row.length < expectedMinSize) {
            throw new MappingException("Linha inválida para mapear Organizador. Esperado ao menos " + expectedMinSize + " colunas.");
        }
    }
}

