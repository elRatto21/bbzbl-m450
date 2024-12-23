package com.ronanski11.teezinator.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ronanski11.teezinator.dto.ConsumedTeaDto;
import com.ronanski11.teezinator.model.ConsumedTea;
import com.ronanski11.teezinator.model.Image;
import com.ronanski11.teezinator.model.Role;
import com.ronanski11.teezinator.model.Tea;
import com.ronanski11.teezinator.model.User;
import com.ronanski11.teezinator.repository.ConsumedTeaCrudRepository;
import com.ronanski11.teezinator.repository.ConsumedTeaRepository;
import com.ronanski11.teezinator.repository.ImageRepository;
import com.ronanski11.teezinator.repository.TeaRepository;
import com.ronanski11.teezinator.repository.UserRepository;

@Service
public class TeaService {

	@Autowired
	private TeaRepository teaRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ConsumedTeaRepository consumedTeaRepository;

	@Autowired
	ConsumedTeaCrudRepository consumedTeaCrudRepository;

	@Autowired
	ImageRepository imageRepository;

	public ConsumedTea addTea(ConsumedTeaDto coDto, String username) {
		ConsumedTea consumedTea = new ConsumedTea();
		if (coDto.getTimeOfConsumption() == null) {
			consumedTea.setTime(LocalDateTime.parse(coDto.getTimeOfConsumption().split("\\+")[0]));
		} else {
			consumedTea.setTime(LocalDateTime.now());
		}
		consumedTea.setTea(teaRepository.findById(coDto.getTeaId()).get());
		consumedTea.setUser(username);
		consumedTea.setSugar(coDto.getSugar());
		consumedTea.setType(coDto.getType());
		if (coDto.getImage() != null) {
			consumedTea.setImage(imageRepository.save(new Image(coDto.getImage())).getId());
			consumedTea.setPreview(coDto.getImagePreview());
		} else {
			consumedTea.setImage(null);
			consumedTea.setPreview(null);
		}
		return consumedTeaRepository.save(consumedTea);
	}

	public List<Tea> getAllTeas() {
		return teaRepository.findAll();
	}

	public Tea findById(String teaId) {
		return teaRepository.findById(teaId).get();
	}

	public ConsumedTea findCTeaById(String cTeaId) {
		return consumedTeaCrudRepository.findById(cTeaId).get();
	}

	public Role getUserRole(String username) {
		return userRepository.findByUsername(username).get().getRole();
	}

	public List<String> getAllUsernames() {
		List<String> usernames = new ArrayList<String>();
		for (User user : userRepository.findAll()) {
			usernames.add(user.getUsername());
		}
		return usernames;
	}

	public List<Tea> getMultipleById(String[] ids) {
		List<Tea> teas = new ArrayList<Tea>();
		for (String id : ids) {
			teas.add(teaRepository.findById(id).get());
		}
		return teas;
	}

	public ConsumedTea updateCTea(ConsumedTea cTea) {
		ConsumedTea existingCTea = consumedTeaCrudRepository.findById(cTea.getId())
				.orElseThrow(() -> new IllegalArgumentException("ConsumedTea with ID " + cTea.getId() + " not found"));

		if (cTea.getTime() != null) {
			existingCTea.setTime(cTea.getTime());
		}
		if (cTea.getTea() != null) {
			existingCTea.setTea(cTea.getTea());
		}
		if (cTea.getUser() != null) {
			existingCTea.setUser(cTea.getUser());
		}

		existingCTea.setSugar(cTea.isSugar());
		if (cTea.getType() != null) {
			existingCTea.setType(cTea.getType());
		}
		if (cTea.getImage() != null) {
			existingCTea.setImage(cTea.getImage());
		}
		if (cTea.getPreview() != null) {
			existingCTea.setPreview(cTea.getPreview());
		}

		return consumedTeaCrudRepository.save(existingCTea);
	}
	
	public void deleteCTeaById(String cTeaId) {
		consumedTeaCrudRepository.deleteById(cTeaId);
	}
	
	public void deleteTeaById(String teaId) {
		teaRepository.deleteById(teaId);
	}
}
