package pl.sda.carrental.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sda.carrental.ObjectNotFoundInRepositoryException;
import pl.sda.carrental.model.ClientModel;
import pl.sda.carrental.repository.ClientRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientModel findById(Long id) {
            return clientRepository.findById(id)
                    .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Client not found"));
    }

    public List<ClientModel> getAllClients() {
        return clientRepository.findAll();
    }

    public void addClient(ClientModel client) {
        clientRepository.save(client);
    }

    public ClientModel editClient(Long id, ClientModel client) {
        ClientModel found = clientRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No client under that ID!"));

        found.setName(client.getName());
        found.setSurname(client.getSurname());
        found.setEmail(client.getEmail());
        found.setAddress(client.getAddress());

        clientRepository.deleteById(id);

        return clientRepository.save(found);

    }

    public void removeClient(Long id) {
        clientRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No client under that ID!"));
        clientRepository.deleteById(id);
    }
}
