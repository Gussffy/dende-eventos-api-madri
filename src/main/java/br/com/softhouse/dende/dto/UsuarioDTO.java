package br.com.softhouse.dende.dto;

import br.com.softhouse.dende.model.enums.Sexo;
import java.time.LocalDate;

/**
 * Classe DTO unificada para receber e enviar dados de um usuário na API
 */
public class UsuarioDTO {
    private Long id;
    private String nome;
    private LocalDate dataNascimento;
    private String idade;
    private Sexo sexo;
    private String email;

    // @JsonIgnore // Para não expor a senha na resposta da API mas não tem o Spring Boot ainda para usar essa anotação
    private String senha;
    private Boolean ativo;

    public UsuarioDTO() {}

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
}