package br.com.softhouse.dende.controllers;

import br.com.dende.softhouse.annotations.Controller;
import br.com.dende.softhouse.annotations.request.*;
import br.com.dende.softhouse.process.route.ResponseEntity;
import br.com.softhouse.dende.dto.ApiResponse;
import br.com.softhouse.dende.dto.OrganizadorRequestDTO;
import br.com.softhouse.dende.dto.OrganizadorResponseDTO;
import br.com.softhouse.dende.dto.StatusChangeRequestDTO;
import br.com.softhouse.dende.services.OrganizadorService;

@Controller
@RequestMapping(path = "/organizadores")
public class OrganizadorController {

    private final OrganizadorService organizadorService;

    public OrganizadorController() {
        this.organizadorService = new OrganizadorService();
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrganizadorResponseDTO>> cadastrar(@RequestBody OrganizadorRequestDTO request) {
        try {
            OrganizadorResponseDTO response = organizadorService.cadastrar(request);
            ApiResponse<OrganizadorResponseDTO> apiResponse = new ApiResponse<>(
                    response, "Organizador cadastrado com sucesso", 201
            );
            return ResponseEntity.status(201, apiResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<OrganizadorResponseDTO> apiResponse = new ApiResponse<>(
                    e.getMessage(), 400, "Bad Request"
            );
            return ResponseEntity.status(400, apiResponse);
        }
    }

    @PutMapping(path = "/{organizadorId}")
    public ResponseEntity<ApiResponse<OrganizadorResponseDTO>> alterar(
            @PathVariable(parameter = "organizadorId") Long id,
            @RequestBody OrganizadorRequestDTO request) {
        try {
            OrganizadorResponseDTO response = organizadorService.atualizar(id, request);
            ApiResponse<OrganizadorResponseDTO> apiResponse = new ApiResponse<>(
                    response, "Organizador atualizado com sucesso", 200
            );
            return ResponseEntity.ok(apiResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<OrganizadorResponseDTO> apiResponse = new ApiResponse<>(
                    e.getMessage(), 400, "Bad Request"
            );
            return ResponseEntity.status(400, apiResponse);
        }
    }

    @GetMapping(path = "/{organizadorId}")
    public ResponseEntity<ApiResponse<OrganizadorResponseDTO>> visualizar(@PathVariable(parameter = "organizadorId") Long id) {
        try {
            OrganizadorResponseDTO response = organizadorService.buscarPorId(id);
            ApiResponse<OrganizadorResponseDTO> apiResponse = new ApiResponse<>(
                    response, "Organizador encontrado", 200
            );
            return ResponseEntity.ok(apiResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<OrganizadorResponseDTO> apiResponse = new ApiResponse<>(
                    e.getMessage(), 404, "Not Found"
            );
            return ResponseEntity.status(404, apiResponse);
        }
    }

    @PatchMapping(path = "/{organizadorId}/{status}")
    public ResponseEntity<ApiResponse<OrganizadorResponseDTO>> alterarStatus(
            @PathVariable(parameter = "organizadorId") Long id,
            @PathVariable(parameter = "status") boolean ativar,
            @RequestBody StatusChangeRequestDTO request) {

        try {
            if (request == null || request.getSenha() == null || request.getSenha().trim().isEmpty()) {
                ApiResponse<OrganizadorResponseDTO> apiResponse = new ApiResponse<>(
                        "Senha é obrigatória", 400, "Bad Request"
                );
                return ResponseEntity.status(400, apiResponse);
            }

            OrganizadorResponseDTO response;
            String operacao;

            if (ativar) {
                response = organizadorService.ativarComSenha(id, request.getSenha());
                operacao = "ativado";
            } else {
                response = organizadorService.desativarComSenha(id, request.getSenha());
                operacao = "desativado";
            }

            ApiResponse<OrganizadorResponseDTO> apiResponse = new ApiResponse<>(
                    response, "Organizador " + operacao + " com sucesso", 200
            );
            return ResponseEntity.ok(apiResponse);

        } catch (IllegalArgumentException e) {
            int status = e.getMessage().contains("Senha incorreta") ? 401 : 400;
            String erro = e.getMessage().contains("Senha incorreta") ? "Unauthorized" : "Bad Request";
            ApiResponse<OrganizadorResponseDTO> apiResponse = new ApiResponse<>(
                    e.getMessage(), status, erro
            );
            return ResponseEntity.status(status, apiResponse);
        }
    }
}