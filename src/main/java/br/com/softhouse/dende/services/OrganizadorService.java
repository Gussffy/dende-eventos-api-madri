package br.com.softhouse.dende.services;

import br.com.softhouse.dende.dto.OrganizadorRequestDTO;
import br.com.softhouse.dende.dto.OrganizadorResponseDTO;
import br.com.softhouse.dende.model.Organizador;
import br.com.softhouse.dende.repositories.OrganizadorRepository;
import br.com.softhouse.dende.repositories.EventoRepository;

/**
    SERVICE DE ORGANIZADORES

    Esta classe é responsável por implementar TODAS as regras de negócio relacionadas a organizadores.
    Ela atua como uma camada intermediária entre o Controller (camada de apresentação) e o
    Repositório (camada de dados).

     Princípios aplicados:
    - Single Responsibility: Cada metodo tem uma responsabilidade única
    - Validações: Todas as regras de negócio são validadas aqui
    - Tratamento de exceções: Lança exceções com mensagens claras para o controller
 */

public class OrganizadorService {

    // Repositórios para acessar os dados de organizadores e eventos (CRUD)
    private final OrganizadorRepository organizadorRepository;
    private final EventoRepository eventoRepositorio;

    // Construtor que inicializa os repositórios
    public OrganizadorService() {

        // Obtém a instância única do repositório de organizadores e eventos (padrão Singleton)
        this.organizadorRepository = OrganizadorRepository.getInstance();
        this.eventoRepositorio = EventoRepository.getInstance();
    }

    // Cadastrar Organizador (User Stories 2)
    public OrganizadorResponseDTO cadastrar(OrganizadorRequestDTO request) throws IllegalArgumentException {

        // Validação do nome, não pode ser nulo, vazio ou conter apenas espaços
        if (request.getNome() == null || request.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        // Validação da data de nascimento, não pode ser nula
        if (request.getDataNascimento() == null) {
            throw new IllegalArgumentException("Data de nascimento é obrigatória");
        }
        // Validação do sexo, não pode ser nulo
        if (request.getSexo() == null) {
            throw new IllegalArgumentException("Sexo é obrigatório");
        }
        // Validação do email, não pode ser nulo, vazio ou conter apenas espaços
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        // Validação da senha, não pode ser nula, vazia ou conter apenas espaços
        if (request.getSenha() == null || request.getSenha().trim().isEmpty()) {
            throw new IllegalArgumentException("Senha é obrigatória");
        }

        // Validação de email único, verifica se o email já existe no repositório
        if (organizadorRepository.emailExiste(request.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso");
        }

        // Validação do CNPJ, se fornecido, deve ser único
        if (request.getCnpj() != null && !request.getCnpj().isEmpty()) {
            if (organizadorRepository.cnpjExiste(request.getCnpj())) {
                throw new IllegalArgumentException("CNPJ já está em uso");
            }
        }

        // Criação do organizador a partir do DTO e salvamento no repositório
        // O metodo toEntity() cria um novo objeto Organizador com os dados do DTO
        Organizador organizador = request.toEntity();

        // O metodo salvar() atribui um ID único ao organizador e o armazena no repositório
        organizador = organizadorRepository.salvar(organizador);

        // Retorna um DTO de resposta com os dados do organizador criado
        return new OrganizadorResponseDTO(organizador);
    }

    // Buscar Organizador por ID (User stories 4 (parcial))
    public OrganizadorResponseDTO buscarPorId(Long id) throws IllegalArgumentException {

        // Busca o organizador no repositório por ID
        Organizador organizador = organizadorRepository.buscarPorId(id);

        // Se o organizador não for encontrado, lança uma exceção com mensagem clara
        if (organizador == null) {
            throw new IllegalArgumentException("Organizador não encontrado");
        }

        // Retorna um DTO de resposta com os dados do organizador encontrado
        return new OrganizadorResponseDTO(organizador);
    }

    // Metodo auxiliar para buscar a entidade Organizador por ID, usado internamente para validações e atualizações
    public Organizador buscarEntidadePorId(Long id) throws IllegalArgumentException {
        Organizador organizador = organizadorRepository.buscarPorId(id);
        if (organizador == null) {
            throw new IllegalArgumentException("Organizador não encontrado");
        }
        return organizador;
    }

    // Atualizar Organizador (User Stories 3)
    public OrganizadorResponseDTO atualizar(Long id, OrganizadorRequestDTO request) throws IllegalArgumentException {

        // Busca o organizador (entidade) existente no repositório pelo ID
        Organizador existente = buscarEntidadePorId(id);

        // Verifica se o email do request é diferente do email existente e lança exceção, pois o email não pode ser alterado
        if (request.getEmail() != null && !request.getEmail().equals(existente.getEmail())) {
            throw new IllegalArgumentException("Não é permitido alterar o email");
        }

        // Verifica se o CNPJ do request é diferente do CNPJ existente, se sim, valida se o novo CNPJ é único e atualiza
        if (request.getCnpj() != null && !request.getCnpj().equals(existente.getCnpj())) {
            if (organizadorRepository.cnpjExiste(request.getCnpj())) {
                throw new IllegalArgumentException("CNPJ já está em uso");
            }

            // Atualiza o CNPJ do organizador existente com o novo CNPJ do request
            existente.setCnpj(request.getCnpj());
        }

        // Atualiza os campos do organizador existente apenas se eles forem fornecidos no DTO de requisição (não nulos)
        if (request.getNome() != null) existente.setNome(request.getNome());
        if (request.getDataNascimento() != null) existente.setDataNascimento(request.getDataNascimento());
        if (request.getSexo() != null) existente.setSexo(request.getSexo());
        if (request.getSenha() != null) existente.setSenha(request.getSenha());
        if (request.getRazaoSocial() != null) existente.setRazaoSocial(request.getRazaoSocial());
        if (request.getNomeFantasia() != null) existente.setNomeFantasia(request.getNomeFantasia());

        // Salva as alterações no repositório
        organizadorRepository.atualizar(existente);

        // Retorna um DTO de resposta com os dados do organizador atualizado
        return new OrganizadorResponseDTO(existente);
    }

    /** Ativar ou desativar Organizador com senha semelhante ao do UsuarioService */

    public OrganizadorResponseDTO ativarComSenha(Long id, String senha) throws IllegalArgumentException {
        Organizador organizador = buscarEntidadePorId(id);

        if (!organizador.getSenha().equals(senha)) {
            throw new IllegalArgumentException("Senha incorreta");
        }

        if (organizador.getAtivo()) {
            throw new IllegalArgumentException("Organizador já está ativo");
        }

        organizador.setAtivo(true);
        organizadorRepository.atualizar(organizador);
        return new OrganizadorResponseDTO(organizador);
    }

    public OrganizadorResponseDTO desativarComSenha(Long id, String senha) throws IllegalArgumentException {
        Organizador organizador = buscarEntidadePorId(id);

        if (!organizador.getSenha().equals(senha)) {
            throw new IllegalArgumentException("Senha incorreta");
        }

        if (!organizador.getAtivo()) {
            throw new IllegalArgumentException("Organizador já está inativo");
        }

        if (eventoRepositorio.organizadorTemEventosAtivosOuEmExecucao(id)) {
            throw new IllegalArgumentException("Não é possível desativar: organizador possui eventos ativos ou em execução");
        }

        organizador.setAtivo(false);
        organizadorRepository.atualizar(organizador);
        return new OrganizadorResponseDTO(organizador);
    }
}