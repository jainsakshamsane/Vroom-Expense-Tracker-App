package com.vroomcab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import com.vroomcab.DBhandler.DBHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class register extends AppCompatActivity {

    EditText name;
    EditText phone;
    EditText email;
    EditText password;
    Button register;
    TextView message;

    private AppBarConfiguration appBarConfiguration;

    DBHandler dbhandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        message = findViewById(R.id.message);
        dbhandler = new DBHandler(register.this);

        EditText name = findViewById(R.id.name);
        EditText phone = findViewById(R.id.phone);

        String namePattern = "^[A-Za-z]+$";
        String phonePattern = "^[0-9]+$";

        String nameText = name.getText().toString();
        String phoneText = phone.getText().toString();

        Pattern namePatternCompiled = Pattern.compile(namePattern);
        Matcher nameMatcher = namePatternCompiled.matcher(nameText);

        Pattern phonePatternCompiled = Pattern.compile(phonePattern);
        Matcher phoneMatcher = phonePatternCompiled.matcher(phoneText);

        if (!nameMatcher.matches()) {
            name.setError("Please enter a valid name with only alphabets.");
        }

        if (!phoneMatcher.matches()) {
            phone.setError("Please enter a valid phone number with only numbers.");
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                long times = System.currentTimeMillis(); //to get the current time
                SimpleDateFormat dates = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()); //pattern of date to show
                String formats = dates.format(new Date(times)); // creating string formats to store the date in it.

                dbhandler.register(
                        name.getText().toString(),
                        phone.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString(),
                        formats
                );
                Toast.makeText(register.this, "Succesfully signedup", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(register.this, login.class);
                startActivity(intent);
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(register.this, login.class);
                startActivity(intent);
            }
        });
    }

}
