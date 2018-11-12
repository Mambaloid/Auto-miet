package ru.labon.automiet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

import ru.labon.automiet.R;
import ru.labon.automiet.models.ExpandableAccountGroup;
import ru.labon.automiet.models.ExpandableAccountItem;

/**
 * Created by HP on 05.12.2017.
 */

public class AccountListAdapter extends ExpandableRecyclerViewAdapter<AccountListAdapter.GroupAccountViewHolder, AccountListAdapter.ItemAccountViewHolder> {

    private LayoutInflater inflater;
    public class GroupAccountViewHolder extends GroupViewHolder {

        private TextView groupTitle;

        public GroupAccountViewHolder(View itemView) {
            super(itemView);
            groupTitle = itemView.findViewById(R.id.group_title);
        }

        public void setGroupTitle(ExpandableGroup group) {
            groupTitle.setText(group.getTitle());
        }
    }

    public class ItemAccountViewHolder extends ChildViewHolder {

        private TextView titleItem;
        private TextView valueItem;

        public ItemAccountViewHolder(View itemView) {
            super(itemView);
            titleItem = itemView.findViewById(R.id.item_title);
            valueItem = itemView.findViewById(R.id.item_value);
        }

        public void onBind(ExpandableAccountItem item) {
            titleItem.setText(item.getTitle());
            valueItem.setText(item.getValue());
        }
    }

    public AccountListAdapter(List<? extends ExpandableGroup> groups, Context context) {
        super(groups);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public GroupAccountViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_account_group, parent, false);
        return new GroupAccountViewHolder(view);
    }

    @Override
    public ItemAccountViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_account_item, parent, false);
        return new ItemAccountViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(ItemAccountViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final ExpandableAccountItem item = (ExpandableAccountItem) group.getItems().get(childIndex);
        holder.onBind(item);
    }

    @Override
    public void onBindGroupViewHolder(GroupAccountViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setGroupTitle(group);
    }
}
