package br.com.softhouse.dende.dto;

import java.util.List;

// DTO de resposta para a compra de ingressos, contendo informações sobre o resultado da compra
public class CompraResponseDTO {
    private String mensagem;
    private List<String> codigosIngressos;
    private Double valorTotal;
    private String status;

    public CompraResponseDTO(String mensagem, List<String> codigosIngressos, Double valorTotal, String status) {
        this.mensagem = mensagem;
        this.codigosIngressos = codigosIngressos;
        this.valorTotal = valorTotal;
        this.status = status;
    }

    public CompraResponseDTO(String mensagem, String status) {
        this.mensagem = mensagem;
        this.status = status;
    }
    public String getMensagem() { return mensagem; }
    public List<String> getCodigosIngressos() { return codigosIngressos; }
    public Double getValorTotal() { return valorTotal; }
    public String getStatus() { return status; }
}