package dev.matheuspereira.webflux.blocking.codes.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Tag(name = "Heath Check")
@RestController
@RequestMapping(value = "/health", produces = "application/json")
public class HealthResource {

  @Operation(description = "Retorna de forma não bloqueante uma sequência de playlists")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Request accepted"),
      @ApiResponse(responseCode = "500", description = "Internal server error"),
  })
  @GetMapping
  public Mono<String> generate() throws InterruptedException {
    log.info("HEALTH CHECK APPLICATION");
    return Mono.just("status: UP").delayElement(Duration.ofSeconds(1));
  }
}
