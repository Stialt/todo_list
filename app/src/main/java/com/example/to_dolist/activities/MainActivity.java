package com.example.to_dolist.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.to_dolist.R;
import com.example.to_dolist.models.Task;
import com.example.to_dolist.adapters.TaskAdapter;
import com.example.to_dolist.viewmodels.TaskViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TaskViewModel taskViewModel;

    public static final int ADD_TASK_REQUEST = 1;
    public static final int EDIT_TASK_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_task);

        //Start activity for adding new note
        buttonAddNote.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            startActivityForResult(intent, ADD_TASK_REQUEST);
        });

        //Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        final TaskAdapter adapter = new TaskAdapter();
        recyclerView.setAdapter(adapter);

        //Don't create instance
        //Get instance from system (which is lifecycle aware)
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        //Observe changes on data from ViewModel
        taskViewModel.getAllTasks().observe(this, tasks -> adapter.submitList(tasks));

        //Set item deletion on swipe (to the right or left)
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                //Leave this empty since no Drag-and-Drop functionality implemented
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                taskViewModel.delete(adapter.getTaskAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        //Set OnItemClickListener for items in RecyclerView
        adapter.setOnItemClickListener(task -> {
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            intent.putExtra(AddEditTaskActivity.EXTRA_ID, task.getId());
            intent.putExtra(AddEditTaskActivity.EXTRA_TITLE, task.getTitle());
            intent.putExtra(AddEditTaskActivity.EXTRA_DESCRIPTION, task.getDescription());
            intent.putExtra(AddEditTaskActivity.EXTRA_PRIORITY, task.getPriority());

            startActivityForResult(intent, EDIT_TASK_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Add note
        if (requestCode == ADD_TASK_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                String title = data.getStringExtra(AddEditTaskActivity.EXTRA_TITLE);
                String description = data.getStringExtra(AddEditTaskActivity.EXTRA_DESCRIPTION);
                int priority = data.getIntExtra(AddEditTaskActivity.EXTRA_PRIORITY, 1);

                Task note = new Task(title, description, priority);
                taskViewModel.insert(note);

                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            }
        }
        //Edit note
        else if (requestCode == EDIT_TASK_REQUEST && resultCode == RESULT_OK) {
            if (data == null) return;

            int id = data.getIntExtra(AddEditTaskActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Task can't be updated", Toast.LENGTH_SHORT).show();
            }
            else {
                String title = data.getStringExtra(AddEditTaskActivity.EXTRA_TITLE);
                String description = data.getStringExtra(AddEditTaskActivity.EXTRA_DESCRIPTION);
                int priority = data.getIntExtra(AddEditTaskActivity.EXTRA_PRIORITY, 1);

                Task note = new Task(title, description, priority);
                note.setId(id);

                taskViewModel.update(note);

                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Not saved", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.delete_all_tasks:
                taskViewModel.deleteAllNotes();
                Toast.makeText(this, "All tasks deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
