package com.ronanski11.teezinator;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.ronanski11.teezinator.model.ConsumedTea;
import com.ronanski11.teezinator.model.EntryType;
import com.ronanski11.teezinator.model.Tea;
import com.ronanski11.teezinator.repository.ConsumedTeaRepository;
import com.ronanski11.teezinator.service.StatsService;

@ActiveProfiles("test")
@SpringBootTest
public class StatsTests {

	@Mock
	private ConsumedTeaRepository consumedTeaRepository;

	@InjectMocks
	private StatsService statsService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	   @Test
	    void testGetTotalStatsByUser_WithNoConsumedTea() {
	        String username = "testUser";
	        when(consumedTeaRepository.findByUser(username)).thenReturn(Arrays.asList());

	        Map<String, Integer> stats = statsService.getTotalStatsByUser(username);

	        assertTrue(stats.isEmpty(), "Stats should be empty when no teas are consumed.");
	    }
	   
	    @Test
	    void testGetTotalStatsByUser_WithMultipleConsumedTeas() {
	    	Tea tea1 = new Tea();
	    	tea1.setId("1");
	    	tea1.setName("Maracuja Orange");
	    	
	    	Tea tea2 = new Tea();
	    	tea2.setId("2");
	    	tea2.setName("Tulsi Relax");
	    	
	        String username = "testUser";
	        ConsumedTea cTea1 = new ConsumedTea("1", tea1, username, "", "", null, false, EntryType.REGULAR);
	        ConsumedTea cTea2 = new ConsumedTea("2", tea1, username, "", "", null, false, EntryType.REGULAR);
	        ConsumedTea cTea3 = new ConsumedTea("3", tea2, username, "", "", null, false, EntryType.REGULAR);

	        when(consumedTeaRepository.findByUser(username)).thenReturn(Arrays.asList(cTea1, cTea2, cTea3));

	        Map<String, Integer> stats = statsService.getTotalStatsByUser(username);

	        assertAll("stats",
	            () -> assertEquals(2, stats.size(), "There should be two types of tea."),
	            () -> assertEquals(2, stats.get("1").intValue(), "Green Tea should be counted twice."),
	            () -> assertEquals(1, stats.get("2").intValue(), "Black Tea should be counted once.")
	        );
	    }

}
