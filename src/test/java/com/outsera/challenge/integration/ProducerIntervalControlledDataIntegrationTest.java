package com.outsera.challenge.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "movie.csv.path=classpath:test-movies.csv",
        "spring.datasource.url=jdbc:h2:mem:testmoviedb"
})
@AutoConfigureMockMvc
class ProducerIntervalControlledDataIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnTiedMinAndMaxWhenMultipleProducersShareTheSameInterval() throws Exception {
        mockMvc.perform(get("/v1/producers/interval"))
                .andExpect(status().isOk())
                // Min interval=1: Alpha (2000→2001) e Gamma (2003→2004), ordenados por nome
                .andExpect(jsonPath("$.min", hasSize(2)))
                .andExpect(jsonPath("$.min[0].producer").value("Producer Alpha"))
                .andExpect(jsonPath("$.min[0].interval").value(1))
                .andExpect(jsonPath("$.min[0].previousWin").value(2000))
                .andExpect(jsonPath("$.min[0].followingWin").value(2001))
                .andExpect(jsonPath("$.min[1].producer").value("Producer Gamma"))
                .andExpect(jsonPath("$.min[1].interval").value(1))
                .andExpect(jsonPath("$.min[1].previousWin").value(2003))
                .andExpect(jsonPath("$.min[1].followingWin").value(2004))
                // Max interval=5: Beta (2005→2010) e Delta (2004→2009), ordenados por nome
                .andExpect(jsonPath("$.max", hasSize(2)))
                .andExpect(jsonPath("$.max[0].producer").value("Producer Beta"))
                .andExpect(jsonPath("$.max[0].interval").value(5))
                .andExpect(jsonPath("$.max[0].previousWin").value(2005))
                .andExpect(jsonPath("$.max[0].followingWin").value(2010))
                .andExpect(jsonPath("$.max[1].producer").value("Producer Delta"))
                .andExpect(jsonPath("$.max[1].interval").value(5))
                .andExpect(jsonPath("$.max[1].previousWin").value(2004))
                .andExpect(jsonPath("$.max[1].followingWin").value(2009));
    }

    @Test
    void shouldExcludeProducerWithSingleWinAndNonWinnerMovies() throws Exception {
        // Producer Single tem apenas 1 vitória (sem intervalo), Producer Loser nunca ganhou
        mockMvc.perform(get("/v1/producers/interval"))
                .andExpect(status().isOk())
                // min e max juntos têm exatamente 4 entradas (2+2); nenhuma delas é "Single" ou "Loser"
                .andExpect(jsonPath("$.min", hasSize(2)))
                .andExpect(jsonPath("$.max", hasSize(2)))
                .andExpect(jsonPath("$.min[?(@.producer == 'Producer Single')]").isEmpty())
                .andExpect(jsonPath("$.min[?(@.producer == 'Producer Loser')]").isEmpty())
                .andExpect(jsonPath("$.max[?(@.producer == 'Producer Single')]").isEmpty())
                .andExpect(jsonPath("$.max[?(@.producer == 'Producer Loser')]").isEmpty());
    }
}
