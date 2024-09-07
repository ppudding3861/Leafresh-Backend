package com.leafresh.backend.todo.service;

import java.util.ArrayList;
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


	public List<ToDoDTO> getAllToDos() {

		List<ToDoEntity> toDoEntities = todoRepository.findAll();
		List<ToDoDTO> toDoDTOs = new ArrayList<>();
		for (ToDoEntity toDoEntity : toDoEntities) {
			ToDoDTO toDoDTO = new ToDoDTO();
			toDoDTO.setTodoId(toDoEntity.getTodoId());
			toDoDTO.setTodoContent(toDoEntity.getTodoContent());

			toDoDTOs.add(toDoDTO);

		}

		return toDoDTOs;
	}

	public ToDoDTO todoSave(ToDoDTO toDoDTO) {

		ToDoEntity toDoEntity = new ToDoEntity();
		toDoEntity.setTodoContent(toDoDTO.getTodoContent());
		toDoEntity.setTodoId(toDoDTO.getTodoId());

		todoRepository.save(toDoEntity);

		return new ToDoDTO();

	}

	public ToDoEntity updateTodoStatus(Integer id, String status) {
		ToDoEntity toDoEntity = todoRepository.findById(id).orElse(null);
		toDoEntity.setTodoStatus(status);
		return todoRepository.save(toDoEntity);


	}
}
