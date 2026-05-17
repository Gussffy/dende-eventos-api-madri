package br.com.softhouse.dende.services;

import br.com.softhouse.dende.dto.EmpresaDTO;
import br.com.softhouse.dende.dto.OrganizadorDTO;
import br.com.softhouse.dende.mappers.EmpresaMapper;
import br.com.softhouse.dende.mappers.OrganizadorMapper;
import br.com.softhouse.dende.model.Organizador;
import br.com.softhouse.dende.repositories.EventoRepository;
import br.com.softhouse.dende.repositories.OrganizadorRepository;

/**
 * SERVICE DE ORGANIZADORES
 *
 * Esta classe é responsável por implementar TODAS as regras de negócio relacionadas a organizadores.
 * Ela atua como uma camada intermediária entre o Controller (camada de apresentação) e o
 * Repositório (camada de dados).
 *
 * Princípios aplicados:
 * - Single Responsibility: Cada metodo tem uma responsabilidade única
 * - Validações: Todas as regras de negócio são validadas aqui
 * - Tratamento de exceções: Lança exceções com mensagens claras para o controller
 *
 * Nota: O cadastro de empresa é opcional e acontece junto com o cadastro do organizador.
 * Não existem endpoints próprios para empresa; ela é extraída do OrganizadorDTO quando enviada.
 */
public class OrganizadorService {

    private final OrganizadorRepository organizadorRepository;
    private final EventoRepository eventoRepository;
    private final EmpresaService empresaService;

    public OrganizadorService() {
        this.organizadorRepository = OrganizadorRepository.getInstance();
        this.eventoRepository = EventoRepository.getInstance();
        this.empresaService = new EmpresaService();
    }

    public OrganizadorDTO cadastrar(OrganizadorDTO dto) throws IllegalArgumentException {
        if (dto == null) {
            throw new IllegalArgumentException("Dados do organizador são obrigatórios");
        }

        // Validações de campos obrigatórios
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (dto.getDataNascimento() == null) {
            throw new IllegalArgumentException("Data de nascimento é obrigatória");
        }
        if (dto.getSexo() == null) {
            throw new IllegalArgumentException("Sexo é obrigatório");
        }
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        if (dto.getSenha() == null || dto.getSenha().trim().isEmpty()) {
            throw new IllegalArgumentException("Senha é obrigatória");
        }

        // Verificar se email já existe
        if (organizadorRepository.emailExiste(dto.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso");
        }

        validarDadosEmpresaOpcional(dto);

        // Converter DTO para entidade
        Organizador organizador = OrganizadorMapper.toEntity(dto);

        // Salvar no repositório primeiro para obter o ID
        organizador = organizadorRepository.salvar(organizador);

        // Se houver dados de empresa, cadastrá-la junto com o organizador
        if (temDadosEmpresa(dto)) {
            EmpresaDTO empresaDTO = OrganizadorMapper.toEmpresaDTO(dto, organizador.getId());
            EmpresaDTO empresaCadastrada = empresaService.cadastrar(empresaDTO);
            organizador.setEmpresa(EmpresaMapper.toEntity(empresaCadastrada));
            organizadorRepository.atualizar(organizador);
        }

        return OrganizadorMapper.toDTO(organizador);
    }

    public OrganizadorDTO buscarPorId(Long id) throws IllegalArgumentException {
        Organizador organizador = organizadorRepository.buscarPorId(id);
        if (organizador == null) {
            throw new IllegalArgumentException("Organizador não encontrado");
        }
        return OrganizadorMapper.toDTO(organizador);
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

    public OrganizadorDTO atualizar(Long id, OrganizadorDTO dto) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("ID do organizador é obrigatório");
        }
        if (dto == null) {
            throw new IllegalArgumentException("Dados do organizador são obrigatórios");
        }

        Organizador existente = buscarEntidadePorId(id);

        if (dto.getEmail() != null && !dto.getEmail().equals(existente.getEmail())) {
            throw new IllegalArgumentException("Não é permitido alterar o email");
        }

        Organizador organizadorAtualizado = OrganizadorMapper.updateEntity(existente, dto);
        organizadorRepository.atualizar(organizadorAtualizado);
        return OrganizadorMapper.toDTO(organizadorAtualizado);
    }

    public OrganizadorDTO ativarComSenha(Long id, String senha) throws IllegalArgumentException {
        Organizador organizador = buscarEntidadePorId(id);

        if (!organizador.getSenha().equals(senha)) {
            throw new IllegalArgumentException("Senha incorreta");
        }

        if (organizador.getAtivo()) {
            throw new IllegalArgumentException("Organizador já está ativo");
        }

        organizador.setAtivo(true);
        organizadorRepository.atualizar(organizador);

        return OrganizadorMapper.toDTO(organizador);
    }

    public OrganizadorDTO desativarComSenha(Long id, String senha) throws IllegalArgumentException {
        Organizador organizador = buscarEntidadePorId(id);

        if (!organizador.getSenha().equals(senha)) {
            throw new IllegalArgumentException("Senha incorreta");
        }

        if (!organizador.getAtivo()) {
            throw new IllegalArgumentException("Organizador já está inativo");
        }

        if (eventoRepository.organizadorTemEventosAtivosOuEmExecucao(id)) {
            throw new IllegalArgumentException("Não é possível desativar: organizador possui eventos ativos ou em execução");
        }

        organizador.setAtivo(false);
        organizadorRepository.atualizar(organizador);

        return OrganizadorMapper.toDTO(organizador);
    }

    public boolean emailExiste(String email) {
        return organizadorRepository.emailExiste(email);
    }

    private void validarDadosEmpresaOpcional(OrganizadorDTO dto) {
        boolean temAlgumDado = temAlgumDadoEmpresa(dto);
        if (!temAlgumDado) {
            return;
        }

        if (dto.getCnpj() == null || dto.getCnpj().trim().isEmpty()) {
            throw new IllegalArgumentException("CNPJ é obrigatório quando a empresa for informada");
        }
        if (dto.getRazaoSocial() == null || dto.getRazaoSocial().trim().isEmpty()) {
            throw new IllegalArgumentException("Razão social é obrigatória quando a empresa for informada");
        }
        if (dto.getNomeFantasia() == null || dto.getNomeFantasia().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome fantasia é obrigatório quando a empresa for informada");
        }
    }

    private boolean temDadosEmpresa(OrganizadorDTO dto) {
        return dto.getCnpj() != null && !dto.getCnpj().trim().isEmpty();
    }

    private boolean temAlgumDadoEmpresa(OrganizadorDTO dto) {
        return (dto.getCnpj() != null && !dto.getCnpj().trim().isEmpty())
                || (dto.getRazaoSocial() != null && !dto.getRazaoSocial().trim().isEmpty())
                || (dto.getNomeFantasia() != null && !dto.getNomeFantasia().trim().isEmpty());
    }
}

