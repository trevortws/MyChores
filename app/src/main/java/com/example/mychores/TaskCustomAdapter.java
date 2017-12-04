package com.example.mychores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by user on 18/11/2017.
 */

public class TaskCustomAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Task> myTask;
    LayoutInflater inflater;
    public TaskCustomAdapter(Context context, ArrayList<Task> myTask){
        this.context=context;
        this.myTask=myTask;
    }
    @Override
    public int getCount(){
        return myTask.size();
    }
    @Override
    public Object getItem(int position){
        return myTask.get(position);
    }
    @Override
    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        if (inflater==null){
            inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView==null){
            convertView=inflater.inflate(R.layout.tasktermplate,parent,false);
        }
        TextView taskName= (TextView) convertView.findViewById(R.id.taskName);
        TextView taskDescription=(TextView) convertView.findViewById(R.id.taskDescription);
        ImageView taskIcon=(ImageView)convertView.findViewById(R.id.task_icon) ;
        Task currentTask= myTask.get(position);
        taskName.setText(currentTask.getTaskName());
        if (currentTask.getTaskDescribtion().length()>30){
            taskDescription.setText(currentTask.getTaskDescribtion().subSequence(0,30)+"...");
        }
        else{taskDescription.setText(currentTask.getTaskDescribtion());}
        taskIcon.setImageResource(currentTask.getTaskIcon());
        return convertView;


    }

}
