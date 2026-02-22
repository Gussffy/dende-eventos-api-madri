package br.com.softhouse.dende.model.enums;

public enum StatusIngresso {
    ATIVO("Ativo - Aguardando evento"),
    UTILIZADO("Utilizado - Evento já realizado"),
    CANCELADO("Cancelado pelo usuário"),
    REEMBOLSADO("Reembolsado - Evento cancelado"),
    PENDENTE("Pendente - Aguardando pagamento");

    private final String descricao;

    StatusIngresso(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean podeSerCancelado() {
        return this == ATIVO || this == PENDENTE;
    }
}