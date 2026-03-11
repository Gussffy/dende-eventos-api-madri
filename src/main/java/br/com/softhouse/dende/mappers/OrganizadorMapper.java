package br.com.softhouse.dende.mappers;

import br.com.softhouse.dende.dto.OrganizadorRequestDTO;
import br.com.softhouse.dende.dto.OrganizadorResponseDTO;
import br.com.softhouse.dende.model.Organizador;

// Mapper para converter entre Organizador, OrganizadorRequestDTO e OrganizadorResponseDTO
public class OrganizadorMapper {

    private OrganizadorMapper() {}

    // Converte um OrganizadorRequestDTO para um objeto Organizador (entidade)
    public static Organizador toEntity(OrganizadorRequestDTO dto) {
        if (dto == null) return null;

        // Criar um novo objeto Organizador e preencher seus campos com os dados do DTO
        Organizador organizador = new Organizador();
        organizador.setNome(dto.getNome());
        organizador.setDataNascimento(dto.getDataNascimento());
        organizador.setSexo(dto.getSexo());
        organizador.setEmail(dto.getEmail());
        organizador.setSenha(dto.getSenha());
        organizador.setCnpj(dto.getCnpj());
        organizador.setRazaoSocial(dto.getRazaoSocial());
        organizador.setNomeFantasia(dto.getNomeFantasia());

        return organizador;
    }

    // Converte um objeto Organizador (entidade) para um OrganizadorResponseDTO
    public static OrganizadorResponseDTO toResponseDTO(Organizador organizador) {
        if (organizador == null) return null;
        return new OrganizadorResponseDTO(organizador);
    }

    // Atualiza os campos de um objeto Organizador com os dados de um OrganizadorRequestDTO (usado para update)
    public static Organizador updateEntity(Organizador organizador, OrganizadorRequestDTO dto) {

        if (dto.getEmail() != null && !dto.getEmail().equals(organizador.getEmail())) {
            throw new IllegalArgumentException("Não é permitido alterar o email");
        }
        if (dto == null) return organizador;

        if (dto.getNome() != null) {
            organizador.setNome(dto.getNome());
        }
        if (dto.getDataNascimento() != null) {
            organizador.setDataNascimento(dto.getDataNascimento());
        }
        if (dto.getSexo() != null) {
            organizador.setSexo(dto.getSexo());
        }
        if (dto.getSenha() != null) {
            organizador.setSenha(dto.getSenha());
        }
        if (dto.getCnpj() != null) {
            organizador.setCnpj(dto.getCnpj());
        }
        if (dto.getRazaoSocial() != null) {
            organizador.setRazaoSocial(dto.getRazaoSocial());
        }
        if (dto.getNomeFantasia() != null) {
            organizador.setNomeFantasia(dto.getNomeFantasia());
        }

        return organizador;
    }
}