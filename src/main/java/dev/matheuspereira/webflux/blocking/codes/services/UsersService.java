package dev.matheuspereira.webflux.blocking.codes.services;

import dev.matheuspereira.webflux.blocking.codes.models.User;
import dev.matheuspereira.webflux.blocking.codes.repositories.UsersRepository;
import dev.matheuspereira.webflux.blocking.codes.requests.BatchStatusUsersReq;
import dev.matheuspereira.webflux.blocking.codes.responses.StatusUserUpdatedRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService {
  private final UsersRepository usersRepository;

  public Flux<User> findAll() {
    return this.usersRepository.findAll();
  }

  public Mono<User> save(User user) {
    return this.usersRepository.save(user);
  }

  /**
   * I will keep this code only as complementary documentation to article XX of the series.
   * @param batchStatusUsersReq
   * @return - This method always throws an exception
   * @exception - block()/blockFirst()/blockLast() are blocking, which is not supported in thread reactor-http-nio-2
   */
  public Flux<StatusUserUpdatedRes> batchStatusManagerBlockerError(BatchStatusUsersReq batchStatusUsersReq) {
    Flux<StatusUserUpdatedRes> flux = Flux.empty();
    for (int id : batchStatusUsersReq.getBachIds()) {
      Mono<User> userFound = usersRepository.findById(id);
      User user = userFound.block();
      user.setStatus(batchStatusUsersReq.getStatus());
      usersRepository.save(user);

      StatusUserUpdatedRes userUpdatedRes = StatusUserUpdatedRes.builder()
          .id(user.getId())
          .status(user.getStatus())
          .build();
      flux = Flux.concat(flux, Mono.just(userUpdatedRes));
    }
    return flux;
  }

  /**
   * I will keep this code only as complementary documentation to article XX of the series.
   * @param batchStatusUsersReq
   * @return - This method always return success but not update users
   */
  public Flux<StatusUserUpdatedRes> batchStatusManagerNoSubscribe(BatchStatusUsersReq batchStatusUsersReq) {
    Flux<StatusUserUpdatedRes> flux = Flux.empty();
    for (int id : batchStatusUsersReq.getBachIds()) {
      Mono<User> userFound = usersRepository.findById(id).log("DEBUG USERS METHODS");
      var userUpdatedRes = userFound.map(u -> {
        u.setStatus(batchStatusUsersReq.getStatus());
        usersRepository.save(u);
        return StatusUserUpdatedRes.builder()
            .id(u.getId())
            .status(u.getStatus())
            .build();
      });
      flux = Flux.concat(flux, userUpdatedRes);
    }

    return flux;
  }

  public Flux<StatusUserUpdatedRes> batchStatusManager(BatchStatusUsersReq batchStatusUsersReq) {
    Flux<StatusUserUpdatedRes> flux = Flux.empty();
    for (int id : batchStatusUsersReq.getBachIds()) {
      Mono<User> userFound = usersRepository.findById(id).log("DEBUG USERS METHODS");
      var userUpdatedRes = userFound.map(u -> {
        u.setStatus(batchStatusUsersReq.getStatus());
        usersRepository.save(u).subscribe();
        return StatusUserUpdatedRes.builder()
            .id(u.getId())
            .status(u.getStatus())
            .build();
      });
      flux = Flux.concat(flux, userUpdatedRes);
    }

    return flux;
  }


}