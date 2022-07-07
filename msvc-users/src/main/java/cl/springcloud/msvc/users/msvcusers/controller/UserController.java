package cl.springcloud.msvc.users.msvcusers.controller;

import cl.springcloud.msvc.users.msvcusers.model.entities.User;
import cl.springcloud.msvc.users.msvcusers.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> detail() {
        return ResponseEntity.ok(userService.listUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> detail(@PathVariable Long id) {
        Optional<User> user = userService.byIdUser(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody User user, BindingResult result) {
        if (userService.byMailUser(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "correo ya existe"));
        }
        if (result.hasErrors()) {
            return validateErrors(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity edit(@Valid @RequestBody User user, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return validateErrors(result);
        }
        Optional<User> userRepo = userService.byIdUser(id);
        if (userRepo.isPresent()) {
            User userdb = userRepo.get();
            if (userdb.getEmail().equalsIgnoreCase(user.getEmail())
                    && userService.byMailUser(user.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "correo ya existe"));
            }

            userdb.setName(user.getName());
            userdb.setEmail(user.getEmail());

            return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(userdb));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        Optional<User> user = userService.byIdUser(id);
        if (user.isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("/users-courses")
    public ResponseEntity getStudentsByCourse(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(userService.findAllByIds(ids));
    }

    private ResponseEntity validateErrors(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "el campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
