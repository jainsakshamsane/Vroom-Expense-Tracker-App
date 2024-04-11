package com.vroomcab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vroomcab.Models.expensess;

import java.util.List;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ExpenseViewHolder> {
    private List<expensess> expensesList;

    public ExpensesAdapter(List<expensess> expensesList) {
        this.expensesList = expensesList;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_expense, parent, false);
        return new ExpenseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        expensess expense = expensesList.get(position);

        holder.textViewAmount.setText("Amount: ₹" + expense.getAmount());
        holder.textViewCategory.setText("Category: " + expense.getCategory());
        holder.textViewProduct.setText("ProductName: " + expense.getProductname());
    }

    public void setExpensesList(List<expensess> expensesList) {
        this.expensesList = expensesList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return expensesList.size();
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAmount, textViewCategory, textViewProduct;

        ExpenseViewHolder(View itemView) {
            super(itemView);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewProduct = itemView.findViewById(R.id.textViewProduct);
        }
    }
}
