package tn.esprit.studentmanagement.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.studentmanagement.entities.*;
import tn.esprit.studentmanagement.repositories.EnrollmentRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Builder
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private Enrollment enrollment1;
    private Enrollment enrollment2;
    private Student student;
    private Course course;

    @BeforeEach
    void setUp() {
        // Créer Student
        student = Student.builder()
                .idStudent(1L)
                .firstName("Cyrine")
                .lastName("Ben Hlila")
                .email("syrinebh05@gmail.com")
                .build();

        // Créer Course
        course = Course.builder()
                .idCourse(1L)
                .name("Mathématiques")
                .description("Cours de mathématiques avancées")
                .build();

        // Créer Enrollments
        enrollment1 = Enrollment.builder()
                .idEnrollment(1L)
                .enrollmentDate(LocalDate.of(2024, 9, 1))
                .grade(16.5)
                .status(Status.ACTIVE)
                .student(student)
                .course(course)
                .build();

        enrollment2 = Enrollment.builder()
                .idEnrollment(2L)
                .enrollmentDate(LocalDate.of(2024, 9, 2))
                .grade(14.0)
                .status(Status.COMPLETED)
                .student(student)
                .course(course)
                .build();
    }

    @Test
    void testGetAllEnrollments_ShouldReturnList() {
        // Arrange
        List<Enrollment> expectedEnrollments = Arrays.asList(enrollment1, enrollment2);
        when(enrollmentRepository.findAll()).thenReturn(expectedEnrollments);

        // Act
        List<Enrollment> result = enrollmentService.getAllEnrollments();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getIdEnrollment());
        assertEquals(2L, result.get(1).getIdEnrollment());
        assertEquals(16.5, result.get(0).getGrade());
        assertEquals(14.0, result.get(1).getGrade());
        verify(enrollmentRepository, times(1)).findAll();
    }

    @Test
    void testGetAllEnrollments_WhenEmpty_ShouldReturnEmptyList() {
        // Arrange
        when(enrollmentRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Enrollment> result = enrollmentService.getAllEnrollments();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(enrollmentRepository, times(1)).findAll();
    }

    @Test
    void testGetEnrollmentById_WhenExists_ShouldReturnEnrollment() {
        // Arrange
        Long enrollmentId = 1L;
        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.of(enrollment1));

        // Act
        Enrollment result = enrollmentService.getEnrollmentById(enrollmentId);

        // Assert
        assertNotNull(result);
        assertEquals(enrollmentId, result.getIdEnrollment());
        assertEquals(LocalDate.of(2024, 9, 1), result.getEnrollmentDate());
        assertEquals(16.5, result.getGrade());
        assertEquals(Status.ACTIVE, result.getStatus());
        assertEquals("Cyrine", result.getStudent().getFirstName());
        assertEquals("Mathématiques", result.getCourse().getName());
        verify(enrollmentRepository, times(1)).findById(enrollmentId);
    }

    @Test
    void testGetEnrollmentById_WhenNotExists_ShouldThrowException() {
        // Arrange
        Long enrollmentId = 99L;
        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> enrollmentService.getEnrollmentById(enrollmentId)
        );

        assertEquals("Enrollment not found with id: " + enrollmentId, exception.getMessage());
        verify(enrollmentRepository, times(1)).findById(enrollmentId);
    }

    @Test
    void testSaveEnrollment_ShouldReturnSavedEnrollment() {
        // Arrange
        Enrollment newEnrollment = Enrollment.builder()
                .enrollmentDate(LocalDate.now())
                .grade(15.0)
                .status(Status.ACTIVE)
                .student(student)
                .course(course)
                .build();

        Enrollment savedEnrollment = Enrollment.builder()
                .idEnrollment(3L)
                .enrollmentDate(LocalDate.now())
                .grade(15.0)
                .status(Status.ACTIVE)
                .student(student)
                .course(course)
                .build();

        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(savedEnrollment);

        // Act
        Enrollment result = enrollmentService.saveEnrollment(newEnrollment);

        // Assert
        assertNotNull(result);
        assertEquals(3L, result.getIdEnrollment());
        assertEquals(15.0, result.getGrade());
        assertEquals(Status.ACTIVE, result.getStatus());
        assertEquals(student, result.getStudent());
        assertEquals(course, result.getCourse());
        verify(enrollmentRepository, times(1)).save(newEnrollment);
    }
    @Test
    void testDeleteEnrollment_ShouldCallRepository() {
        // Arrange
        Long enrollmentId = 1L;
        doNothing().when(enrollmentRepository).deleteById(enrollmentId);

        // Act
        enrollmentService.deleteEnrollment(enrollmentId);

        // Assert
        verify(enrollmentRepository, times(1)).deleteById(enrollmentId);
    }

    @Test
    void testUpdateEnrollment_ShouldReturnUpdatedEnrollment() {
        // Arrange
        Enrollment updatedEnrollment = Enrollment.builder()
                .idEnrollment(1L)
                .enrollmentDate(LocalDate.of(2024, 10, 1))
                .grade(18.0)
                .status(Status.COMPLETED)
                .student(student)
                .course(course)
                .build();

        when(enrollmentRepository.save(updatedEnrollment)).thenReturn(updatedEnrollment);

        // Act
        Enrollment result = enrollmentService.saveEnrollment(updatedEnrollment);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdEnrollment());
        assertEquals(18.0, result.getGrade());
        assertEquals(Status.COMPLETED, result.getStatus());
        assertEquals(LocalDate.of(2024, 10, 1), result.getEnrollmentDate());
        verify(enrollmentRepository, times(1)).save(updatedEnrollment);
    }

    @Test
    void testGetEnrollmentById_NullId_ShouldThrowException() {
        // Arrange
        when(enrollmentRepository.findById(null)).thenThrow(new IllegalArgumentException());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            enrollmentService.getEnrollmentById(null);
        });
    }
    @Test
    void testGetEnrollmentById_VerifyStudentAndCourseRelations() {
        // Arrange
        Long enrollmentId = 1L;
        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.of(enrollment1));

        // Act
        Enrollment result = enrollmentService.getEnrollmentById(enrollmentId);

        // Assert
        assertNotNull(result.getStudent());
        assertEquals("Cyrine", result.getStudent().getFirstName());
        assertEquals("Ben Hlila", result.getStudent().getLastName());

        assertNotNull(result.getCourse());
        assertEquals("Mathématiques", result.getCourse().getName());

        verify(enrollmentRepository, times(1)).findById(enrollmentId);
    }
}