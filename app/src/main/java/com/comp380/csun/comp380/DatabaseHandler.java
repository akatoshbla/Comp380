package com.comp380.csun.comp380;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLDataException;
import java.sql.SQLException;

/**
 * Created by gdfairclough on 2/15/15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    //database details
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "expenseTracker.db";
    private static final String TABLE_EXPENSES = "expenses";
    private static final String TABLE_CATEGORIES = "categories";
    private static final String TABLE_VENDORS = "vendors";
    private static final String TABLE_PASSWORD = "password";

    //column names
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_VENDOR = "vendor";
    private static final String COLUMN_COST = "cost";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_PASSWORD = "passwords";

    //category table column names
    private static final String COLUMN_CATID = "category_id";
    private static final String COLUMN_CATDESC = "category_desc";

    //vendors table column names
    private static final String COLUMN_VENDID = "vendor_id";
    private static final String COLUMN_VENDDESC = "vendor_desc";


    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    //called when database is first created
    public void onCreate(SQLiteDatabase db) {



        //----------initialize database tables ---------------

        //strings to initialize database tables
        String CREATE_EXPENSES_TABLE = "CREATE TABLE " +
                TABLE_EXPENSES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CATEGORY
                + " Integer," + COLUMN_VENDOR + " INTEGER," + COLUMN_COST +
                " REAL," + COLUMN_DATE + " DATE DEFAULT CURRENT_DATE NOT NULL,"+
                "FOREIGN KEY(" + COLUMN_CATEGORY + ") REFERENCES " + TABLE_CATEGORIES +
                "("+ COLUMN_CATID + "), " +
                "FOREIGN KEY(" + COLUMN_VENDOR + ") REFERENCES " + TABLE_VENDORS +
                "("+ COLUMN_VENDID + ") )";

        //initialize categories table
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " +
                TABLE_CATEGORIES + "("
                + COLUMN_CATID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CATDESC
                + " TEXT UNIQUE)";

        //password table
        String CREATE_PASSWORD_TABLE  = "CREATE TABLE " +
                TABLE_PASSWORD + "(" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_PASSWORD
                + " VARCHAR(255)" + ")";

        //vendors table
        String CREATE_VENDORS_TABLE = "CREATE TABLE " +
                TABLE_VENDORS + "(" + COLUMN_VENDID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_VENDDESC
                + " VARCHAR(255) UNIQUE" + ")";

        // ---------------------------------------------------

        //execute the above sql statements

        db.execSQL(CREATE_EXPENSES_TABLE);
        db.execSQL(CREATE_CATEGORIES_TABLE);
        db.execSQL(CREATE_VENDORS_TABLE);
        db.execSQL(CREATE_PASSWORD_TABLE);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {

        //ensure that foreign keys are activated
        String FOREIGN_KEYS_ON = "PRAGMA foreign_keys = ON";
        db.execSQL(FOREIGN_KEYS_ON);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASSWORD);
        onCreate(db);

    }

    //add an expense to the database
    public void addExpense(Expense expense){

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY, getCategoryID(expense.getCategory()));
        values.put(COLUMN_COST, expense.getCost());
        values.put(COLUMN_VENDOR, getVendorID(expense.getVendor()));
        if (expense.getDate() != null){
            values.put(COLUMN_DATE, expense.getDate());
        }

        //insert data into the writable database
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_EXPENSES, null,values);
        db.close();

    }
    public void testCategoryValues(){
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

    public void testExpenseValues(){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //insert test expenses
        values.put(COLUMN_CATEGORY, "4");
        values.put(COLUMN_COST, "10");
        values.put(COLUMN_VENDOR, "1");
        values.put(COLUMN_DATE, "02/11/15");
        db.insert(TABLE_EXPENSES, null,values);

        values.put(COLUMN_CATEGORY, "2");
        values.put(COLUMN_COST, "400");
        values.put(COLUMN_VENDOR, "2");
        values.put(COLUMN_DATE, "");
        db.insert(TABLE_EXPENSES, null,values);

        values.put(COLUMN_CATEGORY, "3");
        values.put(COLUMN_COST, "28");
        values.put(COLUMN_VENDOR, "3");
        values.put(COLUMN_DATE, "02/20/15");
        db.insert(TABLE_EXPENSES, null,values);

    }

    public void testVendors(){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_VENDDESC, "Five Guys");
        db.insert(TABLE_VENDORS,null,values);
        values.put(COLUMN_VENDDESC, "United");
        db.insert(TABLE_VENDORS,null,values);
        values.put(COLUMN_VENDDESC, "Shell");
        db.insert(TABLE_VENDORS,null,values);

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

    //lookup to see if a category exists
    public int getCategoryID(String category){
        SQLiteDatabase db = this.getReadableDatabase();

        String categoryQuery = "SELECT * FROM " + TABLE_CATEGORIES
                + " WHERE " + COLUMN_CATDESC + " = \"" + category +"\"";
        //query the categories table for the specified value
        Cursor cursor = db.rawQuery(categoryQuery,null);
        //category does not exist, so add it to the table
        if(!cursor.moveToFirst())
        {
            ContentValues catValue = new ContentValues();
            catValue.put(COLUMN_CATDESC,category);
            db.insert(TABLE_CATEGORIES,null,catValue);
        }
        //query the categories table for the specified category
        cursor = db.rawQuery(categoryQuery,null);
        //check for null cursor, indicating an error
        if(cursor != null){
            cursor.moveToFirst();
            //return the integer specified at that location
            return (cursor.getInt(cursor.getColumnIndex(COLUMN_CATID)));

        }else{

            //return a -1 to indicate an error
            return -1;

        }


    }

    public String[] getVendorStrings(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_VENDDESC+ " FROM " + TABLE_VENDORS,null);
        if (cursor != null){
            //store vendor text to a string
            int i = 0;
            String[] vendors = new String[cursor.getCount()];
            while(cursor.moveToNext()){
                String vendor = cursor.getString(cursor.getColumnIndex(COLUMN_VENDDESC));
                vendors[i] = vendor;
                i++;
            }
            db.close();
            return vendors;
        }
        Log.d("Vendor String Error","Error reading vendors to string");
        db.close();
        return null;

    }

    //lookup to see if a category exists
    public int getVendorID(String vendor){
        SQLiteDatabase db = this.getReadableDatabase();

        String vendorQuery = "SELECT * FROM " + TABLE_VENDORS
                + " WHERE " + COLUMN_VENDDESC + " = \"" + vendor +"\"";
        //query the vendors table for the specified value
        Cursor cursor = db.rawQuery(vendorQuery,null);
        //vendor does not exist, so add it to the table
        if(!cursor.moveToFirst())
        {
            ContentValues vendorValue = new ContentValues();
            vendorValue.put(COLUMN_VENDDESC,vendor);
            db.insert(TABLE_VENDORS,null,vendorValue);
        }
        //query the vendors table for the specified vendor
        cursor = db.rawQuery(vendorQuery,null);
        //check for null cursor, indicating an error
        if(cursor != null){
            cursor.moveToFirst();
            //return the integer specified at that location
            return (cursor.getInt(cursor.getColumnIndex(COLUMN_VENDID)));

        }else{

            //return a -1 to indicate an error
            return -1;

        }


    }

    //Gets all the rows of the db on one cursor for ListView
    public Cursor getAllRows() {
        SQLiteDatabase db = this.getReadableDatabase();

        //join categories and expenses tables
        String JOIN_TABLES_SQL = "SELECT " + COLUMN_ID +","+ COLUMN_CATDESC +","+COLUMN_VENDDESC
                +","+COLUMN_COST+","+COLUMN_DATE+ " FROM " + TABLE_EXPENSES +","+TABLE_CATEGORIES
                + "," + TABLE_VENDORS + " WHERE "+ TABLE_EXPENSES +"."+COLUMN_CATEGORY+"="
                +TABLE_CATEGORIES+"."+COLUMN_CATID +" AND " + TABLE_EXPENSES + "." + COLUMN_VENDOR
                + "=" + TABLE_VENDORS+"."+COLUMN_VENDID;

        Cursor cursor = db.rawQuery(JOIN_TABLES_SQL,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    //Gets all String names of table
    public String[] tableNames() {
        String[] names = new String[] {COLUMN_CATDESC, COLUMN_VENDDESC, COLUMN_COST,
                COLUMN_DATE,COLUMN_ID};
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
            Log.d("Not Found","expense not found");
        }
        db.close();
        return expense;
    }

    //delete an expense
    public void deleteExpense(int expenseId){


        String delete = "DELETE FROM " + TABLE_EXPENSES+  " WHERE " + COLUMN_ID
                + " = \"" + expenseId + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.execSQL(delete);
        }catch(android.database.SQLException e){
            Log.d("Exception",e.getLocalizedMessage());
        }


    }

    //delete a category
    public void deleteCategory(int categoryId){

        String delete = "DELETE FROM " + TABLE_CATEGORIES+  " WHERE " + COLUMN_CATID
                + " = " + categoryId + "";

        SQLiteDatabase db = this.getWritableDatabase();

        //try to delete the category, and throw an error if something goes wrong (printed to log)
        try{
            db.execSQL(delete);
        }catch(android.database.SQLException e){
            Log.d("Exception",e.getLocalizedMessage());
        }


    }

    //----------------------PASSWORD METHODS--------------------------------------

    //Password
    public void addPassword(String password) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, password);

        //insert into password table
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_PASSWORD, null, values);
        db.close();
    }

    // Checks to find if there is a password in the DB - TABLE_PASSWORD
    public boolean hasPassword() {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_PASSWORD};
        Cursor cursor = db.query(TABLE_PASSWORD, columns, null, null, null,
                null, null);
        cursor.getCount();
        if (cursor.getCount() > 0){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean checkPassword(String pw) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_PASSWORD};
        Cursor cursor = db.query(TABLE_PASSWORD, columns, null, null, null,
                null, null);
        cursor.moveToFirst();
        if (cursor.getString(1).equals(pw)) {
            return true;
        }
        else {
            return false;
        }
    }
}