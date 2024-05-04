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
import pl.sda.carrental.configuration.auth.model.Client;
import pl.sda.carrental.model.Branch;
import pl.sda.carrental.service.ClientService;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
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
    private ClientService clientServiceMock;

    private Branch branch1;
    private Client client1;
    private Client client2;


    @BeforeEach
    void setUp() {
        branch1 = new Branch(1L, "name1", "address1", null,
                new HashSet<>(), new HashSet<>(), new HashSet<>(),
                null, null);

        client1 = new Client(1L,
                "login",
                "password",
                "name1",
                "surname1",
                branch1,
                null,
                "email1@email.pl",
                "address1");
        client2 = new Client(2L,
                "login",
                "password",
                "name2",
                "surname2",
                null,
                null,
                "email2@email.pl",
                "address2");
        branch1.getClients().add(client1);
    }

    @Test
    void shouldFindById() throws Exception {
        //given
        given(clientServiceMock.findById(anyLong())).willReturn(client1);

        //when
        ResultActions response = mockMvc.perform(get("/api/manageL2/clients/1"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class));
    }

    @Test
    void shouldGetAllClients() throws Exception {
        //given
        List<Client> list = Collections.singletonList(client1);
        given(clientServiceMock.getAllClients()).willReturn(list);

        //when
        ResultActions response = mockMvc.perform(get("/api/manageL2/clients"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect((result) -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    JSONAssert.assertEquals(objectMapper.writeValueAsString(list), contentAsString, true);
                });
    }


    @Test
    void shouldEditClient() throws Exception {
        //given
        Client changed = new Client(123L, "login", "password", "changedName", "changedSurname", branch1, null,
                "changedEmail", "changedAddress");
        given(clientServiceMock.editClient(anyLong(), any(Client.class))).willReturn(changed);

        //when
        ResultActions response = mockMvc.perform(put("/api/manageL2/clients/1")
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
        doNothing().when(clientServiceMock).removeClient(anyLong());

        //when
        ResultActions response = mockMvc.perform(delete("/api/manageL2/clients/1"));

        //then
        response.andExpect(status().isOk());
    }

    @Test
    void shouldAssignClientToBranch() throws Exception {
        //given
        given(clientServiceMock.assignClientToBranch(anyLong(), anyLong())).willReturn(client2);

        //when
        ResultActions response = mockMvc.perform(patch("/api/manageL2/clients/client/2/assignToBranch/1"));

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
        doNothing().when(clientServiceMock).removeClientFromBranch(anyLong(), anyLong());

        //when
        ResultActions response = mockMvc.perform(patch("/api/manageL2/clients/client/1/detachFromBranch/1"));

        //then
        response.andExpect(status().isOk());
    }
}























