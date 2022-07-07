package cl.springcloud.msvc.users.msvcusers.service;

import cl.springcloud.msvc.users.msvcusers.model.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> listUsers();

    Optional<User> byIdUser(Long id);

    User saveUser(User user);

    void deleteUser(Long id);

    List<User> findAllByIds(Iterable<Long> ids);

    Optional<User> byMailUser(String email);

}
