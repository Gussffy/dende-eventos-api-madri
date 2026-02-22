package br.com.softhouse.dende.controllers;

import br.com.dende.softhouse.annotations.Controller;
import br.com.dende.softhouse.annotations.request.*;
import br.com.dende.softhouse.process.route.ResponseEntity;
import br.com.softhouse.dende.dto.IngressoRequestDTO;
import br.com.softhouse.dende.dto.IngressoResponseDTO;
import br.com.softhouse.dende.dto.CompraResponseDTO;
import br.com.softhouse.dende.dto.CancelamentoResponseDTO;
import br.com.softhouse.dende.services.IngressoService;
import java.util.List;

/**
    CONTROLADOR DE INGRESSOS

    Esta classe é responsável por RECEBER as requisições HTTP relacionadas a ingressos
    e DELEGAR para o serviço de ingressos (IngressoService) a execução das regras de negócio.

 */
@Controller
@RequestMapping(path = "") // Rota base vazia, as rotas completas são definidas nos métodos para seguir o padrão REST
public class IngressoController {

    // Instância do serviço de ingressos para delegar as operações
    private final IngressoService ingressoService;

    // Construtor que inicializa o serviço de ingressos
    public IngressoController() {
        this.ingressoService = new IngressoService();
    }

    // Mapeia a rota POST /organizadores/{organizadorId}/eventos/{eventoId}/ingressos para comprar um ingresso para um evento específico
    @PostMapping(path = "/organizadores/{organizadorId}/eventos/{eventoId}/ingressos")
    public ResponseEntity<String> comprar(
            @PathVariable(parameter = "organizadorId") Long organizadorId,
            @PathVariable(parameter = "eventoId") Long eventoId,
            @RequestBody IngressoRequestDTO request) {
        try {
            // Chama o service para comprar o ingresso, passando o ID do organizador, o ID do evento e os dados da compra
            CompraResponseDTO response = ingressoService.comprar(organizadorId, eventoId, request);
            return ResponseEntity.status(201, "Compra realizada com sucesso. ID do ingresso: " + response.getCodigosIngressos());
        } catch (IllegalArgumentException e) {

            // Captura exceções de validação (usuário não encontrado, evento não encontrado, ingressos esgotados, etc.)
            return ResponseEntity.status(400, "Erro ao comprar ingresso: " + e.getMessage());
        }
    }

    // Mapeia a rota POST /usuarios/{usuarioId}/ingressos/{ingressoId} para cancelar um ingresso específico de um usuário
    @PostMapping(path = "/usuarios/{usuarioId}/ingressos/{ingressoId}")
    public ResponseEntity<String> cancelar(
            @PathVariable(parameter = "usuarioId") Long usuarioId,
            @PathVariable(parameter = "ingressoId") Long ingressoId) {
        try {
            // Chama o service para cancelar o ingresso, passando o ID do usuário e o ID do ingresso
            CancelamentoResponseDTO response = ingressoService.cancelar(usuarioId, ingressoId);
            return ResponseEntity.ok("Cancelamento realizado com sucesso. ID do ingresso cancelado: " + response.getCodigoIngresso());
        } catch (IllegalArgumentException e) {

            // Captura exceções de validação (usuário não encontrado, ingresso não encontrado, ingresso já cancelado, etc.)
            return ResponseEntity.status(400, "Erro ao cancelar ingresso: " + e.getMessage());
        }
    }

    // Mapeia a rota GET /usuarios/{usuarioId}/ingressos para listar todos os ingressos de um usuário específico
    @GetMapping(path = "/usuarios/{usuarioId}/ingressos")
    public ResponseEntity<List<IngressoResponseDTO>> listar(@PathVariable(parameter = "usuarioId") Long usuarioId) {
        try {
            // Chama o service para listar os ingressos do usuário, passando o ID do usuário
            List<IngressoResponseDTO> ingressos = ingressoService.listarPorUsuario(usuarioId);
            return ResponseEntity.ok(ingressos);
        } catch (IllegalArgumentException e) {
            // Captura exceções de validação (usuário não encontrado, etc.)
            return ResponseEntity.status(404, null);
        }
    }
}