package pl.sda.carrental.service;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.sda.carrental.exceptionHandling.ObjectAlreadyAssignedToBranchException;
import pl.sda.carrental.exceptionHandling.ObjectNotFoundInRepositoryException;
import pl.sda.carrental.model.Branch;
import pl.sda.carrental.model.Revenue;
import pl.sda.carrental.repository.BranchRepository;
import pl.sda.carrental.repository.RevenueRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RevenueServiceTest {
    private final RevenueRepository revenueRepositoryMock = mock(RevenueRepository.class);
    private final BranchRepository branchRepositoryMock = mock(BranchRepository.class);

    private final RevenueService revenueService = new RevenueService(revenueRepositoryMock, branchRepositoryMock);

    private Revenue revenue;
    private Branch branch;

    @BeforeEach
    void setUp() {
        revenue = new Revenue(1L, new BigDecimal("200.0"));
        branch = new Branch();
    }


    @Test
    void shouldGetRevenuesWhenNonEmpty() {
        //given
        when(revenueRepositoryMock.findAll()).thenReturn(Collections.singletonList(revenue));

        //when
        List<Revenue> allRevenues = revenueService.getAllRevenues();

        //then
        assertThat(allRevenues)
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(revenue);
    }

    @Test
    void shouldGetRevenuesWhenEmpty() {
        //given
        when(revenueRepositoryMock.findAll()).thenReturn(new ArrayList<>());

        //when
        List<Revenue> allRevenues = revenueService.getAllRevenues();

        //then
        assertThat(allRevenues).isEmpty();
    }

    @Test
    void shouldGetRevenueById() {
        //given
        when(revenueRepositoryMock.findById(anyLong())).thenReturn(Optional.of(revenue));

        //when
        Revenue revenueById = revenueService.getRevenueById(1L);

        //then
        assertThat(revenueById)
                .isNotNull()
                .isInstanceOf(Revenue.class)
                .isEqualTo(revenue);
    }

    @Test
    void shouldNotGetRevenueById() {
        //given
        when(revenueRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        //when
        ThrowableAssert.ThrowingCallable callable = () -> revenueService.getRevenueById(1L);

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(ObjectNotFoundInRepositoryException.class)
                .hasMessage("No revenue under ID #1!");
    }

    @Test
    void shouldAddRevenue() {
        //given
        when(revenueRepositoryMock.save(any(Revenue.class))).thenReturn(revenue);

        //when
        Revenue savedRevenue = revenueService.addRevenue(revenue);

        //then
        assertThat(savedRevenue)
                .isNotNull()
                .isInstanceOf(Revenue.class)
                .isEqualTo(revenue);
    }

    @Test
    void shouldEditRevenue() {
        //given
        when(revenueRepositoryMock.findById(anyLong())).thenReturn(Optional.of(revenue));
        when(revenueRepositoryMock.save(any(Revenue.class))).thenReturn(revenue);

        //when
        Revenue editedRevenue = revenueService.editRevenue(1L, new Revenue(1L, new BigDecimal("100.0")));

        //then
        assertThat(editedRevenue)
                .isNotNull()
                .isInstanceOf(Revenue.class);
        assertThat(editedRevenue.getRevenueId()).isEqualTo(1L);
        assertThat(editedRevenue.getTotalAmount()).isEqualTo(BigDecimal.valueOf(100.0));
    }

    @Test
    void shouldUpdateRevenue() {
        //given
        when(revenueRepositoryMock.findById(anyLong())).thenReturn(Optional.of(revenue));
        when(revenueRepositoryMock.save(any(Revenue.class))).thenReturn(revenue);

        //when
        Revenue updatedRevenue = revenueService.updateRevenue(1L, new BigDecimal("100.0"));

        //then
        assertThat(updatedRevenue)
                .isNotNull()
                .isInstanceOf(Revenue.class);
        assertThat(updatedRevenue.getRevenueId()).isEqualTo(1L);
        assertThat(updatedRevenue.getTotalAmount()).isEqualTo(BigDecimal.valueOf(300.0));
    }

    @Test
    void shouldDeleteRevenue() {
        //given
        branch.setRevenue(revenue);
        when(revenueRepositoryMock.findById(anyLong())).thenReturn(Optional.of(revenue));
        when(branchRepositoryMock.findAll()).thenReturn(Collections.singletonList(branch));
        when(branchRepositoryMock.save(any(Branch.class))).thenReturn(branch);
        doNothing().when(revenueRepositoryMock).deleteById(anyLong());

        //when
        revenueService.deleteRevenue(1L);

        //then
        assertThat(branch.getRevenue()).isNull();
        verify(branchRepositoryMock, times(1)).save(branch);
        verify(revenueRepositoryMock, times(1)).deleteById(1L);
    }

    @Test
    void shouldAssignRevenueToBranchByAccordingIds() {
        //given
        when(revenueRepositoryMock.findById(anyLong())).thenReturn(Optional.of(revenue));
        when(branchRepositoryMock.findById(anyLong())).thenReturn(Optional.of(branch));
        when(branchRepositoryMock.save(any(Branch.class))).thenReturn(branch);

        //when
        revenueService.assignRevenueToBranchByAccordingIds(1L, 1L);

        //then
        assertThat(branch.getRevenue()).isEqualTo(revenue);
        verify(branchRepositoryMock, times(1)).save(branch);
    }

    @Test
    void shouldNotAssignRevenueToBranchByAccordingIdsWhenBranchDoesNotExist() {
        //given
        when(revenueRepositoryMock.findById(anyLong())).thenReturn(Optional.of(revenue));

        //when
        ThrowableAssert.ThrowingCallable callable = () -> revenueService.assignRevenueToBranchByAccordingIds(1L, 1L);

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(ObjectNotFoundInRepositoryException.class)
                .hasMessage("No branch under ID #1!");
    }

    @Test
    void shouldAssignRevenueToBranchByAccordingIdsWhenRevenueIsAlreadyAssigned() {
        //given
        branch.setRevenue(revenue);
        when(revenueRepositoryMock.findById(anyLong())).thenReturn(Optional.of(revenue));
        when(branchRepositoryMock.findById(anyLong())).thenReturn(Optional.of(branch));

        //when
        ThrowableAssert.ThrowingCallable callable = () -> revenueService.assignRevenueToBranchByAccordingIds(1L, 1L);

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(ObjectAlreadyAssignedToBranchException.class)
                .hasMessage("Revenue already exists for branch under ID #1!");
    }
}