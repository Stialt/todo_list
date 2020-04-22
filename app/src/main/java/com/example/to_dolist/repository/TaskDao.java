package com.example.to_dolist.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.to_dolist.models.Task;

import java.util.List;

import io.reactivex.Completable;


@Dao
public interface TaskDao {

    @Insert
    Completable insert(Task task);

    @Update
    Completable update(Task task);

    @Delete
    Completable delete(Task task);

    @Query("DELETE FROM task_table")
    Completable deleteAllTasks();

    @Query("SELECT * FROM task_table ORDER by priority DESC")
    LiveData<List<Task>> getAllTasks();
}
