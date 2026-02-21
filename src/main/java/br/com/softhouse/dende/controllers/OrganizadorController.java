package br.com.softhouse.dende.controllers;

import br.com.dende.softhouse.annotations.Controller;
import br.com.dende.softhouse.annotations.request.*;
import br.com.dende.softhouse.process.route.ResponseEntity;
import br.com.softhouse.dende.dto.OrganizadorRequestDTO;
import br.com.softhouse.dende.dto.OrganizadorResponseDTO;
import br.com.softhouse.dende.dto.StatusChangeRequestDTO;
import br.com.softhouse.dende.dto.MensagemResponseDTO;
import br.com.softhouse.dende.services.OrganizadorService;

/**
    CONTROLADOR DE ORGANIZADORES

    Esta classe é responsável por RECEBER as requisições HTTP relacionadas a organizadores
    e DELEGAR para o serviço de organizadores (OrganizadorService) a execução das regras de negócio.

*/
@Controller
@RequestMapping(path = "/organizadores") // Define a rota base para todas as operações relacionadas a organizadores
public class OrganizadorController {

    // Instância do serviço de organizadores para delegar as operações
    private final OrganizadorService organizadorService;


    //Construtor que inicializa o serviço de organizadores
    public OrganizadorController() {
        this.organizadorService = new OrganizadorService();
    }

    // Mapeia requisições HTTP POST para este metodo
    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody OrganizadorRequestDTO request) {
        try {
            // Chama o service para cadastrar o organizador
            // O service retorna um DTO de resposta com os dados do organizador criado
            OrganizadorResponseDTO response = organizadorService.cadastrar(request);

            // Retorna status 201 (Created) com mensagem de sucesso contendo o ID gerado
            return ResponseEntity.status(201, "Organizador cadastrado com sucesso. ID: " + response.getId());

        } catch (IllegalArgumentException e) {
            // Captura exceções de validação (campos obrigatórios, email duplicado, CNPJ duplicado, etc.)
            // Retorna status 400 (Bad Request) com a mensagem de erro
            return ResponseEntity.status(400, "Erro ao cadastrar organizador: " + e.getMessage());
        }
    }

    // Mapeia requisições PUT para /organizadores/{organizadorId}
    @PutMapping(path = "/{organizadorId}")
    public ResponseEntity<String> alterar(
            @PathVariable(parameter = "organizadorId") Long id,  // Extrai o ID do organizador da URL
            @RequestBody OrganizadorRequestDTO request) {        // Extrai o corpo da requisição (JSON)
        try {

            // Chama o service para atualizar o organizador
            OrganizadorResponseDTO response = organizadorService.atualizar(id, request);

            // Retorna status 200 (OK) com mensagem de sucesso contendo o ID
            return ResponseEntity.ok("Organizador atualizado com sucesso. ID: " + response.getId());

        } catch (IllegalArgumentException e) {

            // Captura exceções (organizador não encontrado, email não pode mudar, CNPJ duplicado, etc.)
            // Retorna status 400 (Bad Request) com a mensagem de erro
            return ResponseEntity.status(400, "Erro ao atualizar organizador: " + e.getMessage());
        }
    }

    // Mapeia requisições GET para /organizadores/{organizadorId}
    @GetMapping(path = "/{organizadorId}")
    public ResponseEntity<OrganizadorResponseDTO> visualizar(@PathVariable(parameter = "organizadorId") Long id) {
        try {

            // Chama o service para buscar o organizador por ID
            OrganizadorResponseDTO response = organizadorService.buscarPorId(id);

            // Retorna status 200 (OK) com o DTO que será convertido automaticamente para JSON
            // O DTO inclui todos os dados do organizador (nome, email, idade, e dados da empresa se houver)
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {

            // Se o organizador não for encontrado, retorna status 404 (Not Found) com corpo null
            return ResponseEntity.status(404, null);
        }
    }

    // Mapeia requisições PATCH para /organizadores/{organizadorId}/{status}
    @PatchMapping(path = "/{organizadorId}/{status}")
    public ResponseEntity<String> alterarStatus(
            @PathVariable(parameter = "organizadorId") Long id,      // Extrai o ID do organizador da URL
            @PathVariable(parameter = "status") boolean ativar,      // Extrai o status da URL (true/false)
            @RequestBody StatusChangeRequestDTO request) {           // Extrai a senha do corpo da requisição

        try {

            // Verifica se o request é nulo OU a senha é nula OU vazia
            if (request == null || request.getSenha() == null || request.getSenha().trim().isEmpty()) {

                // Retorna 400 (Bad Request) com mensagem clara
                return ResponseEntity.status(400, "Senha é obrigatória para alterar o status do organizador");
            }

            OrganizadorResponseDTO response;

            // Decide qual metodo do service chamar baseado no parâmetro 'ativar'
            if (ativar) {

                // Ativar organizador (requer senha)
                response = organizadorService.ativarComSenha(id, request.getSenha());
            } else {

                // Desativar organizador (requer senha + verificação de eventos ativos)
                response = organizadorService.desativarComSenha(id, request.getSenha());
            }

            // Retorna status 200 (OK) com mensagem de sucesso contendo o ID
            // A mensagem varia conforme a operação (ativado/desativado)
            return ResponseEntity.ok("Organizador " + (ativar ? "ativado" : "desativado") + " com sucesso. ID: " + response.getId());

        } catch (IllegalArgumentException e) {

            // Verifica se a mensagem de erro contém "Senha incorreta"
            // Se sim, retorna 401 (Unauthorized) - erro de autenticação
            // Se não, retorna 400 (Bad Request) - erro de validação (ex: eventos ativos)
            int status = e.getMessage().contains("Senha incorreta") ? 401 : 400;

            // Retorna a resposta de erro com o status apropriado e a mensagem da exceção
            return ResponseEntity.status(status, "Erro ao alterar status do organizador: " + e.getMessage());
        }
    }
}