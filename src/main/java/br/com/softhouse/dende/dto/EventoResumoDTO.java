package br.com.softhouse.dende.dto;

/**
 * DTO para envio de dados resumidos de um evento em listagens
 */
public class EventoResumoDTO {
    private Long id;
    private String nome;
    private String periodo;
    private String local;
    private Double precoIngresso;
    private Integer ingressosVendidos;
    private Integer capacidadeMaxima;
    private Boolean ativo;

    public EventoResumoDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }

    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }

    public Double getPrecoIngresso() { return precoIngresso; }
    public void setPrecoIngresso(Double precoIngresso) { this.precoIngresso = precoIngresso; }

    public Integer getIngressosVendidos() { return ingressosVendidos; }
    public void setIngressosVendidos(Integer ingressosVendidos) { this.ingressosVendidos = ingressosVendidos; }

    public Integer getCapacidadeMaxima() { return capacidadeMaxima; }
    public void setCapacidadeMaxima(Integer capacidadeMaxima) { this.capacidadeMaxima = capacidadeMaxima; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}