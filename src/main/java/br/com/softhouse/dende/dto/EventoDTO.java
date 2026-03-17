package br.com.softhouse.dende.dto;

import br.com.softhouse.dende.model.enums.TipoEvento;
import br.com.softhouse.dende.model.enums.ModalidadeEvento;
import java.time.LocalDateTime;

/**
 * Classe DTO unificada para receber e enviar dados completos de um evento na API
 */
public class EventoDTO {
    private Long id;
    private Long organizadorId;
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

    public EventoDTO() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrganizadorId() { return organizadorId; }
    public void setOrganizadorId(Long organizadorId) { this.organizadorId = organizadorId; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getPagina() { return pagina; }
    public void setPagina(String pagina) { this.pagina = pagina; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }

    public LocalDateTime getDataFinal() { return dataFinal; }
    public void setDataFinal(LocalDateTime dataFinal) { this.dataFinal = dataFinal; }

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }

    public TipoEvento getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(TipoEvento tipoEvento) { this.tipoEvento = tipoEvento; }

    public Long getEventoPrincipalId() { return eventoPrincipalId; }
    public void setEventoPrincipalId(Long eventoPrincipalId) { this.eventoPrincipalId = eventoPrincipalId; }

    public ModalidadeEvento getModalidade() { return modalidade; }
    public void setModalidade(ModalidadeEvento modalidade) { this.modalidade = modalidade; }

    public Integer getCapacidadeMaxima() { return capacidadeMaxima; }
    public void setCapacidadeMaxima(Integer capacidadeMaxima) { this.capacidadeMaxima = capacidadeMaxima; }

    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public Double getPrecoIngresso() { return precoIngresso; }
    public void setPrecoIngresso(Double precoIngresso) { this.precoIngresso = precoIngresso; }

    public Boolean getEstornaCancelamento() { return estornaCancelamento; }
    public void setEstornaCancelamento(Boolean estornaCancelamento) { this.estornaCancelamento = estornaCancelamento; }

    public Double getTaxaEstorno() { return taxaEstorno; }
    public void setTaxaEstorno(Double taxaEstorno) { this.taxaEstorno = taxaEstorno; }

    public Integer getIngressosVendidos() { return ingressosVendidos; }
    public void setIngressosVendidos(Integer ingressosVendidos) { this.ingressosVendidos = ingressosVendidos; }

    public Integer getIngressosDisponiveis() { return ingressosDisponiveis; }
    public void setIngressosDisponiveis(Integer ingressosDisponiveis) { this.ingressosDisponiveis = ingressosDisponiveis; }
}