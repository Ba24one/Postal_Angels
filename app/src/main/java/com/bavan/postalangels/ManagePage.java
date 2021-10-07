package com.bavan.postalangels;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class ManagePage extends AppCompatActivity {

    Button btnstaffviewStatus, btnontheway, btnhome3, btnStaffPage;
    EditText staffpackageCode;
    PostalAngelsDB paDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_page);

        List<String> statuses = Arrays.asList("Warehouse", "On the way", "Delivered");

        Spinner statusview = findViewById(R.id.statusview);

        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.customized_spinner_items, statuses);
        adapter.setDropDownViewResource(R.layout.customized_spinner_items);

        statusview.setAdapter(adapter);

        btnstaffviewStatus = findViewById(R.id.btnstaffviewStatus);
        btnontheway = findViewById(R.id.btnontheway);
        btnhome3 = findViewById(R.id.btnbackhome3);
        btnStaffPage = findViewById(R.id.btngocreate);

        paDB = new PostalAngelsDB(this);

        staffpackageCode = findViewById(R.id.postalpackagecode);

        btnstaffviewStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String packageStatusTXT = statusview.getSelectedItem().toString();

                if(packageStatusTXT.equals("Warehouse") || packageStatusTXT.equals("On the way")){
                    Cursor res = paDB.getStatus(packageStatusTXT);
                    if(res.getCount()==0){
                        Toast.makeText(ManagePage.this, "No packages under this category yet!", Toast.LENGTH_SHORT).show();
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(ManagePage.this);
                    builder.setCancelable(true);
                    builder.setTitle("Package Details");
                    builder.setMessage(buffer.toString());
                    builder.show();
                }
                else{
                    Cursor res = paDB.getStatus(packageStatusTXT);
                    if(res.getCount()==0){
                        Toast.makeText(ManagePage.this, "No packages under this category yet!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    StringBuffer buffer = new StringBuffer();
                    while(res.moveToNext()){
                        buffer.append("Package Code :"+res.getString(0)+"\n");
                        buffer.append("Sender's Address :"+res.getString(1)+"\n");
                        buffer.append("Reciever's Address :"+res.getString(2)+"\n");
                        buffer.append("Recieved Date :"+res.getString(3)+"\n");
                        buffer.append("Delivered Date :"+res.getString(5)+"\n");
                        buffer.append("Status :"+res.getString(4)+"\n\n");
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(ManagePage.this);
                    builder.setCancelable(true);
                    builder.setTitle("Package Details");
                    builder.setMessage(buffer.toString());
                    builder.show();
                }
            }
        });

        btnontheway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String packagecodeTXT = staffpackageCode.getText().toString();

                if(packagecodeTXT.equals("")) {
                    Toast.makeText(ManagePage.this, "Please enter package code", Toast.LENGTH_SHORT).show();
                }
                else{
                    Boolean checkpackage = paDB.checkpackagecode(packagecodeTXT);
                    if(checkpackage==true){
                        Boolean checkpackageupdatestatus = paDB.updateontheway(packagecodeTXT, "On the way");
                        if(checkpackageupdatestatus==true)
                            Toast.makeText(ManagePage.this, "Package status updated", Toast.LENGTH_SHORT).show();

                        else
                            Toast.makeText(ManagePage.this, "Package status NOT updated!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(ManagePage.this, "No package under this code! Please check again", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        btnhome3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btnStaffPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StaffPage.class);
                startActivity(intent);
            }
        });

    }
}