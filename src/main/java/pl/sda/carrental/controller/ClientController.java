package pl.sda.carrental.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.sda.carrental.configuration.auth.model.Client;
import pl.sda.carrental.service.ClientService;

import java.util.List;

@RestController
@SecurityRequirement(name = "basicAuth")
@RequiredArgsConstructor
@RequestMapping("/api")
public class ClientController {
    private final ClientService clientService;

    @GetMapping("/manageL2/clients/{id}")
    public Client findById(@PathVariable Long id) {
        return clientService.findById(id);
    }

    @GetMapping("/manageL2/clients")
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @PutMapping("/manageL2/clients/{id}")
    public Client modifyClient(@PathVariable Long id, @RequestBody Client client) {
        return clientService.editClient(id, client);
    }

    @DeleteMapping("/manageL2/clients/{id}")
    public void removeClient(@PathVariable Long id) {
        clientService.removeClient(id);
    }

    @PatchMapping("/manageL2/clients/client/{client_id}/assignToBranch/{branch_id}")
    public Client assignClientToBranch(@PathVariable Long client_id, @PathVariable Long branch_id) {
        return clientService.assignClientToBranch(client_id, branch_id);
    }

    @PatchMapping("/manageL2/clients/client/{client_id}/detachFromBranch/{branch_id}")
    public void detachClientFromBranch(@PathVariable Long client_id, @PathVariable Long branch_id) {
        clientService.removeClientFromBranch(client_id, branch_id);
    }

}
