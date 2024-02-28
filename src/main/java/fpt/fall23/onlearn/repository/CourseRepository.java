package fpt.fall23.onlearn.repository;

import fpt.fall23.onlearn.dto.course.CourseResponse;
import fpt.fall23.onlearn.entity.Course;
import fpt.fall23.onlearn.enums.CourseStatus;
import fpt.fall23.onlearn.enums.EnrollStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findAllBySubjectId(Long subjectId);

    List<Course> findALlByTeacherId(Long teacherId);

    List<Course> findAllByStatus(CourseStatus courseStatus);

    @Query(
            nativeQuery = true,
            value = "select c.* " +
                    "from course c left join enroll e on c.id = e.course_id " +
                    "where c.name like :value or c.price between :minPrice and :maxPrice"
    )
    List<Course> findCourseByFilter(String value, Double minPrice, Double maxPrice);


    @Query(
            value = "select new fpt.fall23.onlearn.dto.course.CourseResponse(c, e.finishDate) " +
                    "from Course c left join Enroll e on c.id = e.course.id " +
                    "where e.student.id = :studentId and e.status = :status "
    )
    Page<CourseResponse> findCourseEnrolls(Long studentId, EnrollStatus status, Pageable pageable);


    @Query(nativeQuery = true,
            value = "SELECT *\n" +
                    "FROM course c\n" +
                    "WHERE c.id NOT IN (\n" +
                    "    SELECT e.course_id\n" +
                    "    FROM enroll e\n" +
                    "    WHERE e.student_id = :studentId AND e.status in ('PROCESSING', 'DONE')\n" +
                    ")\n" +
                    "AND c.status LIKE :status AND c.name like :searchValue")
    List<Course> findCourseUnEnrolls(Long studentId, String status, @Param("searchValue") String value);


    @Query(nativeQuery = true,
            value = "select c.* from course c \n" +
                    "left join lesson s on c.id = s.course_id\n" +
                    "left join quiz q on s.id = q.lesson_id\n" +
                    "where q.id = :quizId")
    Course findCourseByQuiz(Long quizId);

}
