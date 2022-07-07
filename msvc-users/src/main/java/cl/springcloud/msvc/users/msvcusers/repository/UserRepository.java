package cl.springcloud.msvc.users.msvcusers.repository;

import cl.springcloud.msvc.users.msvcusers.model.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
