package ru.labon.automiet.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import cn.pedant.SweetAlert.SweetAlertDialog;

import java.io.File;
import java.util.List;

import me.grantland.widget.AutofitTextView;
import ru.labon.automiet.R;
import ru.labon.automiet.controllers.PlayerActivity;
import ru.labon.automiet.helpers.FullHelper;
import ru.labon.automiet.models.ThemeAb;

/**
 * Created by HP on 03.10.2017.
 */

public class ListAdapter extends BaseAdapter {

    public static final String MATERIAL = "MATERIALS";
    public static final String TEST = "TESTS";
    private List<ThemeAb> data;
    private LayoutInflater lInflater;
    private Context ctx;
    private String type;

    static class ViewHolder {
        TextView itemDescription;
        TextView itemSubDescription;
        ImageView videoImg;
        RelativeLayout downloaderCont;
        CircularProgressBar downloaderProgressBar;
        AutofitTextView downloaderStatusTextView;
        FileDownloadListener fileDownloadListener;
        int position;
        int downloadId = -1;
        String path = "";

        public ViewHolder(int pos) {
            position = pos;
            fileDownloadListener = new FileDownloadListener() {
                @Override
                protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    Log.d("downloader", soFarBytes + "  " + totalBytes);
                    downloadId = task.getId();
                    path = task.getPath();
                }

                @Override
                protected void started(BaseDownloadTask task) {
                    downloaderCont.setVisibility(View.VISIBLE);
                }

                @Override
                protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                    Log.d("downloader", etag);
                }

                @Override
                protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    float percent = ((float) soFarBytes / totalBytes) * 100;
                    Log.d("downloader", "-------------- " + percent + "%");
                    downloaderProgressBar.setProgress(percent);
                    downloaderStatusTextView.setText(((int) percent) + "%");
                }

                @Override
                protected void blockComplete(BaseDownloadTask task) {
                }

                @Override
                protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                }

                @Override
                protected void completed(BaseDownloadTask task) {
                    downloaderCont.setVisibility(View.INVISIBLE);
                    downloadId = -1;
                }

                @Override
                protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                }

                @Override
                protected void error(BaseDownloadTask task, Throwable e) {
                    Log.d("downloader", e.getMessage());
                }

                @Override
                protected void warn(BaseDownloadTask task) {
                }
            };
        }
    }

    public ListAdapter(List<ThemeAb> data, Context ctx, String type) {
        this.data = data;
        this.ctx = ctx;
        this.lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.type = type;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final int videoNum = i + 1;
        final boolean videoExist = FullHelper.checkVideoExist(ctx, videoNum);
        switch (type) {
            case MATERIAL:
                //if (convertView == null) {
                convertView = lInflater.inflate(R.layout.material_list_view_item, parent, false);
                viewHolder = new ViewHolder(i);
                viewHolder.itemDescription = convertView.findViewById(R.id.item_description);
                viewHolder.videoImg = convertView.findViewById(R.id.videoImageView);
                viewHolder.downloaderCont = convertView.findViewById(R.id.downloader_cont);
                viewHolder.downloaderProgressBar = convertView.findViewById(R.id.circular_progress_bar_nav_drawer);
                viewHolder.downloaderStatusTextView = convertView.findViewById(R.id.text_view_nav_drawer);
                if (viewHolder.downloadId > 0) {
                 FileDownloader.getImpl().getStatus(viewHolder.downloadId, viewHolder.path);
                }
                convertView.setTag(viewHolder);
                //} else {
                //    viewHolder = (ViewHolder) convertView.getTag();
                if (i == viewHolder.position)
                    Log.d("ADAPTER", "TRUE " + i + " = " + viewHolder.position);
                else Log.d("ADAPTER", "FALSE " + i + " = " + viewHolder.position);
                //}
                viewHolder.itemDescription.setText(data.get(i).getName());

                viewHolder.videoImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (videoExist) {
                            Intent intent = new Intent(ctx, PlayerActivity.class);
                            intent.putExtra("video_exist", true);
                            intent.putExtra("video_num", videoNum);
                            ctx.startActivity(intent);
                        } else {

                            //FullHelper.videoDialog(ctx, videoNum, viewHolder.fileDownloadListener);
                        }
                    }
                });
                break;
            case TEST:
                if (convertView == null) {
                    convertView = lInflater.inflate(R.layout.test_list_view_item, parent, false);
                    viewHolder = new ViewHolder(i);
                    viewHolder.itemDescription = convertView.findViewById(R.id.item_description);
                    viewHolder.itemSubDescription = convertView.findViewById(R.id.item_sub_description);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.itemDescription.setText(data.get(i).getName());
                viewHolder.itemSubDescription.setText(data.get(i).getQuestCount() + " вопросов");
                break;
        }
        return convertView;
    }
}
