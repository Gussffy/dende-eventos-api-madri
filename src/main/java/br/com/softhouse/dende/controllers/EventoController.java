package br.com.softhouse.dende.controllers;

import br.com.dende.softhouse.annotations.Controller;
import br.com.dende.softhouse.annotations.request.*;
import br.com.dende.softhouse.process.route.ResponseEntity;
import br.com.softhouse.dende.dto.EventoRequestDTO;
import br.com.softhouse.dende.dto.EventoResponseDTO;
import br.com.softhouse.dende.dto.EventoResumoDTO;
import br.com.softhouse.dende.dto.StatusChangeRequestDTO;
import br.com.softhouse.dende.services.EventoService;
import java.util.List;

/**
    CONTROLADOR DE EVENTOS

    Esta classe é responsável por RECEBER as requisições HTTP relacionadas a eventos
    e DELEGAR para o serviço de eventos (EventoService) a execução das regras de negócio.

 */
@Controller
@RequestMapping(path = "")  // Rota base vazia, as rotas completas são definidas nos métodos para seguir o padrão REST
public class EventoController {

    // Instância do serviço de eventos para delegar as operações
    private final EventoService eventoService;

    public EventoController() {

        // Inicializa o serviço de eventos
        this.eventoService = new EventoService();
    }

    // Mapeia a rota POST /organizadores/{organizadorId}/eventos para cadastrar um novo evento para um organizador específico
    @PostMapping(path = "/organizadores/{organizadorId}/eventos")
    public ResponseEntity<String> cadastrar(
            @PathVariable(parameter = "organizadorId") Long organizadorId,
            @RequestBody EventoRequestDTO request) {
        try {
            // Chama o service para cadastrar o evento, passando o ID do organizador e os dados do evento
            EventoResponseDTO response = eventoService.cadastrar(organizadorId, request);

            // Retorna status 201 (Created) com mensagem de sucesso contendo o ID do evento criado
            return ResponseEntity.status(201, "Evento cadastrado com sucesso. ID: " + response.getId());
        } catch (IllegalArgumentException e) {

            // Captura exceções de validação (campos obrigatórios, organizador não encontrado, etc.)
            return ResponseEntity.status(400, "Erro ao cadastrar evento: " + e.getMessage());
        }
    }

    // Mapeia a rota PUT /organizadores/{organizadorId}/eventos/{eventoId} para atualizar os dados de um evento existente
    @PutMapping(path = "/organizadores/{organizadorId}/eventos/{eventoId}")
    public ResponseEntity<String> alterar(
            @PathVariable(parameter = "organizadorId") Long organizadorId,
            @PathVariable(parameter = "eventoId") Long eventoId,
            @RequestBody EventoRequestDTO request) {
        try {
            // Chama o service para atualizar o evento, passando o ID do organizador, o ID do evento e os dados atualizados
            EventoResponseDTO response = eventoService.atualizar(organizadorId, eventoId, request);

            // Retorna status 200 (OK) com mensagem de sucesso contendo o ID do evento atualizado
            return ResponseEntity.ok("Evento atualizado com sucesso. ID: " + response.getId());
        } catch (IllegalArgumentException e) {

            // Captura exceções de validação (organizador ou evento não encontrado, campos obrigatórios, etc.)
            return ResponseEntity.status(400, "Erro ao atualizar evento: " + e.getMessage());
        }
    }

    // Mapeia a rota PATCH /organizadores/{organizadorId}/eventos/{status} para ativar ou desativar um evento
    @PatchMapping(path = "/organizadores/{organizadorId}/eventos/{status}")
    public ResponseEntity<String> alterarStatusEvento(
            @PathVariable(parameter = "organizadorId") Long organizadorId,
            @PathVariable(parameter = "status") boolean ativar,
            @RequestBody StatusChangeRequestDTO request) {
        try {
            // Valida se o ID do evento foi fornecido no corpo da requisição, caso contrário retorna erro
            if (request.getEventoId() == null) {
                return ResponseEntity.status(400, "ID do evento é obrigatório");
            }

            // Chama o service para ativar ou desativar o evento, passando o ID do organizador, o ID do evento e a ação (ativar/desativar)
            EventoResponseDTO response;
            if (ativar) {
                response = eventoService.ativar(organizadorId, request.getEventoId());
            } else {
                response = eventoService.desativar(organizadorId, request.getEventoId());
            }

            // Retorna status 200 (OK) com mensagem de sucesso indicando se o evento foi ativado ou desativado, incluindo o nome do evento para clareza
            return ResponseEntity.ok(
                    "Evento " + (ativar ? "ativado" : "desativado") + " com sucesso: " + response.getNome()
            );
        } catch (IllegalArgumentException e) {

            // Captura exceções de validação (organizador ou evento não encontrado.)
            return ResponseEntity.status(400, "Erro:" + e.getMessage());
        }
    }

    // Mapeia a rota GET /organizadores/{organizadorId}/eventos para listar os eventos de um organizador específico, retornando um resumo dos eventos
    @GetMapping(path = "/organizadores/{organizadorId}/eventos")
    public ResponseEntity<List<EventoResumoDTO>> listarDoOrganizador(
            @PathVariable(parameter = "organizadorId") Long organizadorId) {

        // Chama o service para listar os eventos do organizador, passando o ID do organizador
        List<EventoResumoDTO> eventos = eventoService.listarPorOrganizador(organizadorId);
        return ResponseEntity.ok(eventos);
    }

    // Mapeia a rota GET /eventos para listar todos os eventos ativos, retornando um feed dos eventos
    @GetMapping(path = "/eventos")
    public ResponseEntity<List<EventoResponseDTO>> feed() {

        // Chama o service para listar os eventos ativos, sem necessidade de passar um ID de organizador
        List<EventoResponseDTO> eventos = eventoService.feedAtivos();
        return ResponseEntity.ok(eventos);
    }
}