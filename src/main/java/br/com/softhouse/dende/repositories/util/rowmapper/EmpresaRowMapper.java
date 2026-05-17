package br.com.softhouse.dende.repositories.util.rowmapper;

import br.com.softhouse.dende.exceptions.MappingException;
import br.com.softhouse.dende.model.Empresa;
import br.com.softhouse.dende.repositories.util.RowMapper;

public class EmpresaRowMapper implements RowMapper<Empresa> {

    @Override
    public Empresa mapRow(String[] row) {
        validateRow(row, 5);

        Empresa empresa = new Empresa();
        empresa.setId(RowMapperParsers.toLong(row[0]));
        empresa.setOrganizadorId(RowMapperParsers.toLong(row[1]));
        empresa.setCnpj(RowMapperParsers.text(row[2]));
        empresa.setRazaoSocial(RowMapperParsers.text(row[3]));
        empresa.setNomeFantasia(RowMapperParsers.text(row[4]));
        return empresa;
    }

    private void validateRow(String[] row, int expectedMinSize) {
        if (row == null || row.length < expectedMinSize) {
            throw new MappingException("Linha inválida para mapear Empresa. Esperado ao menos " + expectedMinSize + " colunas.");
        }
    }
}

