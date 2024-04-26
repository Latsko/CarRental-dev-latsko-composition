package pl.sda.carrental.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import pl.sda.carrental.model.Revenue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.sql.init.mode=never",
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RevenueRepositoryTest {
    @Autowired
    private RevenueRepository revenueRepository;

    private Revenue revenue;

    @BeforeEach
    public void setUp() {
        revenue = new Revenue()
                .withTotalAmount(BigDecimal.valueOf(10000.0));
    }


    @Test
    public void shouldFindRevenueById() {
        //given
        revenueRepository.save(revenue);

        //when
        Revenue found = revenueRepository.findById(1L).orElse(null);

        //then
        assertThat(found).isNotNull();
        assertThat(found.getRevenueId()).isEqualTo(1L);
    }

    @Test
    public void shouldSaveRevenue() {
        //given

        //when
        Revenue saved = revenueRepository.save(revenue);

        //then
        assertThat(saved).isNotNull();
        assertThat(saved.getRevenueId()).isGreaterThan(0);
    }

    @Test
    public void shouldFindAllRevenues() {
        //given
        Revenue revenue1 = new Revenue()
                .withTotalAmount(BigDecimal.valueOf(20000.0));
        Revenue revenue2 = new Revenue()
                .withTotalAmount(BigDecimal.valueOf(30000.0));

        revenueRepository.save(revenue1);
        revenueRepository.save(revenue2);

        //when
        List<Revenue> list = revenueRepository.findAll();

        //then
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(2);
    }


    @Test
    public void shouldUpdateRevenue() {
        //given
        revenueRepository.save(revenue);

        //when
        Revenue saved = revenueRepository.findById(revenue.getRevenueId()).get();
        saved.setTotalAmount(BigDecimal.valueOf(15000.0));
        Revenue updated = revenueRepository.save(saved);

        //then
        assertThat(updated.getTotalAmount()).isEqualTo(BigDecimal.valueOf(15000.0));
    }

    @Test
    public void shouldDeleteRevenue() {
        //given
        revenueRepository.save(revenue);

        //when
        revenueRepository.deleteById(revenue.getRevenueId());
        Optional<Revenue> empty = revenueRepository.findById(revenue.getRevenueId());

        //then
        assertThat(empty).isEmpty();
    }
}