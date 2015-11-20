package com.nativeappstudio.milewski_10529136.todolist;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static com.nativeappstudio.milewski_10529136.todolist.R.color.*;
import static com.nativeappstudio.milewski_10529136.todolist.R.color.priorNormal;

/**
 * Created by victo on 20-11-2015.
 */
public class taskAdapter extends ArrayAdapter<Task> {

    private Activity activity;
    private ArrayList Tasks;

    public taskAdapter(Activity activity, ArrayList<Task> Tasks) {
        super(activity,0,Tasks);
        this.activity = activity;
        this.Tasks = Tasks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder view;

        if(rowView == null)
        {
            // Get a new instance of the row layout view
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.adapter_layout, null);

            // Hold the view objects in an object, that way the don't need to be "re-  finded"
            view = new ViewHolder();
            view.title_text= (TextView) rowView.findViewById(R.id.adaptitle);
            view.priority_text= (TextView) rowView.findViewById(R.id.adapPrior);

            rowView.setTag(view);
        } else {
            view = (ViewHolder) rowView.getTag();
        }

        /** Set data to your Views. */
        Task item = (Task) Tasks.get(position);
        view.title_text.setText(item.getTitle());
        view.priority_text.setText(item.getPriority());

        switch (item.getPriority()) {
            case "high":
                view.priority_text.setBackgroundColor(Color.RED);
                break;
            case "normal":
                view.priority_text.setBackgroundColor(Color.YELLOW);
                break;
            case "low":
                view.priority_text.setBackgroundColor(Color.GREEN);
                break;
            default:
                view.priority_text.setBackgroundColor(Color.TRANSPARENT);
                break;
        }

        return rowView;
    }

    protected static class ViewHolder{
        protected TextView title_text;
        protected TextView priority_text;
    }

}
