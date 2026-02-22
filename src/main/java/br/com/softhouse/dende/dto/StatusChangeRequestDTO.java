package br.com.softhouse.dende.dto;

// Classe DTO para receber os dados de solicitação de mudança de status do ingresso
public class StatusChangeRequestDTO {
    private String senha;
    private Long eventoId;

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }
}