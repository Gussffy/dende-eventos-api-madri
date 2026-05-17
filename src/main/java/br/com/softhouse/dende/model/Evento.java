package br.com.softhouse.dende.model;

import br.com.softhouse.dende.model.enums.TipoEvento;
import br.com.softhouse.dende.model.enums.ModalidadeEvento;
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Classe de modelo para representar um evento no sistema
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Evento {

    // Atributos do evento
    private Long id;
    private Long organizadorId;
    private String nome;
    private String pagina;
    private String descricao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFinal;
    private TipoEvento tipoEvento;
    private Long eventoPrincipalId;
    private ModalidadeEvento modalidade;
    private Integer capacidadeMaxima;
    private String local;
    private Boolean ativo = false; // Por padrão, o evento é criado como inativo
    private Double precoIngresso;
    private Boolean estornaCancelamento = true; // Por padrão, permite estorno
    private Double taxaEstorno = 0.0; // Taxa de estorno padrão
    private Integer ingressosVendidos = 0; // Inicializa a contagem de ingressos vendidos

    // Metodo para validar as datas do evento
    public boolean validarDatas() {
        LocalDateTime agora = LocalDateTime.now();
        if (dataInicio.isBefore(agora)) return false;
        if (dataFinal.isBefore(dataInicio)) return false;
        long duracaoMinutos = Duration.between(dataInicio, dataFinal).toMinutes();
        return duracaoMinutos >= 30;
    }

    // Metodo para verificar se o evento tem ingressos disponíveis
    public boolean temIngressosDisponiveis() {
        return ingressosVendidos < capacidadeMaxima;
    }

    // Metodo para calcular o número de ingressos disponíveis
    public int ingressosDisponiveis() {
        return capacidadeMaxima - ingressosVendidos;
    }

    // Metodo para verificar se o evento já aconteceu
    public boolean eventoJaAconteceu() {
        return dataFinal.isBefore(LocalDateTime.now());
    }

    // Metodo para verificar se o evento está em andamento
    public boolean eventoEmAndamento() {
        LocalDateTime agora = LocalDateTime.now();
        return agora.isAfter(dataInicio) && agora.isBefore(dataFinal);
    }

    // Metodo para verificar se o evento pode ser ativado (deve ter datas válidas e não estar ativo)
    public boolean podeSerAtivado() {
        return validarDatas() && !ativo;
    }

    // Metodo para calcular o valor do reembolso em caso de cancelamento, considerando a taxa de estorno
    public Double calcularReembolso(Double valorPago) {
        if (!estornaCancelamento) return 0.0;
        return valorPago * (1 - taxaEstorno / 100);
    }

    // Metodo para vender um ingresso, incrementando a contagem de ingressos vendidos
    public void venderIngresso() {
        if (temIngressosDisponiveis()) {
            this.ingressosVendidos++;
        }
    }

    // Metodo para cancelar um ingresso, decrementando a contagem de ingressos vendidos (se houver ingressos vendidos)
    public void cancelarIngresso() {
        if (this.ingressosVendidos > 0) {
            this.ingressosVendidos--;
        }
    }

    // Metodo para formatar o período do evento em uma string legível
    public String getPeriodoFormatado() {
        // Formata as datas de início e fim do evento usando o padrão "dd/MM/yyyy HH:mm"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm ");
        // Retorna uma string no formato "dataInicio até dataFinal", por exemplo: "01/01/2024 18:00 até 01/01/2024 22:00"
        return dataInicio.format(formatter) + " até " + dataFinal.format(formatter);
    }
}