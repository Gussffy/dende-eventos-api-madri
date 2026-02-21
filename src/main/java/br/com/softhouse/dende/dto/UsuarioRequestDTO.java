package br.com.softhouse.dende.dto;

import br.com.softhouse.dende.model.Usuario;
import br.com.softhouse.dende.model.enums.Sexo;
import java.time.LocalDate;

// Classe DTO para receber os dados de criação ou atualização de um usuário
public class UsuarioRequestDTO {
    private String nome;
    private LocalDate dataNascimento;
    private Sexo sexo;
    private String email;
    private String senha;

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

    public Usuario toEntity() {
        Usuario usuario = new Usuario();
        usuario.setNome(this.nome);
        usuario.setDataNascimento(this.dataNascimento);
        usuario.setSexo(this.sexo);
        usuario.setEmail(this.email);
        usuario.setSenha(this.senha);
        return usuario;
    }
}