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
import pl.sda.carrental.model.Revenue;
import pl.sda.carrental.service.RevenueService;

import java.math.BigDecimal;
import java.util.Collections;
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
class RevenueControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RevenueService revenueServiceMock;

    private Revenue revenue;

    @BeforeEach
    void setUp() {
        revenue = new Revenue(1L, new BigDecimal("100.0"));
    }

    @Test
    void shouldGetAllRevenues() throws Exception {
        //given
        List<Revenue> list = Collections.singletonList(revenue);
        given(revenueServiceMock.getAllRevenues()).willReturn(list);

        //when
        ResultActions response = mockMvc.perform(get("/api/admin/revenues"));

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
        given(revenueServiceMock.addRevenue(any(Revenue.class))).willReturn(revenue);

        //when
        ResultActions response = mockMvc.perform(post("/api/admin/revenues")
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
        given(revenueServiceMock.editRevenue(anyLong(), any(Revenue.class))).willReturn(changed);

        //when
        ResultActions response = mockMvc.perform(put("/api/admin/revenues/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changed)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.revenueId", is(123L), Long.class))
                .andExpect(jsonPath("$.totalAmount", is(new BigDecimal("200.0")), BigDecimal.class));
    }

    @Test
    void shouldAssignRevenueToBranch() throws Exception {
        //given
        doNothing().when(revenueServiceMock).assignRevenueToBranchByAccordingIds(anyLong(), anyLong());

        //when
        ResultActions response = mockMvc.perform(patch("/api/admin/revenues/assignRevenue/1/toBranch/1"));

        //then
        response.andExpect(status().isOk());
    }

    @Test
    void shouldDeleteRevenue() throws Exception {
        //given
        doNothing().when(revenueServiceMock).deleteRevenue(anyLong());

        //when
        ResultActions response = mockMvc.perform(delete("/api/admin/revenues/1"));

        //then
        response.andExpect(status().isOk());
    }
}