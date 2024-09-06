package com.leafresh.backend.todo.model;

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

	@Column(name = "todo_status", nullable = false)
	private String todoStatus = "N"; // 기본값 'N'

	@Column(name = "todo_status2", nullable = false)
	private boolean todoSatus2 = false;


	public ToDoEntity() {
	}

	public ToDoEntity(Integer todoId, String todoContent, LocalDateTime todoCreateAt, Integer userId,
		String todoStatus) {
		this.todoId = todoId;
		this.todoContent = todoContent;
		this.todoCreateAt = todoCreateAt;
		this.userId = userId;
		this.todoStatus = todoStatus;
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

	public String getTodoStatus() {
		return todoStatus;
	}

	public void setTodoStatus(String todoStatus) {
		this.todoStatus = todoStatus;
	}
}
