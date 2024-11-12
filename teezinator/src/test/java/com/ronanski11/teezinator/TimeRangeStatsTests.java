package com.ronanski11.teezinator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.ronanski11.teezinator.model.ConsumedTea;
import com.ronanski11.teezinator.model.Tea;
import com.ronanski11.teezinator.repository.ConsumedTeaRepository;
import com.ronanski11.teezinator.repository.TeaRepository;
import com.ronanski11.teezinator.service.StatsService;

@ActiveProfiles("test")
@SpringBootTest
public class TimeRangeStatsTests {

	@Mock
	private TeaRepository teaRepository;

	@Mock
	private ConsumedTeaRepository consumedTeaRepository;

	@InjectMocks
	private StatsService statsService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void test_ValidInput() {
		LocalDateTime date1 = LocalDateTime.of(2024, 11, 1, 0, 0);
		LocalDateTime date2 = LocalDateTime.of(2024, 11, 30, 23, 59);

		Tea tea = new Tea();
		tea.setId("1");
		tea.setName("Apfel");

		ConsumedTea ctea1 = new ConsumedTea();
		ctea1.setTime(LocalDateTime.of(2024, 11, 10, 12, 0));

		ctea1.setTea(tea);

		ConsumedTea ctea2 = new ConsumedTea();
		ctea2.setTime(LocalDateTime.of(2024, 11, 20, 14, 0));
		ctea2.setTea(tea);

		when(consumedTeaRepository.findByUser("testUser")).thenReturn(List.of(ctea1, ctea2));

		Map<String, Integer> stats = statsService.getUserStatsBetweenDates("testUser", date1, date2);

		assertEquals(1, stats.size());
		assertEquals(2, stats.get("1"));
	}

	@Test
	public void test_Date1AfterDate2() {
		LocalDateTime date1 = LocalDateTime.of(2024, 12, 1, 0, 0);
		LocalDateTime date2 = LocalDateTime.of(2024, 11, 1, 0, 0);

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> statsService.getUserStatsBetweenDates("testUser", date1, date2));
		assertEquals("Das Startdatum darf nicht nach dem Enddatum liegen", exception.getMessage());
	}

	@Test
	public void test_UserNotFound() {
		LocalDateTime date1 = LocalDateTime.of(2024, 11, 1, 0, 0);
		LocalDateTime date2 = LocalDateTime.of(2024, 11, 30, 23, 59);

		when(consumedTeaRepository.findByUser("nonExistentUser")).thenReturn(Collections.emptyList());

		NoSuchElementException exception = assertThrows(NoSuchElementException.class,
				() -> statsService.getUserStatsBetweenDates("nonExistentUser", date1, date2));
		assertEquals("Der Benutzer existiert nicht", exception.getMessage());
	}

	@Test
	public void test_NullDate1() {
		LocalDateTime date2 = LocalDateTime.of(2024, 11, 30, 23, 59);

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> statsService.getUserStatsBetweenDates("testUser", null, date2));
		assertEquals("Beide Datumswerte müssen gesetzt sein", exception.getMessage());
	}

	@Test
	public void test_NullDate2() {
		LocalDateTime date1 = LocalDateTime.of(2024, 11, 1, 0, 0);

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> statsService.getUserStatsBetweenDates("testUser", date1, null));
		assertEquals("Beide Datumswerte müssen gesetzt sein", exception.getMessage());
	}

	@Test
	public void test_NoActivityInRange() {
		LocalDateTime date1 = LocalDateTime.of(2024, 11, 1, 0, 0);
		LocalDateTime date2 = LocalDateTime.of(2024, 11, 30, 23, 59);

		ConsumedTea tea1 = new ConsumedTea();
		tea1.setTime(LocalDateTime.of(2024, 10, 10, 12, 0));
		tea1.setTea(new Tea());

		when(consumedTeaRepository.findByUser("testUser")).thenReturn(List.of(tea1));

		Map<String, Integer> stats = statsService.getUserStatsBetweenDates("testUser", date1, date2);

		assertTrue(stats.isEmpty());
	}

	@Test
	public void test_ExactSameDate() {
		LocalDateTime date = LocalDateTime.of(2024, 11, 12, 12, 0);

		Tea tea = new Tea();
		tea.setId("1");
		tea.setName("Kamille");

		ConsumedTea ctea = new ConsumedTea();
		ctea.setTime(date);
		ctea.setTea(tea);

		when(consumedTeaRepository.findByUser("testUser")).thenReturn(List.of(ctea));

		Map<String, Integer> stats = statsService.getUserStatsBetweenDates("testUser", date, date);

		assertEquals(1, stats.get("1"));
	}

}
