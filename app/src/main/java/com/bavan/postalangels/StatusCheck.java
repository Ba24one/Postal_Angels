package com.bavan.postalangels;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StatusCheck extends AppCompatActivity {

    Button btntrack, btnbackhome;
    EditText inputcheckstatus;
    TextView statustxt;
    ImageView notrecievedimage, warehouseimage, onthewayimage, deliveredimage, welcomeimage;
    PostalAngelsDB paDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_check);

        btnbackhome = findViewById(R.id.btnbackhome);
        btntrack = findViewById(R.id.btntrack);

        inputcheckstatus = findViewById(R.id.inputcheckstatus);

        statustxt = findViewById(R.id.statustxt);

        notrecievedimage = findViewById(R.id.notrecievedimage);
        warehouseimage = findViewById(R.id.warehouseimage);
        onthewayimage = findViewById(R.id.onthewayimage);
        deliveredimage = findViewById(R.id.deliveredimage);
        welcomeimage = findViewById(R.id.welcomeimage);

        paDB = new PostalAngelsDB(this);


        btntrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputcheckstatusTXT = inputcheckstatus.getText().toString();

                notrecievedimage.setVisibility(View.INVISIBLE);
                warehouseimage.setVisibility(View.INVISIBLE);
                onthewayimage.setVisibility(View.INVISIBLE);
                deliveredimage.setVisibility(View.INVISIBLE);
                welcomeimage.setVisibility(View.INVISIBLE);

                if(inputcheckstatusTXT.equals("")) {
                    Toast.makeText(StatusCheck.this, "Please enter your package code", Toast.LENGTH_SHORT).show();
                    welcomeimage.setVisibility(View.VISIBLE);
                }
                else{
                    Boolean checkpackage = paDB.checkpackagecode(inputcheckstatusTXT);
                    if(checkpackage==true) {
                        Cursor res = paDB.customerpackagestatus(inputcheckstatusTXT);
                        if (res.getCount() == 0) {
                            notrecievedimage.setVisibility(View.VISIBLE);
                            statustxt.setText("OOPS!\nTry contacting the head office");
                            Toast.makeText(StatusCheck.this, "Your package is not yet recieved!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        while (res.moveToNext()) {
                            String STATUS = res.getString(4);
                            statustxt.setText(STATUS);

                            if (STATUS.equals("Warehouse")) {
                                warehouseimage.setVisibility(View.VISIBLE);
                            } else if (STATUS.equals("On the way")) {
                                onthewayimage.setVisibility(View.VISIBLE);
                            } else if (STATUS.equals("Delivered")) {
                                deliveredimage.setVisibility(View.VISIBLE);
                            } else {
                                welcomeimage.setVisibility(View.VISIBLE);
                                statustxt.setText("Welcome!");
                            }
                        }
                    }
                    else{
                        notrecievedimage.setVisibility(View.VISIBLE);
                        statustxt.setText("OOPS!\nTry contacting the head office");
                        Toast.makeText(StatusCheck.this, "Your package is not yet recieved!", Toast.LENGTH_SHORT).show();

                    }
                }

            }

        });


        btnbackhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }
}