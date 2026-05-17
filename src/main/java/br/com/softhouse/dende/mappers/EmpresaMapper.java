package br.com.softhouse.dende.mappers;

import br.com.softhouse.dende.dto.EmpresaDTO;
import br.com.softhouse.dende.model.Empresa;

/**
 * MAPPER DE EMPRESA
 *
 * Classe responsável por converter entre a entidade Empresa e seu DTO (EmpresaDTO).
 * Implementa métodos para transformação de dados entre as camadas de Model e DTO.
 *
 * Padrão de Utility Class:
 * - Construtor privado (não pode ser instanciada)
 * - Todos os métodos stateless e públicos
 */
public final class EmpresaMapper {

    // Construtor privado para impedir instanciação
    private EmpresaMapper() {
        throw new AssertionError("Não deve instanciar EmpresaMapper");
    }

    /**
     * Converte uma entidade Empresa para um DTO EmpresaDTO
     *
     * @param empresa a entidade Empresa a ser convertida
     * @return EmpresaDTO com os dados da empresa
     */
    public static EmpresaDTO toDTO(Empresa empresa) {
        if (empresa == null) {
            return null;
        }

        return new EmpresaDTO(
                empresa.getId(),
                empresa.getOrganizadorId(),
                empresa.getCnpj(),
                empresa.getRazaoSocial(),
                empresa.getNomeFantasia()
        );
    }

    /**
     * Converte um DTO EmpresaDTO para a entidade Empresa
     *
     * @param dto o DTO a ser convertido
     * @return Empresa com os dados do DTO
     */
    public static Empresa toEntity(EmpresaDTO dto) {
        if (dto == null) {
            return null;
        }

        return new Empresa(
                dto.getId(),
                dto.getOrganizadorId(),
                dto.getCnpj(),
                dto.getRazaoSocial(),
                dto.getNomeFantasia()
        );
    }

    /**
     * Atualiza uma entidade Empresa existente com dados de um DTO
     *
     * @param entity a entidade existente a ser atualizada
     * @param dto o DTO contendo os novos dados
     * @return a entidade atualizada
     */
    public static Empresa updateEntity(Empresa entity, EmpresaDTO dto) {
        if (entity == null || dto == null) {
            return entity;
        }

        if (dto.getOrganizadorId() != null) {
            entity.setOrganizadorId(dto.getOrganizadorId());
        }

        if (dto.getCnpj() != null && !dto.getCnpj().trim().isEmpty()) {
            entity.setCnpj(dto.getCnpj());
        }

        if (dto.getRazaoSocial() != null && !dto.getRazaoSocial().trim().isEmpty()) {
            entity.setRazaoSocial(dto.getRazaoSocial());
        }

        if (dto.getNomeFantasia() != null && !dto.getNomeFantasia().trim().isEmpty()) {
            entity.setNomeFantasia(dto.getNomeFantasia());
        }

        return entity;
    }
}

