package com.comp380.csun.comp380;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gdfairclough on 2/15/15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    //database details
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "expenseTracker.db";
    private static final String TABLE_EXPENSES = "expenses";

    //column names
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_VENDOR = "vendor";
    private static final String COLUMN_COST = "cost";
    private static final String COLUMN_DATE = "date";


    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        //initialize database
        String CREATE_EXPENSES_TABLE  = "CREATE TABLE " +
                TABLE_EXPENSES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CATEGORY
                + " TEXT," + COLUMN_VENDOR + " TEXT," + COLUMN_COST +
                " REAL," + COLUMN_DATE +" DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(CREATE_EXPENSES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        db.execSQL("DROP TABLE IF EXISTS" + TABLE_EXPENSES);
        onCreate(db);

    }

    //add an expense to the database
    public void addExpense(Expense expense){

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY, expense.getCategory());
        values.put(COLUMN_COST, expense.getCost());
        values.put(COLUMN_VENDOR, expense.getVendor());
        values.put(COLUMN_DATE, expense.getDate());

        //insert data into the writable database
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_EXPENSES, null,values);
        db.close();

    }

    //Gets all the rows of the db on one cursor for ListView
    public Cursor getAllRows() {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_CATEGORY, COLUMN_VENDOR, COLUMN_COST,
                COLUMN_DATE};
        Cursor cursor = db.query(true, TABLE_EXPENSES, columns, null, null,
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    //Gets all String names of table
    public String[] tableNames() {
        String[] names = new String[] {COLUMN_ID, COLUMN_CATEGORY, COLUMN_VENDOR, COLUMN_COST,
                COLUMN_DATE};
        return names;
    }

    //find an expense using this method which generates a SELECT statement
    public Expense findExpense(String category){
        String query = "SELECT * FROM " + TABLE_EXPENSES + " WHERE "+ COLUMN_CATEGORY
                + " = \"" + category + "\" ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Expense expense  = new Expense();

        //move the cursor to the first record returned
        if (cursor.moveToFirst()){
            //why call again?
            //cursor.moveToFirst();
            expense.setID(Integer.parseInt(cursor.getString(0)));
            expense.setCategory(cursor.getString(1));
            expense.setVendor(cursor.getString(2));
            expense.setCost(Float.parseFloat(cursor.getString(3)));
            expense.setDate(cursor.getString(4));

        }else {
            expense = null;
            System.out.println("expense not found");       }
        db.close();
        return expense;
    }

    //delete an expense
    public boolean deleteExpense(String category){
        boolean result = false;

        String query = "SELECT * FROM " + TABLE_EXPENSES +  " WHERE " + COLUMN_ID
                + " = \"" + category + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Expense expense = new Expense();

        if (cursor.moveToFirst()){
            expense.setID(Integer.parseInt(cursor.getString(1)));
            db.delete(TABLE_EXPENSES, COLUMN_ID + "=?",
                    new String[] {String.valueOf(expense.getId())} );
            cursor.close();
            result = true;
        }
        db.close();
        return result;

    }
}