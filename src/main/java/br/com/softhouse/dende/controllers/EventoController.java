package br.com.softhouse.dende.controllers;

import br.com.dende.softhouse.annotations.Controller;
import br.com.dende.softhouse.annotations.request.*;
import br.com.dende.softhouse.process.route.ResponseEntity;
import br.com.softhouse.dende.dto.ApiResponse;
import br.com.softhouse.dende.dto.EventoDTO;
import br.com.softhouse.dende.dto.EventoResumoDTO;
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
    public ResponseEntity<ApiResponse<EventoDTO>> cadastrar(
            @PathVariable(parameter = "organizadorId") Long organizadorId,
            @RequestBody EventoDTO dto) {
        // Tenta cadastrar o evento usando o serviço, e retorna uma resposta formatada com ApiResponse
        try {
            EventoDTO response = eventoService.cadastrar(organizadorId, dto);
            ApiResponse<EventoDTO> apiResponse = new ApiResponse<>(
                    response, "Evento cadastrado com sucesso", 201
            );
            return ResponseEntity.status(201, apiResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<EventoDTO> apiResponse = new ApiResponse<>(
                    e.getMessage(), 400, "Bad Request"
            );
            return ResponseEntity.status(400, apiResponse);
        }
    }

    // Mapeia a rota PUT /organizadores/{organizadorId}/eventos/{eventoId} para o metodo alterar, que recebe o ID do organizador, o ID do evento e os dados atualizados do evento no corpo da requisição
    @PutMapping(path = "/organizadores/{organizadorId}/eventos/{eventoId}")
    public ResponseEntity<ApiResponse<EventoDTO>> alterar(
            @PathVariable(parameter = "organizadorId") Long organizadorId,
            @PathVariable(parameter = "eventoId") Long eventoId,
            @RequestBody EventoDTO dto) {
        // Tenta atualizar o evento usando o serviço, e retorna uma resposta formatada com ApiResponse
        try {
            EventoDTO response = eventoService.atualizar(organizadorId, eventoId, dto);
            ApiResponse<EventoDTO> apiResponse = new ApiResponse<>(
                    response, "Evento atualizado com sucesso", 200
            );
            return ResponseEntity.ok(apiResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<EventoDTO> apiResponse = new ApiResponse<>(
                    e.getMessage(), 400, "Bad Request"
            );
            return ResponseEntity.status(400, apiResponse);
        }
    }

    // Mapeia a rota PATCH /organizadores/{organizadorId}/eventos/{eventoId}/{status} para o metodo alterarStatusEvento, que recebe o ID do organizador, o ID do evento e o novo status (ativo/inativo) como parâmetros de caminho
    @PatchMapping(path = "/organizadores/{organizadorId}/eventos/{eventoId}/{status}")
    public ResponseEntity<ApiResponse<EventoDTO>> alterarStatusEvento(
            @PathVariable(parameter = "organizadorId") Long organizadorId,
            @PathVariable(parameter = "eventoId") Long eventoId,
            @PathVariable(parameter = "status") boolean ativar) {
        // Tenta ativar ou desativar o evento usando o serviço, e retorna uma resposta formatada com ApiResponse indicando o resultado da operação
        try {
            EventoDTO response;
            String operacao;

            if (ativar) {
                response = eventoService.ativar(organizadorId, eventoId);
                operacao = "ativado";
            } else {
                response = eventoService.desativar(organizadorId, eventoId);
                operacao = "desativado";
            }

            ApiResponse<EventoDTO> apiResponse = new ApiResponse<>(
                    response, "Evento " + operacao + " com sucesso", 200
            );
            return ResponseEntity.ok(apiResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<EventoDTO> apiResponse = new ApiResponse<>(
                    e.getMessage(), 400, "Bad Request"
            );
            return ResponseEntity.status(400, apiResponse);
        }
    }

    // Mapeia a rota GET /organizadores/{organizadorId}/eventos para o metodo listarDoOrganizador, que recebe o ID do organizador como parâmetro de caminho e retorna uma lista de resumos dos eventos desse organizador
    @GetMapping(path = "/organizadores/{organizadorId}/eventos")
    public ResponseEntity<ApiResponse<List<EventoResumoDTO>>> listarDoOrganizador(
            @PathVariable(parameter = "organizadorId") Long organizadorId) {
        // Tenta listar os eventos do organizador usando o serviço, e retorna uma resposta formatada com ApiResponse contendo a lista de resumos dos eventos ou uma mensagem de erro em caso de falha
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

    // Mapeia a rota GET /eventos para o metodo feed, que retorna uma lista de eventos ativos disponíveis para os usuários
    @GetMapping(path = "/eventos")
    public ResponseEntity<ApiResponse<List<EventoDTO>>> feed() {
        // Tenta obter o feed de eventos ativos usando o serviço, e retorna uma resposta formatada com ApiResponse contendo a lista de eventos ou uma mensagem de erro em caso de falha
        try {
            List<EventoDTO> response = eventoService.feedAtivos();
            ApiResponse<List<EventoDTO>> apiResponse = new ApiResponse<>(
                    response, "Feed de eventos carregado", 200
            );
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            ApiResponse<List<EventoDTO>> apiResponse = new ApiResponse<>(
                    e.getMessage(), 400, "Bad Request"
            );
            return ResponseEntity.status(400, apiResponse);
        }
    }
}