package br.com.softhouse.dende.controllers;

import br.com.dende.softhouse.annotations.Controller;
import br.com.dende.softhouse.annotations.request.*;
import br.com.dende.softhouse.process.route.ResponseEntity;
import br.com.softhouse.dende.dto.ApiResponse;
import br.com.softhouse.dende.dto.UsuarioDTO;
import br.com.softhouse.dende.dto.StatusChangeRequestDTO;
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
    public ResponseEntity<ApiResponse<UsuarioDTO>> cadastrar(@RequestBody UsuarioDTO dto) {
        // Tenta cadastrar o usuário e retorna a resposta adequada
        try {
            UsuarioDTO response = usuarioService.cadastrar(dto);
            ApiResponse<UsuarioDTO> apiResponse = new ApiResponse<>(
                    response, "Usuário cadastrado com sucesso", 201
            );
            return ResponseEntity.status(201, apiResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<UsuarioDTO> apiResponse = new ApiResponse<>(
                    e.getMessage(), 400, "Bad Request"
            );
            return ResponseEntity.status(400, apiResponse);
        }
    }

    // Mapeia a requisição PUT /usuarios/{usuarioId} para atualizar os dados de um usuário existente
    @PutMapping(path = "/{usuarioId}")
    public ResponseEntity<ApiResponse<UsuarioDTO>> alterar(
            @PathVariable(parameter = "usuarioId") Long id,
            @RequestBody UsuarioDTO dto) {
        // Tenta atualizar o usuário e retorna a resposta adequada
        try {
            UsuarioDTO response = usuarioService.atualizar(id, dto);
            ApiResponse<UsuarioDTO> apiResponse = new ApiResponse<>(
                    response, "Usuário atualizado com sucesso", 200
            );
            return ResponseEntity.ok(apiResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<UsuarioDTO> apiResponse = new ApiResponse<>(
                    e.getMessage(), 400, "Bad Request"
            );
            return ResponseEntity.status(400, apiResponse);
        }
    }

    // Mapeia a requisição GET /usuarios/{usuarioId} para visualizar os dados de um usuário específico
    @GetMapping(path = "/{usuarioId}")
    public ResponseEntity<ApiResponse<UsuarioDTO>> visualizar(@PathVariable(parameter = "usuarioId") Long id) {
        // Tenta buscar o usuário por ID e retorna a resposta adequada
        try {
            UsuarioDTO response = usuarioService.buscarPorId(id);
            ApiResponse<UsuarioDTO> apiResponse = new ApiResponse<>(
                    response, "Usuário encontrado", 200
            );
            return ResponseEntity.ok(apiResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<UsuarioDTO> apiResponse = new ApiResponse<>(
                    e.getMessage(), 404, "Not Found"
            );
            return ResponseEntity.status(404, apiResponse);
        }
    }

    // Mapeia a requisição PATCH /usuarios/{usuarioId}/{status} para ativar ou desativar um usuário, exigindo a senha para autenticação
    @PatchMapping(path = "/{usuarioId}/{status}")
    public ResponseEntity<ApiResponse<UsuarioDTO>> alterarStatus(
            @PathVariable(parameter = "usuarioId") Long id,
            @PathVariable(parameter = "status") boolean ativar,
            @RequestBody StatusChangeRequestDTO request) {
        // Tenta ativar ou desativar o usuário com base no status e retorna a resposta adequada
        try {
            if (request == null || request.getSenha() == null || request.getSenha().trim().isEmpty()) {
                ApiResponse<UsuarioDTO> apiResponse = new ApiResponse<>(
                        "Senha é obrigatória", 400, "Bad Request"
                );
                return ResponseEntity.status(400, apiResponse);
            }
            // Verificar se o usuário existe antes de tentar alterar o status
            UsuarioDTO response;
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
            ApiResponse<UsuarioDTO> apiResponse = new ApiResponse<>(
                    response, "Usuário " + operacao + " com sucesso", 200
            );
            return ResponseEntity.ok(apiResponse);
            // Se ocorrer um erro, verifica se é por senha incorreta ou outro motivo e retorna a resposta adequada
        } catch (IllegalArgumentException e) {
            int status = e.getMessage().contains("Senha incorreta") ? 401 : 400;
            String erro = e.getMessage().contains("Senha incorreta") ? "Unauthorized" : "Bad Request";
            ApiResponse<UsuarioDTO> apiResponse = new ApiResponse<>(
                    e.getMessage(), status, erro
            );
            return ResponseEntity.status(status, apiResponse);
        }
    }
}