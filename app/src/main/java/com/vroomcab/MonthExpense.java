package com.vroomcab;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vroomcab.DBhandler.DBHandler;
import com.vroomcab.Models.expensess;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MonthExpense extends AppCompatActivity {

    private DatePicker fromDatePicker, toDatePicker;
    private DBHandler dbHandler;
    private ImageView back;
    private TextView totalExpensesTextView;
    private RecyclerView recyclerViewExpenses;
    private ExpensseAdapter expenseAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthexpense);

        fromDatePicker = findViewById(R.id.fromDatePicker);
        toDatePicker = findViewById(R.id.toDatePicker);
        totalExpensesTextView = findViewById(R.id.totalExpensesTextView);
        recyclerViewExpenses = findViewById(R.id.recyclerViewExpenses);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the calctotal button click, for example, navigate to the register activity
                Intent intent = new Intent(MonthExpense.this, viewexpense.class);
                startActivity(intent);
            }
        });

        dbHandler = new DBHandler(this);
        expenseAdapter = new ExpensseAdapter(new ArrayList<>());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewExpenses.setLayoutManager(layoutManager);
        recyclerViewExpenses.setAdapter(expenseAdapter);
    }

    public void calculateExpenses(View view) {

        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userid", null);

        // Get selected "from" and "to" dates
        String fromDate = getFormattedDate(fromDatePicker);
        String toDate = getFormattedDate(toDatePicker);

        // Implement logic to calculate expenses based on the date range
        double totalExpenses = calculateTotalExpenses(fromDate, toDate);

        // Display or use the totalExpenses as needed
        totalExpensesTextView.setText(Html.fromHtml("From Date : " + fromDate + " To Date : " + toDate + "<br><div style='text-align: center;'><font color='red'><b>Rs. " + totalExpenses + "</b></font></div>"));

        // Load and display expenses list within the selected date range
        List<expensess> expensesList = loadExpensesList(userId, fromDate, toDate);
        expenseAdapter.updateData(expensesList);
    }

    private String getFormattedDate(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1; // Month is 0-based
        int year = datePicker.getYear();

        return String.format(Locale.US, "%04d-%02d-%02d", year, month, day);
    }

    private double calculateTotalExpenses(String fromDate, String toDate) {
        return dbHandler.calculateExpenses(fromDate, toDate);
    }

    private List<expensess> loadExpensesList(String userId, String fromDate, String toDate) {
        return dbHandler.getAllExpenseDataWithinDateRange(userId, fromDate, toDate);
    }
}
