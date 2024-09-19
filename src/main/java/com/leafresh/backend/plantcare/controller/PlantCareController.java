package com.leafresh.backend.plantcare.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.leafresh.backend.plantcare.model.PlantCareDTO;
import com.leafresh.backend.plantcare.model.PlantCareEntity;
import com.leafresh.backend.plantcare.service.PlantCareService;

@RestController
@RequestMapping("/garden-diary/plant-care")
public class PlantCareController {


	private PlantCareService plantCareService;

	@Autowired
	public PlantCareController(PlantCareService plantCareService) {
		this.plantCareService = plantCareService;
	}

	// plantCare 를 저장하거나 update 하기

	@PostMapping("/save")
	public ResponseEntity<Map<String, Object>> saveOrUpdatePlantCare(@RequestBody PlantCareDTO plantCareDTO) {

		try{
			plantCareService.saveOrUpdatePlantCare(plantCareDTO);

			Map<String, Object> response = new HashMap<>();
			response.put("message", "저장완료요~");
			response.put("data", plantCareDTO);
			return ResponseEntity.ok(response);
		}catch(ResponseStatusException e){

			// 에러발생 처리
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("error", e.getMessage());

			return ResponseEntity.status(e.getStatusCode()).body(errorResponse);

		}

	}


	// userId별로 이벤트를 반환하도록 수정
	@GetMapping("/events")
	public ResponseEntity<Map<String, Object>> getPlantCareEventsByUserId(@RequestParam Integer userId) {
		// userId로 필터링해서 이벤트 가져오기
		List<PlantCareEntity> events = plantCareService.getEventsByUserId(userId);

		Map<String, Object> response = new HashMap<>();
		response.put("data", events);

		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Map<String, String>> deletePlantCareEvent(@RequestParam Integer userId, @RequestParam String eventDate) {
		System.out.println("Delete event request received for userId: " + userId + ", eventDate: " + eventDate);
		try{
			boolean deleted = plantCareService.deleteEventByUserIdAndDate(userId, eventDate);

			if (deleted) {
				Map<String, String> response = new HashMap<>();
				response.put("message", "이벤트가 삭제되었습니다.");
				return ResponseEntity.ok(response);
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "이벤트를 찾을 수 없습니다.");
			}


		}catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이벤트 삭제 중 오류가 발생했습니다.");


		}



	}






}
