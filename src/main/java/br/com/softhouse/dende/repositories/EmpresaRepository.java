package br.com.softhouse.dende.repositories;

import br.com.softhouse.dende.model.Empresa;
import br.com.softhouse.dende.repositories.util.CrudRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REPOSITÓRIO DE EMPRESA
 *
 * Esta classe é responsável por gerenciar o acesso aos dados de Empresa.
 * Implementa o padrão SINGLETON, garantindo que exista apenas uma instância
 * em toda a aplicação.
 *
 * Armazena empresas em memória com índices por ID e por Organizador ID,
 * permitindo buscas rápidas e eficientes.
 *
 * Relacionamento com Organizador:
 * - 1 organizador → 0..1 empresa (UNIQUE na tabela)
 * - Uma empresa "pertence" a exatamente um organizador
 * - Deletar um organizador em cascata deleta a empresa
 */
public class EmpresaRepository implements CrudRepository<Empresa, Long> {

    // Instância única do repositório (padrão Singleton)
    private static EmpresaRepository instance;

    // Mapas para armazenar as empresas com índices para busca rápida
    private final Map<Long, Empresa> empresasPorId;                    // id → Empresa
    private final Map<Long, Empresa> empresasPorOrganizadorId;         // organizador_id → Empresa
    private final Map<String, Empresa> empresasPorCnpj;                // cnpj → Empresa

    // Variável para gerar IDs únicos automaticamente
    private long proximoId;

    /**
     * Construtor privado para impedir a criação de múltiplas instâncias.
     */
    private EmpresaRepository() {
        this.empresasPorId = new HashMap<>();
        this.empresasPorOrganizadorId = new HashMap<>();
        this.empresasPorCnpj = new HashMap<>();
        this.proximoId = 1;
    }

    /**
     * Metodo para obter a instância única do repositório (Singleton).
     * Sincronizado para garantir thread-safety.
     *
     * @return A instância única do repositório
     */
    public static synchronized EmpresaRepository getInstance() {
        if (instance == null) {
            instance = new EmpresaRepository();
        }
        return instance;
    }

    /**
     * Salva uma nova empresa ou atualiza uma existente.
     * Se a empresa não possuir ID, um novo ID é gerado automaticamente.
     *
     * @param empresa a empresa a ser salva
     * @return a empresa salva com ID atribuído
     */
    @Override
    public Empresa salvar(Empresa empresa) {
        if (empresa.getId() == null) {
            empresa.setId(proximoId++);
        }

        empresasPorId.put(empresa.getId(), empresa);
        empresasPorOrganizadorId.put(empresa.getOrganizadorId(), empresa);
        empresasPorCnpj.put(empresa.getCnpj(), empresa);

        return empresa;
    }

    /**
     * Busca uma empresa por seu ID.
     *
     * @param id o ID da empresa
     * @return a empresa encontrada, ou null se não existir
     */
    @Override
    public Empresa buscarPorId(Long id) {
        return empresasPorId.get(id);
    }

    /**
     * Busca uma empresa pelo ID do organizador.
     * Como a relação é 1:1, retorna no máximo uma empresa.
     *
     * @param organizadorId o ID do organizador
     * @return a empresa do organizador, ou null se não existir
     */
    public Empresa buscarPorOrganizadorId(Long organizadorId) {
        return empresasPorOrganizadorId.get(organizadorId);
    }

    /**
     * Busca uma empresa pelo CNPJ.
     * CNPJ é único na tabela, portanto retorna no máximo uma empresa.
     *
     * @param cnpj o CNPJ da empresa
     * @return a empresa encontrada, ou null se não existir
     */
    public Empresa buscarPorCnpj(String cnpj) {
        return empresasPorCnpj.get(cnpj);
    }

    /**
     * Verifica se um CNPJ já existe no repositório.
     *
     * @param cnpj o CNPJ a ser verificado
     * @return true se o CNPJ já está registrado, false caso contrário
     */
    public boolean cnpjExiste(String cnpj) {
        return empresasPorCnpj.containsKey(cnpj);
    }

    /**
     * Verifica se um organizador já possui uma empresa vinculada.
     *
     * @param organizadorId o ID do organizador
     * @return true se o organizador possui uma empresa, false caso contrário
     */
    public boolean organizadorTemEmpresa(Long organizadorId) {
        return empresasPorOrganizadorId.containsKey(organizadorId);
    }

    /**
     * Atualiza uma empresa existente.
     *
     * @param empresa a empresa com dados atualizados
     */
    @Override
    public void atualizar(Empresa empresa) {
        if (empresa.getId() != null) {
            Empresa existente = empresasPorId.get(empresa.getId());

            if (existente != null) {
                // Se o CNPJ foi alterado, atualiza o índice de CNPJ
                if (!existente.getCnpj().equals(empresa.getCnpj())) {
                    empresasPorCnpj.remove(existente.getCnpj());
                    empresasPorCnpj.put(empresa.getCnpj(), empresa);
                }

                // Atualiza a empresa no mapa de IDs
                empresasPorId.put(empresa.getId(), empresa);

                // Atualiza o mapa de organizador (normalmente não muda, mas deixamos a opção)
                if (!existente.getOrganizadorId().equals(empresa.getOrganizadorId())) {
                    empresasPorOrganizadorId.remove(existente.getOrganizadorId());
                    empresasPorOrganizadorId.put(empresa.getOrganizadorId(), empresa);
                }
            }
        }
    }

    /**
     * Deleta uma empresa pelo ID.
     *
     * @param id o ID da empresa a ser deletada
     */
    @Override
    public void deletar(Long id) {
        Empresa empresa = empresasPorId.remove(id);
        if (empresa != null) {
            empresasPorOrganizadorId.remove(empresa.getOrganizadorId());
            empresasPorCnpj.remove(empresa.getCnpj());
        }
    }

    /**
     * Deleta todas as empresas de um organizador (usado quando organizador é deletado em cascata).
     *
     * @param organizadorId o ID do organizador
     */
    public void deletarPorOrganizadorId(Long organizadorId) {
        Empresa empresa = empresasPorOrganizadorId.remove(organizadorId);
        if (empresa != null) {
            empresasPorId.remove(empresa.getId());
            empresasPorCnpj.remove(empresa.getCnpj());
        }
    }

    /**
     * Retorna o número total de empresas registradas.
     *
     * @return quantidade de empresas
     */
    public int contarEmpresas() {
        return empresasPorId.size();
    }

    @Override
    public List<Empresa> listarTodos() {
        return List.copyOf(empresasPorId.values());
    }
}

