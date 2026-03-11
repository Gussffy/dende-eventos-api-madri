package br.com.softhouse.dende.dto;

/**
    Classe genérica para padronizar todas as respostas da API
    <T> Tipo dos dados retornados em caso de sucesso

 */

public class ApiResponse<T> {
    private String mensagem;
    private int statusCode;
    private T dados;           // Preenchido em caso de sucesso
    private String erro;        // Preenchido em caso de erro
    private long timestamp;

    // Construtor para sucesso com dados
    public ApiResponse(T dados, String mensagem, int statusCode) {
        this.dados = dados;
        this.mensagem = mensagem;
        this.statusCode = statusCode;
        this.erro = null;
        this.timestamp = System.currentTimeMillis();
    }

    // Construtor para erro sem dados
    public ApiResponse(String mensagem, int statusCode, String erro) {
        this.dados = null;
        this.mensagem = mensagem;
        this.statusCode = statusCode;
        this.erro = erro;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters e Setters
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }

    public T getDados() { return dados; }
    public void setDados(T dados) { this.dados = dados; }

    public String getErro() { return erro; }
    public void setErro(String erro) { this.erro = erro; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}