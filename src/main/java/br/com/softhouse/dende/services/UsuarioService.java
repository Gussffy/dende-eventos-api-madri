package br.com.softhouse.dende.services;

import br.com.softhouse.dende.dto.UsuarioRequestDTO;
import br.com.softhouse.dende.dto.UsuarioResponseDTO;
import br.com.softhouse.dende.model.Usuario;
import br.com.softhouse.dende.repositories.UsuarioRepository;

/**
    SERVICE DE USUÁRIOS

    Esta classe é responsável por implementar TODAS as regras de negócio relacionadas a usuários.
    Ela atua como uma camada intermediária entre o Controller (camada de apresentação) e o
    Repositório (camada de dados).

    Princípios aplicados:
    - Single Responsibility: Cada metodo tem uma responsabilidade única
    - Validações: Todas as regras de negócio são validadas aqui
    - Tratamento de exceções: Lança exceções com mensagens claras para o controller
 */

public class UsuarioService {

    //Repositório de usuários para acessar os dados (CRUD)
    private final UsuarioRepository usuarioRepositorio;


    //Construtor que inicializa o repositório
    public UsuarioService() {

        //Obtém a instância única do repositório de usuários (padrão Singleton)
        this.usuarioRepositorio = UsuarioRepository.getInstance();
    }

    // Cadastrar Usuário Comum (User Stories 1)
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO request) throws IllegalArgumentException {

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
        if (usuarioRepositorio.emailExiste(request.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso");
        }

        // Criação do objeto Usuario a partir do DTO de requisição
        // Metodo toEntity() cria um novo objeto Usuario com os dados do DTO
        Usuario usuario = request.toEntity();

        // Salva o usuário no repositório (UsuarioRepository)
        // O metodo salvar() atribui um ID único ao usuário e o armazena no repositório
        usuario = usuarioRepositorio.salvar(usuario);

        // Retorna um DTO de resposta com os dados do usuário criado
        // O DTO não inclui a senha por questão de segurança
        return new UsuarioResponseDTO(usuario);
    }

    // Buscar Usuário por ID (User stories 4 (parcial))
    public UsuarioResponseDTO buscarPorId(Long id) throws IllegalArgumentException {

        // Busca o usuário no repositório pelo ID
        Usuario usuario = usuarioRepositorio.buscarPorId(id);

        // Se o usuário não for encontrado, lança uma exceção com mensagem clara
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        // Retorna um DTO de resposta com os dados do usuário encontrado
        return new UsuarioResponseDTO(usuario);
    }

    // Metodo auxiliar para buscar a entidade Usuario por ID, usado internamente para validações e atualizações
    public Usuario buscarEntidadePorId(Long id) throws IllegalArgumentException {
        Usuario usuario = usuarioRepositorio.buscarPorId(id);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        return usuario;
    }

    // Atualizar Usuário (User Stories 3)
    public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO request) throws IllegalArgumentException {

        // Busca o usuário (entidade) existente no repositório pelo ID
        Usuario existente = buscarEntidadePorId(id);

        // Verifica se o email no DTO de requisição é diferente do email existente, se sim, lança uma exceção
        if (request.getEmail() != null && !request.getEmail().equals(existente.getEmail())) {
            throw new IllegalArgumentException("Não é permitido alterar o email");
        }

        // Atualiza os campos do usuário existente apenas se eles forem fornecidos no DTO de requisição (não nulos)
        if (request.getNome() != null) existente.setNome(request.getNome());
        if (request.getDataNascimento() != null) existente.setDataNascimento(request.getDataNascimento());
        if (request.getSexo() != null) existente.setSexo(request.getSexo());
        if (request.getSenha() != null) existente.setSenha(request.getSenha());

        // Salva as alterações no repositório
        usuarioRepositorio.atualizar(existente);

        // Retorna o DTO de resposta com os dados do usuário atualizados
        return new UsuarioResponseDTO(existente);
    }

    // Ativar Usuário com senha (User Stories 6)
    public UsuarioResponseDTO ativarComSenha(Long id, String senha) throws IllegalArgumentException {

        // Busca o usuário (entidade com senha armazenada) no repositório pelo ID para validar a senha
        Usuario usuario = buscarEntidadePorId(id);

        // Verifica se a senha fornecida corresponde à senha armazenada do usuário, se não, lança uma exceção
        if (!usuario.getSenha().equals(senha)) {
            throw new IllegalArgumentException("Senha incorreta");
        }

        // Verifica se o usuário já está ativo, se sim, lança uma exceção
        if (usuario.getAtivo()) {
            throw new IllegalArgumentException("Usuário já está ativo");
        }

        // Ativa o usuário
        usuario.setAtivo(true);

        // Salva as alterações no repositório
        usuarioRepositorio.atualizar(usuario);

        // Retorna o DTO
        return new UsuarioResponseDTO(usuario);
    }

    // Desativar Usuário com senha (User Stories 5)
    // Metodo semelhante ao metodo de ativação, mas para desativar o usuário
    public UsuarioResponseDTO desativarComSenha(Long id, String senha) throws IllegalArgumentException {
        Usuario usuario = buscarEntidadePorId(id);

        if (!usuario.getSenha().equals(senha)) {
            throw new IllegalArgumentException("Senha incorreta");
        }

        if (!usuario.getAtivo()) {
            throw new IllegalArgumentException("Usuário já está inativo");
        }

        usuario.setAtivo(false);
        usuarioRepositorio.atualizar(usuario);
        return new UsuarioResponseDTO(usuario);
    }
}