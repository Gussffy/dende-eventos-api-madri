package br.com.softhouse.dende.controllers;

import br.com.dende.softhouse.annotations.Controller;
import br.com.dende.softhouse.annotations.request.*;
import br.com.dende.softhouse.process.route.ResponseEntity;
import br.com.softhouse.dende.dto.UsuarioRequestDTO;
import br.com.softhouse.dende.dto.UsuarioResponseDTO;
import br.com.softhouse.dende.dto.StatusChangeRequestDTO;
import br.com.softhouse.dende.services.UsuarioService;

/**
    Controlador de Usuários

    Esta classe é responsável por RECEBER as requisições HTTP relacionadas a usuários
    e DELEGAR para o serviço de usuários (UsuarioService) a execução das regras de negócio.

 */
@Controller
@RequestMapping(path = "/usuarios") // Define a rota base para todas as operações relacionadas a usuários
public class UsuarioController {

    // Instância do serviço de usuários para delegar as operações
    private final UsuarioService usuarioService;

    // Construtor que inicializa o serviço de usuários
    public UsuarioController() {
        this.usuarioService = new UsuarioService();
    }

    // Mapeia a rota POST /usuarios para cadastrar um novo usuário
    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody UsuarioRequestDTO request) {
        try {
            // Chama o service para cadastrar o usuário
            // O service retorna um DTO de resposta com os dados do usuário criado
            UsuarioResponseDTO response = usuarioService.cadastrar(request);

            // Retorna uma resposta de sucesso com o ID do usuário criado
            return ResponseEntity.status(201, "Usuário cadastrado com sucesso. ID: " + response.getId());
        } catch (IllegalArgumentException e) {

            // Captura as exceções lançadas pelo service e retorna uma resposta de erro com a mensagem da exceção
            return ResponseEntity.status(400, "Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    // Mapeia a rota PUT /usuarios/{usuarioId} para atualizar os dados de um usuário existente
    @PutMapping(path = "/{usuarioId}")
    public ResponseEntity<String> alterar(
            @PathVariable(parameter = "usuarioId") Long id,  // Extrai o ID do usuário da URL
            @RequestBody UsuarioRequestDTO request) {        // Extrai o corpo da requisição (JSON)
        try {

            // Chama o service para atualizar o usuário
            UsuarioResponseDTO response = usuarioService.atualizar(id, request);

            // Retorna uma resposta de sucesso com o ID do usuário atualizado
            return ResponseEntity.ok("Usuário atualizado com sucesso. ID: " + response.getId());
        } catch (IllegalArgumentException e) {

            // Captura as exceções lançadas pelo service e retorna uma resposta de erro com a mensagem da exceção
            return ResponseEntity.status(400, "Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    // Mapeia a rota GET /usuarios/{usuarioId} para visualizar os dados de um usuário por ID
    @GetMapping(path = "/{usuarioId}")
    public ResponseEntity<UsuarioResponseDTO> visualizar(@PathVariable(parameter = "usuarioId") Long id) {
        try {
            // Chama o service para buscar o usuário por ID
            UsuarioResponseDTO response = usuarioService.buscarPorId(id);

            // Retorna uma resposta de sucesso com os dados do usuário encontrado
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {

            // Se o usuário não for encontrado, retorna status 404 (Not Found) com corpo null
              return ResponseEntity.status(404, null);
        }
    }

    // Mapeia a rota PATCH /usuarios/{usuarioId}/{status} para ativar ou desativar um usuário, exigindo a senha para autenticação
    @PatchMapping(path = "/{usuarioId}/{status}")
    public ResponseEntity<String> alterarStatus(
            @PathVariable(parameter = "usuarioId") Long id,     // Extrai o ID do usuário da URL
            @PathVariable(parameter = "status") boolean ativar, // Extrai o status (true para ativar, false para desativar) da URL
            @RequestBody StatusChangeRequestDTO request) {      // Extrai o corpo da requisição (JSON) que deve conter a senha para autenticação

        try {

            // Verifica se o request é nulo OU a senha é nula OU vazia
            if (request == null || request.getSenha() == null || request.getSenha().trim().isEmpty()) {

                // Retorna status 400 (Bad Request) com mensagem de erro indicando que a senha é obrigatória
                return ResponseEntity.status(400, "Senha é obrigatória para alterar o status do usuário");
            }

            UsuarioResponseDTO response;

            // Chama o service para ativar ou desativar o usuário, passando o ID, o status desejado e a senha para autenticação
            if (ativar) {
                response = usuarioService.ativarComSenha(id, request.getSenha());
            } else {
                response = usuarioService.desativarComSenha(id, request.getSenha());
            }

            // Retorna status 200 (OK) com mensagem de sucesso contendo ID e novo status
            return ResponseEntity.ok("Status do usuário atualizado com sucesso. ID: " + response.getId() + ", Ativo: " + response.getAtivo());

        } catch (IllegalArgumentException e) {

            // Verifica se a mensagem de erro contém "Senha incorreta"
            // Se sim, retorna 401 (Unauthorized) - erro de autenticação
            // Se não, retorna 400 (Bad Request) - erro de validação
            int status = e.getMessage().contains("Senha incorreta") ? 401 : 400;

            // Retorna a resposta de erro com o status apropriado e a mensagem da exceção
            return ResponseEntity.status(status, "Erro ao alterar status do usuário: " + e.getMessage());
        }
    }
}