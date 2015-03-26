package com.comp380.csun.comp380;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by David on 3/11/2015.
 */

// TODO: Need to label methods
public class BudgetReport {

    private DatabaseHandler db;
    private int budgetCurrent;
    private int budgetMax;
    private int progressBarColor;
    private int progressBar;
    private float[] myPieChartData;
    private ArrayList<Transaction> topFiveExpenses;

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

    public int getBudgetCurrent() {
        return budgetCurrent;
    }

    public int getBudgetMax() {
        return budgetMax;
    }

    public int getProgressBarColor() {
        return progressBarColor;
    }

    public int getProgressBar() {
        return progressBar;
    }

    public String getTopFiveExpenses(int position) {
        if (topFiveExpenses != null && position < topFiveExpenses.size()) {
            return topFiveExpenses.get(position).toString();
        }
        return "";
    }

    public Transaction getExpenseNames(int position) {
        if (topFiveExpenses != null && position < topFiveExpenses.size()) {
            return topFiveExpenses.get(position);
        }
        return new Transaction("", 0);
    }


    public float[] getMyPieChartData() { return myPieChartData; }

    // This method will set the budgetCurrent and topFiveExpenses ArrayList
    private void setBudgetReport(String tabName) {

        int totalBudgetSum = 0;
        topFiveExpenses = new ArrayList<>();
        Cursor cursor;
        Transaction transaction;

        if (tabName.equals("All")) {
            cursor = db.categoryByCost();
            budgetMax = 150; // Testing purposes

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
                budgetMax = 150; //  Testing Purposes

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
        sortTransactions(topFiveExpenses);

        if (totalBudgetSum == 0) {
            budgetCurrent = 0;
            budgetMax = 150;
            topFiveExpenses = null;
        }
    }

    // This method will setup the Top 5 Categories and costs in a TreeMap with a
    // Transaction object as the value.
    private void sortTransactions(ArrayList<Transaction> alist) {
        TransactionComparator comparator = new TransactionComparator();
        Collections.sort(alist, comparator);
    }

    // Access the db and sets the budgetMax based on tabName shown
    private void setBudgetMax(String tabName) {
        if (tabName.equals("All")) {
            // TODO: Add all the category incomes up and set to budgetMax
            int sum = 0;
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

    // Sets the status color of the defCom textView in fragment layout
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
        if (budgetMax == 0) {
            progressBar = 0;
        } else {
            progressBar = (int)((double)(budgetCurrent) / budgetMax * 100);
        }
    }

    private void setMyPieChartData() {
        float total = 0;

        if (topFiveExpenses != null) {
            myPieChartData = new float[topFiveExpenses.size()];
            for (int i = 0; i < myPieChartData.length; i++) {
                total += topFiveExpenses.get(i).getCost();
            }
            for (int i = 0; i < myPieChartData.length; i++) {
                myPieChartData[i] = 360 * (topFiveExpenses.get(i).getCost() / total);
            }
        }
        else {
            myPieChartData = new float[2];
            myPieChartData[0] = 50;
            myPieChartData[1] = 50;
        }
    }

    // Object for the Transactions that are put into an ArrayList(DataStructure)
    public static class Transaction {

        private String transactionName;
        private float transactionCost;

        public Transaction(String name, float cost) {
            transactionName = name;
            transactionCost = cost;
        }

        public String getTransactionName() {
            return transactionName;
        }

        public float getCost()  {
            return transactionCost;
        }

        public String toString() {

            String temp = String.format("%-20s%6.2f", transactionName, transactionCost);
            return temp;
        }
    }

    public static class TransactionComparator implements Comparator<Transaction> {

        @Override
        public int compare(Transaction tOne, Transaction tTwo) {
            Float costOne = tOne.getCost();
            Float costTwo = tTwo.getCost();

            if (costOne > costTwo) {
                return -1;
            }
            else if (costOne  < costTwo) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }
}
