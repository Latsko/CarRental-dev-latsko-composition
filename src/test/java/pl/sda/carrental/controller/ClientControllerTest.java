package pl.sda.carrental.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.sda.carrental.configuration.auth.repository.ClientRepository;
import pl.sda.carrental.model.Branch;
import pl.sda.carrental.configuration.auth.entity.Client;
import pl.sda.carrental.repository.*;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ClientRepository clientRepositoryMock;
    @MockBean
    private BranchRepository branchRepositoryMock;
    @MockBean
    private RentRepository rentRepositoryMock;
    @MockBean
    private ReturnRepository returnRepositoryMock;
    @MockBean
    private ReservationRepository reservationRepositoryMock;
    private Branch branch1;
    private Client client1;
    private Client client2;


    @BeforeEach
    void setUp() {
        branch1 = new Branch(1L, "name1", "address1", null,
                new HashSet<>(), new HashSet<>(), new HashSet<>(),
                null, null);

        client1 = new Client(1L, "name1", "surname1", "email1@email.pl", "address1", branch1);
        client2 = new Client(2L, "name2", "surname2", "email2@email.pl", "address2", null);
        branch1.getClients().add(client1);
    }

    @Test
    void shouldFindById() throws Exception {
        //given
        given(clientRepositoryMock.findById(anyLong())).willReturn(Optional.of(client1));

        //when
        ResultActions response = mockMvc.perform(get("/clients/1"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId", is(1L), Long.class));
    }

    @Test
    void shouldGetAllClients() throws Exception {
        //given
        List<Client> list = Collections.singletonList(client1);
        given(clientRepositoryMock.findAll()).willReturn(list);

        //when
        ResultActions response = mockMvc.perform(get("/clients"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect((result) -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    JSONAssert.assertEquals(objectMapper.writeValueAsString(list), contentAsString, true);
                });
    }

    @Test
    void shouldAddClient() throws Exception {
        //given
        given(clientRepositoryMock.save(any(Client.class))).willReturn(client1);

        //when
        ResultActions response = mockMvc.perform(post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(client1)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is("name1")))
                .andExpect(jsonPath("$.surname", is("surname1")))
                .andExpect(jsonPath("$.email", is("email1@email.pl")))
                .andExpect(jsonPath("$.address", is("address1")));
    }

    @Test
    void shouldEditClient() throws Exception {
        //given
        given(clientRepositoryMock.findById(anyLong())).willReturn(Optional.of(client1));
        given(branchRepositoryMock.save(any(Branch.class))).willReturn(branch1);
        given(clientRepositoryMock.save(any(Client.class))).willReturn(client1);
        Client changed = new Client(123L, "changedName", "changedSurname",
                "changedEmail", "changedAddress", branch1);

        //when
        ResultActions response = mockMvc.perform(put("/clients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changed)));

        //then
        response.andDo(print())
                .andExpect(jsonPath("$.name", is("changedName")))
                .andExpect(jsonPath("$.surname", is("changedSurname")))
                .andExpect(jsonPath("$.email", is("changedEmail")))
                .andExpect(jsonPath("$.address", is("changedAddress")));
    }

    @Test
    void shouldDeleteClient() throws Exception {
        //given
        given(clientRepositoryMock.findById(anyLong())).willReturn(Optional.of(client1));
        given(rentRepositoryMock.findAll()).willReturn(new ArrayList<>());
        given(returnRepositoryMock.findAll()).willReturn(new ArrayList<>());
        given(reservationRepositoryMock.findAll()).willReturn(new ArrayList<>());
        doNothing().when(rentRepositoryMock).deleteAll(List.of());
        doNothing().when(returnRepositoryMock).deleteAll(List.of());
        doNothing().when(reservationRepositoryMock).deleteAll(List.of());

        //when
        ResultActions response = mockMvc.perform(delete("/clients/1"));

        //then
        response.andExpect(status().isOk());
        verify(rentRepositoryMock).deleteAll(List.of());
        verify(returnRepositoryMock).deleteAll(List.of());
        verify(reservationRepositoryMock).deleteAll(List.of());
        verify(clientRepositoryMock).deleteById(1L);
    }

    @Test
    void shouldAssignClientToBranch() throws Exception {
        //given
        given(clientRepositoryMock.findById(anyLong())).willReturn(Optional.of(client2));
        given(branchRepositoryMock.findById(anyLong())).willReturn(Optional.of(branch1));
        given(branchRepositoryMock.save(any(Branch.class))).willReturn(branch1);
        given(clientRepositoryMock.save(any(Client.class))).willReturn(client2);

        //when
        ResultActions response = mockMvc.perform(patch("/clients/client/2/assignToBranch/1"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("name2")))
                .andExpect(jsonPath("$.surname", is("surname2")))
                .andExpect(jsonPath("$.email", is("email2@email.pl")))
                .andExpect(jsonPath("$.address", is("address2")));
    }

    @Test
    void shouldDetachClientFromBranch() throws Exception {
        //given
        given(branchRepositoryMock.findById(anyLong())).willReturn(Optional.of(branch1));
        given(branchRepositoryMock.save(any(Branch.class))).willReturn(branch1);

        //when
        ResultActions response = mockMvc.perform(patch("/clients/client/1/detachFromBranch/1"));

        //then
        response.andExpect(status().isOk());
        verify(branchRepositoryMock).save(branch1);
    }
}























