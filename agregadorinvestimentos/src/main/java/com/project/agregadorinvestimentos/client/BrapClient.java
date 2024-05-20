package com.project.agregadorinvestimentos.client;

import com.project.agregadorinvestimentos.client.dtos.BrapResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "BrapClient",
        url = "https://brapi.dev"
)
public interface BrapClient {

    //confugarando rotas chamandas

    @GetMapping(value = "/api/quote/{stockId}")
    BrapResponseDto getQuote(@RequestParam("token") String token,
                             @PathVariable("stockId") String stockId);
}
