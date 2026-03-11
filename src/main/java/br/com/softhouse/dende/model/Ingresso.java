package br.com.softhouse.dende.model;

import br.com.softhouse.dende.model.enums.StatusIngresso;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

// Classe de modelo para representar um ingresso no sistema
public class Ingresso {

    // Atributos do ingresso
    private Long id;
    private Long usuarioId;
    private Long eventoId;
    private Long eventoVinculadoId;
    private String codigo;
    private LocalDateTime dataCompra;
    private Double valorPago;
    private StatusIngresso status;
    private Boolean ingressoPrincipal;

    public Ingresso() {
        this.dataCompra = LocalDateTime.now();  // Define a data de compra como o momento atual
        this.status = StatusIngresso.PENDENTE;  // Define o status inicial do ingresso como PENDENTE
        this.ingressoPrincipal = true;          // Por padrão, o ingresso é considerado principal
        this.codigo = gerarCodigo();            // Gera um código único para o ingresso usando UUID
    }

    public Ingresso(Long usuarioId, Long eventoId, Double valorPago) {
        this();
        this.usuarioId = usuarioId;
        this.eventoId = eventoId;
        this.valorPago = valorPago;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }

    public Long getEventoVinculadoId() { return eventoVinculadoId; }
    public void setEventoVinculadoId(Long eventoVinculadoId) { this.eventoVinculadoId = eventoVinculadoId; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public LocalDateTime getDataCompra() { return dataCompra; }
    public void setDataCompra(LocalDateTime dataCompra) { this.dataCompra = dataCompra; }

    public Double getValorPago() { return valorPago; }
    public void setValorPago(Double valorPago) { this.valorPago = valorPago; }

    public StatusIngresso getStatus() { return status; }
    public void setStatus(StatusIngresso status) { this.status = status; }

    public Boolean getIngressoPrincipal() { return ingressoPrincipal; }
    public void setIngressoPrincipal(Boolean ingressoPrincipal) { this.ingressoPrincipal = ingressoPrincipal; }

    // Metodo para gerar um código único para o ingresso usando UUID
    private String gerarCodigo() {
        return UUID.randomUUID().toString().toUpperCase().replace("-", "").substring(0, 12);
    }

    // Metodos para gerenciar o status do ingresso
    public boolean podeSerCancelado() {
        return status.podeSerCancelado();
    }

    // Metodo para cancelar o ingresso, alterando seu status para CANCELADO
    public void cancelar() {
        if (podeSerCancelado()) {
            this.status = StatusIngresso.CANCELADO;
        }
    }

    // Metodo para confirmar o pagamento do ingresso, alterando seu status para ATIVO
    public void confirmarPagamento() {
        if (status == StatusIngresso.PENDENTE) {
            this.status = StatusIngresso.ATIVO;
        }
    }

    // Metodo para reembolsar o ingresso, alterando seu status para REEMBOLSADO
    public void reembolsar() {
        if (status == StatusIngresso.ATIVO || status == StatusIngresso.CANCELADO) {
            this.status = StatusIngresso.REEMBOLSADO;
        }
    }

    // Metodo para formatar a data de compra do ingresso em um formato legível
    public String getDataCompraFormatada() {
        // Formata a data de compra usando o padrão "dd/MM/yyyy HH:mm"
        return dataCompra.format(DateTimeFormatter.ofPattern(" dd/MM/yyyy HH:mm "));
    }
}