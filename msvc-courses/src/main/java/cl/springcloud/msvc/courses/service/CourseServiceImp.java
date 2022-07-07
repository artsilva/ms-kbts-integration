package cl.springcloud.msvc.courses.service;

import cl.springcloud.msvc.courses.clients.UserClientRest;
import cl.springcloud.msvc.courses.model.User;
import cl.springcloud.msvc.courses.model.entity.Course;
import cl.springcloud.msvc.courses.model.entity.CourseUser;
import cl.springcloud.msvc.courses.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImp implements CourseService {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserClientRest client;

    @Override
    @Transactional(readOnly = true)
    public List<Course> listCourses() {
        return (List<Course>) courseRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> byIdCourse(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> byIdCourseWithUsers(Long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            if (!course.getCourseUsers().isEmpty()) {
                List<Long> ids = course.getCourseUsers().stream().map(courseUser -> courseUser.getUserId())
                        .collect(Collectors.toList());
                List<User> users = client.getStudentsByCourse(ids);
                course.setUsers(users);
            }
            return Optional.of(course);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteCourseUserById(Long id) {
        courseRepository.deleteCourseUserById(id);
    }

    @Override
    @Transactional
    public Optional<User> assignUser(User user, Long courseId) {
        Optional<Course> c = courseRepository.findById(courseId);
        if (c.isPresent()) {
            User userMsvc = client.detail(user.getId());
            Course course = c.get();
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(userMsvc.getId());

            course.addCourseUser(courseUser);
            courseRepository.save(course);

            return Optional.of(userMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<User> createUser(User user, Long courseId) {
        Optional<Course> c = courseRepository.findById(courseId);
        if (c.isPresent()) {
            User newUserMsvc = client.create(user);
            Course course = c.get();
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(newUserMsvc.getId());

            course.addCourseUser(courseUser);
            courseRepository.save(course);

            return Optional.of(newUserMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<User> deleteUser(User user, Long courseId) {
        Optional<Course> c = courseRepository.findById(courseId);
        if (c.isPresent()) {
            User userMsvc = client.create(user);
            Course course = c.get();
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(userMsvc.getId());

            course.removeCourseUser(courseUser);
            courseRepository.save(course);

            return Optional.of(userMsvc);
        }
        return Optional.empty();
    }
}
