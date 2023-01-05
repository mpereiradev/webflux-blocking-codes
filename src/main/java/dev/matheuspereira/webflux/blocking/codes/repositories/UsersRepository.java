package dev.matheuspereira.webflux.blocking.codes.repositories;

import dev.matheuspereira.webflux.blocking.codes.models.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UsersRepository extends ReactiveCrudRepository<User, Integer> {/**/
}
