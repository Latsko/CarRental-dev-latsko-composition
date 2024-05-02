package pl.sda.carrental.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import pl.sda.carrental.configuration.auth.model.Employee;
import pl.sda.carrental.configuration.auth.repository.EmployeeRepository;
import pl.sda.carrental.model.enums.Position;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.sql.init.mode=never",
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class EmployeeRepositoryTest {
    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        employee = new Employee(1L,
                "login",
                "password",
                "name",
                "surname",
                null,
                null,
                Position.EMPLOYEE);
    }

    @Test
    public void shouldFindEmployeeById() {
        //given
        employeeRepository.save(employee);

        //when
        Employee found = employeeRepository.findById(1L).orElse(null);

        //then
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(1L);
    }

    @Test
    public void shouldSaveEmployee() {
        //given

        //when
        Employee saved = employeeRepository.save(employee);

        //then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void shouldFindAllEmployees() {
        //given
        Employee employee1 = new Employee(1L, "login1", "password1",
                "name1","surname1",
                null, null, Position.EMPLOYEE);
        Employee employee2 = new Employee(2L, "login2", "password2",
                "name2", "surname2",
                null, null, Position.EMPLOYEE);
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        //when
        List<Employee> list = employeeRepository.findAll();

        //then
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(2);
    }


    @Test
    public void shouldUpdateEmployee() {
        //given
        employeeRepository.save(employee);

        //when
        Employee saved = employeeRepository.findById(employee.getId()).get();
        saved.setName("newName");
        saved.setSurname("newSurname");
        saved.setPassword("newPassword");
        Employee updated = employeeRepository.save(saved);

        //then
        assertThat(updated.getName()).isEqualTo("newName");
        assertThat(updated.getSurname()).isEqualTo("newSurname");
        assertThat(updated.getPassword()).isEqualTo("newPassword");
    }

    @Test
    public void shouldDeleteEmployee() {
        //given
        employeeRepository.save(employee);

        //when
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> empty = employeeRepository.findById(employee.getId());

        //then
        assertThat(empty).isEmpty();
    }
}