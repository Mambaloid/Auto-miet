package ru.labon.automiet.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.grantland.widget.AutofitTextView;
import ru.labon.automiet.R;
import ru.labon.automiet.controllers.CardsActivity;
import ru.labon.automiet.controllers.PlayerActivity;
import ru.labon.automiet.helpers.FullHelper;
import ru.labon.automiet.models.ThemeAb;

/**
 * Created by Admin on 27.03.2018.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ThemeViewHolder> {

    private HashMap<Integer, Integer> taskIds = new HashMap<>();

    private List<ThemeAb> data;
    private Context context;

    private FileDownloadListener taskDownloadListener = new FileDownloadSampleListener() {

        private ThemeViewHolder checkCurrentHolder(final BaseDownloadTask task) {
            final ThemeViewHolder tag = (ThemeViewHolder) task.getTag();
            Log.d("---downloader", tag == null ? "checkCurrentHolder tag null id: " + tag.id + " position: " + tag.position  : "checkCurrentHolder not null id: " + tag.id  + " position: " + tag.position);
            if (tag.id != task.getId()) {
                tag.updateNotDownloaded();
                return null;
            }

            return tag;
        }

        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            super.pending(task, soFarBytes, totalBytes);
            final ThemeViewHolder tag = checkCurrentHolder(task);
            Log.d("---downloader", tag == null ? "pending tag null" : "pending not null");
            if (tag == null) {
                return;
            }


            float percent = ((float) soFarBytes / totalBytes) * 100;
            tag.updateDownloading(FileDownloadStatus.pending, percent);
        }

        @Override
        protected void started(BaseDownloadTask task) {
            super.started(task);
            final ThemeViewHolder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }

        }

        @Override
        protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
            super.connected(task, etag, isContinue, soFarBytes, totalBytes);
            final ThemeViewHolder tag = checkCurrentHolder(task);
            Log.d("---downloader", tag == null ? "connected tag null" : "connected not null");
            if (tag == null) {
                return;
            }
            float percent = ((float) soFarBytes / totalBytes) * 100;
            tag.updateDownloading(FileDownloadStatus.connected, percent);
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            super.progress(task, soFarBytes, totalBytes);
            final ThemeViewHolder tag = checkCurrentHolder(task);
            Log.d("---downloader", tag == null ? "progress tag null" : "progress not null");
            if (tag == null) {
                return;
            }

            float percent = ((float) soFarBytes / totalBytes) * 100;
            tag.updateDownloading(FileDownloadStatus.progress, percent);
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            super.error(task, e);
            final ThemeViewHolder tag = checkCurrentHolder(task);
            Log.d("---downloader", tag == null ? "error tag null" : "error not null");
            if (tag == null) {
                for (Iterator<Map.Entry<Integer, Integer>> it = taskIds.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<Integer, Integer> entry = it.next();
                    if (entry.getValue() == task.getId()) {
                        it.remove();
                    }
                }
                return;
            }
            taskIds.remove(tag.position);
            Toast.makeText(context, "Ошибка загрузки", Toast.LENGTH_SHORT).show();
            tag.updateNotDownloaded();
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            super.paused(task, soFarBytes, totalBytes);
            final ThemeViewHolder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            super.completed(task);
            final ThemeViewHolder tag = checkCurrentHolder(task);
            Log.d("---downloader", tag == null ? "completed tag null" : "completed not null");
            if (tag == null) {

                for (Iterator<Map.Entry<Integer, Integer>> it = taskIds.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<Integer, Integer> entry = it.next();
                    if (entry.getValue() == task.getId()) {
                        it.remove();
                    }
                }


                return;
            }

            taskIds.remove(tag.position);
            tag.updateDownloaded();
        }
    };


    public RVAdapter(List<ThemeAb> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ThemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("---downloader", "!!!!!!create view holder");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_list_view_item, parent, false);
        final ThemeViewHolder pvh = new ThemeViewHolder(v, context);
        pvh.videoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (RVAdapter.this.taskIds.containsKey(pvh.position)) {return;}
                    int videoNum = pvh.position + 1;
                    if (FullHelper.checkVideoExist(context, videoNum)) {
                        Intent intent = new Intent(context, PlayerActivity.class);
                        intent.putExtra("video_exist", true);
                        intent.putExtra("video_num", videoNum);
                        context.startActivity(intent);
                    } else {

                        FullHelper.videoDialog(context, videoNum, pvh, taskDownloadListener, new FullHelper.Consumer<Integer>() {
                            @Override
                            public void execute(Integer arg) {
                                pvh.id = arg;
                                if (!taskIds.containsKey(pvh.position)) {
                                    taskIds.put(pvh.position, pvh.id);
                                }
                            }
                        });
                    }


            }
        });
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ThemeViewHolder holder, int position) {
        holder.itemDescription.setText(data.get(holder.getAdapterPosition()).getName());
        holder.position = holder.getAdapterPosition();
        if (taskIds.containsKey(holder.getAdapterPosition())) {
            Log.d("---downloader", "value " + taskIds.get(holder.getAdapterPosition()) + " for key " + holder.getAdapterPosition());
            //holder.update(taskIds.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            holder.id = taskIds.get(holder.getAdapterPosition());
            holder.position = holder.getAdapterPosition();
            holder.downloaderCont.setVisibility(View.VISIBLE);
        } else {
            Log.d("---downloader", "set holder id to -1 for  " + holder.getAdapterPosition());
            holder.update(-1, holder.getAdapterPosition());
            holder.updateNotDownloaded();
        }
        holder.themeId = data.get(holder.getAdapterPosition()).getNum();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public static class ThemeViewHolder extends RecyclerView.ViewHolder {
        TextView itemDescription;
        ImageView videoImg;
        RelativeLayout downloaderCont;
        CircularProgressBar downloaderProgressBar;
        AutofitTextView downloaderStatusTextView;
        int position = -1;
        int themeId = -1;
        int id = -1;

        public void update(final int id, final int position) {
            this.id = id;
            this.position = position;
        }


        public void updateDownloading(int status, float percent) {
            downloaderProgressBar.setProgress(percent);
            downloaderStatusTextView.setText(((int) percent) + "%");

            switch (status) {
                case FileDownloadStatus.pending:
                    downloaderCont.setVisibility(View.VISIBLE);
                    break;
                case FileDownloadStatus.started:
                    downloaderCont.setVisibility(View.VISIBLE);
                    break;
                case FileDownloadStatus.connected:
                    downloaderCont.setVisibility(View.VISIBLE);
                    break;
                case FileDownloadStatus.progress:
                    downloaderCont.setVisibility(View.VISIBLE);
                    break;
                default:
                    downloaderCont.setVisibility(View.VISIBLE);
                    break;
            }
        }

        public void updateNotDownloaded() {
            downloaderCont.setVisibility(View.INVISIBLE);
        }

        public void updateDownloaded() {
            downloaderCont.setVisibility(View.INVISIBLE);
        }


        ThemeViewHolder(View itemView, final Context context) {
            super(itemView);
            itemDescription = itemView.findViewById(R.id.item_description);
            videoImg = itemView.findViewById(R.id.videoImageView);
            downloaderCont = itemView.findViewById(R.id.downloader_cont);
            downloaderProgressBar = itemView.findViewById(R.id.circular_progress_bar_nav_drawer);
            downloaderStatusTextView = itemView.findViewById(R.id.text_view_nav_drawer);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CardsActivity.class);
                    intent.putExtra("id_theme", themeId);
                    context.startActivity(intent);
                }
            });

        }
    }
//
//    public interface DownloadIdInterface {
//        void id(int id);
//    }




}
