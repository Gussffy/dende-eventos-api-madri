package br.com.softhouse.dende.dto;

import br.com.softhouse.dende.model.Organizador;
import br.com.softhouse.dende.model.enums.Sexo;
import java.time.LocalDate;

// Classe DTO para receber os dados de um organizador em requisições da API
public class OrganizadorRequestDTO {
    private String nome;
    private LocalDate dataNascimento;
    private Sexo sexo;
    private String email;
    private String senha;
    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public Sexo getSexo() { return sexo; }
    public void setSexo(Sexo sexo) { this.sexo = sexo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }

    public String getNomeFantasia() { return nomeFantasia; }
    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }

    public Organizador toEntity() {
        Organizador organizador = new Organizador();
        organizador.setNome(this.nome);
        organizador.setDataNascimento(this.dataNascimento);
        organizador.setSexo(this.sexo);
        organizador.setEmail(this.email);
        organizador.setSenha(this.senha);
        organizador.setCnpj(this.cnpj);
        organizador.setRazaoSocial(this.razaoSocial);
        organizador.setNomeFantasia(this.nomeFantasia);
        return organizador;
    }
}