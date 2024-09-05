package com.leafresh.backend.todo.model;

import java.time.LocalDateTime;
public class ToDoDTO {

	private Integer todoId;
	private String todoContent;
	private LocalDateTime todoCreateAt;
	private Integer userId;

	public ToDoDTO() {
	}

	public ToDoDTO(Integer todoId, String todoContent, LocalDateTime todoCreate_at, Integer userId) {
		this.todoId = todoId;
		this.todoContent = todoContent;
		this.todoCreateAt = todoCreateAt;
		this.userId = userId;
	}

	public Integer getTodoId() {
		return todoId;
	}

	public void setTodoId(Integer todoId) {
		this.todoId = todoId;
	}

	public String getTodoContent() {
		return todoContent;
	}

	public void setTodoContent(String todoContent) {
		this.todoContent = todoContent;
	}

	public LocalDateTime getTodoCreateAt(LocalDateTime todoCreateAt) {
		return this.todoCreateAt;
	}

	public void setTodoCreateAt(LocalDateTime todoCreateAt) {
		this.todoCreateAt = todoCreateAt;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "ToDoDTO{" +
			"todoId=" + todoId +
			", todoContent='" + todoContent + '\'' +
			", todoCreateAt=" + todoCreateAt +
			", userId=" + userId +
			'}';
	}
}