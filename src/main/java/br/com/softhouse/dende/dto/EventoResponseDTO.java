package br.com.softhouse.dende.dto;

import br.com.softhouse.dende.model.Evento;
import br.com.softhouse.dende.model.enums.TipoEvento;
import br.com.softhouse.dende.model.enums.ModalidadeEvento;
import java.time.LocalDateTime;

// Classe DTO para enviar os dados completos de um evento em respostas da API
public class EventoResponseDTO {
    private Long id;
    private String nome;
    private String pagina;
    private String descricao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFinal;
    private String periodo;
    private TipoEvento tipoEvento;
    private Long eventoPrincipalId;
    private ModalidadeEvento modalidade;
    private Integer capacidadeMaxima;
    private String local;
    private Boolean ativo;
    private Double precoIngresso;
    private Boolean estornaCancelamento;
    private Double taxaEstorno;
    private Integer ingressosVendidos;
    private Integer ingressosDisponiveis;

    public EventoResponseDTO(Evento evento) {
        this.id = evento.getId();
        this.nome = evento.getNome();
        this.pagina = evento.getPagina();
        this.descricao = evento.getDescricao();
        this.dataInicio = evento.getDataInicio();
        this.dataFinal = evento.getDataFinal();
        this.periodo = evento.getPeriodoFormatado();
        this.tipoEvento = evento.getTipoEvento();
        this.eventoPrincipalId = evento.getEventoPrincipalId();
        this.modalidade = evento.getModalidade();
        this.capacidadeMaxima = evento.getCapacidadeMaxima();
        this.local = evento.getLocal();
        this.ativo = evento.getAtivo();
        this.precoIngresso = evento.getPrecoIngresso();
        this.estornaCancelamento = evento.getEstornaCancelamento();
        this.taxaEstorno = evento.getTaxaEstorno();
        this.ingressosVendidos = evento.getIngressosVendidos();
        this.ingressosDisponiveis = evento.ingressosDisponiveis();
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getPagina() { return pagina; }
    public String getDescricao() { return descricao; }
    public LocalDateTime getDataInicio() { return dataInicio; }
    public LocalDateTime getDataFinal() { return dataFinal; }
    public String getPeriodo() { return periodo; }
    public TipoEvento getTipoEvento() { return tipoEvento; }
    public Long getEventoPrincipalId() { return eventoPrincipalId; }
    public ModalidadeEvento getModalidade() { return modalidade; }
    public Integer getCapacidadeMaxima() { return capacidadeMaxima; }
    public String getLocal() { return local; }
    public Boolean getAtivo() { return ativo; }
    public Double getPrecoIngresso() { return precoIngresso; }
    public Boolean getEstornaCancelamento() { return estornaCancelamento; }
    public Double getTaxaEstorno() { return taxaEstorno; }
    public Integer getIngressosVendidos() { return ingressosVendidos; }
    public Integer getIngressosDisponiveis() { return ingressosDisponiveis; }
}