package com.vroomcab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vroomcab.DBhandler.DBHandler;
import com.vroomcab.Models.expensess;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    private List<expensess> myexpenses;
    private Context context;
    private DBHandler dbHandler;

    public ExpenseAdapter(List<expensess> myexpenses, Context context) {
        this.context = context;
        this.myexpenses = myexpenses;
        dbHandler = new DBHandler(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.expense_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final expensess myexpensess = myexpenses.get(position);
        holder.amount.setText("₹" + myexpensess.getAmount() + "/-");
        holder.category.setText(myexpensess.getCategory());
        holder.date.setText(myexpensess.getDate());
        holder.proname.setText(myexpensess.getProductname());
    }

    @Override
    public int getItemCount() {
        return myexpenses.size();
    }

    public void setOnEventItemClickListener(viewexpense viewexpense) {
    }

    public interface OnEventItemClickListener {
        void onEventItemClick(String expense);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView amount;
        TextView category;
        TextView date;
        TextView proname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.amount);
            category = itemView.findViewById(R.id.category);
            date = itemView.findViewById(R.id.date);
            proname = itemView.findViewById(R.id.productname);
        }
    }
}
