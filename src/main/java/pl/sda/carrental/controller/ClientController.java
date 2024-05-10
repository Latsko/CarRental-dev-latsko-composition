package pl.sda.carrental.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.sda.carrental.configuration.auth.model.Client;
import pl.sda.carrental.service.ClientService;

import java.util.List;

@RestController
@SecurityRequirement(name = "basicAuth")
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Clients")
public class ClientController {
    private final ClientService clientService;

    @Operation(summary = "Gets selected clients",
            description = "Available IDs of clients to choose from: {16, 17, 18, ..., 30}")
    @GetMapping("/manageL2/clients/{id}")
    public Client findById(@Parameter(name = "id", example = "16", description = "client ID")
                           @PathVariable Long id) {
        return clientService.findById(id);
    }

    @Operation(summary = "Gets all clients")
    @GetMapping("/manageL2/clients")
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @Operation(summary = "Edits selected client")
    @PutMapping("/manageL2/clients/{id}")
    public Client modifyClient(@Parameter(name = "id", example = "16", description = "client ID")
                               @PathVariable Long id,
                               @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                       content = @Content(schema = @Schema(implementation = Client.class),
                                               examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                       name = "exampleClient",
                                                       value = "{\"login\": \"string\"," +
                                                               " \"password\": \"string\"," +
                                                               " \"name\": \"string\"," +
                                                               " \"surname\": \"string\"," +
                                                               " \"email\": \"string\"," +
                                                               " \"address\": \"string\"}"
                                               )
                                       )
                               )
                               @RequestBody Client client) {
        return clientService.editClient(id, client);
    }

    @Operation(
            summary = "Removes selected client",
            description = "Available IDs of clients to choose from: {16, 17, 18, ..., 30}" +
                    "<br>When a client is removed, all reservations, rent and returnals associated with that client is removed as well"
    )
    @DeleteMapping("/manageL2/clients/{id}")
    public void removeClient(@Parameter(name = "id", example = "16", description = "client ID")
                             @PathVariable Long id) {
        clientService.removeClient(id);
    }

    @Operation(summary = "Assigns selected client to selected branch")
    @PatchMapping("/manageL2/clients/client/{client_id}/assignToBranch/{branch_id}")
    public Client assignClientToBranch(@Parameter(name = "client_id", example = "16", description = "client ID")
                                       @PathVariable Long client_id,
                                       @Parameter(name = "branch_id", example = "1", description = "branch ID")
                                       @PathVariable Long branch_id) {
        return clientService.assignClientToBranch(client_id, branch_id);
    }

    @Operation(summary = "Detaches selected client from selected branch")
    @PatchMapping("/manageL2/clients/client/{client_id}/detachFromBranch/{branch_id}")
    public void detachClientFromBranch(@Parameter(name = "client_id", example = "16", description = "client ID")
                                       @PathVariable Long client_id,
                                       @Parameter(name = "branch_id", example = "1", description = "branch ID")
                                       @PathVariable Long branch_id) {
        clientService.removeClientFromBranch(client_id, branch_id);
    }

}
