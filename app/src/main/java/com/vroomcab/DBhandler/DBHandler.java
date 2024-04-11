package com.vroomcab.DBhandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vroomcab.Models.expensess;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {


    private static final String DB_NAME = "vroomcab";
    private static final int DB_VERSION = 4;
    private static final String TABLE_USER = "user";
    private static final String USERID_COL = "userid";
    private static final String NAME_COL = "name";
    private static final String PHONE_COL = "phone";
    private static final String EMAIL_COL = "email";
    private static final String PASSWORD_COL = "password";
    private static final String CREATED_AT_COL = "created_at";


    private static final String TABLE_EXPENSES = "expense";
    private static final String EXPENSE_ID_COL = "expense";
    private static final String EXPENSE_USERID_COL = "userid";
    private static final String AMOUNT_COL = "amount";
    private static final String CATEGORY_COL = "category";
    private static final String PRODUCTNAME_COL = "productname";
    private static final String DATE_COL = "date";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_USER + " ("
                + USERID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + PHONE_COL + " TEXT,"
                + EMAIL_COL + " TEXT,"
                + PASSWORD_COL + " TEXT, "
                + CREATED_AT_COL + " TEXT)";
        db.execSQL(query);

        String query1 = "CREATE TABLE " + TABLE_EXPENSES + " ("
                + EXPENSE_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EXPENSE_USERID_COL + " TEXT,"
                + AMOUNT_COL + " TEXT, "
                + CATEGORY_COL + " TEXT,"
                + PRODUCTNAME_COL + " TEXT,"
                + DATE_COL + " TEXT)";
        db.execSQL(query1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        onCreate(db);
    }

    public void register(String name, String phone, String email, String password, String dates) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COL, name);
        values.put(PHONE_COL, phone);
        values.put(EMAIL_COL, email);
        values.put(PASSWORD_COL, password);
        values.put(CREATED_AT_COL, dates);
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public String login(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String userRole = null;
        String sql = "SELECT * FROM user WHERE email = '" + email + "' AND password = '" + password + "' ;";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            userRole = cursor.getString(cursor.getColumnIndexOrThrow("userid"));
        }
        return userRole;
    }


    public void expenses(String userid, String amount, String category, String date, String pname) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(EXPENSE_USERID_COL, userid);
            values.put(AMOUNT_COL, amount);
            values.put(CATEGORY_COL, category);
            values.put(DATE_COL, date);
            values.put(PRODUCTNAME_COL, pname);

            db.insert(TABLE_EXPENSES, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }


    public List<expensess> getAllExpenseData(String userId) {
        String sql = "SELECT * FROM " + TABLE_EXPENSES + " WHERE " + EXPENSE_USERID_COL + " = ? ORDER BY " + DATE_COL + " DESC";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        List<expensess> storeexpense = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{userId});

        if (cursor.moveToFirst()) {
            do {
                String expense = cursor.getString(cursor.getColumnIndexOrThrow(EXPENSE_ID_COL));
                String amount = cursor.getString(cursor.getColumnIndexOrThrow(AMOUNT_COL));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY_COL));
                String proname = cursor.getString(cursor.getColumnIndexOrThrow(PRODUCTNAME_COL));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DATE_COL));

                storeexpense.add(new expensess(expense, amount, category, date, proname));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return storeexpense;
    }


    public double displayTotalForDate(String selectedDate) {

        String sql = "SELECT * FROM " + TABLE_EXPENSES + " WHERE " + EXPENSE_USERID_COL + " = ? AND date = ?";
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        List<expensess> storeexpense = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{selectedDate});

        double totalAmount = 0.0;

        if (cursor.moveToFirst()) {
            do {
                String expense = cursor.getString(cursor.getColumnIndexOrThrow("expense"));
                String amount = cursor.getString(cursor.getColumnIndexOrThrow("amount"));
                String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                String proname = cursor.getString(cursor.getColumnIndexOrThrow("productname"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));

                totalAmount += Double.parseDouble(amount);

                storeexpense.add(new expensess(expense, amount, category, date, proname));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return totalAmount;
    }

    public double getDailyTotalExpenses(String date, int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        double dailyTotalExpenses = 0;

        Cursor cursor = db.rawQuery("SELECT SUM(" + AMOUNT_COL + ") FROM " + TABLE_EXPENSES +
                        " WHERE " + DATE_COL + " = ? AND " + EXPENSE_USERID_COL + " = ?",
                new String[]{date, String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            dailyTotalExpenses = cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return dailyTotalExpenses;
    }


    public double getMonthlyTotalExpenses(String year, String month) {
        SQLiteDatabase db = this.getReadableDatabase();
        double monthlyTotalExpenses = 0;

        String formattedMonth = String.format("%02d", Integer.parseInt(month));
        String formattedYear = String.valueOf(year);

        Cursor cursor = db.rawQuery("SELECT SUM(" + AMOUNT_COL + ") FROM " + TABLE_EXPENSES +
                        " WHERE strftime('%m-%Y', " + DATE_COL + ") = ?",
                new String[]{formattedMonth + "-" + formattedYear});

        if (cursor.moveToFirst()) {
            monthlyTotalExpenses = cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return monthlyTotalExpenses;
    }

    public List<expensess> getDailyExpenseDetails(String date, int userId) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        List<expensess> dailyExpenses = new ArrayList<>();

        String sql = "SELECT * FROM " + TABLE_EXPENSES +
                " WHERE " + EXPENSE_USERID_COL + " = ?" +
                " AND " + DATE_COL + " = ?" +
                " ORDER BY " + DATE_COL + " ASC";

        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{String.valueOf(userId), date});

        if (cursor.moveToFirst()) {
            do {
                // Retrieve expense details from the cursor
                String expenseId = cursor.getString(cursor.getColumnIndexOrThrow(EXPENSE_ID_COL));
                String amount = cursor.getString(cursor.getColumnIndexOrThrow(AMOUNT_COL));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY_COL));
                String proname = cursor.getString(cursor.getColumnIndexOrThrow(PRODUCTNAME_COL));
                String expenseDate = cursor.getString(cursor.getColumnIndexOrThrow(DATE_COL));

                dailyExpenses.add(new expensess(expenseId, amount, category, expenseDate, proname));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return dailyExpenses;
    }

    public double calculateExpenses(String fromDate, String toDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalExpenses = 0;

        Cursor cursor = db.rawQuery("SELECT SUM(" + AMOUNT_COL + ") FROM " + TABLE_EXPENSES +
                        " WHERE " + DATE_COL + " BETWEEN ? AND ?",
                new String[]{fromDate, toDate});

        if (cursor.moveToFirst()) {
            totalExpenses = cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return totalExpenses;
    }

    public List<expensess> getAllExpenseDataWithinDateRange(String userId, String fromDate, String toDate) {
        String sql = "SELECT * FROM " + TABLE_EXPENSES + " WHERE " + EXPENSE_USERID_COL + " = ? AND " +
                DATE_COL + " BETWEEN ? AND ? ORDER BY " + DATE_COL + " DESC";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        List<expensess> storeExpense = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{userId, fromDate, toDate});

        if (cursor.moveToFirst()) {
            do {
                String expenseId = cursor.getString(cursor.getColumnIndexOrThrow(EXPENSE_ID_COL));
                String amount = cursor.getString(cursor.getColumnIndexOrThrow(AMOUNT_COL));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY_COL));
                String proname = cursor.getString(cursor.getColumnIndexOrThrow(PRODUCTNAME_COL));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DATE_COL));

                storeExpense.add(new expensess(expenseId, amount, category, date, proname));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return storeExpense;
    }
}