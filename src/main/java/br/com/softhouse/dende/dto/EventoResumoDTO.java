package br.com.softhouse.dende.dto;

import br.com.softhouse.dende.model.Evento;

public class EventoResumoDTO {
    private Long id;
    private String nome;
    private String periodo;
    private String local;
    private Double precoIngresso;
    private Integer ingressosVendidos;
    private Integer capacidadeMaxima;
    private Boolean ativo;

    // Construtor que recebe um objeto Evento e extrai os dados necessários para o resumo
    public EventoResumoDTO(Evento evento) {
        this.id = evento.getId();
        this.nome = evento.getNome();
        this.periodo = evento.getPeriodoFormatado();
        this.local = evento.getLocal();
        this.precoIngresso = evento.getPrecoIngresso();
        this.ingressosVendidos = evento.getIngressosVendidos();
        this.capacidadeMaxima = evento.getCapacidadeMaxima();
        this.ativo = evento.getAtivo();
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getPeriodo() { return periodo; }
    public String getLocal() { return local; }
    public Double getPrecoIngresso() { return precoIngresso; }
    public Integer getIngressosVendidos() { return ingressosVendidos; }
    public Integer getCapacidadeMaxima() { return capacidadeMaxima; }
    public Boolean getAtivo() { return ativo; }
}