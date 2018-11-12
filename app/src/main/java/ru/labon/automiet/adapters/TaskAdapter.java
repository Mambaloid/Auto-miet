package ru.labon.automiet.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import ru.labon.automiet.R;
import ru.labon.automiet.controllers.PlayerActivity;
import ru.labon.automiet.controllers.TestActivity;
import ru.labon.automiet.helpers.FullHelper;
import ru.labon.automiet.models.Task;
import ru.labon.automiet.models.ThemeAb;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.VMViewHolder> {

    private List<Task> tasks;
    private Context context;



    public TaskAdapter(List<Task> tasks, Context context) {
        this.tasks = tasks;
        this.context = context;
    }

    @Override
    public VMViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        final VMViewHolder vh = new VMViewHolder(v, context);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull VMViewHolder holder, int position) {
        int tasksNumber = tasks.get(position).getTaskNumber();
        holder.itemDescription.setText("Билет\n№" + tasksNumber);
        holder.tasksNumber = tasksNumber;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public int getItemCount() {
        return tasks.size();
    }


    public static class VMViewHolder extends RecyclerView.ViewHolder {
        TextView itemDescription;
        int tasksNumber = -1;



        VMViewHolder(View itemView, final Context context) {
            super(itemView);
            itemDescription = itemView.findViewById(R.id.item_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, TestActivity.class);
                    intent.putExtra("tasks_number", tasksNumber);
                    context.startActivity(intent);
                }
            });

        }
    }


}
