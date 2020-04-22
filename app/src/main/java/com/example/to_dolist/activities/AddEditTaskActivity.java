package com.example.to_dolist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.to_dolist.R;

public class AddEditTaskActivity extends AppCompatActivity {

    public static final String EXTRA_ID =
            "com.example.to_dolist.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.example.to_dolist.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "com.example.to_dolist.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY =
            "com.example.to_dolist.EXTRA_PRIORITY";

    private EditText editTextTitle;
    private EditText editTextDescription;
    private SeekBar seekBar;
    private TextView textViewSeekbar;
    //private NumberPicker numberPickerPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        //numberPickerPriority = findViewById(R.id.number_picker_priority);

        //numberPickerPriority.setMinValue(1);
        //numberPickerPriority.setMaxValue(10);

        seekBar = findViewById(R.id.seekBar);
        textViewSeekbar = findViewById(R.id.textViewSeekbar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewSeekbar.setText( String.valueOf(progress/11 + 1));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if (getSupportActionBar() != null)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Task");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            //numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));
            int priority = intent.getIntExtra(EXTRA_PRIORITY, 1);
            seekBar.setProgress(11 * priority - 11);
            textViewSeekbar.setText(String.valueOf(priority));
        } else {
            setTitle("Add Task");
        }
    }

    private void saveTask() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        //int priority = numberPickerPriority.getValue();
        int priority = seekBar.getProgress()/11 + 1;

        //if (title.trim().isEmpty() || description.trim().isEmpty()) {
        if (title.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_task:
                saveTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        //return super.onOptionsItemSelected(item);
    }

}
