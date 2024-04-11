package com.vroomcab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.vroomcab.DBhandler.DBHandler;
import com.vroomcab.Models.expensess;
import java.util.ArrayList;
import java.util.List;

public class total extends AppCompatActivity {

    private Button totalButton;
    private DatePicker datePicker;
    private TextView totalTextView;

    DBHandler dbHandler;

    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.total);

        totalButton = findViewById(R.id.totalButton);
        datePicker = findViewById(R.id.datePicker);
        totalTextView = findViewById(R.id.totalTextView);
        logout = findViewById(R.id.logout);

        totalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = datePicker.getYear();
                int month = datePicker.getMonth() + 1; // Month is zero-based
                int day = datePicker.getDayOfMonth();
                String selectedDate = String.format("%04d-%02d-%02d", year, month, day);

                double totalAmount = dbHandler.displayTotalForDate(selectedDate);

                // Update the TextView with the total amount
                TextView totalAmountTextView = findViewById(R.id.totalTextView);
                totalAmountTextView.setText("Total Amount for " + selectedDate + ": " + totalAmount);

                Log.e("Expense data", "Total Amount for " + selectedDate + ": " + totalAmount);
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedpreferences = getSharedPreferences("my preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(view.getContext(), login.class);
                startActivity(intent);
            }
        });
    }
}
