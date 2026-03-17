package br.com.softhouse.dende.dto;

/**
 * DTO de resposta para o cancelamento de um ingresso
 */
public class CancelamentoResponseDTO {
    private String mensagem;
    private Double valorPago;
    private Double valorReembolsado;
    private String codigoIngresso;

    public CancelamentoResponseDTO(String mensagem, Double valorPago, Double valorReembolsado, String codigoIngresso) {
        this.mensagem = mensagem;
        this.valorPago = valorPago;
        this.valorReembolsado = valorReembolsado;
        this.codigoIngresso = codigoIngresso;
    }

    public String getMensagem() { return mensagem; }
    public Double getValorPago() { return valorPago; }
    public Double getValorReembolsado() { return valorReembolsado; }
    public String getCodigoIngresso() { return codigoIngresso; }
}