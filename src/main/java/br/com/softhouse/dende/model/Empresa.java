package br.com.softhouse.dende.model;

/**
 * MODELO DE EMPRESA
 *
 * Esta classe representa uma empresa vinculada a um organizador.
 * É uma entidade fraca que depende do organizador - uma empresa não existe sem seu organizador.
 * Relacionamento: 1 organizador → 0..1 empresa
 *
 * Atributos:
 * - id: Identificador único da empresa
 * - organizadorId: ID do organizador proprietário (FK)
 * - cnpj: CNPJ da empresa (único, no formato XX.XXX.XXX/XXXX-XX)
 * - razaoSocial: Razão social completa
 * - nomeFantasia: Nome fantasia para uso comercial
 */
public class Empresa {

    // Atributos da empresa
    private Long id;
    private Long organizadorId;
    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;

    // Construtor padrão
    public Empresa() {
    }

    // Construtor com parâmetros principais
    public Empresa(Long organizadorId, String cnpj, String razaoSocial, String nomeFantasia) {
        this.organizadorId = organizadorId;
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
    }

    // Construtor completo
    public Empresa(Long id, Long organizadorId, String cnpj, String razaoSocial, String nomeFantasia) {
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
        return "Empresa{" +
                "id=" + id +
                ", organizadorId=" + organizadorId +
                ", cnpj='" + cnpj + '\'' +
                ", razaoSocial='" + razaoSocial + '\'' +
                ", nomeFantasia='" + nomeFantasia + '\'' +
                '}';
    }
}

