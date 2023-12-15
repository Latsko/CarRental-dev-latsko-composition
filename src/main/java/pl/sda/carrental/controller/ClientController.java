package pl.sda.carrental.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.sda.carrental.model.Client;
import pl.sda.carrental.service.ClientService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clients")
public class ClientController {
    private final ClientService clientService;

    @GetMapping("/{id}")
    public Client findById(@PathVariable Long id) {
        return clientService.findById(id);
    }

    @GetMapping
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @PostMapping
    public void addClient(@RequestBody Client client) {
        clientService.addClient(client);
    }

    @PutMapping("/{id}")
    public Client modifyClient(@PathVariable Long id, @RequestBody Client client) {
        return clientService.editClient(id, client);
    }

    @DeleteMapping("/{id}")
    public void removeClient(@PathVariable Long id) {
        clientService.removeClient(id);
    }

    @PatchMapping("/client/{client_id}/assignToBranch/{branch_id}")
    public void assignClientToBranch(@PathVariable Long client_id, @PathVariable Long branch_id) {
        clientService.assignClientToBranch(client_id, branch_id);
    }

    @PatchMapping("/client/{client_id}/detachFromBranch/{branch_id}")
    public void detachClientFromBranch(@PathVariable Long client_id, @PathVariable Long branch_id) {
        clientService.removeClientFromBranch(client_id, branch_id);
    }

}
