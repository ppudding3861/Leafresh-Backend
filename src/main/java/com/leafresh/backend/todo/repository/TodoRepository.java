package com.leafresh.backend.todo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leafresh.backend.todo.model.ToDoEntity;

@Repository
public interface TodoRepository extends JpaRepository<ToDoEntity, Integer> {


	List<ToDoEntity> findAllByUserIdAndTodoSelectedDate(Integer userId, LocalDate selectedDate);
}
