package br.com.softhouse.dende.dto;

/**
 * DATA TRANSFER OBJECT (DTO) DE EMPRESA
 *
 * Esta classe é responsável por transferir dados da entidade Empresa
 * entre as camadas da aplicação (Controller ↔ Service ↔ Repository).
 *
 * Atributos:
 * - id: Identificador único da empresa
 * - organizadorId: ID do organizador proprietário
 * - cnpj: CNPJ da empresa
 * - razaoSocial: Razão social
 * - nomeFantasia: Nome fantasia
 */
public class EmpresaDTO {

    private Long id;
    private Long organizadorId;
    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;

    // Construtor padrão
    public EmpresaDTO() {
    }

    // Construtor com parâmetros principais
    public EmpresaDTO(Long organizadorId, String cnpj, String razaoSocial, String nomeFantasia) {
        this.organizadorId = organizadorId;
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
    }

    // Construtor completo
    public EmpresaDTO(Long id, Long organizadorId, String cnpj, String razaoSocial, String nomeFantasia) {
        this.id = id;
        this.organizadorId = organizadorId;
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrganizadorId() {
        return organizadorId;
    }

    public void setOrganizadorId(Long organizadorId) {
        this.organizadorId = organizadorId;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    @Override
    public String toString() {
        return "EmpresaDTO{" +
                "id=" + id +
                ", organizadorId=" + organizadorId +
                ", cnpj='" + cnpj + '\'' +
                ", razaoSocial='" + razaoSocial + '\'' +
                ", nomeFantasia='" + nomeFantasia + '\'' +
                '}';
    }
}

