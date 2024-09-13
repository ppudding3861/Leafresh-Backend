package com.leafresh.backend.todo.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.leafresh.backend.todo.model.ToDoDTO;
import com.leafresh.backend.todo.model.ToDoEntity;
import com.leafresh.backend.todo.service.ToDoService;

@RestController
@RequestMapping("/garden-diary/todo")
public class ToDoController {

	private final ToDoService toDoService;

	@Autowired
	public ToDoController(ToDoService toDoService) {
		this.toDoService = toDoService;
	}


	// 오늘날짜 + userID 기준으로 보여준다.
	@GetMapping("/today")
	public ResponseEntity<Map<String, Object>> getTodayTodos(Integer userId) {
		Map<String, Object> map = new HashMap<>();

		// 오늘날짜를 가져옴
		LocalDate today = LocalDate.now();

		// 서비스에서 userId 와 selecteDate 기준으로 필터링된 데이터 가져오기
		List<ToDoEntity> todayTodos = toDoService.getTodayTodosByUserIdAndSelectedDate(userId, today);

		if (todayTodos != null && !todayTodos.isEmpty()) {

			// todoEntity 리스트를 todoDTO 리스트로 변환
			List<ToDoDTO> toDoDTOS = todayTodos.stream()
				.map(toDoEntity -> {
					ToDoDTO dto = new ToDoDTO();
					dto.setTodoContent(toDoEntity.getTodoContent());
					dto.setTodoId(toDoEntity.getTodoId());
					return dto;
				})
				.collect(Collectors.toList());

			map.put("todoIndex", toDoDTOS);
			return  ResponseEntity.ok(map);
		}
		return ResponseEntity.noContent().build();
	}

	// 오늘의 할일을 추가한다
	@PostMapping("/add")
	public ResponseEntity<Map<String, Object>> addTodo(@RequestBody ToDoDTO toDoDTO) {

	try{
		// 서비스 계층에서 검증 및 할 일 추가
		ToDoEntity savedTodo = toDoService.addTodo(toDoDTO);

		// 응답생성
		Map<String, Object> response = new HashMap<>();
		response.put("todoIndex", toDoDTO);

		return ResponseEntity.ok(response);

		} catch (ResponseStatusException e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("error", e.getMessage());

			return ResponseEntity.status(e.getStatusCode()).body(errorResponse);

		}
	}


	// 	오늘 할일을 삭제한다.
	@PostMapping("/delete")
	public ResponseEntity<Map<String, Object>> deleteTodo(@RequestBody Map<String, Object> payload) {
		Integer todoId = (Integer) payload.get("todoId"); // todoId를 Integer로 받음
		int action = (int) payload.get("action");  // action 추출
		Map<String, Object> response = new HashMap<>();


		if (todoId == null) {
			response.put("status", "failed");
			response.put("message", "todoId가 없습니다.");
			return ResponseEntity.badRequest().body(response);  // 400 Bad Request 반환
		}



		if (action == 0) {
			// action이 0이면 해당 todoId를 삭제
			toDoService.deleteTodoById(todoId);
			response.put("status", "success");
			response.put("message", "할 일이 성공적으로 삭제되었습니다.");
			response.put("deletedTodoId", todoId);  // 삭제된 todoId 반환
			return ResponseEntity.ok(response);  // 200 OK와 함께 Map 반환
		} else {
			// action이 1이면 삭제하지 않음 (다른 로직 가능)
			response.put("status", "failed");
			response.put("message", "할 일이 삭제되지 않았습니다.");
			return ResponseEntity.ok(response);  // 200 OK와 함께 Map 반환
		}



	}

}










