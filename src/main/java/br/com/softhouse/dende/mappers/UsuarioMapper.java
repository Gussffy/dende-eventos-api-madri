package br.com.softhouse.dende.mappers;

import br.com.softhouse.dende.dto.UsuarioDTO;
import br.com.softhouse.dende.model.Usuario;

/**
 * Mapper para converter entre as classes de modelo (entidade) e as classes de DTO (Data Transfer Object).
 * Esta classe é responsável por transformar os dados entre as camadas de apresentação (DTOs) e a camada de negócio (entidades).
 */

// Mapper para converter entre Usuario, UsuarioDTO
public class UsuarioMapper {

    private UsuarioMapper() {}

    // Converte um UsuarioDTO para um objeto Usuario (entidade)
    public static Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) return null; // Verificação de null para evitar NullPointerException

        // Criar um novo objeto Usuario e preencher seus campos com os dados do DTO
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setDataNascimento(dto.getDataNascimento());
        usuario.setSexo(dto.getSexo());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        usuario.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);

        return usuario;
    }

    // Converte um objeto Usuario (entidade) para um UsuarioDTO
    public static UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) return null;

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setDataNascimento(usuario.getDataNascimento());
        dto.setIdade(usuario.getIdade());
        dto.setSexo(usuario.getSexo());
        dto.setEmail(usuario.getEmail());
        dto.setAtivo(usuario.getAtivo());
        // Senha NÃO é copiada para o DTO (segurança)

        return dto;
    }

    // Atualiza os campos de um objeto Usuario com os dados de um UsuarioDTO (usado para update)
    public static Usuario updateEntity(Usuario usuario, UsuarioDTO dto) {

        if (dto.getEmail() != null && !dto.getEmail().equals(usuario.getEmail())) {
            throw new IllegalArgumentException("Não é permitido alterar o email");
        }
        if (dto.getNome() != null) {
            usuario.setNome(dto.getNome());
        }
        if (dto.getDataNascimento() != null) {
            usuario.setDataNascimento(dto.getDataNascimento());
        }
        if (dto.getSexo() != null) {
            usuario.setSexo(dto.getSexo());
        }
        if (dto.getSenha() != null) {
            usuario.setSenha(dto.getSenha());
        }
        if (dto.getAtivo() != null) {
            usuario.setAtivo(dto.getAtivo());
        }

        return usuario;
    }
}