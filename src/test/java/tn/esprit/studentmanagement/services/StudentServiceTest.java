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

    // Test 1: Récupérer tous les étudiants
    @Test
    void testGetAllStudents_ShouldReturnList() {
        // Arrange
        List<Student> expectedStudents = Arrays.asList(student1, student2);
        when(studentRepository.findAll()).thenReturn(expectedStudents);

        // Act
        List<Student> result = studentService.getAllStudents();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Cyrine", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        verify(studentRepository, times(1)).findAll();
    }

    // Test 2: Récupérer un étudiant par ID - Trouvé
    @Test
    void testGetStudentById_WhenExists_ShouldReturnStudent() {
        // Arrange
        Long studentId = 1L;
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student1));

        // Act
        Student result = studentService.getStudentById(studentId);

        // Assert
        assertNotNull(result);
        assertEquals(studentId, result.getIdStudent());
        assertEquals("Cyrine", result.getFirstName());
        verify(studentRepository, times(1)).findById(studentId);
    }

    // Test 3: Récupérer un étudiant par ID - Non trouvé
    @Test
    void testGetStudentById_WhenNotExists_ShouldReturnNull() {
        // Arrange
        Long studentId = 99L;
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // Act
        Student result = studentService.getStudentById(studentId);

        // Assert
        assertNull(result);
        verify(studentRepository, times(1)).findById(studentId);
    }

    // Test 4: Sauvegarder un nouvel étudiant
    @Test
    void testSaveStudent_ShouldReturnSavedStudent() {
        // Arrange
        Student newStudent = Student.builder()
                .firstName("Alice")
                .lastName("Johnson")
                .email("alice@example.com")
                .phone("98765432")
                .dateOfBirth(LocalDate.of(2001, 3, 10))
                .address("Paris, France")
                .build();

        Student savedStudent = Student.builder()
                .idStudent(3L)
                .firstName("Alice")
                .lastName("Johnson")
                .email("alice@example.com")
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
        verify(studentRepository, times(1)).save(newStudent);
    }

    // Test 5: Supprimer un étudiant
    @Test
    void testDeleteStudent_ShouldCallRepository() {
        // Arrange
        Long studentId = 1L;
        doNothing().when(studentRepository).deleteById(studentId);

        // Act
        studentService.deleteStudent(studentId);

        // Assert
        verify(studentRepository, times(1)).deleteById(studentId);
    }

    // Test 6: Liste vide d'étudiants
    @Test
    void testGetAllStudents_WhenEmpty_ShouldReturnEmptyList() {
        // Arrange
        when(studentRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Student> result = studentService.getAllStudents();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(studentRepository, times(1)).findAll();
    }

    // Test 7: Mettre à jour un étudiant existant
    @Test
    void testUpdateStudent_ShouldReturnUpdatedStudent() {
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
        verify(studentRepository, times(1)).save(existingStudent);
    }

    // Test 8: Test edge case - ID très grand
    @Test
    void testGetStudentById_WithLargeId_ShouldReturnNull() {
        // Arrange
        when(studentRepository.findById(999999L)).thenReturn(Optional.empty());

        // Act
        Student result = studentService.getStudentById(999999L);

        // Assert
        assertNull(result);
        verify(studentRepository, times(1)).findById(999999L);
    }

    // Test 9: Vérification des propriétés après sauvegarde
    @Test
    void testSaveStudent_VerifyProperties() {
        // Arrange
        Student newStudent = Student.builder()
                .firstName("Test")
                .lastName("User")
                .email("test@test.com")
                .phone("55555555")
                .dateOfBirth(LocalDate.now())
                .address("Test Address")
                .build();

        Student savedStudent = Student.builder()
                .idStudent(10L)
                .firstName("Test")
                .lastName("User")
                .email("test@test.com")
                .phone("55555555")
                .dateOfBirth(LocalDate.now())
                .address("Test Address")
                .build();

        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

        // Act
        Student result = studentService.saveStudent(newStudent);

        // Assert
        assertAll(
                () -> assertEquals(10L, result.getIdStudent()),
                () -> assertEquals("Test", result.getFirstName()),
                () -> assertEquals("User", result.getLastName()),
                () -> assertEquals("test@test.com", result.getEmail()),
                () -> assertEquals("55555555", result.getPhone()),
                () -> assertEquals("Test Address", result.getAddress())
        );
        verify(studentRepository, times(1)).save(newStudent);
    }

    // Test 10: Vérifier qu'aucune interaction non désirée
    @Test
    void testGetAllStudents_NoUnwantedInteractions() {
        // Arrange
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student1));

        // Act
        List<Student> result = studentService.getAllStudents();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        // Vérifie que seules les méthodes attendues sont appelées
        verify(studentRepository, only()).findAll();
        verifyNoMoreInteractions(studentRepository);
    }
}