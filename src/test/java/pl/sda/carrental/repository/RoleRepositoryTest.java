package pl.sda.carrental.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import pl.sda.carrental.configuration.auth.model.Role;
import pl.sda.carrental.configuration.auth.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.sql.init.mode=never",
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    private Role role;

    @BeforeEach
    public void setUp() {
        role = new Role(1L, "ROLE", null);
    }

    @Test
    public void shouldFindById() {
        //given
        roleRepository.save(role);

        //when
        Role found = roleRepository.findByName("ROLE");

        //then
        assertThat(found.getName()).isEqualTo("ROLE");
    }

    @Test
    public void shouldFindRoleById() {
        //given
        roleRepository.save(role);

        //when
        Role found = roleRepository.findById(1L).orElse(null);

        //then
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(1L);
    }

    @Test
    public void shouldSaveRole() {
        //given

        //when
        Role saved = roleRepository.save(role);

        //then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void shouldFindAllRoles() {
        //given
        Role role1 = new Role(1L, "ROLE1", null);
        Role role2 = new Role(2L, "ROLE2", null);

        roleRepository.save(role1);
        roleRepository.save(role2);

        //when
        List<Role> list = roleRepository.findAll();

        //then
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(2);
    }


    @Test
    public void shouldUpdateRole() {
        //given
        roleRepository.save(role);

        //when
        Role saved = roleRepository.findById(role.getId()).get();
        saved.setName("NEW_NAME");
        Role updated = roleRepository.save(saved);

        //then
        assertThat(updated.getName()).isEqualTo("NEW_NAME");
    }

    @Test
    public void shouldDeleteRole() {
        //given
        roleRepository.save(role);

        //when
        roleRepository.deleteById(role.getId());
        Optional<Role> empty = roleRepository.findById(role.getId());

        //then
        assertThat(empty).isEmpty();
    }
}