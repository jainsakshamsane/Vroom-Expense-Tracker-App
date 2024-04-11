package com.vroomcab;// ExpenseAdapter.java
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vroomcab.Models.expensess;

import java.util.List;

public class ExpensseAdapter extends RecyclerView.Adapter<ExpensseAdapter.ViewHolder> {

    private List<expensess> expenseList;

    public ExpensseAdapter(List<expensess> expenseList) {
        this.expenseList = expenseList;
    }

    public void updateData(List<expensess> newExpenseList) {
        expenseList.clear();
        expenseList.addAll(newExpenseList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        expensess expense = expenseList.get(position);
        holder.bind(expense);
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView amountTextView, categoryTextView, dateTextView, pronameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            pronameTextView = itemView.findViewById(R.id.productnameTextView);
        }

        public void bind(expensess expense) {
            amountTextView.setText("Amount: ₹" + expense.getAmount());
            categoryTextView.setText("Category: " + expense.getCategory());
            dateTextView.setText("Date: " + expense.getDate());
            pronameTextView.setText("Product Name: " + expense.getProductname());
        }
    }
}
