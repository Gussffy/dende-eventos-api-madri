package br.com.softhouse.dende.controllers;

import br.com.dende.softhouse.annotations.Controller;
import br.com.dende.softhouse.annotations.request.*;
import br.com.dende.softhouse.process.route.ResponseEntity;
import br.com.softhouse.dende.dto.ApiResponse;
import br.com.softhouse.dende.dto.CompraRequestDTO;
import br.com.softhouse.dende.dto.CompraResponseDTO;
import br.com.softhouse.dende.dto.CancelamentoResponseDTO;
import br.com.softhouse.dende.dto.IngressoDTO;
import br.com.softhouse.dende.services.IngressoService;
import java.util.List;

/**
 * CONTROLLER DE INGRESSOS
 *
 * Essa classe é responsável por receber as requisições HTTP relacionadas a ingressos, como compra, cancelamento e listagem.
 * Ela atua como uma camada de apresentação, delegando a lógica de negócios para o IngressoService e formatando as respostas em um formato consistente (ApiResponse).
 */
@Controller
@RequestMapping(path = "")
public class IngressoController {

    private final IngressoService ingressoService;// Injeção de dependência do serviço de ingressos

    public IngressoController() {
        this.ingressoService = new IngressoService();// Instanciação do serviço de ingressos
    }

    // Mapeia a rota POST /organizadores/{organizadorId}/eventos/{eventoId}/ingressos para o metodo comprar, que recebe o ID do organizador, o ID do evento e os dados da compra no corpo da requisição
    @PostMapping(path = "/organizadores/{organizadorId}/eventos/{eventoId}/ingressos")
    public ResponseEntity<ApiResponse<CompraResponseDTO>> comprar(
            @PathVariable(parameter = "organizadorId") Long organizadorId,
            @PathVariable(parameter = "eventoId") Long eventoId,
            @RequestBody CompraRequestDTO request) {
        // Tenta processar a compra usando o serviço, e retorna uma resposta formatada com ApiResponse
        try {
            if (request == null || request.getUsuarioEmail() == null || request.getUsuarioEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("Email do usuário é obrigatório");
            }

            CompraResponseDTO compraResponse = ingressoService.comprar(organizadorId, eventoId, request);
            ApiResponse<CompraResponseDTO> apiResponse = new ApiResponse<>(
                    compraResponse, "Compra processada com sucesso", 201
            );
            return ResponseEntity.status(201, apiResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<CompraResponseDTO> apiResponse = new ApiResponse<>(
                    e.getMessage(), 400, "Bad Request"
            );
            return ResponseEntity.status(400, apiResponse);
        } catch (Exception e) {
            ApiResponse<CompraResponseDTO> apiResponse = new ApiResponse<>(
                    "Erro interno ao processar compra: " + e.getMessage(),
                    500, "Internal Server Error"
            );
            return ResponseEntity.status(500, apiResponse);
        }
    }

    // Mapeia a rota POST /usuarios/{usuarioId}/ingressos/{ingressoId} para o metodo cancelar, que recebe o ID do usuário e o ID do ingresso no caminho da requisição
    @PostMapping(path = "/usuarios/{usuarioId}/ingressos/{ingressoId}")
    public ResponseEntity<ApiResponse<CancelamentoResponseDTO>> cancelar(
            @PathVariable(parameter = "usuarioId") Long usuarioId,
            @PathVariable(parameter = "ingressoId") Long ingressoId) {
        // Tenta processar o cancelamento usando o serviço, e retorna uma resposta formatada com ApiResponse
        try {
            if (usuarioId == null || ingressoId == null) {
                throw new IllegalArgumentException("ID do usuário e do ingresso são obrigatórios");
            }

            CancelamentoResponseDTO cancelamentoResponse = ingressoService.cancelar(usuarioId, ingressoId);
            ApiResponse<CancelamentoResponseDTO> apiResponse = new ApiResponse<>(
                    cancelamentoResponse, "Cancelamento realizado com sucesso", 200
            );
            return ResponseEntity.ok(apiResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<CancelamentoResponseDTO> apiResponse = new ApiResponse<>(
                    e.getMessage(), 400, "Bad Request"
            );
            return ResponseEntity.status(400, apiResponse);
        } catch (Exception e) {
            ApiResponse<CancelamentoResponseDTO> apiResponse = new ApiResponse<>(
                    "Erro interno ao cancelar ingresso: " + e.getMessage(),
                    500, "Internal Server Error"
            );
            return ResponseEntity.status(500, apiResponse);
        }
    }

    // Mapeia a rota GET /usuarios/{usuarioId}/ingressos para o metodo listar, que recebe o ID do usuário no caminho da requisição
    @GetMapping(path = "/usuarios/{usuarioId}/ingressos")
    public ResponseEntity<ApiResponse<List<IngressoDTO>>> listar(@PathVariable(parameter = "usuarioId") Long usuarioId) {
        // Tenta listar os ingressos do usuário usando o serviço, e retorna uma resposta formatada com ApiResponse contendo a lista de ingressos ou uma mensagem de erro em caso de falha
        try {
            if (usuarioId == null) {
                throw new IllegalArgumentException("ID do usuário é obrigatório");
            }

            List<IngressoDTO> ingressos = ingressoService.listarPorUsuario(usuarioId);
            ApiResponse<List<IngressoDTO>> apiResponse = new ApiResponse<>(
                    ingressos, "Ingressos listados com sucesso", 200
            );
            return ResponseEntity.ok(apiResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<List<IngressoDTO>> apiResponse = new ApiResponse<>(
                    e.getMessage(), 404, "Not Found"
            );
            return ResponseEntity.status(404, apiResponse);
        } catch (Exception e) {
            ApiResponse<List<IngressoDTO>> apiResponse = new ApiResponse<>(
                    "Erro interno ao listar ingressos: " + e.getMessage(),
                    500, "Internal Server Error"
            );
            return ResponseEntity.status(500, apiResponse);
        }
    }
}