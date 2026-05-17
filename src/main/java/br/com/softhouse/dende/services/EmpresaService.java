package br.com.softhouse.dende.services;

import br.com.softhouse.dende.exceptions.ConflictException;
import br.com.softhouse.dende.exceptions.NotFoundException;
import br.com.softhouse.dende.exceptions.ValidationException;
import br.com.softhouse.dende.dto.request.EmpresaRequestDTO;
import br.com.softhouse.dende.dto.response.EmpresaResponseDTO;
import br.com.softhouse.dende.mappers.EmpresaMapper;
import br.com.softhouse.dende.model.Empresa;
import br.com.softhouse.dende.repositories.EmpresaRepository;

/**
 * SERVIÇO DE EMPRESA
 *
 * Esta classe implementa toda a lógica de negócio relacionada a empresas.
 * Responsabilidades:
 * - Validações de dados (CNPJ, razão social, nome fantasia)
 * - Regras de negócio (unicidade de CNPJ, 1:1 com organizador)
 * - Orchestração entre Controller e Repository
 *
 * Padrão: Singleton
 * Lança: IllegalArgumentException para erros de negócio
 */
public class EmpresaService {

    // Instância única do repositório
    private final EmpresaRepository empresaRepository;

    /**
     * Construtor padrão que inicializa o repositório singleton
     */
    public EmpresaService() {
        this.empresaRepository = EmpresaRepository.getInstance();
    }

    /**
     * Cadastra uma nova empresa para um organizador.
     *
     * Validações:
     * - organizadorId é obrigatório
     * - CNPJ é obrigatório e deve ser único
     * - Razão social é obrigatória
     * - Nome fantasia é obrigatório
     * - Organizador não pode ter mais de uma empresa
     *
     * @param dto dados da empresa a cadastrar
     * @return EmpresaResponseDTO da empresa cadastrada
     * @throws IllegalArgumentException se alguma validação falhar
     */
    public EmpresaResponseDTO cadastrar(EmpresaRequestDTO dto) {
        // Validações
        if (dto.getOrganizadorId() == null) {
            throw new ValidationException("ID do organizador é obrigatório");
        }

        if (dto.getCnpj() == null || dto.getCnpj().trim().isEmpty()) {
            throw new ValidationException("CNPJ é obrigatório");
        }

        if (dto.getRazaoSocial() == null || dto.getRazaoSocial().trim().isEmpty()) {
            throw new ValidationException("Razão social é obrigatória");
        }

        if (dto.getNomeFantasia() == null || dto.getNomeFantasia().trim().isEmpty()) {
            throw new ValidationException("Nome fantasia é obrigatório");
        }

        // Validar unicidade do CNPJ
        if (empresaRepository.cnpjExiste(dto.getCnpj())) {
            throw new ConflictException("CNPJ já cadastrado no sistema");
        }

        // Validar que o organizador não possui outra empresa
        if (empresaRepository.organizadorTemEmpresa(dto.getOrganizadorId())) {
            throw new ConflictException("Este organizador já possui uma empresa vinculada");
        }

        // Converte DTO para entidade
        Empresa empresa = EmpresaMapper.toEntity(dto);

        // Salva no repositório
        empresa = empresaRepository.salvar(empresa);

        // Retorna o DTO com ID atribuído
        return EmpresaMapper.toDTO(empresa);
    }

