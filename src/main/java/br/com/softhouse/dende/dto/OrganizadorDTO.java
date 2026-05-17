package br.com.softhouse.dende.dto;

import br.com.softhouse.dende.model.enums.Sexo;
import java.time.LocalDate;

/**
 * Classe DTO para receber e enviar dados de um organizador na API.
 *
 * Pode incluir dados de empresa opcionais:
 * - cnpj: CNPJ da empresa (se o organizador tiver empresa)
 * - razaoSocial: Razão social (se o organizador tiver empresa)
 * - nomeFantasia: Nome fantasia (se o organizador tiver empresa)
 *
 * A empresa é um recurso dependente e é cadastrada junto com o organizador.
 */
public class OrganizadorDTO {
    private Long id;
    private String nome;
    private LocalDate dataNascimento;
    private String idade;
    private Sexo sexo;
    private String email;
    private String senha;
    private Boolean ativo;

    // Dados opcionais de empresa
    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;

    public OrganizadorDTO() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getIdade() { return idade; }
    public void setIdade(String idade) { this.idade = idade; }

    public Sexo getSexo() { return sexo; }
    public void setSexo(Sexo sexo) { this.sexo = sexo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    // Getters e Setters para dados de empresa (opcionais)
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }

    public String getNomeFantasia() { return nomeFantasia; }
    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }
}