package br.com.softhouse.dende.controllers;

import br.com.dende.softhouse.annotations.Controller;
import br.com.dende.softhouse.annotations.request.*;
import br.com.dende.softhouse.process.route.ResponseEntity;
import br.com.softhouse.dende.dto.ApiResponse;
import br.com.softhouse.dende.dto.EventoRequestDTO;
import br.com.softhouse.dende.dto.EventoResponseDTO;
import br.com.softhouse.dende.dto.EventoResumoDTO;
import br.com.softhouse.dende.services.EventoService;
import java.util.List;

@Controller
@RequestMapping(path = "")
public class EventoController {

    private final EventoService eventoService;

    public EventoController() {
        this.eventoService = new EventoService();
    }

    @PostMapping(path = "/organizadores/{organizadorId}/eventos")
    public ResponseEntity<ApiResponse<EventoResponseDTO>> cadastrar(
            @PathVariable(parameter = "organizadorId") Long organizadorId,
            @RequestBody EventoRequestDTO request) {
        try {
            EventoResponseDTO response = eventoService.cadastrar(organizadorId, request);
            ApiResponse<EventoResponseDTO> apiResponse = new ApiResponse<>(
                    response, "Evento cadastrado com sucesso", 201
            );
            return ResponseEntity.status(201, apiResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<EventoResponseDTO> apiResponse = new ApiResponse<>(
                    e.getMessage(), 400, "Bad Request"
            );
            return ResponseEntity.status(400, apiResponse);
        }
    }

    @PutMapping(path = "/organizadores/{organizadorId}/eventos/{eventoId}")
    public ResponseEntity<ApiResponse<EventoResponseDTO>> alterar(
            @PathVariable(parameter = "organizadorId") Long organizadorId,
            @PathVariable(parameter = "eventoId") Long eventoId,
            @RequestBody EventoRequestDTO request) {
        try {
            EventoResponseDTO response = eventoService.atualizar(organizadorId, eventoId, request);
            ApiResponse<EventoResponseDTO> apiResponse = new ApiResponse<>(
                    response, "Evento atualizado com sucesso", 200
            );
            return ResponseEntity.ok(apiResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<EventoResponseDTO> apiResponse = new ApiResponse<>(
                    e.getMessage(), 400, "Bad Request"
            );
            return ResponseEntity.status(400, apiResponse);
        }
    }

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
        } catch (IllegalArgumentException e) {
            ApiResponse<EventoResponseDTO> apiResponse = new ApiResponse<>(
                    e.getMessage(), 400, "Bad Request"
            );
            return ResponseEntity.status(400, apiResponse);
        }
    }

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
            ApiResponse<List<EventoResumoDTO>> apiResponse = new ApiResponse<>(
                    e.getMessage(), 400, "Bad Request"
            );
            return ResponseEntity.status(400, apiResponse);
        }
    }

    @GetMapping(path = "/eventos")
    public ResponseEntity<ApiResponse<List<EventoResponseDTO>>> feed() {
        try {
            List<EventoResponseDTO> response = eventoService.feedAtivos();
            ApiResponse<List<EventoResponseDTO>> apiResponse = new ApiResponse<>(
                    response, "Feed de eventos carregado", 200
            );
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            ApiResponse<List<EventoResponseDTO>> apiResponse = new ApiResponse<>(
                    e.getMessage(), 400, "Bad Request"
            );
            return ResponseEntity.status(400, apiResponse);
        }
    }
}