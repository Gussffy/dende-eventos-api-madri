package br.com.softhouse.dende.mappers;

import br.com.softhouse.dende.dto.OrganizadorDTO;
import br.com.softhouse.dende.model.Organizador;

// Mapper para converter entre Organizador e OrganizadorDTO
public class OrganizadorMapper {

    private OrganizadorMapper() {}

    // Converte um OrganizadorDTO para um objeto Organizador (entidade)
    public static Organizador toEntity(OrganizadorDTO dto) {
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
        organizador.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);

        return organizador;
    }

    // Converte um objeto Organizador (entidade) para um OrganizadorDTO
    public static OrganizadorDTO toDTO(Organizador organizador) {
        if (organizador == null) return null;

        OrganizadorDTO dto = new OrganizadorDTO();
        dto.setId(organizador.getId());
        dto.setNome(organizador.getNome());
        dto.setDataNascimento(organizador.getDataNascimento());
        dto.setIdade(organizador.getIdade());
        dto.setSexo(organizador.getSexo());
        dto.setEmail(organizador.getEmail());
        dto.setCnpj(organizador.getCnpj());
        dto.setRazaoSocial(organizador.getRazaoSocial());
        dto.setNomeFantasia(organizador.getNomeFantasia());
        dto.setAtivo(organizador.getAtivo());
        // Senha NÃO é copiada para o DTO (segurança)

        return dto;
    }

    // Atualiza os campos de um objeto Organizador com os dados de um OrganizadorDTO (usado para update)
    public static Organizador updateEntity(Organizador organizador, OrganizadorDTO dto) {

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
        if (dto.getAtivo() != null) {
            organizador.setAtivo(dto.getAtivo());
        }

        return organizador;
    }
}