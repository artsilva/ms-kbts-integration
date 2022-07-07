package cl.springcloud.msvc.courses.service;

import cl.springcloud.msvc.courses.model.User;
import cl.springcloud.msvc.courses.model.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<Course> listCourses();

    Optional<Course> byIdCourse(Long id);

    Optional<Course> byIdCourseWithUsers(Long id);

    Course saveCourse(Course course);

    void deleteCourse(Long id);

    void deleteCourseUserById(Long id);

    Optional<User> assignUser(User user, Long courseId);

    Optional<User> createUser(User user, Long courseId);

    Optional<User> deleteUser(User user, Long courseId);
}
