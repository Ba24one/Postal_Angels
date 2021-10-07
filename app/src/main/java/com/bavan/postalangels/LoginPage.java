package com.bavan.postalangels;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class LoginPage extends AppCompatActivity {

    EditText inputusername, inputpassword;
    Button btnlogin;
    PostalAngelsDB paDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        inputusername = findViewById(R.id.inputusername);
        inputpassword = findViewById(R.id.inputpassword);

        btnlogin = findViewById(R.id.btnlogin);

        paDB = new PostalAngelsDB(this);

        List<String> department = Arrays.asList("Staff", "Postal");

        Spinner inputdepartment = findViewById(R.id.statusview);

        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.customized_spinner_items, department);
        adapter.setDropDownViewResource(R.layout.customized_spinner_items);

        inputdepartment.setAdapter(adapter);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usernameTXT = inputusername.getText().toString();
                String passwordTXT = inputpassword.getText().toString();
                String departmentTXT = inputdepartment.getSelectedItem().toString();

                Boolean checkinsertdata1 = paDB.insertemployeedata("Adam", "a123", "Staff");
                Boolean checkinsertdata2 = paDB.insertemployeedata("Chris", "c123", "Postal");
                Boolean checkinsertdata3 = paDB.insertemployeedata("Messi", "m123", "Staff");
                Boolean checkinsertdata4 = paDB.insertemployeedata("Ronaldo", "r123", "Postal");

                Boolean checkuserpass = paDB.checkusernamepassword(usernameTXT, passwordTXT, departmentTXT);
                if(checkuserpass==true){
                    Toast.makeText(LoginPage.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    if(departmentTXT.equals("Staff")){
                        Intent intent = new Intent(getApplicationContext(), StaffPage.class);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(getApplicationContext(), PostalPage.class);
                        startActivity(intent);
                    }
                } else{
                    Toast.makeText(LoginPage.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}