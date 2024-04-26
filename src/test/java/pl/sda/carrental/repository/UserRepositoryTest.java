package pl.sda.carrental.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import pl.sda.carrental.configuration.auth.entity.Client;
import pl.sda.carrental.configuration.auth.entity.User;
import pl.sda.carrental.configuration.auth.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.sql.init.mode=never",
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new Client(1L,
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
    public void shouldFindByLogin() {
        //given
        userRepository.save(user);

        //when
        User found = userRepository.findByLogin("login").get();

        //then
        assertThat(found).isNotNull();
        assertThat(found.getLogin()).isEqualTo("login");
    }

    @Test
    public void shouldFindUserById() {
        //given
        userRepository.save(user);

        //when
        User found = userRepository.findById(1L).orElse(null);

        //then
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(1L);
    }

    @Test
    public void shouldSaveUser() {
        //given

        //when
        User saved = userRepository.save(user);

        //then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void shouldFindAllUsers() {
        //given
        User user1 = new Client(1L, "login1", "password1",
                "name1","surname1",
                null, null,
                "email1", "address1");
        User user2 = new Client(2L, "login2", "password2",
                "name2", "surname2",
                null, null, "email2",
                "address2");
        userRepository.save(user1);
        userRepository.save(user2);

        //when
        List<User> list = userRepository.findAll();

        //then
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(2);
    }


    @Test
    public void shouldUpdateUser() {
        //given
        userRepository.save(user);

        //when
        User saved = userRepository.findById(user.getId()).get();
        saved.setName("newName");
        saved.setSurname("newSurname");
        saved.setPassword("newPassword");
        User updated = userRepository.save(saved);

        //then
        assertThat(updated.getName()).isEqualTo("newName");
        assertThat(updated.getSurname()).isEqualTo("newSurname");
        assertThat(updated.getPassword()).isEqualTo("newPassword");
    }

    @Test
    public void shouldDeleteUser() {
        //given
        userRepository.save(user);

        //when
        userRepository.deleteById(user.getId());
        Optional<User> empty = userRepository.findById(user.getId());

        //then
        assertThat(empty).isEmpty();
    }
}