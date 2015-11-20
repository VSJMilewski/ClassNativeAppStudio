package com.nativeappstudio.milewski_10529136.todolist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.IDN;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ListActivity extends AppCompatActivity {

    public static final String FileToSave = "storedTasks.txt";

    private TextView TaskText;
    private ListView TaskList;
    public static ArrayList<Task> Tasks = new ArrayList<>();
    private taskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TaskText = (TextView) findViewById(R.id.NewTask);
        TaskList = (ListView) findViewById(R.id.TaskList);

        readFile();

        adapter = new taskAdapter(this,Tasks);
        TaskList.setAdapter(adapter);
        TaskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListActivity.this, ChangeTaskActivity.class);
                intent.putExtra("place", (int) id);
                startActivity(intent);
            }
        });
        TaskList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Tasks.remove((int) id);
                tasksChanged();
                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        saveFile();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        tasksChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("tasks", Tasks);
    }

    @Override
    public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        Tasks = (ArrayList<Task>) inState.getSerializable("tasks");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    public void readFile() {
        Scanner scan = null;
        try {
            scan = new Scanner(openFileInput(FileToSave));
            Tasks.clear();
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if(line.equals("<S>")) {
                    String title = scan.nextLine();
                    String desc = scan.nextLine();
                    String prior = scan.nextLine();
                    Tasks.add(new Task(title,desc,prior));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveFile() {
        String start = "<S>";
        String stop = "</S>";
        PrintStream out = null;
        try {
            out = new PrintStream(openFileOutput(FileToSave, MODE_PRIVATE));
            for (Task task : Tasks) {
                out.println(start);
                out.println(task.getTitle());
                out.println(task.getDescription());
                out.println(task.getPriority());
                out.println(stop);
            }
            out.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(getApplicationContext(), "no file",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void addTask(View view) {
        String title = TaskText.getText().toString();
        if(title.equals("")) {
            Toast.makeText(this, "You did not enter a task", Toast.LENGTH_SHORT).show();
        } else {

            Tasks.add(new Task(title));
            tasksChanged();
        }
    }

    public void tasksChanged() {
        reOrderTasks();
        adapter.notifyDataSetChanged();
        //saveFile();
    }

    public void reOrderTasks() {
        ArrayList<Task> high = new ArrayList<>();
        ArrayList<Task> normal = new ArrayList<>();
        ArrayList<Task> low = new ArrayList<>();
        ArrayList<Task> no = new ArrayList<>();

        for(Task task : Tasks) {
            String prior = task.getPriority();
            switch(prior) {
                case "":        no.add(task);
                                break;
                case "low":     low.add(task);
                                break;
                case "normal":  normal.add(task);
                                break;
                case "high":    high.add(task);
                                break;
                default:        no.add(task);
                                break;
            }
        }
        Tasks.clear();
        if(!high.isEmpty()) {
            Tasks.addAll(high);
        }
        if(!normal.isEmpty()) {
            Tasks.addAll(normal);
        }
        if(!low.isEmpty()) {
            Tasks.addAll(low);
        }
        if(!no.isEmpty()) {
            Tasks.addAll(no);
        }
    }
}
