package br.com.softhouse.dende.controllers;

import br.com.dende.softhouse.annotations.Controller;
import br.com.dende.softhouse.annotations.request.*;
import br.com.dende.softhouse.process.route.ResponseEntity;
import br.com.softhouse.dende.exceptions.ApiExceptionMapper;
import br.com.softhouse.dende.exceptions.ValidationException;
import br.com.softhouse.dende.dto.request.StatusChangeRequestDTO;
import br.com.softhouse.dende.dto.request.UsuarioRequestDTO;
import br.com.softhouse.dende.dto.response.ApiResponse;
import br.com.softhouse.dende.dto.response.UsuarioResponseDTO;
import br.com.softhouse.dende.services.UsuarioService;

/**
 * CONTROLLER DE USUÁRIOS
 *
 * Essa classe é responsável por receber as requisições HTTP relacionadas a usuários, como cadastro, atualização, ativação/desativação e visualização.
 * Ela atua como uma camada de apresentação, delegando a lógica de negócios para o UsuarioService e formatando as respostas em um formato consistente (ApiResponse).
 */
@Controller
@RequestMapping(path = "/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;// Injeção de dependência do serviço de usuários

    public UsuarioController() {
        this.usuarioService = new UsuarioService();// Instanciação do serviço de usuários
    }

    // Mapeia a requisição POST /usuarios para cadastrar um novo usuário
    @PostMapping
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> cadastrar(@RequestBody UsuarioRequestDTO dto) {
        try {
            UsuarioResponseDTO response = usuarioService.cadastrar(dto);
            ApiResponse<UsuarioResponseDTO> apiResponse = new ApiResponse<>(
                    response, "Usuário cadastrado com sucesso", 201
            );
            return ResponseEntity.status(201, apiResponse);
        } catch (Exception e) {
            return ApiExceptionMapper.toResponse(e);
        }
    }

    // Mapeia a requisição PUT /usuarios/{usuarioId} para atualizar os dados de um usuário existente
    @PutMapping(path = "/{usuarioId}")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> alterar(
            @PathVariable(parameter = "usuarioId") Long id,
            @RequestBody UsuarioRequestDTO dto) {
        try {
            UsuarioResponseDTO response = usuarioService.atualizar(id, dto);
            ApiResponse<UsuarioResponseDTO> apiResponse = new ApiResponse<>(
                    response, "Usuário atualizado com sucesso", 200
            );
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return ApiExceptionMapper.toResponse(e);
        }
    }

    // Mapeia a requisição GET /usuarios/{usuarioId} para visualizar os dados de um usuário específico
    @GetMapping(path = "/{usuarioId}")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> visualizar(@PathVariable(parameter = "usuarioId") Long id) {
        try {
            UsuarioResponseDTO response = usuarioService.buscarPorId(id);
            ApiResponse<UsuarioResponseDTO> apiResponse = new ApiResponse<>(
                    response, "Usuário encontrado", 200
            );
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return ApiExceptionMapper.toResponse(e);
        }
    }

    // Mapeia a requisição PATCH /usuarios/{usuarioId}/{status} para ativar ou desativar um usuário, exigindo a senha para autenticação
    @PatchMapping(path = "/{usuarioId}/{status}")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> alterarStatus(
            @PathVariable(parameter = "usuarioId") Long id,
            @PathVariable(parameter = "status") boolean ativar,
            @RequestBody StatusChangeRequestDTO request) {
        try {
            if (request == null || request.getSenha() == null || request.getSenha().trim().isEmpty()) {
                throw new ValidationException("Senha é obrigatória");
            }
            UsuarioResponseDTO response;
            String operacao;

            // O serviço de usuário irá verificar a senha e realizar a ativação ou desativação conforme o valor de "ativar"
            if (ativar) {
                response = usuarioService.ativarComSenha(id, request.getSenha());
                operacao = "ativado";
            } else {
                response = usuarioService.desativarComSenha(id, request.getSenha());
                operacao = "desativado";
            }
            ApiResponse<UsuarioResponseDTO> apiResponse = new ApiResponse<>(
                    response, "Usuário " + operacao + " com sucesso", 200
            );
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return ApiExceptionMapper.toResponse(e);
        }
    }
}