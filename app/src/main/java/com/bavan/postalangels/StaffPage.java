package com.bavan.postalangels;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
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

public class StaffPage extends AppCompatActivity {

    EditText packageCode, senderAddress, recieverAddress;
    TextView recievedDate;
    Button btncreate, btnhome, btnmanage;
    DatePickerDialog.OnDateSetListener setListener;
    PostalAngelsDB paDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_page);

        packageCode = findViewById(R.id.packageCode);
        senderAddress = findViewById(R.id.senderAddress);
        recieverAddress = findViewById(R.id.postalpackagecode);
        recievedDate = findViewById(R.id.recievedDate);

        btncreate = findViewById(R.id.btnontheway);
        btnmanage = findViewById(R.id.btnmanage);
        btnhome = findViewById(R.id.btnbackhome2);

        paDB = new PostalAngelsDB(this);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int date = calendar.get(Calendar.DATE);

        recievedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        StaffPage.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth
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
                recievedDate.setText(fulldate);
            }
        };

        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String packageCodeTXT = packageCode.getText().toString();
                String senderAddressTXT = senderAddress.getText().toString();
                String recieverAddressTXT = recieverAddress.getText().toString();
                String recievedDateTXT = recievedDate.getText().toString();

                if(packageCodeTXT.equals("") || senderAddressTXT.equals("") || recieverAddressTXT.equals("") || recievedDateTXT.equals("")){
                    Toast.makeText(StaffPage.this, "Please fill out all fields!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Boolean checkpackage = paDB.checkpackagecode(packageCodeTXT);
                    if(checkpackage==false){
                        Boolean insertpackage = paDB.createpackagedetails(packageCodeTXT, senderAddressTXT, recieverAddressTXT, recievedDateTXT, "Warehouse");
                        if(insertpackage==true){
                            Toast.makeText(StaffPage.this, "New Package Created", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(StaffPage.this, "New Package Not Created", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(StaffPage.this, "Package already exists! please check again", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        btnmanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManagePage.class);
                startActivity(intent);
            }
        });

        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }
}