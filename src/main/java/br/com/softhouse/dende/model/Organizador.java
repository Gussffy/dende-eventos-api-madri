package br.com.softhouse.dende.model;

import br.com.softhouse.dende.model.enums.Sexo;
import java.time.LocalDate;
import java.time.Period;

// Classe de modelo para representar um organizador de eventos no sistema
public class Organizador {

    // Atributos do organizador
    private Long id;
    private String nome;
    private LocalDate dataNascimento;
    private Sexo sexo;
    private String email;
    private String senha;
    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;
    private Boolean ativo;

    public Organizador() {
        this.ativo = true; // Por padrão, o organizador é criado como ativo
    }

    public Organizador(String nome, LocalDate dataNascimento, Sexo sexo, String email, String senha) {
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

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }

    public String getNomeFantasia() { return nomeFantasia; }
    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public boolean isEmpresa() {
        return cnpj != null && !cnpj.isEmpty();
    }

    // Metodo para calcular a idade do organizador
    public String getIdade() {
        if (dataNascimento == null) return ""; // Retorna uma string vazia se a data de nascimento não estiver definida
        Period periodo = Period.between(dataNascimento, LocalDate.now()); // Calcula o período entre a data de nascimento e a data atual
        return periodo.getYears() + " anos, " + periodo.getMonths() + " meses, " + periodo.getDays() + " dias"; // Retorna a idade formatada como "X anos, Y meses, Z dias"
    }
}