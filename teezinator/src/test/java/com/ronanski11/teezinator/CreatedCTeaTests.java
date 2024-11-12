package com.ronanski11.teezinator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.ronanski11.teezinator.dto.ConsumedTeaDto;
import com.ronanski11.teezinator.model.ConsumedTea;
import com.ronanski11.teezinator.model.EntryType;
import com.ronanski11.teezinator.model.Tea;
import com.ronanski11.teezinator.repository.ConsumedTeaRepository;
import com.ronanski11.teezinator.repository.ImageRepository;
import com.ronanski11.teezinator.repository.TeaRepository;
import com.ronanski11.teezinator.service.TeaService;

@SpringBootTest
public class CreatedCTeaTests {
	
	@Mock
    private TeaRepository teaRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ConsumedTeaRepository consumedTeaRepository;

    @InjectMocks
    private TeaService teaService;
    
    @BeforeEach
    public void setUp() {
    	MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testAddTea_AllDataValid() {
        ConsumedTeaDto coDto = new ConsumedTeaDto();
        coDto.setTeaId("tea123");
        coDto.setTimeOfConsumption("2024-11-12T12:30:00+00:00");
        coDto.setImage("image123");
        coDto.setImagePreview("previewImage");
        coDto.setSugar(true);
        coDto.setType(EntryType.REGULAR);

        Tea tea = new Tea();
        when(teaRepository.findById("tea123")).thenReturn(Optional.of(tea));

        ConsumedTea consumedTea = new ConsumedTea();
        consumedTea.setId("consumedTeaId");
        when(consumedTeaRepository.save(any(ConsumedTea.class))).thenReturn(consumedTea);

        ConsumedTea result = teaService.addTea(coDto, "testUser");

        assertNotNull(result);
        assertEquals("testUser", result.getUser());
        assertEquals(true, result.isSugar());
        assertEquals(tea, result.getTea());
        assertEquals("image123", result.getImage());
        assertEquals("previewImage", result.getPreview());
        assertEquals(EntryType.REGULAR, result.getType());
        assertEquals(LocalDateTime.parse("2024-11-12T12:30:00"), result.getTime());
    }

    @Test
    public void testAddTea_TeaNotFound() {
        ConsumedTeaDto coDto = new ConsumedTeaDto();
        coDto.setTeaId("invalidTeaId");

        when(teaRepository.findById("invalidTeaId")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> teaService.addTea(coDto, "testUser"));
    }
    
    @Test
    public void testAddTea_TimeIsNull() {
        ConsumedTeaDto coDto = new ConsumedTeaDto();
        coDto.setTeaId("tea123");

        Tea tea = new Tea();
        when(teaRepository.findById("tea123")).thenReturn(Optional.of(tea));

        ConsumedTea consumedTea = new ConsumedTea();
        when(consumedTeaRepository.save(any(ConsumedTea.class))).thenReturn(consumedTea);

        ConsumedTea result = teaService.addTea(coDto, "testUser");

        assertNotNull(result.getTime());
    }

    @Test
    public void testAddTea_ImageIsNull() {
        ConsumedTeaDto coDto = new ConsumedTeaDto();
        coDto.setTeaId("tea123");
        coDto.setImage(null);
        coDto.setImagePreview(null);

        Tea tea = new Tea();
        when(teaRepository.findById("tea123")).thenReturn(Optional.of(tea));

        ConsumedTea consumedTea = new ConsumedTea();
        when(consumedTeaRepository.save(any(ConsumedTea.class))).thenReturn(consumedTea);

        ConsumedTea result = teaService.addTea(coDto, "testUser");

        assertNull(result.getImage());
        assertNull(result.getPreview());
    }

    @Test
    public void testAddTea_InvalidTimeFormat() {
        ConsumedTeaDto coDto = new ConsumedTeaDto();
        coDto.setTeaId("tea123");
        coDto.setTimeOfConsumption("invalid-time-format");

        Tea tea = new Tea();
        when(teaRepository.findById("tea123")).thenReturn(Optional.of(tea));

        assertThrows(DateTimeParseException.class, () -> teaService.addTea(coDto, "testUser"));
    }
    
    @Test
    public void testAddTea_UsernameIsNull() {
        ConsumedTeaDto coDto = new ConsumedTeaDto();
        coDto.setTeaId("tea123");

        Tea tea = new Tea();
        when(teaRepository.findById("tea123")).thenReturn(Optional.of(tea));

        ConsumedTea consumedTea = new ConsumedTea();
        when(consumedTeaRepository.save(any(ConsumedTea.class))).thenReturn(consumedTea);

        ConsumedTea result = teaService.addTea(coDto, null);

        assertNull(result.getUser());
    }

    @Test
    public void testAddTea_SugarBoundaryValues() {
        ConsumedTeaDto coDto = new ConsumedTeaDto();
        coDto.setTeaId("tea123");
        coDto.setSugar(true);

        Tea tea = new Tea();
        when(teaRepository.findById("tea123")).thenReturn(Optional.of(tea));

        ConsumedTea consumedTea = new ConsumedTea();
        when(consumedTeaRepository.save(any(ConsumedTea.class))).thenReturn(consumedTea);

        ConsumedTea result = teaService.addTea(coDto, "testUser");

        assertTrue(result.isSugar());

        coDto.setSugar(false);
        result = teaService.addTea(coDto, "testUser");

        assertFalse(result.isSugar());
    }

}