package cl.springcloud.msvc.courses.clients;

import cl.springcloud.msvc.courses.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-users", url = "localhost:8001/user")
public interface UserClientRest {

    @GetMapping("/{id}")
    User detail(@PathVariable Long id);

    @PostMapping
    User create(@RequestBody User user);

    @GetMapping("/users-courses")
    List<User> getStudentsByCourse(@RequestParam Iterable
            <Long> ids);
}
