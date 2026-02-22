package br.com.softhouse.dende.dto;

import br.com.softhouse.dende.model.Ingresso;
import br.com.softhouse.dende.model.enums.StatusIngresso;
import java.time.LocalDateTime;

public class IngressoResponseDTO {
    private Long id;
    private String codigo;
    private Long eventoId;
    private String eventoNome;
    private LocalDateTime dataEvento;
    private String local;
    private Double valorPago;
    private StatusIngresso status;
    private String dataCompra;

    public IngressoResponseDTO(Ingresso ingresso, String eventoNome, LocalDateTime dataEvento, String local) {
        this.id = ingresso.getId();
        this.codigo = ingresso.getCodigo();
        this.eventoId = ingresso.getEventoId();
        this.eventoNome = eventoNome;
        this.dataEvento = dataEvento;
        this.local = local;
        this.valorPago = ingresso.getValorPago();
        this.status = ingresso.getStatus();
        this.dataCompra = ingresso.getDataCompraFormatada();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public Long getEventoId() { return eventoId; }
    public String getEventoNome() { return eventoNome; }
    public LocalDateTime getDataEvento() { return dataEvento; }
    public String getLocal() { return local; }
    public Double getValorPago() { return valorPago; }
    public StatusIngresso getStatus() { return status; }
    public String getDataCompra() { return dataCompra; }
}