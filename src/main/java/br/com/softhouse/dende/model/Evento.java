package br.com.softhouse.dende.model;

import br.com.softhouse.dende.model.enums.TipoEvento;
import br.com.softhouse.dende.model.enums.ModalidadeEvento;
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

// Classe de modelo para representar um evento no sistema
public class Evento {

    // Atributos do evento
    private Long id;
    private Long organizadorId;
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
    private Boolean ativo;
    private Double precoIngresso;
    private Boolean estornaCancelamento;
    private Double taxaEstorno;
    private Integer ingressosVendidos;

    public Evento() {
        this.ativo = false;             // Por padrão, o evento é criado como inativo
        this.ingressosVendidos = 0;     // Inicializa a contagem de ingressos vendidos
        this.estornaCancelamento = true;// Por padrão, o evento permite estorno em caso de cancelamento
        this.taxaEstorno = 0.0;         // Por padrão, a taxa de estorno é 0%, ou seja, reembolsa o valor total pago
    }

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

    // Metodo para validar as datas do evento
    public boolean validarDatas() {
        LocalDateTime agora = LocalDateTime.now();
        if (dataInicio.isBefore(agora)) return false;
        if (dataFinal.isBefore(dataInicio)) return false;
        long duracaoMinutos = Duration.between(dataInicio, dataFinal).toMinutes();
        return duracaoMinutos >= 30;
    }

    // Metodo para verificar se o evento tem ingressos disponíveis
    public boolean temIngressosDisponiveis() {
        return ingressosVendidos < capacidadeMaxima;
    }

    // Metodo para calcular o número de ingressos disponíveis
    public int ingressosDisponiveis() {
        return capacidadeMaxima - ingressosVendidos;
    }

    // Metodo para verificar se o evento já aconteceu
    public boolean eventoJaAconteceu() {
        return dataFinal.isBefore(LocalDateTime.now());
    }

    // Metodo para verificar se o evento está em andamento
    public boolean eventoEmAndamento() {
        LocalDateTime agora = LocalDateTime.now();
        return agora.isAfter(dataInicio) && agora.isBefore(dataFinal);
    }

    // Metodo para verificar se o evento pode ser ativado (deve ter datas válidas e não estar ativo)
    public boolean podeSerAtivado() {
        return validarDatas() && !ativo;
    }

    // Metodo para calcular o valor do reembolso em caso de cancelamento, considerando a taxa de estorno
    public Double calcularReembolso(Double valorPago) {
        if (!estornaCancelamento) return 0.0;
        return valorPago * (1 - taxaEstorno / 100);
    }

    // Metodo para vender um ingresso, incrementando a contagem de ingressos vendidos
    public void venderIngresso() {
        if (temIngressosDisponiveis()) {
            this.ingressosVendidos++;
        }
    }

    // Metodo para cancelar um ingresso, decrementando a contagem de ingressos vendidos (se houver ingressos vendidos)
    public void cancelarIngresso() {
        if (this.ingressosVendidos > 0) {
            this.ingressosVendidos--;
        }
    }

    // Metodo para formatar o período do evento em uma string legível
    public String getPeriodoFormatado() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm ");
        return dataInicio.format(formatter) + " até " + dataFinal.format(formatter);
    }
}