package cl.springcloud.msvc.users.msvcusers.service;

import cl.springcloud.msvc.users.msvcusers.client.CourseClientRest;
import cl.springcloud.msvc.users.msvcusers.model.entities.User;
import cl.springcloud.msvc.users.msvcusers.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private CourseClientRest courseClientRest;

    @Override
    @Transactional(readOnly = true)
    public List<User> listUsers() {
        return (List<User>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> byIdUser(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        return repository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        repository.deleteById(id);
        courseClientRest.deleteCourseUserById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllByIds(Iterable<Long> ids) {
        return (List<User>) repository.findAllById(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> byMailUser(String email) {
        return repository.findByEmail(email);
    }
}
