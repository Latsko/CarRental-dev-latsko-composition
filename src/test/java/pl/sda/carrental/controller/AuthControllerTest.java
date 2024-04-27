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
import pl.sda.carrental.configuration.auth.dto.ClientDto;
import pl.sda.carrental.configuration.auth.dto.EmployeeDto;
import pl.sda.carrental.configuration.auth.dto.UserDto;
import pl.sda.carrental.configuration.auth.service.UserService;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userServiceMock;

    private UserDto employee;
    private UserDto client;

    @BeforeEach
    void setUp() {
        employee = new EmployeeDto(1L,
                "login1",
                "password1",
                "full name1",
                null, null);

        client = new ClientDto(2L,
                "login2",
                "password2",
                "full name2",
                null,
                "email@gmail.com",
                null);

    }

    @Test
    public void shouldFindUserByLogin() throws Exception {
        //given
        given(userServiceMock.findUserByLogin(anyString())).willReturn(employee);

        //when
        ResultActions response = mockMvc.perform(get("/api/auth")
                .param("login", employee.getLogin()));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login", is("login1")));
    }

    @Test
    public void shouldFindAllUsers() throws Exception {
        //given
        List<UserDto> users = List.of(employee, client);
        given(userServiceMock.findAllUsers()).willReturn(users);

        //when
        ResultActions response = mockMvc.perform(get("/api/auth/users"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect((result -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    JSONAssert.assertEquals(objectMapper.writeValueAsString(users), contentAsString, true);
                }));
    }

    @Test
    public void shouldRegisterClient() throws Exception {
        //given
        given(userServiceMock.saveClient(any(ClientDto.class))).willReturn((ClientDto) client);

        //when
        ResultActions response = mockMvc.perform(post("/api/auth/newClient")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(client)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login", is(client.getLogin())));
    }

    @Test
    public void shouldRegisterEmployee() throws Exception {
        //given
        given(userServiceMock.saveEmployee(any(EmployeeDto.class))).willReturn((EmployeeDto) employee);

        //when
        ResultActions response = mockMvc.perform(post("/api/auth/newEmployee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login", is(employee.getLogin())));
    }
}