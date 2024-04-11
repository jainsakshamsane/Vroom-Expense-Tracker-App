package com.vroomcab;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vroomcab.DBhandler.DBHandler;
import com.vroomcab.Models.expensess;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ReportsActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private TextView totalExpensesTextView;
    private ImageView back;
    private Button selectDateButton;
    private TextView selectMonthButton;
    private String selectedDate, selectedMonth;
    private SimpleDateFormat indianDateFormat;
    private RecyclerView recyclerViewExpenses;
    private ExpensesAdapter expensesAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.total1);

        dbHandler = new DBHandler(this);
        totalExpensesTextView = findViewById(R.id.totalExpensesTextView);
        selectDateButton = findViewById(R.id.selectDateButton);
        selectMonthButton = findViewById(R.id.selectMonthButton);
        back = findViewById(R.id.back);
        recyclerViewExpenses = findViewById(R.id.recyclerViewExpenses);

        indianDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        indianDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        recyclerViewExpenses.setLayoutManager(new LinearLayoutManager(this));
        expensesAdapter = new ExpensesAdapter(new ArrayList<>());
        recyclerViewExpenses.setAdapter(expensesAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the calctotal button click, for example, navigate to the register activity
                Intent intent = new Intent(ReportsActivity.this, viewexpense.class);
                startActivity(intent);
            }
        });

        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show a date picker dialog
                showDatePickerDialog();
            }
        });

        selectMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show a month picker dialog
                showMonthPickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        calendar.set(selectedYear, selectedMonth, selectedDay);
                        selectedDate = indianDateFormat.format(calendar.getTime());

                        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
                        String userId = sharedPreferences.getString("userid", null);

                        if (userId != null) {
                            int intUserId = Integer.parseInt(userId);

                            List<expensess> expenseList = dbHandler.getDailyExpenseDetails(selectedDate, intUserId);
                            expensesAdapter.setExpensesList(expenseList);
                            expensesAdapter.notifyDataSetChanged();

                            double totalExpenses = dbHandler.getDailyTotalExpenses(selectedDate, intUserId);
                            totalExpensesTextView.setText(Html.fromHtml("Total Expenses for " + selectedDate + ": <br><div style='text-align: center;'><font color='red'><b>Rs. " + totalExpenses + "</b></font></div>"));
                        } else {
                            // Handle the case where user ID is not available in SharedPreferences
                        }
                    }
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    private void showMonthPickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(selectedYear, selectedMonth, selectedDay);

                        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM", Locale.US);
                        monthFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

                        String selectedMonths = monthFormat.format(selectedCalendar.getTime());

                        // Ensure that dbHandler is initialized before using it
                        DBHandler dbHandler = new DBHandler(getApplicationContext());
                        double totalExpenses = dbHandler.getMonthlyTotalExpenses(selectedMonths, String.valueOf(selectedYear));
                        totalExpensesTextView.setText("Total Expenses: $" + totalExpenses);

                        // Don't forget to close the database handler when done
                        dbHandler.close();
                    }
                },
                year,
                month,
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Set date picker mode to MONTH
        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        datePickerDialog.getDatePicker().setSpinnersShown(true);

        // Set the month range from 1 to 12
        datePickerDialog.getDatePicker().setMinDate(getDateInMillis(1, year, calendar.get(Calendar.DAY_OF_MONTH)));
        datePickerDialog.getDatePicker().setMaxDate(getDateInMillis(12, year, calendar.get(Calendar.DAY_OF_MONTH)));

        datePickerDialog.show();
    }

    // Helper method to get the date in milliseconds
    private long getDateInMillis(int month, int year, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month - 1); // Calendar months are 0-based
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTimeInMillis();
    }

    public void callDeveloper(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:8077983770"));
        startActivity(intent);
    }
}
