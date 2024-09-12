package com.leafresh.backend.todo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leafresh.backend.todo.model.ToDoDTO;
import com.leafresh.backend.todo.model.ToDoEntity;
import com.leafresh.backend.todo.repository.TodoRepository;


@Service
public class ToDoService {


	private final TodoRepository todoRepository;

	@Autowired
	public ToDoService(TodoRepository todoRepository) {
		this.todoRepository = todoRepository;
	}


	public ToDoEntity addTodo(ToDoDTO toDoDTO) {
		ToDoEntity newTodo = new ToDoEntity();
		newTodo.setTodoContent(toDoDTO.getTodoContent());
		newTodo.setUserId(toDoDTO.getUserId());
		newTodo.setTodoSelectedDate(toDoDTO.getTodoSelectedDate());

		return todoRepository.save(newTodo);

	}

	public List<ToDoEntity> getTodayTodosByUserIdAndSelectedDate(Integer userId, LocalDate today) {
		return todoRepository.findAllByUserIdAndTodoSelectedDate(userId, today);
	}

	public void deleteTodoById(Integer todoId) {
		todoRepository.deleteById(todoId);
	}

	public List<ToDoEntity> getTodosByUserIdAndDate(Integer userId, LocalDate selectedDate) {
		return todoRepository.findAllByUserIdAndTodoSelectedDate(userId, selectedDate);


	}
}
