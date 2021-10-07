package com.bavan.postalangels;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class PostalPage extends AppCompatActivity {
    EditText postalpackagecode, senderAddress, recieverAddress;
    Button btnviewpending, btndelivered, btnhome4;
    TextView deliveredDate;
    DatePickerDialog.OnDateSetListener setListener;
    PostalAngelsDB paDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postal_page);

        postalpackagecode = findViewById(R.id.postalpackagecode);

        btnviewpending = findViewById(R.id.btnviewpending);
        btndelivered = findViewById(R.id.btndelivered);
        btnhome4 = findViewById(R.id.btnbackhome4);

        deliveredDate = findViewById(R.id.deliveredDate);

        paDB = new PostalAngelsDB(this);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int date = calendar.get(Calendar.DATE);

        deliveredDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        PostalPage.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        ,setListener,year,month,date);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();

            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int date) {
                month = month+1;
                String fulldate = date+"/"+month+"/"+year;
                deliveredDate.setText(fulldate);
            }
        };

        btnviewpending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = paDB.getpendingStatus();
                if(res.getCount()==0){
                    Toast.makeText(PostalPage.this, "Hooray! No Pending Deliveries", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()){
                    buffer.append("Package Code :"+res.getString(0)+"\n");
                    buffer.append("Sender's Address :"+res.getString(1)+"\n");
                    buffer.append("Reciever's Address :"+res.getString(2)+"\n");
                    buffer.append("Recieved Date :"+res.getString(3)+"\n");
                    buffer.append("Status :"+res.getString(4)+"\n\n");
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(PostalPage.this);
                builder.setCancelable(true);
                builder.setTitle("Package Details");
                builder.setMessage(buffer.toString());
                builder.show();
            }
        });

        btndelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postalpackagecodeTXT = postalpackagecode.getText().toString();
                String deliveredDateTXT = deliveredDate.getText().toString();

                if(postalpackagecodeTXT.equals("") || deliveredDateTXT.equals("")) {
                    Toast.makeText(PostalPage.this, "Please enter all fields!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Boolean checkpackage = paDB.checkpackagecode(postalpackagecodeTXT);
                    if(checkpackage==true){
                        Boolean checkpackagedeliveredstatus = paDB.updatedelivered(postalpackagecodeTXT, deliveredDateTXT, "Delivered");
                        if(checkpackagedeliveredstatus==true)
                            Toast.makeText(PostalPage.this, "Package status updated", Toast.LENGTH_SHORT).show();

                        else
                            Toast.makeText(PostalPage.this, "Package status NOT updated! Please make sure to update only pending packages", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(PostalPage.this, "No package under this code! Please check again", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnhome4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}