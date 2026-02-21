package br.com.softhouse.dende.dto;

import br.com.softhouse.dende.model.Organizador;
import br.com.softhouse.dende.model.enums.Sexo;
import java.time.LocalDate;

// Classe DTO para enviar os dados de um organizador em respostas da API
public class OrganizadorResponseDTO {
    private Long id;
    private String nome;
    private LocalDate dataNascimento;
    private String idade;
    private Sexo sexo;
    private String email;
    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;
    private Boolean ativo;

    public OrganizadorResponseDTO(Organizador organizador) {
        this.id = organizador.getId();
        this.nome = organizador.getNome();
        this.dataNascimento = organizador.getDataNascimento();
        this.idade = organizador.getIdade();
        this.sexo = organizador.getSexo();
        this.email = organizador.getEmail();
        this.cnpj = organizador.getCnpj();
        this.razaoSocial = organizador.getRazaoSocial();
        this.nomeFantasia = organizador.getNomeFantasia();
        this.ativo = organizador.getAtivo();
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public String getIdade() { return idade; }
    public Sexo getSexo() { return sexo; }
    public String getEmail() { return email; }
    public String getCnpj() { return cnpj; }
    public String getRazaoSocial() { return razaoSocial; }
    public String getNomeFantasia() { return nomeFantasia; }
    public Boolean getAtivo() { return ativo; }
}