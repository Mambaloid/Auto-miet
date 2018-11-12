package ru.labon.automiet.models;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by HP on 05.12.2017.
 */

public class ExpandableAccountGroup extends ExpandableGroup<ExpandableAccountItem> {
    public ExpandableAccountGroup(String title, List<ExpandableAccountItem> items) {
        super(title, items);
    }
}
