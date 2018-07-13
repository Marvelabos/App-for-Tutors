package com.example.adamstelmaszyk.mojerozliczenia;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class CalenderView extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    private static final String TAG = "CalenderView";
    private int what_choose;

    DataBaseHelper myDb;
    EditText  editKwota, editID;
    Button btnchooseData;
    Calendar cal = Calendar.getInstance();
    Spinner spinner;
    String text_from_money;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    Button btnAddData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_view);


        myDb = new DataBaseHelper(this);

        btnAddData = (Button) findViewById(R.id.btnAdd);
        btnchooseData = (Button) findViewById(R.id.chooseData);

        editKwota = (EditText) findViewById(R.id.JakaKwota);
        editID = (EditText) findViewById(R.id.Id);

        spinner = findViewById(R.id.spinnerView);

        putDate();
        from_money();




    }


    public void checkTime()
    {

        int day = cal.get(Calendar.DAY_OF_WEEK);

    }

    public void add(View view)
    {

        boolean isInserted = myDb.insertData(btnchooseData.getText().toString(), editKwota.getText().toString(), what_choose);

        if (isInserted == true) {
            Toast.makeText(CalenderView.this, "Data Inserted", Toast.LENGTH_LONG).show();
            btnchooseData.setText("");
            editKwota.setText("");
        } else {
            Toast.makeText(CalenderView.this, "Data not Inserted", Toast.LENGTH_LONG).show();
        }
    }

    public void delete_data(View view)
    {
        int deletedRows =myDb.deleteData(editID.getText().toString(), what_choose);

        if(deletedRows >0)
        {
            Toast.makeText(CalenderView.this,"Data Daleted", Toast.LENGTH_LONG).show();
            editID.setText("");
        }
        else
        {
            Toast.makeText(CalenderView.this,"Data not deleted", Toast.LENGTH_LONG).show();
        }


    }

    public void show_data(View view)
    {
        Cursor res = myDb.getAllData(what_choose);
        if(res.getCount() == 0)
        {
            showMessage("Error", "Nothing found");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext())
        {
            buffer.append("Id :"+ res.getString(0)+"\n");
            buffer.append("Data :"+ res.getString(1)+"\n");
            buffer.append("Kwota :"+ res.getString(2)+"\n");
        }

        if(what_choose==0)
        {
            showMessage("Salary", buffer.toString());
        }
        else if(what_choose==1)
        {
            showMessage("Spending", buffer.toString());
        }

    }

    public void showMessage(String title, String Message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public void choose_data(View view)
    {

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                CalenderView.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();



    }

    public void putDate()
    {
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                month = month +1;
                Log.d(TAG, "onDataSet: date: "+year+"/"+month+"/"+day);

                String date = day + "/" + month +"/"+year;
                btnchooseData.setText(date);


            }
        };
    }

    public void come_back_to_menu(View view)
    {
        Intent intent = new Intent(CalenderView.this, MainActivity.class);
        startActivity(intent);
    }

    public void upDate(View view)
    {
        if(btnchooseData.getText().toString()!="" && editKwota.getText().toString()!="" && editID.getText().toString() !="" )
        {
            myDb.updateDate(editID.getText().toString(), btnchooseData.getText().toString(), editKwota.getText().toString(), what_choose);
            editID.setText("");
            editKwota.setText("");
            btnchooseData.setText("");
            Toast.makeText(CalenderView.this,"Update", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(CalenderView.this,"You have a free space", Toast.LENGTH_LONG).show();
        }
    }


    public void from_money ()
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.choose, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l)
    {
        text_from_money = adapterView.getItemAtPosition(position).toString();

        if(position==0)
        {
            what_choose=0;
        }
        else
        {
            what_choose=1;
        }

        String cos = String.valueOf(what_choose);
        Toast.makeText(adapterView.getContext(), cos, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}
