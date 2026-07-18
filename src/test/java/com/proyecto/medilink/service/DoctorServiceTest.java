package com.proyecto.medilink.service;

import com.proyecto.medilink.model.Doctor;
import com.proyecto.medilink.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private MultipartFile imageFile;

    @InjectMocks
    private DoctorService doctorService;

    private Doctor doctor;

    @BeforeEach
    void setUp() {
        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setNombre("Dr. Juan");
        doctor.setApellidos("Pérez");
        doctor.setEspecialidad("Cardiología");
        doctor.setNumeroColegiatura("12345");
        doctor.setEmail("juan@doctor.com");
        doctor.setTelefono("999999999");
        doctor.setImagen(null);  // ✅ SIN imagen por defecto
    }

    // ============================================================
    // 1. TEST GET ALL DOCTORES
    // ============================================================

    @Test
    void testGetAllDoctores_DevuelveLista() {
        List<Doctor> doctores = Arrays.asList(doctor, new Doctor());
        when(doctorRepository.findAll()).thenReturn(doctores);

        List<Doctor> resultado = doctorService.getAllDoctores();

        assertEquals(2, resultado.size());
        verify(doctorRepository).findAll();
    }

    @Test
    void testGetAllDoctores_ListaVacia() {
        when(doctorRepository.findAll()).thenReturn(Arrays.asList());

        List<Doctor> resultado = doctorService.getAllDoctores();

        assertTrue(resultado.isEmpty());
        verify(doctorRepository).findAll();
    }

    // ============================================================
    // 2. TEST GUARDAR DOCTOR (SIN IMAGEN)
    // ============================================================

    @Test
    void testGuardarDoctor_SinImagen_Exitoso() {
        when(doctorRepository.findByEmail(doctor.getEmail())).thenReturn(null);
        when(doctorRepository.save(doctor)).thenReturn(doctor);

        Doctor resultado = doctorService.guardarDoctor(doctor);

        assertNotNull(resultado);
        assertEquals(doctor.getEmail(), resultado.getEmail());
        verify(doctorRepository).findByEmail(doctor.getEmail());
        verify(doctorRepository).save(doctor);
    }

    @Test
    void testGuardarDoctor_SinImagen_EmailDuplicado_LanzaExcepcion() {
        when(doctorRepository.findByEmail(doctor.getEmail())).thenReturn(doctor);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> doctorService.guardarDoctor(doctor));

        assertEquals("El email ya está registrado", exception.getMessage());
        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    // ============================================================
    // 3. TEST GUARDAR DOCTOR (CON IMAGEN)
    // ============================================================

    @Test
    void testGuardarDoctor_ConImagen_Exitoso() throws IOException {
        // Mock imagen
        byte[] imageBytes = "imagen".getBytes();
        when(imageFile.isEmpty()).thenReturn(false);
        when(imageFile.getOriginalFilename()).thenReturn("doctor.jpg");
        when(imageFile.getInputStream()).thenReturn(new ByteArrayInputStream(imageBytes));

        when(doctorRepository.findByEmail(doctor.getEmail())).thenReturn(null);
        when(doctorRepository.save(doctor)).thenReturn(doctor);

        Doctor resultado = doctorService.guardarDoctor(doctor, imageFile);

        assertNotNull(resultado);
        assertNotNull(resultado.getImagen());
        assertTrue(resultado.getImagen().endsWith("_doctor.jpg"));
        verify(doctorRepository).save(doctor);
    }

    @Test
    void testGuardarDoctor_ConImagen_EmailDuplicado_LanzaExcepcion() throws IOException {
        when(doctorRepository.findByEmail(doctor.getEmail())).thenReturn(doctor);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> doctorService.guardarDoctor(doctor, imageFile));

        assertEquals("El email ya está registrado", exception.getMessage());
        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    @Test
    void testGuardarDoctor_ConImagen_SinArchivo_GuardaSinImagen() throws IOException {
        when(imageFile.isEmpty()).thenReturn(true);
        when(doctorRepository.findByEmail(doctor.getEmail())).thenReturn(null);
        when(doctorRepository.save(doctor)).thenReturn(doctor);

        Doctor resultado = doctorService.guardarDoctor(doctor, imageFile);

        assertNotNull(resultado);
        // ✅ El código no modifica la imagen si no hay archivo
        assertNull(resultado.getImagen());
        verify(doctorRepository).save(doctor);
    }

    @Test
    void testGuardarDoctor_ConImagen_ConErrorIOException_LanzaExcepcion() throws IOException {
        when(imageFile.isEmpty()).thenReturn(false);
        when(imageFile.getOriginalFilename()).thenReturn("doctor.jpg");
        when(imageFile.getInputStream()).thenThrow(new IOException("Error de IO"));

        when(doctorRepository.findByEmail(doctor.getEmail())).thenReturn(null);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> doctorService.guardarDoctor(doctor, imageFile));

        assertTrue(exception.getMessage().contains("Error al guardar la imagen del doctor"));
        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    // ============================================================
    // 4. TEST ELIMINAR DOCTOR
    // ============================================================

    @Test
    void testEliminarDoctor_Exitoso() {
        Long doctorId = 1L;
        doNothing().when(doctorRepository).deleteById(doctorId);

        doctorService.eliminarDoctor(doctorId);

        verify(doctorRepository).deleteById(doctorId);
    }

    @Test
    void testEliminarDoctor_NoLanzaExcepcion() {
        Long doctorId = 999L;
        doNothing().when(doctorRepository).deleteById(doctorId);

        assertDoesNotThrow(() -> doctorService.eliminarDoctor(doctorId));
        verify(doctorRepository).deleteById(doctorId);
    }

    // ============================================================
    // 5. TEST FIND BY ID
    // ============================================================

    @Test
    void testFindById_DoctorExiste_DevuelveDoctor() {
        Long doctorId = 1L;
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));

        Doctor resultado = doctorService.findById(doctorId);

        assertNotNull(resultado);
        assertEquals(doctor.getId(), resultado.getId());
        verify(doctorRepository).findById(doctorId);
    }

    @Test
    void testFindById_DoctorNoExiste_DevuelveNull() {
        Long doctorId = 999L;
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());

        Doctor resultado = doctorService.findById(doctorId);

        assertNull(resultado);
        verify(doctorRepository).findById(doctorId);
    }
}
