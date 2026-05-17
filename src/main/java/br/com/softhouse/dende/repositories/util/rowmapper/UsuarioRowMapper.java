package br.com.softhouse.dende.repositories.util.rowmapper;

import br.com.softhouse.dende.exceptions.MappingException;
import br.com.softhouse.dende.model.Usuario;
import br.com.softhouse.dende.model.enums.Sexo;
import br.com.softhouse.dende.repositories.util.RowMapper;

public class UsuarioRowMapper implements RowMapper<Usuario> {

    @Override
    public Usuario mapRow(String[] row) {
        validateRow(row, 8);

        Usuario usuario = new Usuario();
        usuario.setId(RowMapperParsers.toLong(row[0]));
        usuario.setNome(RowMapperParsers.text(row[1]));
        usuario.setDataNascimento(RowMapperParsers.toLocalDate(row[2]));
        usuario.setSexo(mapSexo(row[3]));
        usuario.setEmail(RowMapperParsers.text(row[4]));
        usuario.setSenha(RowMapperParsers.text(row[5]));
        usuario.setAtivo(Boolean.TRUE.equals(RowMapperParsers.toBoolean(row[7])));
        return usuario;
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
            throw new MappingException("Linha inválida para mapear Usuario. Esperado ao menos " + expectedMinSize + " colunas.");
        }
    }
}

