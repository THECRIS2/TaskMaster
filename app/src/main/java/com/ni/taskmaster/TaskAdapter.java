package com.ni.taskmaster;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;

    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.taskName.setText(task.getName());
        holder.taskStatus.setText(task.getStatus());
        holder.taskTime.setText(task.getTime());

        holder.btnEditTask.setOnClickListener(v -> {
            showEditTaskDialog(holder.itemView.getContext(), task, position);
        });

        holder.btnDeleteTask.setOnClickListener(v -> {
            taskList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, taskList.size());
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskName, taskStatus, taskTime;
        Button btnEditTask, btnDeleteTask;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.tvTaskName);
            taskStatus = itemView.findViewById(R.id.tvTaskStatus);
            taskTime = itemView.findViewById(R.id.tvTaskTime);
            btnEditTask = itemView.findViewById(R.id.btnEditTask);
            btnDeleteTask = itemView.findViewById(R.id.btnDeleteTask);
        }
    }

    private void showEditTaskDialog(Context context, Task task, int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText inputName = new EditText(context);
        inputName.setText(task.getName());
        layout.addView(inputName);

        final Spinner statusSpinner = new Spinner(context);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, new String[]{"Pendiente", "Completado"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);
        layout.addView(statusSpinner);

        statusSpinner.setSelection(task.getStatus().equals("Pendiente") ? 0 : 1);

        final Button timeButton = new Button(context);
        timeButton.setText(task.getTime() != null ? task.getTime() : "Seleccionar Hora");
        layout.addView(timeButton);
        final String[] updatedTime = {task.getTime()};
        timeButton.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(context, (view, hourOfDay, minute) -> {
                updatedTime[0] = String.format("%02d:%02d", hourOfDay, minute);
                timeButton.setText(updatedTime[0]);
            }, 12, 0, true);
            timePickerDialog.show();
        });

        dialogBuilder.setTitle("Editar tarea");
        dialogBuilder.setMessage("Modifica el nombre, el estado y la hora de la tarea:");
        dialogBuilder.setView(layout);

        dialogBuilder.setPositiveButton("Guardar", (dialogInterface, i) -> {
            String updatedTaskName = inputName.getText().toString();
            String updatedTaskStatus = statusSpinner.getSelectedItem().toString();

            if (!updatedTaskName.isEmpty()) {
                task.setName(updatedTaskName);
                task.setStatus(updatedTaskStatus);
                task.setTime(updatedTime[0]);
                notifyItemChanged(position);
            }
        });

        dialogBuilder.setNegativeButton("Cancelar", (dialogInterface, i) -> dialogInterface.dismiss());

        dialogBuilder.create().show();
    }
}