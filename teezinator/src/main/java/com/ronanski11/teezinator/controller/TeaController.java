package com.ronanski11.teezinator.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ronanski11.teezinator.dto.ConsumedTeaDto;
import com.ronanski11.teezinator.model.ConsumedTea;
import com.ronanski11.teezinator.model.Role;
import com.ronanski11.teezinator.model.Tea;
import com.ronanski11.teezinator.repository.TeaRepository;
import com.ronanski11.teezinator.security.AuthenticationService;
import com.ronanski11.teezinator.service.TeaService;

@RestController
@RequestMapping("/api/tea")
public class TeaController {

	@Autowired
	private TeaService teaService;

	@Autowired
	AuthenticationService auth;

	@Autowired
	TeaRepository t;

	@PostMapping()
	public ResponseEntity<?> addTea(@RequestBody ConsumedTeaDto consumedTeaDto) {
		String username = auth.getUsername();
		return new ResponseEntity<>("tea with id " + this.teaService.addTea(consumedTeaDto, username).getId() + " added", HttpStatus.OK);
	}

	@GetMapping("/getall")
	public List<Tea> helloWorld() {
		return teaService.getAllTeas();
	}

	@GetMapping("/getTea")
	public Tea getTeaById(@RequestParam(name = "teaId", required = true) String teaId) {
		return teaService.findById(teaId);
	}

	@GetMapping("/getUserRole")
	public ResponseEntity<Role> getUserRole() {
		return ResponseEntity.ok(teaService.getUserRole(auth.getUsername()));
	}

	@GetMapping("/getAllUsernames")
	public ResponseEntity<List<String>> getAllUsernames() {
		if (teaService.getUserRole(auth.getUsername()) != Role.ADMIN) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		} else {
			return ResponseEntity.ok(teaService.getAllUsernames());
		}
	}

	@GetMapping("getMultipleById")
	public ResponseEntity<List<Tea>> getMultipleById(@RequestParam String[] ids) {
		return ResponseEntity.ok(teaService.getMultipleById(ids));
	}

}
