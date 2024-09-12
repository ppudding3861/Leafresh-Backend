package com.leafresh.backend.todo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "todo")
public class ToDoEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "todo_id")
	private Integer todoId;

	@Column(name = "todo_content")
	private String todoContent;

	@CreationTimestamp
	@Column(name = "todo_create_date")
	private LocalDateTime todoCreateAt;

	@Column(name = "user_id")
	private Integer userId;

	// 추가된 필드: 사용자가 선택한 날짜
	@Column(name = "todo_selected_date")
	private LocalDate todoSelectedDate;

	@Column(name = "todo_status", nullable = false)
	private String todoStatus = "N"; // 기본값 'N'

	@Column(name = "todo_status2", nullable = false)
	private boolean todoSatus2 = false;



	public ToDoEntity() {
	}

	public ToDoEntity(Integer todoId, String todoContent, LocalDateTime todoCreateAt, Integer userId,
		LocalDate todoSelectedDate) {
		this.todoId = todoId;
		this.todoContent = todoContent;
		this.todoCreateAt = todoCreateAt;
		this.userId = userId;
		this.todoSelectedDate = todoSelectedDate;
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

	public LocalDateTime getTodoCreateAt() {
		return todoCreateAt;
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

	public LocalDate getTodoSelectedDate() {
		return todoSelectedDate;
	}

	public void setTodoSelectedDate(LocalDate todoSelectedDate) {
		this.todoSelectedDate = todoSelectedDate;
	}

	@Override
	public String toString() {
		return "ToDoEntity{" +
			"todoId=" + todoId +
			", todoContent='" + todoContent + '\'' +
			", todoCreateAt=" + todoCreateAt +
			", userId=" + userId +
			", todoSelectedDate=" + todoSelectedDate +
			'}';
	}
}
