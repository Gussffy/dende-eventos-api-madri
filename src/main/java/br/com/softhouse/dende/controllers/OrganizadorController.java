package br.com.softhouse.dende.controllers;

import br.com.dende.softhouse.annotations.Controller;
import br.com.dende.softhouse.annotations.request.*;
import br.com.dende.softhouse.process.route.ResponseEntity;
import br.com.softhouse.dende.exceptions.ApiExceptionMapper;
import br.com.softhouse.dende.exceptions.ValidationException;
import br.com.softhouse.dende.dto.ApiResponse;
import br.com.softhouse.dende.dto.OrganizadorDTO;
import br.com.softhouse.dende.dto.StatusChangeRequestDTO;
import br.com.softhouse.dende.services.OrganizadorService;

/**
 * CONTROLLER DE ORGANIZADORES
 *
 * Essa classe é responsável por receber as requisições HTTP relacionadas a organizadores,
 * como cadastro, atualização, ativação/desativação e visualização.
 * Ela atua como uma camada de apresentação, delegando a lógica de negócios para o OrganizadorService
 * e formatando as respostas em um formato consistente (ApiResponse).
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
        try {
            OrganizadorDTO response = organizadorService.cadastrar(dto);
            ApiResponse<OrganizadorDTO> apiResponse = new ApiResponse<>(
                    response, "Organizador cadastrado com sucesso", 201
            );
            return ResponseEntity.status(201, apiResponse);
        } catch (Exception e) {
            return ApiExceptionMapper.toResponse(e);
        }
    }

    // Mapeia a rota PUT /organizadores/{organizadorId} para o metodo alterar, que recebe o ID do organizador e os dados atualizados do organizador no corpo da requisição
    @PutMapping(path = "/{organizadorId}")
    public ResponseEntity<ApiResponse<OrganizadorDTO>> alterar(
            @PathVariable(parameter = "organizadorId") Long id,
            @RequestBody OrganizadorDTO dto) {
        try {
            OrganizadorDTO response = organizadorService.atualizar(id, dto);
            ApiResponse<OrganizadorDTO> apiResponse = new ApiResponse<>(
                    response, "Organizador atualizado com sucesso", 200
            );
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return ApiExceptionMapper.toResponse(e);
        }
    }

    // Mapeia a rota GET /organizadores/{organizadorId} para o metodo visualizar, que recebe o ID do organizador como parâmetro da rota
    @GetMapping(path = "/{organizadorId}")
    public ResponseEntity<ApiResponse<OrganizadorDTO>> visualizar(@PathVariable(parameter = "organizadorId") Long id) {
        try {
            OrganizadorDTO response = organizadorService.buscarPorId(id);
            ApiResponse<OrganizadorDTO> apiResponse = new ApiResponse<>(
                    response, "Organizador encontrado", 200
            );
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return ApiExceptionMapper.toResponse(e);
        }
    }

    // Mapeia a rota PATCH /organizadores/{organizadorId}/{status} para o metodo alterarStatus, que recebe o ID do organizador e o novo status (ativo/inativo) como parâmetros de caminho, e a senha no corpo da requisição para autenticação
    @PatchMapping(path = "/{organizadorId}/{status}")
    public ResponseEntity<ApiResponse<OrganizadorDTO>> alterarStatus(
            @PathVariable(parameter = "organizadorId") Long id,
            @PathVariable(parameter = "status") boolean ativar,
            @RequestBody StatusChangeRequestDTO request) {
        try {
            if (request == null || request.getSenha() == null || request.getSenha().trim().isEmpty()) {
                throw new ValidationException("Senha é obrigatória");
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

        } catch (Exception e) {
            return ApiExceptionMapper.toResponse(e);
        }
    }

}