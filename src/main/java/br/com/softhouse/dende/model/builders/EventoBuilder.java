package br.com.softhouse.dende.model.builders;

import br.com.softhouse.dende.model.Evento;
import br.com.softhouse.dende.model.enums.TipoEvento;
import br.com.softhouse.dende.model.enums.ModalidadeEvento;
import java.time.LocalDateTime;

// Builder para criar instâncias de Evento de forma fluida e legível

public class EventoBuilder {

    // Atributos necessários para criar um Evento
    private Long organizadorId;
    private String nome;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFinal;
    private TipoEvento tipoEvento;
    private ModalidadeEvento modalidade;
    private Integer capacidadeMaxima;
    private String local;
    private Double precoIngresso;

    // Atributos opcionais com valores padrão
    private String pagina = "";
    private String descricao = "";
    private Long eventoPrincipalId = null;
    private Boolean estornaCancelamento = true;
    private Double taxaEstorno = 0.0;
    private Boolean ativo = false;
    private Integer ingressosVendidos = 0;

    // Construtor privado para impedir a criação direta de instâncias do builder
    private EventoBuilder() {}

    // Metodo estático para iniciar a construção de um Evento
    public static EventoBuilder builder() {
        return new EventoBuilder();
    }

    // Métodos para configurar os atributos do Evento, retornando o próprio builder para permitir encadeamento
    public EventoBuilder organizadorId(Long organizadorId) {
        this.organizadorId = organizadorId;
        return this;
    }

    public EventoBuilder nome(String nome) {
        this.nome = nome;
        return this;
    }

    public EventoBuilder pagina(String pagina) {
        this.pagina = pagina;
        return this;
    }

    public EventoBuilder descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public EventoBuilder dataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
        return this;
    }

    public EventoBuilder dataFinal(LocalDateTime dataFinal) {
        this.dataFinal = dataFinal;
        return this;
    }

    public EventoBuilder tipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
        return this;
    }

    public EventoBuilder eventoPrincipalId(Long eventoPrincipalId) {
        this.eventoPrincipalId = eventoPrincipalId;
        return this;
    }

    public EventoBuilder modalidade(ModalidadeEvento modalidade) {
        this.modalidade = modalidade;
        return this;
    }

    public EventoBuilder capacidadeMaxima(Integer capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
        return this;
    }

    public EventoBuilder local(String local) {
        this.local = local;
        return this;
    }

    public EventoBuilder ativo(Boolean ativo) {
        this.ativo = ativo;
        return this;
    }

    public EventoBuilder precoIngresso(Double precoIngresso) {
        this.precoIngresso = precoIngresso;
        return this;
    }

    public EventoBuilder estornaCancelamento(Boolean estornaCancelamento) {
        this.estornaCancelamento = estornaCancelamento;
        return this;
    }

    public EventoBuilder taxaEstorno(Double taxaEstorno) {
        this.taxaEstorno = taxaEstorno;
        return this;
    }

    public EventoBuilder ingressosVendidos(Integer ingressosVendidos) {
        this.ingressosVendidos = ingressosVendidos;
        return this;
    }

    // Metodo para construir a instância de Evento com os atributos configurados, realizando validações antes de criar o objeto
    public Evento build() {
        if (organizadorId == null) {
            throw new IllegalStateException("organizadorId é obrigatório");
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalStateException("nome é obrigatório");
        }
        if (dataInicio == null) {
            throw new IllegalStateException("dataInicio é obrigatória");
        }
        if (dataFinal == null) {
            throw new IllegalStateException("dataFinal é obrigatória");
        }
        if (tipoEvento == null) {
            throw new IllegalStateException("tipoEvento é obrigatório");
        }
        if (modalidade == null) {
            throw new IllegalStateException("modalidade é obrigatória");
        }
        if (capacidadeMaxima == null || capacidadeMaxima <= 0) {
            throw new IllegalStateException("capacidadeMaxima deve ser maior que zero");
        }
        if (local == null || local.trim().isEmpty()) {
            throw new IllegalStateException("local é obrigatório");
        }
        if (precoIngresso == null || precoIngresso < 0) {
            throw new IllegalStateException("precoIngresso deve ser maior ou igual a zero");
        }

        // Cria uma nova instância de Evento e configura seus atributos com os valores fornecidos ao builder
        Evento evento = new Evento();
        evento.setOrganizadorId(organizadorId);
        evento.setNome(nome);
        evento.setPagina(pagina);
        evento.setDescricao(descricao);
        evento.setDataInicio(dataInicio);
        evento.setDataFinal(dataFinal);
        evento.setTipoEvento(tipoEvento);
        evento.setEventoPrincipalId(eventoPrincipalId);
        evento.setModalidade(modalidade);
        evento.setCapacidadeMaxima(capacidadeMaxima);
        evento.setLocal(local);
        evento.setAtivo(ativo);
        evento.setPrecoIngresso(precoIngresso);
        evento.setEstornaCancelamento(estornaCancelamento);
        evento.setTaxaEstorno(taxaEstorno);
        evento.setIngressosVendidos(ingressosVendidos);

        return evento;
    }
}