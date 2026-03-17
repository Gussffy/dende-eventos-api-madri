package br.com.softhouse.dende.dto;

import br.com.softhouse.dende.model.enums.StatusIngresso;
import java.time.LocalDateTime;

/**
 * Classe DTO unificada para enviar dados de um ingresso na API
 */
public class IngressoDTO {
    private Long id;
    private Long usuarioId;
    private Long eventoId;
    private String eventoNome;
    private LocalDateTime dataEvento;
    private String local;
    private String codigo;
    private LocalDateTime dataCompra;
    private String dataCompraFormatada;
    private Double valorPago;
    private StatusIngresso status;
    private Boolean ingressoPrincipal;
    private Long eventoVinculadoId;

    public IngressoDTO() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }

    public String getEventoNome() { return eventoNome; }
    public void setEventoNome(String eventoNome) { this.eventoNome = eventoNome; }

    public LocalDateTime getDataEvento() { return dataEvento; }
    public void setDataEvento(LocalDateTime dataEvento) { this.dataEvento = dataEvento; }

    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public LocalDateTime getDataCompra() { return dataCompra; }
    public void setDataCompra(LocalDateTime dataCompra) { this.dataCompra = dataCompra; }

    public String getDataCompraFormatada() { return dataCompraFormatada; }
    public void setDataCompraFormatada(String dataCompraFormatada) { this.dataCompraFormatada = dataCompraFormatada; }

    public Double getValorPago() { return valorPago; }
    public void setValorPago(Double valorPago) { this.valorPago = valorPago; }

    public StatusIngresso getStatus() { return status; }
    public void setStatus(StatusIngresso status) { this.status = status; }

    public Boolean getIngressoPrincipal() { return ingressoPrincipal; }
    public void setIngressoPrincipal(Boolean ingressoPrincipal) { this.ingressoPrincipal = ingressoPrincipal; }

    public Long getEventoVinculadoId() { return eventoVinculadoId; }
    public void setEventoVinculadoId(Long eventoVinculadoId) { this.eventoVinculadoId = eventoVinculadoId; }
}