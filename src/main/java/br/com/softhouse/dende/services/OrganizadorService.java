package br.com.softhouse.dende.services;

import br.com.softhouse.dende.dto.OrganizadorRequestDTO;
import br.com.softhouse.dende.dto.OrganizadorResponseDTO;
import br.com.softhouse.dende.mappers.OrganizadorMapper;
import br.com.softhouse.dende.model.Organizador;
import br.com.softhouse.dende.repositories.OrganizadorRepository;
import br.com.softhouse.dende.repositories.EventoRepository;

/**
 * SERVICE DE ORGANIZADORES

 * Esta classe é responsável por implementar TODAS as regras de negócio relacionadas a organizadores.
 * Ela atua como uma camada intermediária entre o Controller (camada de apresentação) e o
 * Repositório (camada de dados).

 * Princípios aplicados:
 * - Single Responsibility: Cada metodo tem uma responsabilidade única
 * - Validações: Todas as regras de negócio são validadas aqui
 * - Tratamento de exceções: Lança exceções com mensagens claras para o controller
 */
public class OrganizadorService {

    private final OrganizadorRepository organizadorRepository;
    private final EventoRepository eventoRepository;

    public OrganizadorService() {
        this.organizadorRepository = OrganizadorRepository.getInstance();
        this.eventoRepository = EventoRepository.getInstance();
    }

    public OrganizadorResponseDTO cadastrar(OrganizadorRequestDTO request) throws IllegalArgumentException {
        // Validações de campos obrigatórios
        if (request.getNome() == null || request.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (request.getDataNascimento() == null) {
            throw new IllegalArgumentException("Data de nascimento é obrigatória");
        }
        if (request.getSexo() == null) {
            throw new IllegalArgumentException("Sexo é obrigatório");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        if (request.getSenha() == null || request.getSenha().trim().isEmpty()) {
            throw new IllegalArgumentException("Senha é obrigatória");
        }

        // Verificar se email já existe
        if (organizadorRepository.emailExiste(request.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso");
        }

        // Se informou CNPJ, verificar se já existe (US2 - CNPJ único se fornecido)
        if (request.getCnpj() != null && !request.getCnpj().isEmpty()) {
            if (organizadorRepository.cnpjExiste(request.getCnpj())) {
                throw new IllegalArgumentException("CNPJ já está em uso");
            }
        }

        // Converter DTO para entidade
        Organizador organizador = OrganizadorMapper.toEntity(request);

        // Salvar no repositório
        organizador = organizadorRepository.salvar(organizador);

        // Retornar DTO de resposta
        return OrganizadorMapper.toResponseDTO(organizador);
    }

    public OrganizadorResponseDTO buscarPorId(Long id) throws IllegalArgumentException {
        Organizador organizador = organizadorRepository.buscarPorId(id);
        if (organizador == null) {
            throw new IllegalArgumentException("Organizador não encontrado");
        }
        return OrganizadorMapper.toResponseDTO(organizador);
    }

    public Organizador buscarEntidadePorId(Long id) throws IllegalArgumentException {
        Organizador organizador = organizadorRepository.buscarPorId(id);
        if (organizador == null) {
            throw new IllegalArgumentException("Organizador não encontrado");
        }
        return organizador;
    }

    public Organizador buscarEntidadePorEmail(String email) throws IllegalArgumentException {
        Organizador organizador = organizadorRepository.buscarPorEmail(email);
        if (organizador == null) {
            throw new IllegalArgumentException("Organizador não encontrado");
        }
        return organizador;
    }

    public OrganizadorResponseDTO atualizar(Long id, OrganizadorRequestDTO request) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("ID do organizador é obrigatório");
        }

        // Buscar organizador existente
        Organizador existente = buscarEntidadePorId(id);

        // Validar se email foi alterado (US3 - não permitido)
        if (request.getEmail() != null && !request.getEmail().equals(existente.getEmail())) {
            throw new IllegalArgumentException("Não é permitido alterar o email");
        }

        // Se alterou CNPJ, verificar se já existe
        if (request.getCnpj() != null && !request.getCnpj().equals(existente.getCnpj())) {
            if (organizadorRepository.cnpjExiste(request.getCnpj())) {
                throw new IllegalArgumentException("CNPJ já está em uso");
            }
        }

        // Atualizar campos (o mapper já valida o email)
        Organizador organizadorAtualizado = OrganizadorMapper.updateEntity(existente, request);

        // Salvar alterações
        organizadorRepository.atualizar(organizadorAtualizado);

        // Retornar DTO de resposta
        return OrganizadorMapper.toResponseDTO(organizadorAtualizado);
    }

    public OrganizadorResponseDTO ativarComSenha(Long id, String senha) throws IllegalArgumentException {
        Organizador organizador = buscarEntidadePorId(id);

        // Validar senha (US6)
        if (!organizador.getSenha().equals(senha)) {
            throw new IllegalArgumentException("Senha incorreta");
        }

        if (organizador.getAtivo()) {
            throw new IllegalArgumentException("Organizador já está ativo");
        }

        organizador.setAtivo(true);
        organizadorRepository.atualizar(organizador);

        return OrganizadorMapper.toResponseDTO(organizador);
    }

    public OrganizadorResponseDTO desativarComSenha(Long id, String senha) throws IllegalArgumentException {
        Organizador organizador = buscarEntidadePorId(id);

        // Validar senha
        if (!organizador.getSenha().equals(senha)) {
            throw new IllegalArgumentException("Senha incorreta");
        }

        if (!organizador.getAtivo()) {
            throw new IllegalArgumentException("Organizador já está inativo");
        }

        // US5: Verificar se tem eventos ativos ou em execução
        if (eventoRepository.organizadorTemEventosAtivosOuEmExecucao(id)) {
            throw new IllegalArgumentException("Não é possível desativar: organizador possui eventos ativos ou em execução");
        }

        organizador.setAtivo(false);
        organizadorRepository.atualizar(organizador);

        return OrganizadorMapper.toResponseDTO(organizador);
    }

    public boolean emailExiste(String email) {
        return organizadorRepository.emailExiste(email);
    }

    public boolean cnpjExiste(String cnpj) {
        return organizadorRepository.cnpjExiste(cnpj);
    }
}