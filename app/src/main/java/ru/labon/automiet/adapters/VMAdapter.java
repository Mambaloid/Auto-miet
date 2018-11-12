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
import ru.labon.automiet.helpers.FullHelper;
import ru.labon.automiet.models.ThemeAb;

public class VMAdapter extends RecyclerView.Adapter<VMAdapter.VMViewHolder> {

    private List<ThemeAb> themes;
    private List<File> videos;
    private Context context;



    public VMAdapter(List<ThemeAb> themes, List<File> videos, Context context) {
        this.themes = themes;
        this.videos = videos;
        this.context = context;
    }

    @Override
    public VMViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_videomaterial, parent, false);
        final VMViewHolder vh = new VMViewHolder(v, context);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull VMViewHolder holder, int position) {
        final File video = videos.get(holder.getAdapterPosition());
        int themeId = Integer.parseInt(video.getName().replace(".mp4","")) - 1;
        holder.itemDescription.setText(themes.get(themeId).getName());
        holder.itemSubDescription.setText(String.valueOf(video.length()/1024/1024) + " Мб");
        holder.themeId = themeId;
        holder.removeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullHelper.deleteVideo(context, video.getName(), new FullHelper.Consumer<Boolean>() {
                    @Override
                    public void execute(Boolean arg) {
                        if (arg){
                            Toast.makeText(context, "Видео удалено", Toast.LENGTH_SHORT).show();
                            videos.remove(video);
                            notifyDataSetChanged();
                        }else{
                            Toast.makeText(context, "Ошибка удаления видео", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public int getItemCount() {
        return videos.size();
    }


    public static class VMViewHolder extends RecyclerView.ViewHolder {
        TextView itemDescription;
        TextView itemSubDescription;
        ImageView removeImg;
        int themeId = -1;



        VMViewHolder(View itemView, final Context context) {
            super(itemView);
            itemDescription = itemView.findViewById(R.id.item_description);
            itemSubDescription = itemView.findViewById(R.id.item_sub_description);
            removeImg = itemView.findViewById(R.id.videoImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PlayerActivity.class);
                    intent.putExtra("video_exist", true);
                    intent.putExtra("video_num", themeId + 1 );
                    context.startActivity(intent);
                }
            });

        }
    }
    public interface DownloadIdInterface {
        void id(int id);
    }


}
