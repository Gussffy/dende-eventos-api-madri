package br.com.softhouse.dende.model;

import br.com.softhouse.dende.model.enums.Sexo;
import java.time.LocalDate;
import java.time.Period;

// Classe de modelo para representar um usuário do sistema

public class Usuario {

    // Atributos do usuário
    private Long id;
    private String nome;
    private LocalDate dataNascimento;
    private Sexo sexo;
    private String email;
    private String senha;
    private Boolean ativo;

    public Usuario() {
        this.ativo = true; // Por padrão, o usuário é criado como ativo
    }

    public Usuario(String nome, LocalDate dataNascimento, Sexo sexo, String email, String senha) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.email = email;
        this.senha = senha;
        this.ativo = true;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    //Metodo para calcular a idade do usuário
    public String getIdade() {
        if (dataNascimento == null) return "";
        Period periodo = Period.between(dataNascimento, LocalDate.now());
        return periodo.getYears() + " anos, " + periodo.getMonths() + " meses, " + periodo.getDays() + " dias";
    }
}