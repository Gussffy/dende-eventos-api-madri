package br.com.softhouse.dende.repositories.util;

import java.util.List;

public interface CrudRepository<T, ID> {

    T salvar(T entidade);

    void atualizar(T entidade);

    T buscarPorId(ID id);

    void deletar(ID id);

    List<T> listarTodos();
}