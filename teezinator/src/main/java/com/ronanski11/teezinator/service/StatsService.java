package com.ronanski11.teezinator.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.ronanski11.teezinator.model.ConsumedTea;
import com.ronanski11.teezinator.model.User;
import com.ronanski11.teezinator.repository.ConsumedTeaRepository;
import com.ronanski11.teezinator.repository.ImageRepository;
import com.ronanski11.teezinator.repository.TeaRepository;
import com.ronanski11.teezinator.repository.UserRepository;

@Service
public class StatsService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ConsumedTeaRepository consumedTeaRepository;

	@Autowired
	TeaRepository teaRepository;

	@Autowired
	ImageRepository imageRepository;

	@Autowired
	MongoTemplate mongoTemplate;

	DateTimeFormatter weekOfYearFormatter = DateTimeFormatter.ofPattern("w");

	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	public Map<String, Integer> getTotalStatsByUser(String username) {

		Map<String, Integer> stats = new HashMap<String, Integer>();
		List<ConsumedTea> consumedTeas = consumedTeaRepository.findByUser(username);

		for (ConsumedTea consumedTea : consumedTeas) {
			if (!stats.containsKey(consumedTea.getTea().getId())) {
				stats.put(consumedTea.getTea().getId(), 1);
			} else {
				stats.put(consumedTea.getTea().getId(), stats.get(consumedTea.getTea().getId()) + 1);
			}
		}

		return stats;
	}

	public Map<String, Map<String, Integer>> getWeeklyStatsByUser(String username) {
		Map<String, Map<String, Integer>> stats = new HashMap<String, Map<String, Integer>>();
		List<ConsumedTea> consumedTeas = consumedTeaRepository.findByUser(username);
		for (ConsumedTea consumedTea : consumedTeas) {
			String teaId = consumedTea.getTea().getId();

			if (!stats.containsKey(formatWeeklyKey(consumedTea))) {
				Map<String, Integer> map = new HashMap<String, Integer>();
				map.put(teaId, 1);
				stats.put(formatWeeklyKey(consumedTea), map);
			} else if (!stats.get(formatWeeklyKey(consumedTea)).containsKey(teaId)) {
				Map<String, Integer> map = stats.get(formatWeeklyKey(consumedTea));
				map.put(teaId, 1);
				stats.put(formatWeeklyKey(consumedTea), map);
			} else {
				Map<String, Integer> map = stats.get(formatWeeklyKey(consumedTea));
				map.put(teaId, map.get(teaId) + 1);
				stats.put(formatWeeklyKey(consumedTea), map);
			}

		}
		return stats;

	}

	public Map<String, Map<String, Integer>> getDailyStatsByUser(String username) {
		Map<String, Map<String, Integer>> stats = new HashMap<String, Map<String, Integer>>();
		List<ConsumedTea> consumedTeas = consumedTeaRepository.findByUser(username);
		for (ConsumedTea consumedTea : consumedTeas) {
			String teaId = consumedTea.getTea().getId();

			if (!stats.containsKey(formatDailyKey(consumedTea))) {
				Map<String, Integer> map = new HashMap<String, Integer>();
				map.put(teaId, 1);
				stats.put(formatDailyKey(consumedTea), map);
			} else if (!stats.get(formatDailyKey(consumedTea)).containsKey(teaId)) {
				Map<String, Integer> map = stats.get(formatDailyKey(consumedTea));
				map.put(teaId, 1);
				stats.put(formatDailyKey(consumedTea), map);
			} else {
				Map<String, Integer> map = stats.get(formatDailyKey(consumedTea));
				map.put(teaId, map.get(teaId) + 1);
				stats.put(formatDailyKey(consumedTea), map);
			}
		}
		return stats;
	}

	public String formatDailyKey(ConsumedTea consumedTea) {
		return consumedTea.getTime().format(dateFormatter);
	}

	public String formatWeeklyKey(ConsumedTea consumedTea) {
		return String.format("%s-%s", consumedTea.getTime().format(weekOfYearFormatter),
				consumedTea.getTime().getYear());
	}

	public List<LocalDate> getStartAndEndDates(String weekKey) {
		String[] parts = weekKey.split("-");
		int week = Integer.parseInt(parts[0]);
		int year = Integer.parseInt(parts[1]);

		LocalDate startDate = LocalDate.now().withYear(year)
				.with(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear(), week)
				.with(TemporalAdjusters.previousOrSame(WeekFields.of(Locale.getDefault()).getFirstDayOfWeek()));

		LocalDate endDate = startDate.plusWeeks(1);

		return Arrays.asList(startDate, endDate);
	}

	public Map<String, Integer> getTopTeasByUser(String username) {

		return getTotalStatsByUser(username).entrySet().stream()
				.sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).limit(3)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

	public Map<String, Integer> getTotalWeeklyStatsByUser(String username) {
		Map<String, Integer> stats = new HashMap<String, Integer>();
		List<ConsumedTea> consumedTeas = consumedTeaRepository.findByUser(username);

		for (ConsumedTea consumedTea : consumedTeas) {
			if (!stats.containsKey(formatWeeklyKey(consumedTea))) {
				stats.put(formatWeeklyKey(consumedTea), 1);
			} else {
				stats.put(formatWeeklyKey(consumedTea), stats.get(formatWeeklyKey(consumedTea)) + 1);
			}
		}

		return sortMapByWeekAndYearDescending(stats);
	}

	public Map<String, Integer> getTotalDailyStatsByUser(String username) {
		Map<String, Integer> stats = new HashMap<String, Integer>();
		List<ConsumedTea> consumedTeas = consumedTeaRepository.findByUser(username);

		for (ConsumedTea consumedTea : consumedTeas) {
			if (!stats.containsKey(formatDailyKey(consumedTea))) {
				stats.put(formatDailyKey(consumedTea), 1);
			} else {
				stats.put(formatDailyKey(consumedTea), stats.get(formatDailyKey(consumedTea)) + 1);
			}
		}

		return sortMapByDateDescending(stats);
	}

	private Map<String, Integer> sortMapByWeekAndYearDescending(Map<String, Integer> unsortedStats) {
		List<Map.Entry<String, Integer>> entries = new ArrayList<>(unsortedStats.entrySet());

		Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				String[] weekYear1 = o1.getKey().split("-");
				String[] weekYear2 = o2.getKey().split("-");

				int year1 = Integer.parseInt(weekYear1[1]);
				int year2 = Integer.parseInt(weekYear2[1]);
				int yearCompare = Integer.compare(year2, year1);

				if (yearCompare == 0) {
					int week1 = Integer.parseInt(weekYear1[0]);
					int week2 = Integer.parseInt(weekYear2[0]);
					return Integer.compare(week2, week1);
				}
				return yearCompare;
			}
		});

		Map<String, Integer> sortedStats = new LinkedHashMap<>();
		for (Map.Entry<String, Integer> entry : entries) {
			sortedStats.put(entry.getKey(), entry.getValue());
		}

		return sortedStats;
	}

	private Map<String, Integer> sortMapByDateDescending(Map<String, Integer> unsortedStats) {
		List<Map.Entry<String, Integer>> entries = new ArrayList<>(unsortedStats.entrySet());

		Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
			private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

			@Override
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				try {
					return dateFormat.parse(o2.getKey()).compareTo(dateFormat.parse(o1.getKey()));
				} catch (ParseException e) {
					e.printStackTrace();
					return 0;
				}
			}
		});

		Map<String, Integer> sortedStats = new LinkedHashMap<>();
		for (Map.Entry<String, Integer> entry : entries) {
			sortedStats.put(entry.getKey(), entry.getValue());
		}

		return sortedStats;
	}

	public Map<String, Integer> getLifetimeLeaderboard() {

		Map<String, Integer> stats = new HashMap<String, Integer>();
		for (User user : userRepository.findAll()) {
			stats.put(user.getUsername(), consumedTeaRepository.findByUser(user.getUsername()).size());
		}

		Map<String, Integer> sortedStats = stats.entrySet().stream()
				.sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).collect(Collectors.toMap(
						Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

		return sortedStats;
	}

	public List<ConsumedTea> getConsumedTeasByUserAndWeek(String username, String week, boolean sort) {
		List<LocalDate> startAndEndDates = getStartAndEndDates(week);
		LocalDate startDate = startAndEndDates.get(0);
		LocalDate endDate = startAndEndDates.get(1);

		MatchOperation matchUser = Aggregation.match(Criteria.where("user").is(username));
		MatchOperation matchTime = Aggregation
				.match(Criteria.where("time").gte(startDate.atStartOfDay()).lte(endDate.atTime(23, 59, 59)));

		Aggregation aggregation;
		if (sort) {
			// Include the sorting operation in your aggregation pipeline
			aggregation = Aggregation.newAggregation(matchUser, matchTime,
					Aggregation.sort(Sort.Direction.ASC, "time"));
		} else {
			aggregation = Aggregation.newAggregation(matchUser, matchTime);
		}

		AggregationResults<ConsumedTea> results = mongoTemplate.aggregate(aggregation, "consumedTea",
				ConsumedTea.class);

		return results.getMappedResults();
	}

	public Map<String, Integer> getByDay(String username, String day) {
		Map<String, Integer> stats = new HashMap<String, Integer>();
		List<ConsumedTea> consumedTeas = findConsumedTeasByDay(username, day);

		for (ConsumedTea consumedTea : consumedTeas) {
			if (!stats.containsKey(consumedTea.getTea().getId())) {
				stats.put(consumedTea.getTea().getId(), 1);
			} else {
				stats.put(consumedTea.getTea().getId(), stats.get(consumedTea.getTea().getId()) + 1);
			}
		}

		return stats;

	}

	private List<ConsumedTea> findConsumedTeasByDay(String username, String day) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate date = LocalDate.parse(day, formatter);

		LocalDateTime startOfDay = date.atStartOfDay();
		LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

		MatchOperation matchUser = Aggregation.match(Criteria.where("user").is(username));
		MatchOperation matchTime = Aggregation.match(Criteria.where("time").gte(startOfDay).lt(endOfDay));

		Aggregation aggregation = Aggregation.newAggregation(matchUser, matchTime);

		AggregationResults<ConsumedTea> results = mongoTemplate.aggregate(aggregation, "consumedTea",
				ConsumedTea.class);

		return results.getMappedResults();
	}

	public Map<String, Map<String, Integer>> getByWeek(String username, String week) {
		Map<String, Map<String, Integer>> stats = new HashMap<String, Map<String, Integer>>();

		List<ConsumedTea> consumedTeas = getConsumedTeasByUserAndWeek(username, week, true);

		for (ConsumedTea consumedTea : consumedTeas) {
			String teaId = consumedTea.getTea().getId();

			if (!stats.containsKey(formatDailyKey(consumedTea))) {
				Map<String, Integer> map = new HashMap<String, Integer>();
				map.put(teaId, 1);
				stats.put(formatDailyKey(consumedTea), map);
			} else if (!stats.get(formatDailyKey(consumedTea)).containsKey(teaId)) {
				Map<String, Integer> map = stats.get(formatDailyKey(consumedTea));
				map.put(teaId, 1);
				stats.put(formatDailyKey(consumedTea), map);
			} else {
				Map<String, Integer> map = stats.get(formatDailyKey(consumedTea));
				map.put(teaId, map.get(teaId) + 1);
				stats.put(formatDailyKey(consumedTea), map);
			}
		}

		return sortByDateAscending(stats);
	}

	public Map<String, Map<String, Integer>> sortByDateAscending(Map<String, Map<String, Integer>> stats) {
		Map<String, Map<String, Integer>> sortedMap = stats.entrySet().stream()
				.sorted(Map.Entry.comparingByKey(Comparator.comparing(key -> LocalDate.parse(key, dateFormatter))))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue,
						LinkedHashMap::new));

		return sortedMap;
	}

	public String getImageById(String imageId) {
		return imageRepository.findById(imageId).get().getBaseString();
	}

	public List<ConsumedTea> getConsumedTeasByUserAndDay(String username, String day) {

		LocalDate date = LocalDate.parse(day, dateFormatter);

		LocalDateTime startOfDay = date.atStartOfDay();
		LocalDateTime endOfDay = date.plusDays(1).atStartOfDay().minusSeconds(1);

		MatchOperation matchUser = Aggregation.match(Criteria.where("user").is(username));
		MatchOperation matchTime = Aggregation.match(Criteria.where("time").gte(startOfDay).lt(endOfDay));

		SortOperation sortByTimeAsc = Aggregation.sort(Sort.Direction.ASC, "time");

		// Include the sorting operation in your aggregation pipeline
		Aggregation aggregation = Aggregation.newAggregation(matchUser, matchTime, sortByTimeAsc);

		AggregationResults<ConsumedTea> results = mongoTemplate.aggregate(aggregation, "consumedTea",
				ConsumedTea.class);

		return results.getMappedResults();
	}

	public Map<String, Integer> getLeaderBoardByWeek(String week) {

		Map<String, Integer> stats = new HashMap<String, Integer>();
		for (User user : userRepository.findAll()) {
			stats.put(user.getUsername(), getConsumedTeasByUserAndWeek(user.getUsername(), week, false).size());
		}

		Map<String, Integer> sortedStats = stats.entrySet().stream()
				.sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).collect(Collectors.toMap(
						Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

		return sortedStats;

	}
	
	public List<ConsumedTea> getLiveFeed(int pageNumber) {
		return consumedTeaRepository.findLatestTeasPaging(pageNumber);
	}

}
