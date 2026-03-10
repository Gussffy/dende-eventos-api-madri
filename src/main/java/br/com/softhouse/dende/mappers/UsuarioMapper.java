package br.com.softhouse.dende.mappers;

import br.com.softhouse.dende.dto.UsuarioRequestDTO;
import br.com.softhouse.dende.dto.UsuarioResponseDTO;
import br.com.softhouse.dende.model.Usuario;

/**
 * Mapper para converter entre as classes de modelo (entidade) e as classes de DTO (Data Transfer Object).
 * Esta classe é responsável por transformar os dados entre as camadas de apresentação (DTOs) e a camada de negócio (entidades).
 */

// Mapper para converter entre Usuario, UsuarioRequestDTO e UsuarioResponseDTO
public class UsuarioMapper {

    private UsuarioMapper() {}

    // Converte um UsuarioRequestDTO para um objeto Usuario (entidade)
    public static Usuario toEntity(UsuarioRequestDTO dto) {
        if (dto == null) return null; // Verificação de null para evitar NullPointerException

        // Criar um novo objeto Usuario e preencher seus campos com os dados do DTO
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setDataNascimento(dto.getDataNascimento());
        usuario.setSexo(dto.getSexo());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());

        return usuario;
    }

    // Converte um objeto Usuario (entidade) para um UsuarioResponseDTO
    public static UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        if (usuario == null) return null;
        return new UsuarioResponseDTO(usuario);
    }

    // Atualiza os campos de um objeto Usuario com os dados de um UsuarioRequestDTO (usado para update)
    public static Usuario updateEntity(Usuario usuario, UsuarioRequestDTO dto) {

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

        return usuario;
    }
}