package com.vroomcab;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.vroomcab.DBhandler.DBHandler;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class addyourexpense extends AppCompatActivity {

    EditText categoryEditText, productnameEditText;
    EditText amountEditText;
    Button chooseDateButton;
    Button addExpenseButton;
    Button viewExpenseButton;
    DBHandler dbhandler;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addyourexpense);

        categoryEditText = findViewById(R.id.category);
        amountEditText = findViewById(R.id.amount);
        chooseDateButton = findViewById(R.id.chooseDateButton);
        addExpenseButton = findViewById(R.id.addexpense);
        viewExpenseButton = findViewById(R.id.viewexpense);
        productnameEditText = findViewById(R.id.productname);

        dbhandler = new DBHandler(addyourexpense.this);

        chooseDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
                String userId = sharedPreferences.getString("userid", null);

                String amount = amountEditText.getText().toString().trim();
                String category = categoryEditText.getText().toString().trim();
                String nameofproduct = productnameEditText.getText().toString().trim();
                String manualDateInput = chooseDateButton.getText().toString().trim(); // Use the text from the Button

                if (amount.isEmpty() || category.isEmpty() || manualDateInput.isEmpty() || nameofproduct.isEmpty()) {
                    Toast.makeText(addyourexpense.this, "Details can't be empty. Please enter all details.", Toast.LENGTH_LONG).show();
                    return; // Stop execution if details are empty
                }

                Date date = parseDate(manualDateInput);

                if (date != null) {
                    String formattedDate = formatDate(date);

                    dbhandler.expenses(userId, amount, category, formattedDate, nameofproduct);
                    amountEditText.setText("");
                    categoryEditText.setText("");
                    productnameEditText.setText("");
                    chooseDateButton.setText(getString(R.string.choose_date_hint)); // Reset date button text
                    Toast.makeText(addyourexpense.this, "Expense submitted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(addyourexpense.this, "Invalid date format. Please use yyyy-MM-dd.", Toast.LENGTH_LONG).show();
                }
            }
        });

        viewExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(addyourexpense.this, viewexpense.class);
                startActivity(intent);
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String selecteddate = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                chooseDateButton.setText(selecteddate);
            }
        },
           year,
           month,
           day
        );
        datePickerDialog.show();
    }

    private Date parseDate(String inputDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            return sdf.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return sdf.format(date);
    }
}
