package com.ronanski11.teezinator.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ronanski11.teezinator.model.ConsumedTea;
import com.ronanski11.teezinator.security.AuthenticationService;
import com.ronanski11.teezinator.service.StatsService;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

	@Autowired
	StatsService statsService;
	
	@Autowired
	AuthenticationService auth;

	@GetMapping("/getTotalStatsByUser")
	public ResponseEntity<Map<String, Integer>> getTotalStatsByUser() {
		return ResponseEntity.ok(this.statsService.getTotalStatsByUser(auth.getUsername()));
	}

	@GetMapping("/getWeeklyStatsByUser")
	public ResponseEntity<Map<String, Map<String, Integer>>> getWeeklyStatsByUser() {
		return ResponseEntity.ok(statsService.getWeeklyStatsByUser(auth.getUsername()));
	}
	
	@GetMapping("getTotalWeeklyStatsByUser")
	public ResponseEntity<Map<String, Integer>> getTotalWeeklyStatsByUser() {
		return ResponseEntity.ok(statsService.getTotalWeeklyStatsByUser(auth.getUsername()));
	}

	@GetMapping("/getDailyStatsByUser")
	public ResponseEntity<Map<String, Map<String, Integer>>> getDailyStatsByUser() {
		return ResponseEntity.ok(statsService.getDailyStatsByUser(auth.getUsername()));
	}
	
	@GetMapping("getTotalDailyStatsByUser")
	public ResponseEntity<Map<String, Integer>> getTotalDailyStatsByUser() {
		return ResponseEntity.ok(statsService.getTotalDailyStatsByUser(auth.getUsername()));
	}

	@GetMapping("/getTopTeasByUser")
	public ResponseEntity<Map<String, Integer>> getTopTeasByUser() {
		return ResponseEntity.ok(statsService.getTopTeasByUser(auth.getUsername()));
	}
	
	@GetMapping("getLifetimeLeaderboard")
	public ResponseEntity<Map<String, Integer>> getLifetimeLeaderboard() {
		return ResponseEntity.ok(statsService.getLifetimeLeaderboard());
	}
	
	@GetMapping("/weekly")
	public ResponseEntity<Map<String, Map<String, Integer>>> getByWeek(@RequestParam String week, @RequestParam(required = false) String username) {
		// return ResponseEntity.ok(statsService.getConsumedTeasByUserAndWeek(username, week));
		return ResponseEntity.ok(statsService.getByWeek(isUsernameNull(username), week));
	}
	
	@GetMapping("/getWeeklyInfo")
	public ResponseEntity<List<ConsumedTea>> getWeeklyInfo(@RequestParam String week, @RequestParam(required = false) String username) {
		return ResponseEntity.ok(statsService.getConsumedTeasByUserAndWeek(isUsernameNull(username), week, true));
	}
	
	@GetMapping("/getDailyInfo")
	public ResponseEntity<List<ConsumedTea>> getDailyInfo(@RequestParam String day, @RequestParam(required = false) String username) {
		return ResponseEntity.ok(statsService.getConsumedTeasByUserAndDay(isUsernameNull(username), day));
	}
	
	
	@GetMapping("/daily")
	public ResponseEntity<Map<String, Integer>> getByDay(@RequestParam String day, @RequestParam(required = false) String username) {
		return ResponseEntity.ok(statsService.getByDay(isUsernameNull(username), day));
	}
	
	@GetMapping("/getImageById")
	public ResponseEntity<String> getImageById(@RequestParam String imageId) {
		return ResponseEntity.ok(statsService.getImageById(imageId));
	}
	
	@GetMapping("/getLeaderboardByWeek")
	public ResponseEntity<Map<String, Integer>> getLeaderBoardByWeek(@RequestParam String week) {
		return ResponseEntity.ok(statsService.getLeaderBoardByWeek(week));
	}
	
	@GetMapping("/feed")
	public ResponseEntity<List<ConsumedTea>> getLiveFeed(@RequestParam(name = "page") int pageNumber) {
		return ResponseEntity.ok(statsService.getLiveFeed(pageNumber));
	}
	
	public String isUsernameNull(String username) {
		if (username == null) {
			return auth.getUsername();
		} else {
			return username;
		}
	}
}
