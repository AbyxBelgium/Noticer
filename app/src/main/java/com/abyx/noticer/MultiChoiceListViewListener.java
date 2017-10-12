package com.abyx.noticer;

import android.content.Context;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the MultiChoiceModeListener that is used to control the deletion of multiple
 * self-chosen elements.
 *
 * @author Pieter Verschaffelt
 */
public class MultiChoiceListViewListener<T> implements AbsListView.MultiChoiceModeListener {
    private List<T> contents;
    private List<T> selected = new ArrayList<>();
    private MainActivity activity;

    public MultiChoiceListViewListener(List<T> contents, MainActivity activity){
        this.contents = contents;
        this.activity = activity;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        // Every selected item is added to the selected list so that we
        // are able to delete these objects later on
        if (!selected.contains(contents.get(position))) {
            selected.add(contents.get(position));
        } else {
            selected.remove(contents.get(position));
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // Inflate the menu for the CAB
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.menu_contextual, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        // Respond to clicks on the actions in the CAB
        switch (item.getItemId()) {
            case R.id.action_remove:
                // All selected items have to be removed
                contents.removeAll(selected);
                activity.refreshPriceField();
                mode.finish(); // Action picked, so close the CAB
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        // Here you can make any necessary updates to the activity when
        // the CAB is removed. By default, selected items are deselected/unchecked.
    }
}
