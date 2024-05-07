package pl.sda.carrental.service;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.sda.carrental.configuration.auth.model.Client;
import pl.sda.carrental.configuration.auth.repository.ClientRepository;
import pl.sda.carrental.configuration.auth.repository.UserRepository;
import pl.sda.carrental.exceptionHandling.ObjectAlreadyAssignedToBranchException;
import pl.sda.carrental.exceptionHandling.ObjectNotFoundInRepositoryException;
import pl.sda.carrental.model.Branch;
import pl.sda.carrental.repository.BranchRepository;
import pl.sda.carrental.repository.RentRepository;
import pl.sda.carrental.repository.ReservationRepository;
import pl.sda.carrental.repository.ReturnRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ClientServiceTest {
    private final UserRepository userRepositoryMock = mock(UserRepository.class);
    private final ClientRepository clientRepositoryMock = mock(ClientRepository.class);
    private final BranchRepository branchRepositoryMock = mock(BranchRepository.class);
    private final RentRepository rentRepositoryMock = mock(RentRepository.class);
    private final ReturnRepository returnRepositoryMock = mock(ReturnRepository.class);
    private final ReservationRepository reservationRepositoryMock = mock(ReservationRepository.class);
    private final PasswordEncoder passwordEncoderMock = mock(PasswordEncoder.class);

    private final ClientService clientService = new ClientService(userRepositoryMock, clientRepositoryMock, branchRepositoryMock,
            rentRepositoryMock, returnRepositoryMock, reservationRepositoryMock, passwordEncoderMock);

    private Branch branch;
    private Client client;
    @BeforeEach
    void setUp() {
        branch = new Branch();
        client = new Client();
        client.setId(1L);
    }

    @Test
    void shouldFindById() {
        //given
        when(clientRepositoryMock.findById(anyLong())).thenReturn(Optional.of(client));

        //when
        Client foundClient = clientService.findById(1L);

        //then
        assertThat(foundClient)
                .isNotNull()
                .isInstanceOf(Client.class);
        assertThat(foundClient.getId()).isEqualTo(1L);
    }

    @Test
    void shouldNotFindById() {
        //given
        when(clientRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        //when
        ThrowableAssert.ThrowingCallable callable = () -> clientService.findById(1L);

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(ObjectNotFoundInRepositoryException.class)
                .hasMessage("Client not found");
    }

    @Test
    void shouldGetAllClientsWithEmptyList() {
        //given
        List<Client> clients = Collections.singletonList(client);
        when(clientRepositoryMock.findAll()).thenReturn(clients);

        //when
        List<Client> allClients = clientService.getAllClients();

        //then
        assertThat(allClients)
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(client);
    }

    @Test
    void shouldGetAllClientsWithNonEmptyList() {
        //given
        when(clientRepositoryMock.findAll()).thenReturn(new ArrayList<>());

        //when
        List<Client> allClients = clientService.getAllClients();

        //then
        assertThat(allClients).isEmpty();
    }

    @Test
    void shouldEditClient() {
        //given
        client.setBranch(branch);
        branch.getClients().add(client);
        when(clientRepositoryMock.findById(anyLong())).thenReturn(Optional.of(client));
        when(clientRepositoryMock.save(any(Client.class))).thenReturn(client);
        when(branchRepositoryMock.save(any(Branch.class))).thenReturn(branch);
        Client clientToEdit = new Client();
        clientToEdit.setId(1L);
        clientToEdit.setName("nameEdited");
        clientToEdit.setSurname("surnameEdited");
        clientToEdit.setEmail("emailEdited");
        clientToEdit.setAddress("addressEdited");

        //when
        Client editedClient = clientService.editClient(1L, clientToEdit);

        //then
        assertThat(editedClient)
                .isNotNull()
                .isInstanceOf(Client.class);
        assertThat(editedClient.getId()).isEqualTo(1L);
        assertThat(editedClient.getName()).isEqualTo("nameEdited");
        assertThat(editedClient.getSurname()).isEqualTo("surnameEdited");
        assertThat(editedClient.getEmail()).isEqualTo("emailEdited");
        assertThat(editedClient.getAddress()).isEqualTo("addressEdited");
    }

    @Test
    void shouldNotEditClientThatDoesNotExist() {
        //given
        when(clientRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        //when
        ThrowableAssert.ThrowingCallable callable = () -> clientService.editClient(1L, new Client());

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(ObjectNotFoundInRepositoryException.class)
                .hasMessage("No client under that ID!");
    }

    @Test
    void shouldNotEditClientWhenNotInBranch() {
        //given
        client.setBranch(branch);
        when(clientRepositoryMock.findById(anyLong())).thenReturn(Optional.of(client));

        //when
        ThrowableAssert.ThrowingCallable callable = () -> clientService.editClient(1L, new Client());

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(ObjectNotFoundInRepositoryException.class)
                .hasMessage("No client under ID #1 in that branch");
    }

    @Test
    void shouldRemoveClient() {
        //given
        when(clientRepositoryMock.findById(anyLong())).thenReturn(Optional.of(client));
        when(rentRepositoryMock.findAll()).thenReturn(new ArrayList<>());
        when(returnRepositoryMock.findAll()).thenReturn(new ArrayList<>());
        when(reservationRepositoryMock.findAll()).thenReturn(new ArrayList<>());
        doNothing().when(rentRepositoryMock).deleteAll(List.of());
        doNothing().when(returnRepositoryMock).deleteAll(List.of());
        doNothing().when(reservationRepositoryMock).deleteAll(List.of());
        doNothing().when(clientRepositoryMock).deleteById(anyLong());

        //when
        clientService.removeClient(1L);

        //then
        verify(rentRepositoryMock, times(1)).deleteAll(List.of());
        verify(returnRepositoryMock, times(1)).deleteAll(List.of());
        verify(reservationRepositoryMock, times(1)).deleteAll(List.of());
        verify(clientRepositoryMock, times(1)).deleteById(1L);
    }

    @Test
    void shouldNotRemoveClient() {
        //given
        when(clientRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        //when
        ThrowableAssert.ThrowingCallable callable = () -> clientService.removeClient(1L);

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(ObjectNotFoundInRepositoryException.class)
                .hasMessage("No client under that ID!");
    }

    @Test
    void shouldAssignClientToBranch() {
        //given
        when(clientRepositoryMock.findById(anyLong())).thenReturn(Optional.of(client));
        when(branchRepositoryMock.findById(anyLong())).thenReturn(Optional.of(branch));
        when(clientRepositoryMock.save(any(Client.class))).thenReturn(client);
        when(branchRepositoryMock.save(any(Branch.class))).thenReturn(branch);

        //when
        Client clientWithAssignedBranch = clientService.assignClientToBranch(1L, 1L);

        //then
        assertThat(clientWithAssignedBranch)
                .isNotNull()
                .isInstanceOf(Client.class);
        assertThat(clientWithAssignedBranch.getBranch())
                .isNotNull()
                .isEqualTo(branch);
        assertThat(clientWithAssignedBranch.getBranch().getClients())
                .containsExactly(client);
    }

    @Test
    void shouldNotAssignClientThatAlreadyIsAssignedToAnotherBranch() {
        //given
        client.setBranch(branch);
        when(clientRepositoryMock.findById(anyLong())).thenReturn(Optional.of(client));

        //when
        ThrowableAssert.ThrowingCallable callable = () -> clientService.assignClientToBranch(1L, 1L);

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(ObjectAlreadyAssignedToBranchException.class)
                .hasMessage("This client is already assigned to existing branch!");
    }

    @Test
    void shouldNotAssignClientThatDoesNotExist() {
        //given
        when(clientRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        //when
        ThrowableAssert.ThrowingCallable callable = () -> clientService.assignClientToBranch(1L, 1L);

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(ObjectNotFoundInRepositoryException.class)
                .hasMessage("No client under ID #1");
    }

    @Test
    void shouldNotAssignClientWhenBranchDoesNotExist() {
        //given
        when(clientRepositoryMock.findById(anyLong())).thenReturn(Optional.of(client));
        when(branchRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        //when
        ThrowableAssert.ThrowingCallable callable = () -> clientService.assignClientToBranch(1L, 1L);

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(ObjectNotFoundInRepositoryException.class)
                .hasMessage("No branch under ID #1");
    }

    @Test
    void shouldRemoveClientFromBranch() {
        //given
        branch.getClients().add(client);
        client.setBranch(branch);
        when(branchRepositoryMock.findById(anyLong())).thenReturn(Optional.of(branch));
        when(branchRepositoryMock.save(any(Branch.class))).thenReturn(branch);

        //when
        clientService.removeClientFromBranch(1L, 1L);

        //then
        ArgumentCaptor<Branch> branchCaptor = ArgumentCaptor.forClass(Branch.class);
        verify(branchRepositoryMock, times(1)).save(branchCaptor.capture());
        Branch savedBranch = branchCaptor.getValue();
        assertThat(savedBranch).isEqualTo(branch);
    }

    @Test
    void shouldNotRemoveClientWhenBranchDoesNotExist() {
        //given
        when(branchRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        //when
        ThrowableAssert.ThrowingCallable callable = () -> clientService.removeClientFromBranch(1L, 1L);

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(ObjectNotFoundInRepositoryException.class)
                .hasMessage("No branch under ID #1");
        verify(branchRepositoryMock, never()).save(any(Branch.class));
    }

    @Test
    void shouldNotRemoveClientWhenThereIsNoClientInGivenBranch() {
        //given
        when(branchRepositoryMock.findById(anyLong())).thenReturn(Optional.of(branch));

        //when
        ThrowableAssert.ThrowingCallable callable = () -> clientService.removeClientFromBranch(1L, 1L);

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(ObjectNotFoundInRepositoryException.class)
                .hasMessage("No client under ID #1 is assigned to branch under ID #1");
        verify(branchRepositoryMock, never()).save(any(Branch.class));
    }
}