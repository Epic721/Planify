package com.example.planify;

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
import android.view.View;
import android.widget.Toast;

import com.example.planify.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
private TaskViewModel taskViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); //showing content; setting screen to outermost view
        taskViewModel = new ViewModelProvider(this, (ViewModelProvider.AndroidViewModelFactory) ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(TaskViewModel.class);
        binding.floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View raw) {
                Intent intent = new Intent(MainActivity.this, DataInsertActivity.class);
                intent.putExtra("type", "addMode");
                startActivityForResult(intent, 1);
            }
        });
        binding.RV.setLayoutManager(new LinearLayoutManager(this));
        binding.RV.setHasFixedSize(true);
        RVAdapter adapter = new RVAdapter();
        binding.RV.setAdapter(adapter);

        //Fetching all task data
        taskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                adapter.submitList(tasks);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction==ItemTouchHelper.RIGHT) {
                    taskViewModel.delete(adapter.getTask(viewHolder.getAdapterPosition()));
                    Toast.makeText(MainActivity.this, "Task deleted!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, DataInsertActivity.class);
                    intent.putExtra("type", "update");
                    intent.putExtra("title", adapter.getTask(viewHolder.getAdapterPosition()).getTitle());
                    intent.putExtra("desc", adapter.getTask(viewHolder.getAdapterPosition()).getDesc());
                    intent.putExtra("id", adapter.getTask(viewHolder.getAdapterPosition()).getId());
                    startActivityForResult(intent, 2);
                }
            }
        }).attachToRecyclerView(binding.RV);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String title = data.getStringExtra("title");
            String desc = data.getStringExtra("desc");
            Task task = new Task(title, desc);
            taskViewModel.insert(task);
            Toast.makeText(this, "Task added!", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == 2) {
            String title = data.getStringExtra("title");
            String desc = data.getStringExtra("desc");
            Task task = new Task(title, desc);
            task.setId(data.getIntExtra("id", 0));
            taskViewModel.update(task);
            Toast.makeText(this, "Task updated!", Toast.LENGTH_SHORT).show();
        }
    }
}