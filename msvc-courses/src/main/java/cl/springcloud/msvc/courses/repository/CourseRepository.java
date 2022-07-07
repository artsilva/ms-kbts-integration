package cl.springcloud.msvc.courses.repository;

import cl.springcloud.msvc.courses.model.entity.Course;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, Long> {

    @Modifying
    @Query("delete from CourseUser cu where cu.userId=?1")
    void deleteCourseUserById(Long id);
}
