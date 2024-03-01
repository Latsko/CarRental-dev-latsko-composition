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
import pl.sda.carrental.model.Branch;
import pl.sda.carrental.model.Revenue;
import pl.sda.carrental.repository.BranchRepository;
import pl.sda.carrental.repository.RevenueRepository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
class RevenueControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private RevenueRepository revenueRepositoryMock;
    @MockBean
    private BranchRepository branchRepositoryMock;
    private Branch branch;
    private Revenue revenue;

    @BeforeEach
    void setUp() {
        branch = new Branch();
        revenue = new Revenue(1L, new BigDecimal("100.0"));
    }
    @Test
    void shouldGetAllRevenues() throws Exception {
        //given
        List<Revenue> list = Collections.singletonList(revenue);
        given(revenueRepositoryMock.findAll()).willReturn(list);

        //when
        ResultActions response = mockMvc.perform(get("/revenues"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect((result) -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    JSONAssert.assertEquals(objectMapper.writeValueAsString(list), contentAsString, true);
                });
    }

    @Test
    void shouldAddRevenue() throws Exception {
        //given
        given(revenueRepositoryMock.save(any(Revenue.class))).willReturn(revenue);

        //when
        ResultActions response = mockMvc.perform(post("/revenues")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(revenue)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.revenueId", is(1L), Long.class))
                .andExpect(jsonPath("$.totalAmount", is(new BigDecimal("100.0")), BigDecimal.class));
    }

    @Test
    void shouldEditRevenue() throws Exception {
        //given
        Revenue changed = new Revenue(123L, new BigDecimal("200.0"));
        given(revenueRepositoryMock.findById(anyLong())).willReturn(Optional.of(revenue));
        given(revenueRepositoryMock.save(any(Revenue.class))).willReturn(revenue);

        //when
        ResultActions response = mockMvc.perform(put("/revenues/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changed)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.revenueId", is(1L), Long.class))
                .andExpect(jsonPath("$.totalAmount", is(new BigDecimal("200.0")), BigDecimal.class));
    }

    @Test
    void shouldAssignRevenueToBranch() throws Exception {
        //given
        given(branchRepositoryMock.findById(anyLong())).willReturn(Optional.of(branch));
        given(revenueRepositoryMock.findById(anyLong())).willReturn(Optional.of(revenue));
        given(branchRepositoryMock.save(any(Branch.class))).willReturn(branch);

        //when
        ResultActions response = mockMvc.perform(patch("/revenues/assignRevenue/1/toBranch/1"));

        //then
        response.andExpect(status().isOk());
        verify(branchRepositoryMock, times(1)).save(branch);
    }

    @Test
    void shouldDeleteRevenue() throws Exception {
        //given
        given(revenueRepositoryMock.findById(anyLong())).willReturn(Optional.of(revenue));
        given(branchRepositoryMock.findAll()).willReturn(List.of());
        given(branchRepositoryMock.save(any(Branch.class))).willReturn(branch);
        doNothing().when(revenueRepositoryMock).deleteById(anyLong());

        //when
        ResultActions response = mockMvc.perform(delete("/revenues/1"));

        //then
        response.andExpect(status().isOk());
        verify(revenueRepositoryMock).deleteById(1L);
    }
}