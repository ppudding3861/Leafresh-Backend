package com.leafresh.backend.todo.model;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ToDoDTO {


	private Integer todoId;

	@NotBlank(message = "할 일을 입력하세요! ")
	private String todoContent;
	@NotNull(message = "날짜를 입력하세요!")
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