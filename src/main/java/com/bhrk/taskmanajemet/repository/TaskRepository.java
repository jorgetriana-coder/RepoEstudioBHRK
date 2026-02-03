package com.bhrk.taskmanajemet.repository;

import com.bhrk.taskmanajemet.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Integer> {
}
