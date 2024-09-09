package com.leafresh.backend.todo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leafresh.backend.todo.model.ToDoDTO;
import com.leafresh.backend.todo.model.ToDoEntity;
import com.leafresh.backend.todo.service.ToDoService;

@RestController
@CrossOrigin(origins = "http://localhost:8000")
@RequestMapping("/garden-diary/todo")
public class ToDoController {


	private final ToDoService toDoService;

	@Autowired
	public ToDoController(ToDoService toDoService) {
		this.toDoService = toDoService;
	}

	// 전체조회
	@GetMapping("/")
	public ResponseEntity<Map<String, Object>> getAllToDos() {
		Map<String, Object> map = new HashMap<>();

		List<ToDoDTO> toDoDTOList = toDoService.getAllToDos();
		if (toDoDTOList != null && !toDoDTOList.isEmpty()) {
			map.put("todoIndex", toDoDTOList);

			return ResponseEntity.ok(map);
		}else {
			map.put("error", "데이터가 없습니다.");
			return ResponseEntity.ok(map);
		}

	}

	@PostMapping("/create")
	public ResponseEntity<Map<String, Object>> insertToDo(@RequestBody ToDoDTO toDoDTO) {
		Map<String, Object> map = new HashMap<>();
		toDoService.todoSave(toDoDTO);
		map.put("todoIndex", toDoDTO);
		return ResponseEntity.ok(map);

	}


	// 오늘날짜 기준으로 보여준다.
	@GetMapping("/today")
	public ResponseEntity<Map<String, Object>> getTodayTodos() {
		Map<String, Object> map = new HashMap<>();
		List<ToDoEntity> todayTodos = toDoService.getTodayTodos();
		if (todayTodos != null && !todayTodos.isEmpty()) {

			List<ToDoDTO> toDoDTOS = todayTodos.stream()
				.map(toDoEntity -> {
					ToDoDTO dto = new ToDoDTO();
					dto.setTodoContent(toDoEntity.getTodoContent());
					return dto;
				})
				.collect(Collectors.toList());

			map.put("todoIndex", toDoDTOS);
			return  ResponseEntity.ok(map);
		}
		return ResponseEntity.noContent().build();

	}



	@PutMapping("/update-status/{id}")
	public ResponseEntity<ToDoEntity> updateToDoStatus(@PathVariable Integer id, @RequestBody String status) {
		ToDoEntity updateTodo = toDoService.updateTodoStatus(id, status);
		System.out.println("Received status: " + status);
		return ResponseEntity.ok(updateTodo);
	}


}
