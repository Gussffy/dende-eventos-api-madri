package br.com.softhouse.dende.controllers;

import br.com.dende.softhouse.annotations.Controller;
import br.com.dende.softhouse.annotations.request.*;
import br.com.dende.softhouse.process.route.ResponseEntity;
import br.com.softhouse.dende.dto.ApiResponse;
import br.com.softhouse.dende.dto.IngressoRequestDTO;
import br.com.softhouse.dende.dto.IngressoResponseDTO;
import br.com.softhouse.dende.dto.CompraResponseDTO;
import br.com.softhouse.dende.dto.CancelamentoResponseDTO;
import br.com.softhouse.dende.services.IngressoService;
import java.util.List;

@Controller
@RequestMapping(path = "")
public class IngressoController {

    private final IngressoService ingressoService;

    public IngressoController() {
        this.ingressoService = new IngressoService();
    }

    @PostMapping(path = "/organizadores/{organizadorId}/eventos/{eventoId}/ingressos")
    public ResponseEntity<ApiResponse<CompraResponseDTO>> comprar(
            @PathVariable(parameter = "organizadorId") Long organizadorId,
            @PathVariable(parameter = "eventoId") Long eventoId,
            @RequestBody IngressoRequestDTO request) {
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

    @PostMapping(path = "/usuarios/{usuarioId}/ingressos/{ingressoId}")
    public ResponseEntity<ApiResponse<CancelamentoResponseDTO>> cancelar(
            @PathVariable(parameter = "usuarioId") Long usuarioId,
            @PathVariable(parameter = "ingressoId") Long ingressoId) {
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

    @GetMapping(path = "/usuarios/{usuarioId}/ingressos")
    public ResponseEntity<ApiResponse<List<IngressoResponseDTO>>> listar(@PathVariable(parameter = "usuarioId") Long usuarioId) {
        try {
            if (usuarioId == null) {
                throw new IllegalArgumentException("ID do usuário é obrigatório");
            }

            List<IngressoResponseDTO> ingressos = ingressoService.listarPorUsuario(usuarioId);
            ApiResponse<List<IngressoResponseDTO>> apiResponse = new ApiResponse<>(
                    ingressos, "Ingressos listados com sucesso", 200
            );
            return ResponseEntity.ok(apiResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<List<IngressoResponseDTO>> apiResponse = new ApiResponse<>(
                    e.getMessage(), 404, "Not Found"
            );
            return ResponseEntity.status(404, apiResponse);
        } catch (Exception e) {
            ApiResponse<List<IngressoResponseDTO>> apiResponse = new ApiResponse<>(
                    "Erro interno ao listar ingressos: " + e.getMessage(),
                    500, "Internal Server Error"
            );
            return ResponseEntity.status(500, apiResponse);
        }
    }
}