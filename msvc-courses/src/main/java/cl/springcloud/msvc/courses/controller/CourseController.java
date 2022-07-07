package cl.springcloud.msvc.courses.controller;

import cl.springcloud.msvc.courses.model.User;
import cl.springcloud.msvc.courses.model.entity.Course;
import cl.springcloud.msvc.courses.service.CourseService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    CourseService courseService;

    @GetMapping
    public ResponseEntity<List<Course>> toList() {
        return ResponseEntity.ok(courseService.listCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Course>> toList(@PathVariable Long id) {
        Optional<Course> course = courseService.byIdCourseWithUsers(id);//courseService.byIdCourse(id);
        if (course.isPresent()) {
            return ResponseEntity.ok(course);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<Course> create(@Valid @RequestBody Course course, BindingResult result) {
        if (result.hasErrors()) {
            return validateErrors(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.saveCourse(course));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@Valid @RequestBody Course course, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return validateErrors(result);
        }
        Optional<Course> courseRepo = courseService.byIdCourse(id);
        if (courseRepo.isPresent()) {
            Course coursedb = courseRepo.get();
            coursedb.setName(course.getName());

            return ResponseEntity.status(HttpStatus.CREATED).body(courseService.saveCourse(coursedb));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        Optional<Course> course = courseService.byIdCourse(id);
        if (course.isPresent()) {
            courseService.deleteCourse(id);
            return ResponseEntity.status(HttpStatus.CONTINUE).build();
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping("/assign/{courseId}")
    public ResponseEntity assignUser(@RequestBody User user, @PathVariable Long courseId) {
        Optional<User> optionalUser;
        try {
            optionalUser = courseService.assignUser(user, courseId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("Message ", String.format(
                            "Error communication by user id doesnt exist %s", e.getMessage())));
        }
        if (optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(optionalUser.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/create/{courseId}")
    public ResponseEntity createUser(@RequestBody User user, @PathVariable Long courseId) {
        Optional<User> optionalUser;
        try {
            optionalUser = courseService.createUser(user, courseId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("Message ", String.format(
                            "Cannot create due to error communication, user id doesnt exist %s", e.getMessage())));
        }
        if (optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(optionalUser.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/delete/{courseId}")
    public ResponseEntity deleteUser(@RequestBody User user, @PathVariable Long courseId) {
        Optional<User> optionalUser;
        try {
            optionalUser = courseService.createUser(user, courseId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("Message ", String.format(
                            "Cannot delete due to error communication, user id doesnt exist %s", e.getMessage())));
        }
        if (optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(optionalUser.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete-course-user/{id}")
    public ResponseEntity deleteCourseUserById(@PathVariable Long id) {
        courseService.deleteCourseUserById(id);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity validateErrors(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "el campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