    /**
     * Busca uma empresa por ID.
     *
     * @param id o ID da empresa
     * @return EmpresaDTO encontrada
     * @throws IllegalArgumentException se a empresa não existir
     */
    public EmpresaResponseDTO buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("ID inválido");
        }

        Empresa empresa = empresaRepository.buscarPorId(id);
        if (empresa == null) {
            throw new NotFoundException("Empresa não encontrada");
        }

        return EmpresaMapper.toDTO(empresa);
    }

    /**
     * Busca a empresa de um organizador específico.
     *
     * @param organizadorId o ID do organizador
     * @return EmpresaDTO do organizador ou null se não tiver empresa
     * @throws IllegalArgumentException se o ID do organizador for inválido
     */
    public EmpresaResponseDTO buscarPorOrganizadorId(Long organizadorId) {
        if (organizadorId == null || organizadorId <= 0) {
            throw new ValidationException("ID do organizador inválido");
        }

        Empresa empresa = empresaRepository.buscarPorOrganizadorId(organizadorId);
        if (empresa == null) {
            return null; // Organizador pode não ter empresa
        }

        return EmpresaMapper.toDTO(empresa);
    }

    /**
     * Busca uma empresa pelo CNPJ.
     *
     * @param cnpj o CNPJ da empresa
     * @return EmpresaDTO encontrada
     * @throws IllegalArgumentException se o CNPJ for inválido ou empresa não existir
     */
    public EmpresaResponseDTO buscarPorCnpj(String cnpj) {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            throw new ValidationException("CNPJ é obrigatório");
        }

        Empresa empresa = empresaRepository.buscarPorCnpj(cnpj);
        if (empresa == null) {
            throw new NotFoundException("Empresa com CNPJ " + cnpj + " não encontrada");
        }

        return EmpresaMapper.toDTO(empresa);
    }

    /**
     * Atualiza os dados de uma empresa existente.
     *
     * Validações:
     * - Empresa deve existir
     * - Se alterar CNPJ, deve ser único
     * - Razão social e nome fantasia são obrigatórios se fornecidos
     *
     * @param id o ID da empresa a atualizar
     * @param dto dados atualizados
     * @return EmpresaResponseDTO atualizada
     * @throws IllegalArgumentException se validação falhar
     */
    public EmpresaResponseDTO atualizar(Long id, EmpresaRequestDTO dto) {
        if (id == null || id <= 0) {
            throw new ValidationException("ID inválido");
        }

        Empresa empresa = empresaRepository.buscarPorId(id);
        if (empresa == null) {
            throw new NotFoundException("Empresa não encontrada");
        }

        // Validar CNPJ se estiver sendo alterado
        if (dto.getCnpj() != null && !dto.getCnpj().equals(empresa.getCnpj())) {
            if (empresaRepository.cnpjExiste(dto.getCnpj())) {
                throw new ConflictException("CNPJ já cadastrado no sistema");
            }
        }

        // Validar campos obrigatórios
        if (dto.getRazaoSocial() != null && dto.getRazaoSocial().trim().isEmpty()) {
            throw new ValidationException("Razão social não pode estar vazia");
        }

        if (dto.getNomeFantasia() != null && dto.getNomeFantasia().trim().isEmpty()) {
            throw new ValidationException("Nome fantasia não pode estar vazio");
        }

        // Atualizar entidade
        empresa = EmpresaMapper.updateEntity(empresa, dto);
        empresaRepository.atualizar(empresa);

        return EmpresaMapper.toDTO(empresa);
    }

    /**
     * Deleta uma empresa.
     *
     * @param id o ID da empresa a deletar
     * @throws IllegalArgumentException se a empresa não existir
     */
    public void deletar(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("ID inválido");
        }

        Empresa empresa = empresaRepository.buscarPorId(id);
        if (empresa == null) {
            throw new NotFoundException("Empresa não encontrada");
        }

        empresaRepository.deletar(id);
    }

    /**
     * Verifica se um CNPJ já está registrado.
     *
     * @param cnpj o CNPJ a verificar
     * @return true se existe, false caso contrário
     */
    public boolean cnpjExiste(String cnpj) {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            return false;
        }
        return empresaRepository.cnpjExiste(cnpj);
    }

    /**
     * Verifica se um organizador possui empresa vinculada.
     *
     * @param organizadorId o ID do organizador
     * @return true se possui empresa, false caso contrário
     */
    public boolean organizadorTemEmpresa(Long organizadorId) {
        if (organizadorId == null || organizadorId <= 0) {
            return false;
        }
        return empresaRepository.organizadorTemEmpresa(organizadorId);
    }
}

