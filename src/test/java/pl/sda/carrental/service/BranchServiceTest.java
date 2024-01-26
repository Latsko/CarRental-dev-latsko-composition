package pl.sda.carrental.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import pl.sda.carrental.repository.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BranchServiceTest {
    @Mock
    private  BranchRepository branchRepositoryMock;
    @Mock
    private  CarRepository carRepositoryMock;
    @Mock
    private  EmployeeRepository employeeRepositoryMock;
    @Mock
    private  ReservationRepository reservationRepositoryMock;
    @Mock
    private  CarRentalRepository carRentalRepositoryMock;
    @Mock
    private  CarService carServiceMock; // ???????
    @InjectMocks
    private BranchService branchService;

    @Test
    void shouldGetAllBranches() {


    }

}