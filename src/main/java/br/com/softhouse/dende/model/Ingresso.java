package br.com.softhouse.dende.model;

import br.com.softhouse.dende.model.enums.StatusIngresso;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Classe de modelo para representar um ingresso no sistema
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ingresso {

    // Atributos do ingresso
    private Long id;
    private Long usuarioId;
    private Long eventoId;
    private String codigo = UUID.randomUUID().toString().toUpperCase().replace("-", "").substring(0, 12);
    private LocalDateTime dataCompra = LocalDateTime.now();
    private Double valorPago;
    private Double valorEstornado = 0.0;
    private LocalDateTime dataCancelamento;
    private StatusIngresso status = StatusIngresso.PENDENTE;
    private Boolean ingressoPrincipal = true;

    public Ingresso(Long usuarioId, Long eventoId, Double valorPago) {
        this.usuarioId = usuarioId;
        this.eventoId = eventoId;
        this.valorPago = valorPago;
    }


    // (codigo é inicializado inline com UUID)

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
            this.valorEstornado = this.valorPago;
            this.dataCancelamento = LocalDateTime.now();
        }
    }

    // Metodo para formatar a data de compra do ingresso em um formato legível
    public String getDataCompraFormatada() {
        // Formata a data de compra usando o padrão "dd/MM/yyyy HH:mm"
        return dataCompra.format(DateTimeFormatter.ofPattern(" dd/MM/yyyy HH:mm "));
    }
}