package tn.esprit.studentmanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.repositories.StudentRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student1;
    private Student student2;

    @BeforeEach
    void setUp() {
        student1 = Student.builder()
                .idStudent(1L)
                .firstName("Cyrine")
                .lastName("Ben Hlila")
                .email("syrinebh05@gmail.com")
                .phone("95972018")
                .dateOfBirth(LocalDate.of(2003, 1, 16))
                .address("Tunis, Tunisia")
                .build();

        student2 = Student.builder()
                .idStudent(2L)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .phone("12345678")
                .dateOfBirth(LocalDate.of(2000, 5, 15))
                .address("London, UK")
                .build();
    }

    @Test
    void testGetAllStudents_Success() {
        // Arrange (Préparation)
        List<Student> studentList = Arrays.asList(student1, student2);
        when(studentRepository.findAll()).thenReturn(studentList);

        // Act (Exécution)
        List<Student> result = studentService.getAllStudents();

        // Assert (Vérification)
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Cyrine", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        assertEquals(1L, result.get(0).getIdStudent());
        assertEquals(2L, result.get(1).getIdStudent());
        assertEquals("syrinebh05@gmail.com", result.get(0).getEmail());  

        // Vérifie que la méthode du repository a été appelée
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testGetStudentById_Found() {
        // Arrange
        Long studentId = 1L;
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student1));

        // Act
        Student result = studentService.getStudentById(studentId);

        // Assert
        assertNotNull(result);
        assertEquals(studentId, result.getIdStudent());
        assertEquals("Cyrine", result.getFirstName());  // Corrigé: Cyrine
        assertEquals("Ben Hlila", result.getLastName());
        assertEquals("syrinebh05@gmail.com", result.getEmail());
        verify(studentRepository, times(1)).findById(studentId);
    }

    @Test
    void testGetStudentById_NotFound() {
        // Arrange
        Long studentId = 99L;
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // Act
        Student result = studentService.getStudentById(studentId);

        // Assert
        assertNull(result);
        verify(studentRepository, times(1)).findById(studentId);
    }

    @Test
    void testSaveStudent_Success() {
        // Arrange
        Student newStudent = Student.builder()
                .firstName("Alice")
                .lastName("Johnson")
                .email("alice.johnson@example.com")
                .phone("98765432")
                .dateOfBirth(LocalDate.of(2001, 3, 10))
                .address("Paris, France")
                .build();

        Student savedStudent = Student.builder()
                .idStudent(3L)
                .firstName("Alice")
                .lastName("Johnson")
                .email("alice.johnson@example.com")
                .phone("98765432")
                .dateOfBirth(LocalDate.of(2001, 3, 10))
                .address("Paris, France")
                .build();

        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

        // Act
        Student result = studentService.saveStudent(newStudent);

        // Assert
        assertNotNull(result);
        assertEquals(3L, result.getIdStudent());
        assertEquals("Alice", result.getFirstName());
        assertEquals("alice.johnson@example.com", result.getEmail());
        verify(studentRepository, times(1)).save(newStudent);
    }

    @Test
    void testDeleteStudent_Success() {
        // Arrange
        Long studentId = 1L;
        doNothing().when(studentRepository).deleteById(studentId);

        // Act
        studentService.deleteStudent(studentId);

        // Assert
        verify(studentRepository, times(1)).deleteById(studentId);
    }

    @Test
    void testGetAllStudents_EmptyList() {
        // Arrange
        when(studentRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Student> result = studentService.getAllStudents();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testSaveStudent_WithId() {
        // Arrange
        Student existingStudent = Student.builder()
                .idStudent(1L)
                .firstName("Updated")
                .lastName("Name")
                .email("updated@example.com")
                .phone("11111111")
                .dateOfBirth(LocalDate.of(1998, 8, 20))
                .address("New York, USA")
                .build();

        when(studentRepository.save(existingStudent)).thenReturn(existingStudent);

        // Act
        Student result = studentService.saveStudent(existingStudent);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdStudent());
        assertEquals("Updated", result.getFirstName());
        assertEquals("updated@example.com", result.getEmail());
        verify(studentRepository, times(1)).save(existingStudent);
    }

    @Test
    void testGetStudentById_WrongId() {
        // Arrange
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Student result = studentService.getStudentById(999L);

        // Assert
        assertNull(result);
        verify(studentRepository, times(1)).findById(999L);
    }

    @Test
    void testSaveStudent_UpdateExisting() {
        // Arrange
        Student studentToUpdate = Student.builder()
                .idStudent(1L)
                .firstName("Cyrine")
                .lastName("Ben Hlila Updated")
                .email("syrine.updated@gmail.com")
                .phone("95972018")
                .dateOfBirth(LocalDate.of(2003, 1, 16))
                .address("New Address, Tunisia")
                .build();

        when(studentRepository.save(studentToUpdate)).thenReturn(studentToUpdate);

        // Act
        Student result = studentService.saveStudent(studentToUpdate);

        // Assert
        assertNotNull(result);
        assertEquals("Ben Hlila Updated", result.getLastName());
        assertEquals("syrine.updated@gmail.com", result.getEmail());
        assertEquals("New Address, Tunisia", result.getAddress());
        verify(studentRepository, times(1)).save(studentToUpdate);
    }
}