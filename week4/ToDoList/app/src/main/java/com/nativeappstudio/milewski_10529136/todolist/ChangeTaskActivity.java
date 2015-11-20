package com.nativeappstudio.milewski_10529136.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class ChangeTaskActivity extends AppCompatActivity {

    private EditText titleText;
    private EditText descriptionText;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        titleText = (EditText) findViewById(R.id.changeTitle);
        descriptionText = (EditText) findViewById(R.id.changeDescription);

        Intent intent = getIntent();
        pos = intent.getIntExtra("place",0);

        fillActivity();
    }

    public void fillActivity(){
        titleText.setText(ListActivity.Tasks.get(pos).getTitle());
        descriptionText.setText(ListActivity.Tasks.get(pos).getDescription());
        String prior = ListActivity.Tasks.get(pos).getPriority();
        if(prior.equals("high")) {
            RadioButton radio = (RadioButton) findViewById(R.id.high);
            radio.setChecked(true);
        } else if(prior.equals("normal")) {
            RadioButton radio = (RadioButton) findViewById(R.id.normal);
            radio.setChecked(true);
        } else if(prior.equals("low")) {
            RadioButton radio = (RadioButton) findViewById(R.id.low);
            radio.setChecked(true);
        } else {
            RadioButton radio = (RadioButton) findViewById(R.id.none);
            radio.setChecked(true);
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.high:
                if (checked) {
                    ListActivity.Tasks.get(pos).setPriority("high");
                    break;
                }
            case R.id.normal:
                if (checked) {
                    ListActivity.Tasks.get(pos).setPriority("normal");
                    break;
                }
            case R.id.low:
                if (checked) {
                    ListActivity.Tasks.get(pos).setPriority("low");
                    break;
                }
            case R.id.none:
                if (checked) {
                    ListActivity.Tasks.get(pos).setPriority("");
                    break;
                }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ListActivity.Tasks.get(pos).setTitle(titleText.getText().toString());
        ListActivity.Tasks.get(pos).setDescription(descriptionText.getText().toString());
    }
}
