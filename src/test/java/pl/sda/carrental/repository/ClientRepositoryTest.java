package pl.sda.carrental.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import pl.sda.carrental.configuration.auth.entity.Client;
import pl.sda.carrental.configuration.auth.repository.ClientRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.sql.init.mode=never",
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ClientRepositoryTest {
    @Autowired
    private ClientRepository clientRepository;

    private Client client;

    @BeforeEach
    public void setUp() {
        client = new Client(1L,
                "login",
                "password",
                "name",
                "surname",
                null,
                null,
                "email",
                "address");
    }

    @Test
    public void shouldFindClientById() {
        //given
        clientRepository.save(client);

        //when
        Client found = clientRepository.findById(1L).orElse(null);

        //then
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(1L);
    }

    @Test
    public void shouldSaveClient() {
        //given

        //when
        Client saved = clientRepository.save(client);

        //then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void shouldFindAllClients() {
        //given
        Client client1 = new Client(1L, "login1", "password1",
                "name1","surname1",
                null, null,
                "email1", "address1");
        Client client2 = new Client(2L, "login2", "password2",
                "name2", "surname2",
                null, null, "email2",
                "address2");
        clientRepository.save(client1);
        clientRepository.save(client2);

        //when
        List<Client> list = clientRepository.findAll();

        //then
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(2);
    }


    @Test
    public void shouldUpdateClient() {
        //given
        clientRepository.save(client);

        //when
        Client saved = clientRepository.findById(client.getId()).get();
        saved.setName("newName");
        saved.setSurname("newSurname");
        saved.setPassword("newPassword");
        Client updated = clientRepository.save(saved);

        //then
        assertThat(updated.getName()).isEqualTo("newName");
        assertThat(updated.getSurname()).isEqualTo("newSurname");
        assertThat(updated.getPassword()).isEqualTo("newPassword");
    }

    @Test
    public void shouldDeleteClient() {
        //given
        clientRepository.save(client);

        //when
        clientRepository.deleteById(client.getId());
        Optional<Client> empty = clientRepository.findById(client.getId());

        //then
        assertThat(empty).isEmpty();
    }
}