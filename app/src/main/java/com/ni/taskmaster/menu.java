package com.ni.taskmaster;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class menu extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private ArrayList<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        recyclerView = findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList);
        recyclerView.setAdapter(taskAdapter);

        ImageButton btnAddTask = findViewById(R.id.btnAddTask);
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTaskDialog();
            }
        });
    }

    private void showAddTaskDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        dialogBuilder.setTitle("Agregar nueva tarea");
        dialogBuilder.setMessage("Introduce el nombre de la tarea:");
        dialogBuilder.setView(input);

        dialogBuilder.setPositiveButton("Agregar", (dialogInterface, i) -> {
            String taskName = input.getText().toString();
            if (!taskName.isEmpty()) {
                showTimePickerDialog(taskName);
            }
        });

        dialogBuilder.setNegativeButton("Cancelar", (dialogInterface, i) -> dialogInterface.dismiss());

        dialogBuilder.create().show();
    }

    private void showTimePickerDialog(String taskName) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            String taskTime = String.format("%02d:%02d", hourOfDay, minute);
            addTaskToRecyclerView(taskName, taskTime);
        }, 12, 0, true);
        timePickerDialog.show();
    }

    private void addTaskToRecyclerView(String taskName, String taskTime) {
        Task newTask = new Task(taskName, "Pendiente", taskTime);
        taskList.add(newTask);
        taskAdapter.notifyItemInserted(taskList.size() - 1);
    }
}