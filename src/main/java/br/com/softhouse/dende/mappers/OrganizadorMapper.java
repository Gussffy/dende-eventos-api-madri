package br.com.softhouse.dende.mappers;

import br.com.softhouse.dende.exceptions.ConflictException;
import br.com.softhouse.dende.dto.OrganizadorDTO;
import br.com.softhouse.dende.dto.EmpresaDTO;
import br.com.softhouse.dende.model.Organizador;

/**
 * Mapper para converter entre as classes de modelo (entidade) e as classes de DTO (Data Transfer Object).
 * Esta classe é responsável por transformar os dados entre as camadas de apresentação (DTOs) e a camada de negócio (entidades).
 */

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
        dto.setAtivo(organizador.getAtivo());
        if (organizador.getEmpresa() != null) {
            dto.setCnpj(organizador.getEmpresa().getCnpj());
            dto.setRazaoSocial(organizador.getEmpresa().getRazaoSocial());
            dto.setNomeFantasia(organizador.getEmpresa().getNomeFantasia());
        }
        // Senha NÃO é copiada para o DTO (segurança)

        return dto;
    }

    // Atualiza os campos de um objeto Organizador com os dados de um OrganizadorDTO (usado para update)
    public static Organizador updateEntity(Organizador organizador, OrganizadorDTO dto) {
        if (dto == null) return organizador;

        if (dto.getEmail() != null && !dto.getEmail().equals(organizador.getEmail())) {
            throw new ConflictException("Não é permitido alterar o email");
        }

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
        if (dto.getAtivo() != null) {
            organizador.setAtivo(dto.getAtivo());
        }

        return organizador;
    }

    /**
     * Extrai os dados de empresa do OrganizadorDTO e retorna um EmpresaDTO.
     * Se os dados de empresa não estiverem preenchidos, retorna null.
     *
     * @param dto o DTO do organizador
     * @param organizadorId o ID do organizador (necessário para vincular a empresa)
     * @return EmpresaDTO com os dados da empresa, ou null se não houver dados
     */
    public static EmpresaDTO toEmpresaDTO(OrganizadorDTO dto, Long organizadorId) {
        if (dto == null) return null;

        // Se nenhum dado de empresa foi fornecido, retorna null
        if ((dto.getCnpj() == null || dto.getCnpj().trim().isEmpty()) &&
            (dto.getRazaoSocial() == null || dto.getRazaoSocial().trim().isEmpty()) &&
            (dto.getNomeFantasia() == null || dto.getNomeFantasia().trim().isEmpty())) {
            return null;
        }

        // Se algum dado foi fornecido, cria um EmpresaDTO
        return new EmpresaDTO(
                organizadorId,
                dto.getCnpj(),
                dto.getRazaoSocial(),
                dto.getNomeFantasia()
        );
    }
}