package cl.springcloud.msvc.courses.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "course_user")
public class CourseUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true)
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CourseUser)) {
            return false;
        }
        CourseUser courseUser = (CourseUser) obj;
        return userId != null && userId.equals(courseUser.userId);
    }
}
