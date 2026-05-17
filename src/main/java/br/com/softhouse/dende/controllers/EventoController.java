package br.com.softhouse.dende.controllers;

import br.com.dende.softhouse.annotations.Controller;
import br.com.dende.softhouse.annotations.request.*;
import br.com.dende.softhouse.process.route.ResponseEntity;
import br.com.softhouse.dende.exceptions.ApiExceptionMapper;
import br.com.softhouse.dende.exceptions.ValidationException;
import br.com.softhouse.dende.dto.request.EventoRequestDTO;
import br.com.softhouse.dende.dto.response.ApiResponse;
import br.com.softhouse.dende.dto.response.EventoResponseDTO;
import br.com.softhouse.dende.dto.response.EventoResumoDTO;
import br.com.softhouse.dende.services.EventoService;
import java.util.List;

/**
 * CONTROLLER DE EVENTOS
 *
 * Essa classe é responsável por receber as requisições HTTP relacionadas a eventos, como cadastro, atualização, ativação/desativação e listagem.
 * Ela atua como uma camada de apresentação, delegando a lógica de negócios para o EventoService e formatando as respostas em um formato consistente (ApiResponse).
 */
@Controller
@RequestMapping(path = "")
public class EventoController {

    private final EventoService eventoService; // Injeção de dependência do serviço de eventos

    public EventoController() {
        this.eventoService = new EventoService();// Instanciação do serviço de eventos
    }

    // Mapeia a rota POST /organizadores/{organizadorId}/eventos para o metodo cadastrar, que recebe o ID do organizador e os dados do evento no corpo da requisição
    @PostMapping(path = "/organizadores/{organizadorId}/eventos")
    public ResponseEntity<ApiResponse<EventoResponseDTO>> cadastrar(
            @PathVariable(parameter = "organizadorId") Long organizadorId,
            @RequestBody EventoRequestDTO dto) {
        try {
            EventoResponseDTO response = eventoService.cadastrar(organizadorId, dto);
            ApiResponse<EventoResponseDTO> apiResponse = new ApiResponse<>(
                    response, "Evento cadastrado com sucesso", 201
            );
            return ResponseEntity.status(201, apiResponse);
        } catch (Exception e) {
            return ApiExceptionMapper.toResponse(e);
        }
    }

    // Mapeia a rota PUT /organizadores/{organizadorId}/eventos/{eventoId} para o metodo alterar, que recebe o ID do organizador, o ID do evento e os dados atualizados do evento no corpo da requisição
    @PutMapping(path = "/organizadores/{organizadorId}/eventos/{eventoId}")
    public ResponseEntity<ApiResponse<EventoResponseDTO>> alterar(
            @PathVariable(parameter = "organizadorId") Long organizadorId,
            @PathVariable(parameter = "eventoId") Long eventoId,
            @RequestBody EventoRequestDTO dto) {
        try {
            EventoResponseDTO response = eventoService.atualizar(organizadorId, eventoId, dto);
            ApiResponse<EventoResponseDTO> apiResponse = new ApiResponse<>(
                    response, "Evento atualizado com sucesso", 200
            );
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return ApiExceptionMapper.toResponse(e);
        }
    }

    // Mapeia a rota PATCH /organizadores/{organizadorId}/eventos/{eventoId}/{status} para o metodo alterarStatusEvento, que recebe o ID do organizador, o ID do evento e o novo status (ativo/inativo) como parâmetros de caminho
    @PatchMapping(path = "/organizadores/{organizadorId}/eventos/{eventoId}/{status}")
    public ResponseEntity<ApiResponse<EventoResponseDTO>> alterarStatusEvento(
            @PathVariable(parameter = "organizadorId") Long organizadorId,
            @PathVariable(parameter = "eventoId") Long eventoId,
            @PathVariable(parameter = "status") boolean ativar) {
        try {
            EventoResponseDTO response;
            String operacao;

            if (ativar) {
                response = eventoService.ativar(organizadorId, eventoId);
                operacao = "ativado";
            } else {
                response = eventoService.desativar(organizadorId, eventoId);
                operacao = "desativado";
            }

            ApiResponse<EventoResponseDTO> apiResponse = new ApiResponse<>(
                    response, "Evento " + operacao + " com sucesso", 200
            );
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return ApiExceptionMapper.toResponse(e);
        }
    }

    // Mapeia a rota GET /organizadores/{organizadorId}/eventos para o metodo listarDoOrganizador, que recebe o ID do organizador como parâmetro de caminho e retorna uma lista de resumos dos eventos desse organizador
    @GetMapping(path = "/organizadores/{organizadorId}/eventos")
    public ResponseEntity<ApiResponse<List<EventoResumoDTO>>> listarDoOrganizador(
            @PathVariable(parameter = "organizadorId") Long organizadorId) {
        try {
            List<EventoResumoDTO> resumos = eventoService.listarPorOrganizador(organizadorId);
            ApiResponse<List<EventoResumoDTO>> apiResponse = new ApiResponse<>(
                    resumos, "Eventos listados com sucesso", 200
            );
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return ApiExceptionMapper.toResponse(e);
        }
    }

    // Mapeia a rota GET /eventos para o metodo feed, que retorna uma lista de eventos ativos disponíveis para os usuários
    @GetMapping(path = "/eventos")
    public ResponseEntity<ApiResponse<List<EventoResponseDTO>>> feed() {
        try {
            List<EventoResponseDTO> response = eventoService.feedAtivos();
            ApiResponse<List<EventoResponseDTO>> apiResponse = new ApiResponse<>(
                    response, "Feed de eventos carregado", 200
            );
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return ApiExceptionMapper.toResponse(e);
        }
    }
}