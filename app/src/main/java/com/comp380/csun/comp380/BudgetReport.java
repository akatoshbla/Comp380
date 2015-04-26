package com.comp380.csun.comp380;

import android.database.Cursor;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by David on 3/11/2015.
 */

public class BudgetReport {

    // Declared variables
    private DatabaseHandler db;
    private float budgetCurrent;
    private float budgetMax;
    private int progressBarColor;
    private int progressBar;
    private float[] myPieChartData;
    private ArrayList<Transaction> topFiveExpenses;

    // BudgetReport Constructor
    public BudgetReport(DatabaseHandler db, String tabName) {
        this.db = db;

        setBudgetReport(tabName);
        setBudgetMax(tabName);
        setProgressBar();
        setProgressBarColor();
        setBudgetMax(tabName);
        setMyPieChartData();
        db.close();
    }

    // Get Methods
    public float getBudgetCurrent() {
        return budgetCurrent;
    }

    public float getBudgetMax() {
        return budgetMax;
    }

    public int getProgressBarColor() {
        return progressBarColor;
    }

    public int getProgressBar() {
        return progressBar;
    }

    public Transaction getTopFiveExpenses(int position) {
        if (topFiveExpenses != null && position <topFiveExpenses.size()) {
            return topFiveExpenses.get(position);
        }
        return new Transaction();
    }

    // TODO: Fix issue with no vendor name, but a cost
    public Transaction getExpenseNames(int position) {
        if (topFiveExpenses != null && position < topFiveExpenses.size()) {
            return topFiveExpenses.get(position);
        }
        return new Transaction("", 0);
    }

    // Get method for Pie Chart
    public float[] getMyPieChartData() { return myPieChartData; }

    // This method will set the budgetCurrent and topFiveExpenses ArrayList
    private void setBudgetReport(String tabName) {

        float totalBudgetSum = 0;
        topFiveExpenses = new ArrayList<>();
        Cursor cursor;
        Transaction transaction;

        if (tabName.equals("All")) {
            cursor = db.categoryByCost();

            if (cursor != null) {
                // Linear search through list to get total Budget - Categories
                cursor.moveToPrevious();
                while (cursor.moveToNext()) {
                    transaction = new Transaction(db.categoryToString(cursor.getInt(0)),
                            Float.parseFloat(cursor.getString(1)));
                    topFiveExpenses.add(transaction);
                    totalBudgetSum += Float.parseFloat(cursor.getString(1));
                }
            }
        }
        else {
                cursor = db.vendorByCost(tabName);

                if (cursor != null) {
                    // Linear search through list to get total Budget - Vendors
                    cursor.moveToPrevious();
                    while (cursor.moveToNext()) {
                        transaction = new Transaction(db.vendorIdToString(cursor.getInt(0)),
                                Float.parseFloat(cursor.getString(1)));
                        topFiveExpenses.add(transaction);
                        totalBudgetSum += Float.parseFloat(cursor.getString(1));
                    }
                }
            }

        budgetCurrent = totalBudgetSum;

        if (totalBudgetSum == 0) {
            budgetMax = 0;
            topFiveExpenses = null;
        }
    }

    // Access the db and sets the budgetMax based on tabName shown
    private void setBudgetMax(String tabName) {
        if (tabName.equals("All")) {
            // TODO: Add all the category incomes up and set to budgetMax
            float sum = 0;
            String[] temp = db.getCategoriesStrings();

            for (int i = 0; i < temp.length; i++) {
                sum += db.getBudget(temp[i]);
            }

            budgetMax = sum;
        }
        else {
            budgetMax = db.getBudget(tabName);
        }
    }

    // Sets the status color of progress bar
    private void setProgressBarColor() {
        if (progressBar < 40) {
            progressBarColor = R.drawable.greenprogressbar;
        } else if (progressBar >= 40 && progressBar < 60) {
            progressBarColor = R.drawable.yellowprogressbar;
        } else if (progressBar >= 60 && progressBar <= 99) {
            progressBarColor = R.drawable.orangeprogressbar;
        } else {
            progressBarColor = R.drawable.redprogressbar;
        }
    }

    // Sets the progressBar in the fragment layout
    private void setProgressBar() {
        if (budgetMax > 0) {
            progressBar = (int)(budgetCurrent / budgetMax * 100);
        }
        else {
            progressBar = 0;
        }
    }

    // Calculating degree of circle for Pie Chart
    private void setMyPieChartData() {
        float total = 0;

        if (topFiveExpenses != null) {
            myPieChartData = new float[topFiveExpenses.size()];
            for (int i = 0; i < myPieChartData.length; i++) {
                total += topFiveExpenses.get(i).getTransactionCost();
            }
            for (int i = 0; i < myPieChartData.length; i++) {
                myPieChartData[i] = 360 * (topFiveExpenses.get(i).getTransactionCost() / total);
            }
        }
        // Need this for show to user on an empty category / vendor???
        else {
            myPieChartData = new float[2];
            myPieChartData[0] = 50;
            myPieChartData[1] = 50;
        }
    }

    // Object for the Transactions that are put into an ArrayList(DataStructure)
    public static class Transaction {

        // Declared variables
        private String transactionName;
        private float transactionCost;

        // Default constructor
        public Transaction() {
            transactionName = "";
            transactionCost = 0;
        }

        // Transaction constructor with signature
        public Transaction(String name, float cost) {
            transactionName = name;
            transactionCost = cost;
        }

        // Get methods
        public String getTransactionName() {
            return transactionName;
        }

        public float getTransactionCost() { return transactionCost; }

        public String getTransactionCostString()  {
            DecimalFormat cost = new DecimalFormat("$###,###,##0.00");
            if (transactionCost > 0) {
                return cost.format(transactionCost);
            }

            return "";
        }
    }
}