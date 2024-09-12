package com.leafresh.backend.todo.model;

import java.time.LocalDate;

public class ToDoDTO {

	private Integer todoId;
	private String todoContent;
	private LocalDate todoSelectedDate;
	private Integer userId;

	public ToDoDTO() {
	}

	public ToDoDTO(Integer todoId, String todoContent, LocalDate todoSelectedDate, Integer userId) {

		this.todoId = todoId;
		this.todoContent = todoContent;
		this.todoSelectedDate = todoSelectedDate;
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

	public LocalDate getTodoSelectedDate() {
		return todoSelectedDate;
	}

	public void setTodoSelectedDate(LocalDate todoSelectedDate) {
		this.todoSelectedDate = todoSelectedDate;
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
			", todoSelectedDate=" + todoSelectedDate +
			", userId=" + userId +
			'}';
	}
}