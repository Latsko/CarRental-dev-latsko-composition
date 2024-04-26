package pl.sda.carrental.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import pl.sda.carrental.model.Branch;
import pl.sda.carrental.model.CarRental;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.sql.init.mode=never",
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BranchRepositoryTest {

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private CarRentalRepository carRentalRepository;


    private Branch branch;
    private CarRental carRental;

    @BeforeEach
    public void setUp() {
        carRental = new CarRental(null,
                "rentalName",
                "rentalDomain.com",
                "address",
                "owner",
                "logo",
                new HashSet<>());

        carRentalRepository.save(carRental);

        branch = new Branch(null,
                "name",
                "address",
                null,
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                carRental,
                null);
    }


    @Test
    public void shouldFindBranchById() {
        //given
        branchRepository.save(branch);

        //when
        Branch found = branchRepository.findById(1L).orElse(null);

        //then
        assertThat(found).isNotNull();
        assertThat(found.getBranchId()).isEqualTo(1L);
    }

    @Test
    public void shouldSaveBranch() {
        //given

        //when
        Branch saved = branchRepository.save(branch);

        //then
        assertThat(saved).isNotNull();
        assertThat(saved.getBranchId()).isGreaterThan(0);
    }

    @Test
    public void shouldFindAllBranches() {
        //given
        Branch branch1 = new Branch().withName("name1").withAddress("address1");
        Branch branch2 = new Branch().withName("name2").withAddress("address2");
        branch1.setCarRental(carRental);
        branch2.setCarRental(carRental);
        branchRepository.save(branch1);
        branchRepository.save(branch2);

        //when
        List<Branch> list = branchRepository.findAll();

        //then
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(2);
    }


    @Test
    public void shouldUpdateBranch() {
        //given
        branchRepository.save(branch);

        //when
        Branch saved = branchRepository.findById(branch.getBranchId()).get();
        saved.setName("newName");
        saved.setAddress("newAddress");
        Branch updated = branchRepository.save(saved);

        //then
        assertThat(updated.getName()).isEqualTo("newName");
        assertThat(updated.getAddress()).isEqualTo("newAddress");
    }

    @Test
    public void shouldDeleteBranch() {
        //given
        branchRepository.save(branch);

        //when
        branchRepository.deleteById(branch.getBranchId());
        Optional<Branch> empty = branchRepository.findById(branch.getBranchId());

        //then
        assertThat(empty).isEmpty();
    }
}