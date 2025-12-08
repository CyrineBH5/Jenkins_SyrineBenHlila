package tn.esprit.studentmanagement.services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.repositories.DepartmentRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private Department department1;
    private Department department2;

    @BeforeEach
    void setUp() {
        department1 = Department.builder()
                .idDepartment(1L)
                .name("Informatique")
                .location("Bâtiment A")
                .phone("71234567")
                .head("Dr. Ahmed Ben Ali")
                .build();

        department2 = Department.builder()
                .idDepartment(2L)
                .name("Mathématiques")
                .location("Bâtiment B")
                .phone("71234568")
                .head("Dr. Fatima Zohra")
                .build();
    }

    @Test
    void testGetAllDepartments_ShouldReturnList() {
        // Arrange
        List<Department> expectedDepartments = Arrays.asList(department1, department2);
        when(departmentRepository.findAll()).thenReturn(expectedDepartments);

        // Act
        List<Department> result = departmentService.getAllDepartments();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Informatique", result.get(0).getName());
        assertEquals("Mathématiques", result.get(1).getName());
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void testGetDepartmentById_WhenExists_ShouldReturnDepartment() {
        // Arrange
        Long departmentId = 1L;
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department1));

        // Act
        Department result = departmentService.getDepartmentById(departmentId);

        // Assert
        assertNotNull(result);
        assertEquals(departmentId, result.getIdDepartment());
        assertEquals("Informatique", result.getName());
        assertEquals("Dr. Ahmed Ben Ali", result.getHead());
        verify(departmentRepository, times(1)).findById(departmentId);
    }

    @Test
    void testGetDepartmentById_WhenNotExists_ShouldThrowException() {
        // Arrange
        Long departmentId = 99L;
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> departmentService.getDepartmentById(departmentId)
        );

        assertEquals("Department not found with id: " + departmentId, exception.getMessage());
        verify(departmentRepository, times(1)).findById(departmentId);
    }

    @Test
    void testSaveDepartment_ShouldReturnSavedDepartment() {
        // Arrange
        Department newDepartment = Department.builder()
                .name("Physique")
                .location("Bâtiment C")
                .phone("71234569")
                .head("Dr. Karim Saadi")
                .build();

        Department savedDepartment = Department.builder()
                .idDepartment(3L)
                .name("Physique")
                .location("Bâtiment C")
                .phone("71234569")
                .head("Dr. Karim Saadi")
                .build();

        when(departmentRepository.save(any(Department.class))).thenReturn(savedDepartment);

        // Act
        Department result = departmentService.saveDepartment(newDepartment);

        // Assert
        assertNotNull(result);
        assertEquals(3L, result.getIdDepartment());
        assertEquals("Physique", result.getName());
        assertEquals("Dr. Karim Saadi", result.getHead());
        verify(departmentRepository, times(1)).save(newDepartment);
    }

    @Test
    void testDeleteDepartment_ShouldCallRepository() {
        // Arrange
        Long departmentId = 1L;
        doNothing().when(departmentRepository).deleteById(departmentId);

        // Act
        departmentService.deleteDepartment(departmentId);

        // Assert
        verify(departmentRepository, times(1)).deleteById(departmentId);
    }
}