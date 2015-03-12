package com.comp380.csun.comp380;

import java.util.TreeMap;

/**
 * Created by David on 3/11/2015.
 */
public class BudgetReport  {

    private DatabaseHandler db;
    private int budgetCurrent;
    private int budgetMax;
    private int status;
    private int progressBar;
    private TreeMap<Integer, Transaction> topFiveVendors;
    private TreeMap<Integer, Transaction> topFiveCategories;

    public BudgetReport(DatabaseHandler db, Boolean toggle) {
        this.db = db;

        if (toggle) {
            setTopFiveCategories();
            setAllBudgetMax();
            setTotalAllBudgets();
        }
        else {
            setTopFiveVendor();
            setBudgetMax();
            setTotalBudget();
        }
    }

    public int getBudgetCurrent() {
        return budgetCurrent;
    }

    public int getBudgetMax() {
        return budgetMax;
    }

    public int getStatus() {
        return status;
    }

    public int getProgressBar() {
        return progressBar;
    }

    public String getTopFiveVendors(int position) {
        return topFiveVendors.get(position).toString();
    }

    public String getTopFiveCategories(int position) {
        return topFiveCategories.get(position).toString();
    }

    // This method will setup the budgetCurrent, progressBar, and status vars
    private void setTotalAllBudgets() {

        int totalBudget = 0;

        for (int i = 0; i  < topFiveCategories.size(); i++) {
            totalBudget += topFiveCategories.get(i).getCost();
        }


    }

    // This method will setup the budgetCurrent, progressBar, and status vars
    private void setTotalBudget() {

        int totalBudget = 0;

        for (int i = 0; i  < topFiveVendors.size(); i++) {
            totalBudget += topFiveVendors.get(i).getCost();
        }
    }

    // This method will setup the Top 5 Categories and costs in a TreeMap with a
    // Transaction object as the value.
    private void setTopFiveCategories() {

    }

    // This method will setup the Top 5 Vendors and costs in a TreeMap with a
    // Transaction object as the value.
    private void setTopFiveVendor() {

    }

    // Access the db and sets the budget max
    private void setAllBudgetMax() {

    }

    // Access the db and sets the vender
    private void setBudgetMax() {

    }

    public static class Transaction {

        private String transactionName;
        private float transactionCost;

        public Transaction(String name, float cost) {
            transactionName = name;
            transactionCost = cost;
        }

        public float getCost()  {
            return transactionCost;
        }

        public String toString() {
            return transactionName + " " + transactionCost;
        }
    }
}
