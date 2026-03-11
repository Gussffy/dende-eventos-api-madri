package br.com.softhouse.dende.controllers;

import br.com.dende.softhouse.annotations.Controller;
import br.com.dende.softhouse.annotations.request.*;
import br.com.dende.softhouse.process.route.ResponseEntity;
import br.com.softhouse.dende.dto.ApiResponse;
import br.com.softhouse.dende.dto.UsuarioRequestDTO;
import br.com.softhouse.dende.dto.UsuarioResponseDTO;
import br.com.softhouse.dende.dto.StatusChangeRequestDTO;
import br.com.softhouse.dende.services.UsuarioService;

/**
 *     Controlador de Usuários
 *
 *     Esta classe é responsável por RECEBER as requisições HTTP relacionadas a usuários
 *     e DELEGAR para o serviço de usuários (UsuarioService) a execução das regras de negócio.
 */
@Controller
@RequestMapping(path = "/usuarios")
public class UsuarioController {

    // Injeção de dependência do serviço de usuários
    private final UsuarioService usuarioService;

    public UsuarioController() {
        this.usuarioService = new UsuarioService();
    }

    // Mapeia a requisição POST /usuarios para cadastrar um novo usuário
    @PostMapping
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> cadastrar(@RequestBody UsuarioRequestDTO request) {
        // Tenta cadastrar o usuário e retorna a resposta adequada
        try {
            UsuarioResponseDTO response = usuarioService.cadastrar(request);
            ApiResponse<UsuarioResponseDTO> apiResponse = new ApiResponse<>(
                    response, "Usuário cadastrado com sucesso", 201
            );
            return ResponseEntity.status(201, apiResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<UsuarioResponseDTO> apiResponse = new ApiResponse<>(
                    e.getMessage(), 400, "Bad Request"
            );
            return ResponseEntity.status(400, apiResponse);
        }
    }

    // Mapeia a requisição PUT /usuarios/{usuarioId} para atualizar os dados de um usuário existente
    @PutMapping(path = "/{usuarioId}")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> alterar(
            @PathVariable(parameter = "usuarioId") Long id,
            @RequestBody UsuarioRequestDTO request) {
        // Tenta atualizar o usuário e retorna a resposta adequada
        try {
            UsuarioResponseDTO response = usuarioService.atualizar(id, request);
            ApiResponse<UsuarioResponseDTO> apiResponse = new ApiResponse<>(
                    response, "Usuário atualizado com sucesso", 200
            );
            return ResponseEntity.ok(apiResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<UsuarioResponseDTO> apiResponse = new ApiResponse<>(
                    e.getMessage(), 400, "Bad Request"
            );
            return ResponseEntity.status(400, apiResponse);
        }
    }

    // Mapeia a requisição GET /usuarios/{usuarioId} para visualizar os dados de um usuário específico
    @GetMapping(path = "/{usuarioId}")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> visualizar(@PathVariable(parameter = "usuarioId") Long id) {
        // Tenta buscar o usuário por ID e retorna a resposta adequada
        try {
            UsuarioResponseDTO response = usuarioService.buscarPorId(id);
            ApiResponse<UsuarioResponseDTO> apiResponse = new ApiResponse<>(
                    response, "Usuário encontrado", 200
            );
            return ResponseEntity.ok(apiResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<UsuarioResponseDTO> apiResponse = new ApiResponse<>(
                    e.getMessage(), 404, "Not Found"
            );
            return ResponseEntity.status(404, apiResponse);
        }
    }

    // Mapeia a requisição PATCH /usuarios/{usuarioId}/{status} para ativar ou desativar um usuário, exigindo a senha para autenticação
    @PatchMapping(path = "/{usuarioId}/{status}")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> alterarStatus(
            @PathVariable(parameter = "usuarioId") Long id,
            @PathVariable(parameter = "status") boolean ativar,
            @RequestBody StatusChangeRequestDTO request) {
        // Tenta ativar ou desativar o usuário com base no status e retorna a resposta adequada
        try {
            if (request == null || request.getSenha() == null || request.getSenha().trim().isEmpty()) {
                ApiResponse<UsuarioResponseDTO> apiResponse = new ApiResponse<>(
                        "Senha é obrigatória", 400, "Bad Request"
                );
                return ResponseEntity.status(400, apiResponse);
            }
            // Verificar se o usuário existe antes de tentar alterar o status
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
            // Se a operação for bem-sucedida, retorna a resposta com o status 200 e a mensagem de sucesso
            ApiResponse<UsuarioResponseDTO> apiResponse = new ApiResponse<>(
                    response, "Usuário " + operacao + " com sucesso", 200
            );
            return ResponseEntity.ok(apiResponse);

        } catch (IllegalArgumentException e) {
            int status = e.getMessage().contains("Senha incorreta") ? 401 : 400;
            String erro = e.getMessage().contains("Senha incorreta") ? "Unauthorized" : "Bad Request";
            ApiResponse<UsuarioResponseDTO> apiResponse = new ApiResponse<>(
                    e.getMessage(), status, erro
            );
            return ResponseEntity.status(status, apiResponse);
        }
    }
}