package com.abyx.noticer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.text.DateFormat;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This class is a custom BaseAdapter that controls the ListView used in the MainActivity class.
 *
 * @author Pieter Verschaffelt
 */
public class RowAdapter extends BaseAdapter {
    Context context;
    List<Content> data;

    public RowAdapter(Context context, List<Content> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Content getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (vi == null) {
            vi = inflater.inflate(R.layout.listview_main, null);
        }

        Content temp = data.get(position);

        ImageView avatarView = (ImageView) vi.findViewById(R.id.avatarView);
        TextView title = (TextView) vi.findViewById(R.id.title);
        TextView priceLabel = (TextView) vi.findViewById(R.id.priceLabel);
        TextView occasionLabel = (TextView) vi.findViewById(R.id.occasionLabel);
        TextView dateLabel = (TextView) vi.findViewById(R.id.dateLabel);
        TextDrawable drawable;
        if (temp.getLocation().isEmpty()) {
            String occasion = temp.getOccasion().replaceAll("[^A-Za-z]", "");
            drawable = TextDrawable.builder().
                    buildRound(occasion.substring(0, 1).toUpperCase(), temp.getColor());
        } else {
            String location = temp.getLocation().replaceAll("[^A-Za-z]", "");
            drawable = TextDrawable.builder().
                    buildRound(location.substring(0, 1).toUpperCase(), temp.getColor());
        }
        avatarView.setImageDrawable(drawable);
        title.setText(temp.getLocation());
        occasionLabel.setText(temp.getOccasion());
        priceLabel.setText(temp.getExpenses().toString());
        dateLabel.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(temp.getDate().getTime()));

        return vi;
    }

    public void refresh(List<Content> items) {
        this.data = items;
        notifyDataSetChanged();
    }
}