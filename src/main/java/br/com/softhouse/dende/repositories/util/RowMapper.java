package br.com.softhouse.dende.repositories.util;

public interface RowMapper<T> {

    T mapRow(String[] row);
}

