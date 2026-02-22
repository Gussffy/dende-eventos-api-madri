package br.com.softhouse.dende.dto;

import br.com.softhouse.dende.model.Evento;
import br.com.softhouse.dende.model.enums.TipoEvento;
import br.com.softhouse.dende.model.enums.ModalidadeEvento;
import java.time.LocalDateTime;

// Classe DTO para receber os dados de um evento em requisições da API
public class EventoRequestDTO {
    private String nome;
    private String pagina;
    private String descricao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFinal;
    private TipoEvento tipoEvento;
    private Long eventoPrincipalId;
    private ModalidadeEvento modalidade;
    private Integer capacidadeMaxima;
    private String local;
    private Double precoIngresso;
    private Boolean estornaCancelamento;
    private Double taxaEstorno;

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

    public Double getPrecoIngresso() { return precoIngresso; }
    public void setPrecoIngresso(Double precoIngresso) { this.precoIngresso = precoIngresso; }

    public Boolean getEstornaCancelamento() { return estornaCancelamento; }
    public void setEstornaCancelamento(Boolean estornaCancelamento) { this.estornaCancelamento = estornaCancelamento; }

    public Double getTaxaEstorno() { return taxaEstorno; }
    public void setTaxaEstorno(Double taxaEstorno) { this.taxaEstorno = taxaEstorno; }

    public Evento toEntity(Long organizadorId) {
        Evento evento = new Evento();
        evento.setOrganizadorId(organizadorId);
        evento.setNome(this.nome);
        evento.setPagina(this.pagina);
        evento.setDescricao(this.descricao);
        evento.setDataInicio(this.dataInicio);
        evento.setDataFinal(this.dataFinal);
        evento.setTipoEvento(this.tipoEvento);
        evento.setEventoPrincipalId(this.eventoPrincipalId);
        evento.setModalidade(this.modalidade);
        evento.setCapacidadeMaxima(this.capacidadeMaxima);
        evento.setLocal(this.local);
        evento.setPrecoIngresso(this.precoIngresso);
        evento.setEstornaCancelamento(this.estornaCancelamento);
        evento.setTaxaEstorno(this.taxaEstorno);
        return evento;
    }
}