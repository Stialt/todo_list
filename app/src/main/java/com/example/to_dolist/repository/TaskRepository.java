package com.example.to_dolist.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.to_dolist.models.Task;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class TaskRepository {

    private static final String TAG = "TaskRepository";

    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;
    private CompositeDisposable compositeDisposable;

    public TaskRepository(Application application, CompositeDisposable compositeDisposable) {
        this.compositeDisposable = compositeDisposable;
        TaskDatabase database = TaskDatabase.getInstance(application);
        taskDao = database.taskDao();
        allTasks = taskDao.getAllTasks();
    }

    public void insert(final Task task) {
        compositeDisposable.add(
                taskDao.insert(task)
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: insert success");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "onError: insert ", e);
                            }
                        })
        );
    }

    public void update(Task task) {
        compositeDisposable.add(
                taskDao.update(task)
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: update success");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "onError: update ", e);
                            }
                        })
        );
    }

    public void delete(Task task) {
        compositeDisposable.add(
                taskDao.delete(task)
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: delete success");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "onError: delete ", e);
                            }
                        })
        );
    }

    public void deleteAllNote() {
        compositeDisposable.add(
                taskDao.deleteAllTasks()
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: deleteAllNote success");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "onError: deleteAllNote ", e);
                            }
                        })
        );
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

}
