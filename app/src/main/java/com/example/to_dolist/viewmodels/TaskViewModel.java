package com.example.to_dolist.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.to_dolist.repository.TaskRepository;
import com.example.to_dolist.models.Task;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;


public class TaskViewModel extends AndroidViewModel {
    private TaskRepository repository;
    private LiveData<List<Task>> allNotes;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

    //View Model standart constructor with only Application parameter
    public TaskViewModel(Application application) {
        super(application);
        repository = new TaskRepository(application,compositeDisposable);
        allNotes = repository.getAllTasks();
    }

    public void insert(Task task) {
        repository.insert(task);
    }

    public void update(Task task) {
        repository.update(task);
    }

    public void delete(Task task) {
        repository.delete(task);
    }

    public void deleteAllNotes() {
        repository.deleteAllNote();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allNotes;
    }
}
