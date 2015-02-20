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
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "expenseTracker.db";
    private static final String TABLE_EXPENSES = "expenses";
    private static final String TABLE_CATEGORIES = "categories";

    //column names
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_VENDOR = "vendor";
    private static final String COLUMN_COST = "cost";
    private static final String COLUMN_DATE = "date";

    //category table column names
    private static final String COLUMN_CATID = "category_id";
    private static final String COLUMN_CATDESC = "category_desc";


    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    //called when database is first created
    public void onCreate(SQLiteDatabase db) {

        //ensure that foreign keys are activated
        String FOREIGN_KEYS_ON = "PRAGMA foreign_keys = ON";

        //----------initialize database tables ---------------

        //initialize expenses table
        //TODO change COLUMN_CATEGORY to an integer field
        String CREATE_EXPENSES_TABLE = "CREATE TABLE " +
                TABLE_EXPENSES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CATEGORY
                + " TEXT," + COLUMN_VENDOR + " TEXT," + COLUMN_COST +
                " REAL," + COLUMN_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY(" + COLUMN_CATEGORY + ") REFERENCES " + TABLE_CATEGORIES +
                "("+ COLUMN_CATID + "))";

        //initialize categories table
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " +
                TABLE_CATEGORIES + "("
                + COLUMN_CATID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CATDESC
                + " TEXT UNIQUE)";

        // ---------------------------------------------------

        //execute the above sql statements
        db.execSQL(FOREIGN_KEYS_ON);
        db.execSQL(CREATE_EXPENSES_TABLE);
        db.execSQL(CREATE_CATEGORIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
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
    public void textCategoryValues(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues(5);
        values.put(COLUMN_CATDESC, "Transportation");
        db.insert(TABLE_CATEGORIES,null,values);
        values.put(COLUMN_CATDESC, "Travel");
        db.insert(TABLE_CATEGORIES,null,values);
        values.put(COLUMN_CATDESC, "Gas");
        db.insert(TABLE_CATEGORIES,null,values);
        values.put(COLUMN_CATDESC, "Food");
        db.insert(TABLE_CATEGORIES,null,values);
        values.put(COLUMN_CATDESC, "Healthcare");
        db.insert(TABLE_CATEGORIES,null,values);

    }

    public String[] getCategoriesStrings(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_CATDESC + " FROM " + TABLE_CATEGORIES,null);
        if (cursor != null){
            //store category text to a string
            int i = 0;
            String[] categories = new String[cursor.getCount()];
            while(cursor.moveToNext()){
                String category = cursor.getString(cursor.getColumnIndex(COLUMN_CATDESC));
                categories[i] = category;
                i++;
            }
            db.close();
            return categories;
        }
        System.out.print("Error reading categories to string");
        db.close();
        return null;

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