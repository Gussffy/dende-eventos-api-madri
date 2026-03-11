package br.com.softhouse.dende.services;

import br.com.softhouse.dende.dto.UsuarioRequestDTO;
import br.com.softhouse.dende.dto.UsuarioResponseDTO;
import br.com.softhouse.dende.mappers.UsuarioMapper;
import br.com.softhouse.dende.model.Usuario;
import br.com.softhouse.dende.repositories.UsuarioRepository;

/**
 * SERVICE DE USUÁRIOS

 * Esta classe é responsável por implementar TODAS as regras de negócio relacionadas a usuários.
 * Ela atua como uma camada intermediária entre o Controller (camada de apresentação) e o
 * Repositório (camada de dados).

 * Princípios aplicados:
 * - Single Responsibility: Cada metodo tem uma responsabilidade única
 * - Validações: Todas as regras de negócio são validadas aqui
 * - Tratamento de exceções: Lança exceções com mensagens claras para o controller
 */
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService() {

        this.usuarioRepository = UsuarioRepository.getInstance();
    }

    // Metodo para cadastrar um novo usuário
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO request) throws IllegalArgumentException {
        // Validar campos obrigatórios
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

        // Verificar se email já existe (US1 - emails únicos)
        if (usuarioRepository.emailExiste(request.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso");
        }

        // Converter DTO para entidade
        Usuario usuario = UsuarioMapper.toEntity(request);

        // Salvar no repositório
        usuario = usuarioRepository.salvar(usuario);

        // Retornar DTO de resposta
        return UsuarioMapper.toResponseDTO(usuario);
    }

    public UsuarioResponseDTO buscarPorId(Long id) throws IllegalArgumentException {
        Usuario usuario = usuarioRepository.buscarPorId(id);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        return UsuarioMapper.toResponseDTO(usuario);
    }

    public Usuario buscarEntidadePorId(Long id) throws IllegalArgumentException {
        Usuario usuario = usuarioRepository.buscarPorId(id);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        return usuario;
    }

    public Usuario buscarEntidadePorEmail(String email) throws IllegalArgumentException {
        Usuario usuario = usuarioRepository.buscarPorEmail(email);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        return usuario;
    }

    // Metodo para atualizar um usuário existente
    public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO request) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("ID do usuário é obrigatório");
        }

        // Buscar usuário existente
        Usuario existente = buscarEntidadePorId(id);

        // Validar se email foi alterado (US3 - não permitido)
        if (request.getEmail() != null && !request.getEmail().equals(existente.getEmail())) {
            throw new IllegalArgumentException("Não é permitido alterar o email");
        }

        // Atualizar campos (o mapper já valida o email)
        Usuario usuarioAtualizado = UsuarioMapper.updateEntity(existente, request);

        // Salvar alterações
        usuarioRepository.atualizar(usuarioAtualizado);

        // Retornar DTO de resposta
        return UsuarioMapper.toResponseDTO(usuarioAtualizado);
    }

    public UsuarioResponseDTO ativarComSenha(Long id, String senha) throws IllegalArgumentException {
        Usuario usuario = buscarEntidadePorId(id);

        if (!usuario.getSenha().equals(senha)) {
            throw new IllegalArgumentException("Senha incorreta");
        }

        if (usuario.getAtivo()) {
            throw new IllegalArgumentException("Usuário já está ativo");
        }

        usuario.setAtivo(true);
        usuarioRepository.atualizar(usuario);

        return UsuarioMapper.toResponseDTO(usuario);
    }

    public UsuarioResponseDTO desativarComSenha(Long id, String senha) throws IllegalArgumentException {
        Usuario usuario = buscarEntidadePorId(id);

        // Validar senha (segurança)
        if (!usuario.getSenha().equals(senha)) {
            throw new IllegalArgumentException("Senha incorreta");
        }

        if (!usuario.getAtivo()) {
            throw new IllegalArgumentException("Usuário já está inativo");
        }

        usuario.setAtivo(false);
        usuarioRepository.atualizar(usuario);

        return UsuarioMapper.toResponseDTO(usuario);
    }

    public boolean emailExiste(String email) {
        return usuarioRepository.emailExiste(email);
    }
}