package br.com.softhouse.dende.dto;

import br.com.softhouse.dende.model.Usuario;
import br.com.softhouse.dende.model.enums.Sexo;

import java.time.LocalDate;

// Classe DTO para enviar os dados de um usuário em respostas da API
public class UsuarioResponseDTO {
    private Long id;
    private String nome;
    private LocalDate dataNascimento;
    private String idade;
    private Sexo sexo;
    private String email;
    private Boolean ativo;

    public UsuarioResponseDTO() {
    }

    public UsuarioResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.dataNascimento = usuario.getDataNascimento();
        this.idade = usuario.getIdade();
        this.sexo = usuario.getSexo();
        this.email = usuario.getEmail();
        this.ativo = usuario.getAtivo();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getIdade() {
        return idade;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getAtivo() {
        return ativo;
    }

}