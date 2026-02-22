package br.com.softhouse.dende.dto;

public class IngressoRequestDTO {
    private String usuarioEmail;
    private Double totalPago;

    public String getUsuarioEmail() { return usuarioEmail; }
    public void setUsuarioEmail(String usuarioEmail) { this.usuarioEmail = usuarioEmail; }

    public Double getTotalPago() { return totalPago; }
    public void setTotalPago(Double totalPago) { this.totalPago = totalPago; }
}