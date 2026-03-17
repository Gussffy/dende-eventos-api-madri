package br.com.softhouse.dende.dto;

/**
 * DTO para receber dados de uma requisição de compra de ingresso
 */
public class CompraRequestDTO {
    private String usuarioEmail;
    private Double totalPago;

    public String getUsuarioEmail() { return usuarioEmail; }
    public void setUsuarioEmail(String usuarioEmail) { this.usuarioEmail = usuarioEmail; }
    public Double getTotalPago() { return totalPago; }
    public void setTotalPago(Double totalPago) { this.totalPago = totalPago; }
}