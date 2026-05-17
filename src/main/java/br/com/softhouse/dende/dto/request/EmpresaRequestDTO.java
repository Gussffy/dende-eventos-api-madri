package br.com.softhouse.dende.dto.request;

/**
 * DTO de requisição de empresa.
 */
public class EmpresaRequestDTO {

    private Long id;
    private Long organizadorId;
    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;

    public EmpresaRequestDTO() {
    }

    public EmpresaRequestDTO(Long organizadorId, String cnpj, String razaoSocial, String nomeFantasia) {
        this.organizadorId = organizadorId;
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
    }

    public EmpresaRequestDTO(Long id, Long organizadorId, String cnpj, String razaoSocial, String nomeFantasia) {
        this.id = id;
        this.organizadorId = organizadorId;
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrganizadorId() { return organizadorId; }
    public void setOrganizadorId(Long organizadorId) { this.organizadorId = organizadorId; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }

    public String getNomeFantasia() { return nomeFantasia; }
    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }
}

