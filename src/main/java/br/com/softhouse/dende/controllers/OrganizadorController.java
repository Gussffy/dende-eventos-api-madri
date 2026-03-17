package br.com.softhouse.dende.controllers;

import br.com.dende.softhouse.annotations.Controller;
import br.com.dende.softhouse.annotations.request.*;
import br.com.dende.softhouse.process.route.ResponseEntity;
import br.com.softhouse.dende.dto.ApiResponse;
import br.com.softhouse.dende.dto.OrganizadorDTO;
import br.com.softhouse.dende.dto.StatusChangeRequestDTO;
import br.com.softhouse.dende.services.OrganizadorService;

/**
 * CONTROLLER DE ORGANIZADORES
 *
 * Essa classe é responsável por receber as requisições HTTP relacionadas a organizadores, como cadastro, atualização, ativação/desativação e visualização.
 * Ela atua como uma camada de apresentação, delegando a lógica de negócios para o OrganizadorService e formatando as respostas em um formato consistente (ApiResponse).
 */
@Controller
@RequestMapping(path = "/organizadores")
public class OrganizadorController {

    private final OrganizadorService organizadorService; // Injeção de dependência do serviço de organizadores

    public OrganizadorController() {
        this.organizadorService = new OrganizadorService(); // Instanciação do serviço de organizadores
    }

    // Mapeia a rota POST /organizadores para o metodo cadastrar, que recebe os dados do organizador no corpo da requisição
    @PostMapping
    public ResponseEntity<ApiResponse<OrganizadorDTO>> cadastrar(@RequestBody OrganizadorDTO dto) {
        // Tenta cadastrar o organizador usando o serviço, e retorna uma resposta formatada com ApiResponse
        try {
            OrganizadorDTO response = organizadorService.cadastrar(dto);
            ApiResponse<OrganizadorDTO> apiResponse = new ApiResponse<>(
                    response, "Organizador cadastrado com sucesso", 201
            );
            return ResponseEntity.status(201, apiResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<OrganizadorDTO> apiResponse = new ApiResponse<>(
                    e.getMessage(), 400, "Bad Request"
            );
            return ResponseEntity.status(400, apiResponse);
        }
    }

    // Mapeia a rota PUT /organizadores/{organizadorId} para o metodo alterar, que recebe o ID do organizador e os dados atualizados do organizador no corpo da requisição
    @PutMapping(path = "/{organizadorId}")
    public ResponseEntity<ApiResponse<OrganizadorDTO>> alterar(
            @PathVariable(parameter = "organizadorId") Long id,
            @RequestBody OrganizadorDTO dto) {
        // Tenta atualizar o organizador usando o serviço, e retorna uma resposta formatada com ApiResponse
        try {
            OrganizadorDTO response = organizadorService.atualizar(id, dto);
            ApiResponse<OrganizadorDTO> apiResponse = new ApiResponse<>(
                    response, "Organizador atualizado com sucesso", 200
            );
            return ResponseEntity.ok(apiResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<OrganizadorDTO> apiResponse = new ApiResponse<>(
                    e.getMessage(), 400, "Bad Request"
            );
            return ResponseEntity.status(400, apiResponse);
        }
    }

    // Mapeia a rota GET /organizadores/{organizadorId} para o metodo visualizar, que recebe o ID do organizador como parâmetro da rota
    @GetMapping(path = "/{organizadorId}")
    public ResponseEntity<ApiResponse<OrganizadorDTO>> visualizar(@PathVariable(parameter = "organizadorId") Long id) {
        // Tenta buscar o organizador por ID usando o serviço, e retorna uma resposta formatada com ApiResponse
        try {
            OrganizadorDTO response = organizadorService.buscarPorId(id);
            ApiResponse<OrganizadorDTO> apiResponse = new ApiResponse<>(
                    response, "Organizador encontrado", 200
            );
            return ResponseEntity.ok(apiResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<OrganizadorDTO> apiResponse = new ApiResponse<>(
                    e.getMessage(), 404, "Not Found"
            );
            return ResponseEntity.status(404, apiResponse);
        }
    }

    // Mapeia a rota PATCH /organizadores/{organizadorId}/{status} para o metodo alterarStatus, que recebe o ID do organizador e o novo status (ativo/inativo) como parâmetros de caminho, e a senha no corpo da requisição para autenticação
    @PatchMapping(path = "/{organizadorId}/{status}")
    public ResponseEntity<ApiResponse<OrganizadorDTO>> alterarStatus(
            @PathVariable(parameter = "organizadorId") Long id,
            @PathVariable(parameter = "status") boolean ativar,
            @RequestBody StatusChangeRequestDTO request) {
        // Tenta ativar ou desativar o organizador usando o serviço, passando a senha para autenticação, e retorna uma resposta formatada com ApiResponse indicando o resultado da operação
        try {
            if (request == null || request.getSenha() == null || request.getSenha().trim().isEmpty()) {
                ApiResponse<OrganizadorDTO> apiResponse = new ApiResponse<>(
                        "Senha é obrigatória", 400, "Bad Request"
                );
                return ResponseEntity.status(400, apiResponse);
            }

            OrganizadorDTO response;
            String operacao;

            if (ativar) {
                response = organizadorService.ativarComSenha(id, request.getSenha());
                operacao = "ativado";
            } else {
                response = organizadorService.desativarComSenha(id, request.getSenha());
                operacao = "desativado";
            }

            ApiResponse<OrganizadorDTO> apiResponse = new ApiResponse<>(
                    response, "Organizador " + operacao + " com sucesso", 200
            );
            return ResponseEntity.ok(apiResponse);

        } catch (IllegalArgumentException e) {
            int status = e.getMessage().contains("Senha incorreta") ? 401 : 400;
            String erro = e.getMessage().contains("Senha incorreta") ? "Unauthorized" : "Bad Request";
            ApiResponse<OrganizadorDTO> apiResponse = new ApiResponse<>(
                    e.getMessage(), status, erro
            );
            return ResponseEntity.status(status, apiResponse);
        }
    }
}