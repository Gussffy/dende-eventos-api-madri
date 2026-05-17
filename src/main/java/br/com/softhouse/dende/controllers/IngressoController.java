package br.com.softhouse.dende.controllers;

import br.com.dende.softhouse.annotations.Controller;
import br.com.dende.softhouse.annotations.request.*;
import br.com.dende.softhouse.process.route.ResponseEntity;
import br.com.softhouse.dende.exceptions.ApiExceptionMapper;
import br.com.softhouse.dende.exceptions.ValidationException;
import br.com.softhouse.dende.dto.request.CompraRequestDTO;
import br.com.softhouse.dende.dto.response.ApiResponse;
import br.com.softhouse.dende.dto.response.CancelamentoResponseDTO;
import br.com.softhouse.dende.dto.response.CompraResponseDTO;
import br.com.softhouse.dende.dto.response.IngressoResponseDTO;
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
        try {
            if (request == null || request.getUsuarioEmail() == null || request.getUsuarioEmail().trim().isEmpty()) {
                throw new ValidationException("Email do usuário é obrigatório");
            }

            CompraResponseDTO compraResponse = ingressoService.comprar(organizadorId, eventoId, request);
            ApiResponse<CompraResponseDTO> apiResponse = new ApiResponse<>(
                    compraResponse, "Compra processada com sucesso", 201
            );
            return ResponseEntity.status(201, apiResponse);
        } catch (Exception e) {
            return ApiExceptionMapper.toResponse(e);
        }
    }

    // Mapeia a rota POST /usuarios/{usuarioId}/ingressos/{ingressoId} para o metodo cancelar, que recebe o ID do usuário e o ID do ingresso no caminho da requisição
    @PostMapping(path = "/usuarios/{usuarioId}/ingressos/{ingressoId}")
    public ResponseEntity<ApiResponse<CancelamentoResponseDTO>> cancelar(
            @PathVariable(parameter = "usuarioId") Long usuarioId,
            @PathVariable(parameter = "ingressoId") Long ingressoId) {
        try {
            if (usuarioId == null || ingressoId == null) {
                throw new ValidationException("ID do usuário e do ingresso são obrigatórios");
            }

            CancelamentoResponseDTO cancelamentoResponse = ingressoService.cancelar(usuarioId, ingressoId);
            ApiResponse<CancelamentoResponseDTO> apiResponse = new ApiResponse<>(
                    cancelamentoResponse, "Cancelamento realizado com sucesso", 200
            );
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return ApiExceptionMapper.toResponse(e);
        }
    }

    // Mapeia a rota GET /usuarios/{usuarioId}/ingressos para o metodo listar, que recebe o ID do usuário no caminho da requisição
    @GetMapping(path = "/usuarios/{usuarioId}/ingressos")
    public ResponseEntity<ApiResponse<List<IngressoResponseDTO>>> listar(@PathVariable(parameter = "usuarioId") Long usuarioId) {
        try {
            if (usuarioId == null) {
                throw new ValidationException("ID do usuário é obrigatório");
            }

            List<IngressoResponseDTO> ingressos = ingressoService.listarPorUsuario(usuarioId);
            ApiResponse<List<IngressoResponseDTO>> apiResponse = new ApiResponse<>(
                    ingressos, "Ingressos listados com sucesso", 200
            );
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return ApiExceptionMapper.toResponse(e);
        }
    }
}