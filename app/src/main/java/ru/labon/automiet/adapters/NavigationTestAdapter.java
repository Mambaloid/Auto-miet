package ru.labon.automiet.adapters;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.labon.automiet.R;

/**
 * Created by HP on 21.11.2017.
 */

public class NavigationTestAdapter extends RecyclerView.Adapter<NavigationTestAdapter.ViewHolder> {

    public interface ClickInterface{
        void click(int position);
    }

    public ArrayList<Quest> quests;
    private ClickInterface clickInterface;
    private int lastCurrent;

    public NavigationTestAdapter(int count, ClickInterface clickInterface) {
        this.clickInterface = clickInterface;
        this.quests = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            Quest quest = new Quest();
            quest.setTitle(i+"");
            quest.setState(Quest.state_neutral);
            this.quests.add(quest);
        }
        this.lastCurrent = 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;

        public ViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.textViewHorizontalTicket);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontal_ticket_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Quest quest = quests.get(position);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickInterface.click(position);
            }
        });
        holder.textView.setText(quest.getTitle());
        switch (quest.getState()) {
            case Quest.state_neutral:
                holder.textView.setBackgroundResource(R.drawable.ticket_bg_empty);
                break;
            case Quest.state_current:
                holder.textView.setBackgroundResource(R.drawable.ticket_bg_current);
                break;
            case Quest.state_positive:
                holder.textView.setBackgroundResource(R.drawable.ticket_bg_done);
                break;
            case Quest.state_negative:
                holder.textView.setBackgroundResource(R.drawable.ticket_bg_fail);
                break;
        }
    }



    public void changeStateByPosition(int position, int state) {
        if (state == Quest.state_current) {
            if (quests.get(lastCurrent).getState() == Quest.state_current)
                quests.get(lastCurrent).setState(Quest.state_neutral);
            quests.get(position).setState(state);
            notifyDataSetChanged();
        } else {
            quests.get(position).setState(state);
            notifyDataSetChanged();
        }
        lastCurrent = position;
    }
    public int getStateByPosition(int position) {
        return quests.get(position).getState();
    }

    @Override
    public int getItemCount() {
        return quests.size();
    }

    public void setQuests(ArrayList<Quest> quests) {
        this.quests = quests;
        notifyDataSetChanged();
    }

    public class Quest implements Parcelable{
        private String title;
        private int state;


        public static final int state_neutral = 0;
        public static final int state_current = 1;
        public static final int state_positive = 2;
        public static final int state_negative = 3;

        protected Quest() { }

        protected Quest(Parcel in) {
            title = in.readString();
            state = in.readInt();
        }

        public final Creator<Quest> CREATOR = new Creator<Quest>() {
            @Override
            public Quest createFromParcel(Parcel in) {
                return new Quest(in);
            }

            @Override
            public Quest[] newArray(int size) {
                return new Quest[size];
            }
        };

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(state);
            parcel.writeString(title);
        }
    }
}
