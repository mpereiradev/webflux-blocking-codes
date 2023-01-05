package dev.matheuspereira.webflux.blocking.codes.rest;

import com.github.javafaker.Faker;
import dev.matheuspereira.webflux.blocking.codes.models.User;
import dev.matheuspereira.webflux.blocking.codes.requests.BatchStatusUsersReq;
import dev.matheuspereira.webflux.blocking.codes.responses.StatusUserUpdatedRes;
import dev.matheuspereira.webflux.blocking.codes.services.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@Slf4j
@Tag(name = "Users")
@RestController
@AllArgsConstructor
@RequestMapping(value = "/users", produces = "application/json")
public class UsersResource {
  private final UsersService usersService;

  /**
   * In this example we will return all users save in one stream of data.
   * @return
   */
  @Operation(description = "Get all users save in stream type with one second of delay between elements")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "202", description = "Request accepted"),
      @ApiResponse(responseCode = "500", description = "Internal server error"),
  })
  @GetMapping(value = "", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<User> all() {
    return usersService.findAll();
  }

  /**
   * In this example we will perform the same task as in the previous example.
   * But now in the correct way
   * @param size - Integer
   * @return Flux<String>
   * @throws InterruptedException
   */
  @Operation(description = "Start generation users loop with counter passed in stream type")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "202", description = "Request accepted"),
      @ApiResponse(responseCode = "500", description = "Internal server error"),
  })
  @GetMapping(value = "/generate/{size}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<String> generateStream(@PathVariable("size") Integer size) throws InterruptedException {
    log.info("INIT SAVE OF {} USERS STREAM", size);
    return Flux.create((FluxSink<String> fluxSink) -> {
      for(int i = 0; i < size; i++) {
        Faker faker = new Faker();
        User user = User.builder()
            .fullName(faker.name().fullName())
            .email(faker.internet().emailAddress())
            .password(faker.internet().password())
            .build();

        usersService.save(user).subscribe(
            u -> fluxSink.next(u.getFullName())
        );
      }
      fluxSink.complete();
    });
  }


  @Operation(description = "Execute the status update process for a batch of users in a blocking way")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "202", description = "Request accepted"),
      @ApiResponse(responseCode = "500", description = "Internal server error"),
  })
  @PatchMapping(value = "/status/batch", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<StatusUserUpdatedRes> batchStatusManager(@RequestBody BatchStatusUsersReq batchStatusUsersReq) throws InterruptedException {
    log.info("INIT PROCESS STATUS OF {} USERS", batchStatusUsersReq.getBachIds().length);
    return usersService.batchStatusManager(batchStatusUsersReq).log("DEBUG FLUX OF RESULTS");
  }
}
