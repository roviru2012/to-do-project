package com.vaibhav.varun.controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vaibhav.varun.R;
import com.vaibhav.varun.helpers.MyDatabaseHelper;
import com.vaibhav.varun.helpers.MyDatabaseManager;

import java.util.ArrayList;

public class ToDoListHomeActivity extends AppCompatActivity {

    ListViewAdapter listViewAdapter;
    ListView listView;
    Toolbar toolbar;

    public String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("To-do List");
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.list_todo);
        updateUI();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MyDatabaseManager db = new MyDatabaseManager(getApplicationContext()).open();
                final String idText = ((TextView) view.findViewById(R.id.taskId)).getText().toString();
                final String titleText = ((TextView) view.findViewById(R.id.title)).getText().toString();
                final String descriptionText = ((TextView) view.findViewById(R.id.description)).getText().toString();
                final String dateText = ((TextView) view.findViewById(R.id.timestamp)).getText().toString();
                db.update(Integer.parseInt(String.valueOf(idText)), titleText, descriptionText, dateText, 1);
                Toast.makeText(ToDoListHomeActivity.this, "This task is marked completed.", Toast.LENGTH_SHORT).show();
                updateUI();
                db.close();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final String idText = ((TextView) view.findViewById(R.id.taskId)).getText().toString();
                final String titleText = ((TextView) view.findViewById(R.id.title)).getText().toString();
                final String descriptionText = ((TextView) view.findViewById(R.id.description)).getText().toString();
                final String dateText = ((TextView) view.findViewById(R.id.timestamp)).getText().toString();

                AlertDialog.Builder dialog = new AlertDialog.Builder(ToDoListHomeActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                final View inflaterView = inflater.inflate(R.layout.dialog, null);
                dialog.setView(inflaterView);

                final TextView taskEditId = (TextView) inflaterView.findViewById(R.id.taskEditId);
                final EditText title = (EditText) inflaterView.findViewById(R.id.input_title);
                final EditText description = (EditText) inflaterView.findViewById(R.id.input_description);
                final DatePicker date = (DatePicker) inflaterView.findViewById(R.id.datePicker);

                taskEditId.setText(idText);
                title.setText(titleText);
                description.setText(descriptionText);
                int year = Integer.parseInt(dateText.substring(6, 10));
                int monthOfYear = Integer.parseInt(dateText.substring(3, 5)) - 1;
                int dayOfMonth = Integer.parseInt(dateText.substring(0, 2));
                date.init(year, monthOfYear, dayOfMonth, null);

                dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {

                    String getDate() {
                        StringBuilder builder = new StringBuilder();
                        builder.append(date.getDayOfMonth() + "/");
                        String dateMonth = String.valueOf(date.getMonth() + 1);
                        if (dateMonth.length() <= 1) {
                            dateMonth = "0" + dateMonth;
                        }
                        builder.append((dateMonth) + "/");
                        builder.append(date.getYear());
                        return builder.toString();
                    }

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (titleText.equals("")) {
                            title.setError("Must have a title, it cannot be empty.");
                        } else if (descriptionText.equals("")) {
                            description.setError("Must have a description, it cannot be empty.");
                        } else {
                            String date = getDate();
                            final MyDatabaseManager myDatabaseManager = new MyDatabaseManager(getApplicationContext());
                            myDatabaseManager.open();
                            myDatabaseManager.update(Integer.parseInt(String.valueOf(taskEditId.getText())), title.getText().toString(), description.getText().toString(), date, 0);
                            myDatabaseManager.close();
                            updateUI();
                        }
                    }
                });
                dialog.setNegativeButton("Cancel", null);
                dialog.create();
                dialog.show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                LayoutInflater inflater = getLayoutInflater();
                final View inflaterView = inflater.inflate(R.layout.dialog, null);
                dialog.setView(inflaterView);


                final EditText title = (EditText) inflaterView.findViewById(R.id.input_title);
                final EditText description = (EditText) inflaterView.findViewById(R.id.input_description);
                final DatePicker date = (DatePicker) inflaterView.findViewById(R.id.datePicker);

                dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {

                    String getDate() {
                        StringBuilder builder = new StringBuilder();
                        builder.append(date.getDayOfMonth() + "/");
                        String dateMonth = String.valueOf(date.getMonth() + 1);
                        if (dateMonth.length() <= 1) {
                            dateMonth = "0" + dateMonth;
                        }
                        builder.append((dateMonth) + "/");
                        builder.append(date.getYear());
                        return builder.toString();
                    }

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (title.getText().toString().trim().length() == 0 && description.getText().toString().trim().length() == 0) {
                            title.setError("Must have a title, it cannot be empty.");
                            description.setError("Must have a description, it cannot be empty.");
                            Toast.makeText(ToDoListHomeActivity.this, "Title and description cannot be empty.", Toast.LENGTH_SHORT).show();
                        } else if (title.getText().toString().trim().length() == 0) {
                            title.setError("Must have a title, it cannot be empty.");
                            Toast.makeText(ToDoListHomeActivity.this, "Title cannot be empty.", Toast.LENGTH_SHORT).show();
                        } else if (description.getText().toString().trim().length() == 0) {
                            description.setError("Must have a description, it cannot be empty.");
                            Toast.makeText(ToDoListHomeActivity.this, "Description cannot be empty.", Toast.LENGTH_SHORT).show();
                        } else {
                            String date = getDate();
                            final MyDatabaseManager myDatabaseManager = new MyDatabaseManager(getApplicationContext());
                            myDatabaseManager.open();
                            myDatabaseManager.insert(title.getText().toString(), description.getText().toString(), date, "0");
                            myDatabaseManager.close();
                            updateUI();
                        }
                    }
                });
                dialog.setNegativeButton("Cancel", null);
                dialog.create();
                dialog.show();
                return true;
            case R.id.complete:
                Intent intent = new Intent(ToDoListHomeActivity.this, CompletedTaskActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        MyDatabaseManager db = new MyDatabaseManager(getApplicationContext()).open();

        Cursor cursor = db.fetch("0");

        ArrayList<Integer> ids = new ArrayList<>();
        ArrayList<String> title = new ArrayList<>();
        ArrayList<String> description = new ArrayList<>();
        ArrayList<String> date = new ArrayList<>();
        ArrayList<Integer> status = new ArrayList<>();

        while (cursor.moveToNext()) {
            ids.add(cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_ID)));
            title.add(cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_TITLE)));
            description.add(cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_DESCRIPTION)));
            date.add(cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_DATE)));
            status.add(cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_STATUS)));
        }
        listViewAdapter = new ListViewAdapter(this, ids, title, description, date, status);
        listView.setAdapter(listViewAdapter);
        cursor.close();
        db.close();
    }
}
