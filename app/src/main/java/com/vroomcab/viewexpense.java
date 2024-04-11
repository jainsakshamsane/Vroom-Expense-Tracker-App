package com.vroomcab;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.vroomcab.DBhandler.DBHandler;
import com.vroomcab.Models.expensess;

import java.util.List;

public class viewexpense extends AppCompatActivity implements ExpenseAdapter.OnEventItemClickListener {

    RecyclerView recyclerView;
    DBHandler dbHandler;
    TextView amount;
    ImageView back;
    TextView category;
    TextView date;
    Button calctotal, calctotalbutton;
    Button logout;
     String  userId;

    private LinearLayout contentLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewexpense);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        contentLayout = findViewById(R.id.layout123);

        dbHandler = new DBHandler(this);
        category = findViewById(R.id.category);
        amount = findViewById(R.id.amount);
        date = findViewById(R.id.date);
        back = findViewById(R.id.back);
        calctotal = findViewById(R.id.calctotal);
        calctotalbutton = findViewById(R.id.calctotalmonth);
        logout = findViewById(R.id.logout);

        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userid", null);

        List<expensess> myexpensess = dbHandler.getAllExpenseData(userId);
        if (myexpensess.size() > 0) {
            ExpenseAdapter exp1 = new ExpenseAdapter(myexpensess, this);
            exp1.setOnEventItemClickListener(this);
            recyclerView.setAdapter(exp1);
        } else {
            Toast.makeText(this, "There is no event yet", Toast.LENGTH_SHORT).show();
        }

        calctotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the calctotal button click, for example, navigate to the register activity
                Intent intent = new Intent(viewexpense.this, ReportsActivity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the calctotal button click, for example, navigate to the register activity
                Intent intent = new Intent(viewexpense.this, addyourexpense.class);
                startActivity(intent);
            }
        });

        calctotalbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the calctotal button click, for example, navigate to the register activity
                Intent intent = new Intent(viewexpense.this, MonthExpense.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedpreferences = getSharedPreferences("my preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(view.getContext(), welcomepage.class);
                startActivity(intent);
                Toast.makeText(viewexpense.this, "Logged out", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEventItemClick(String eventId) {
        // Handle item click, for now, let's open the MainActivity
        Intent intent = new Intent(viewexpense.this, MainActivity.class);
        intent.putExtra("eventId", eventId);
        startActivity(intent);
    }
}
