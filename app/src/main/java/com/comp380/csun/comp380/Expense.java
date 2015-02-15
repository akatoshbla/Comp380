package com.comp380.csun.comp380;

/**
 * Created by gdfairclough on 2/15/15.
 */
public class Expense {

    private int _id;
    private String _category;
    private String _vendor;
    private float _cost;
    private String _date;

    public Expense(){

    }

    public Expense(int id, String category, String vendor, float cost, String date){
        this._id = id;
        this._category = category;
        this._vendor = vendor;
        this._cost = cost;
        this._date = date;

    }

    public Expense(String category, String vendor, float cost, String date){

        this._category = category;
        this._vendor = vendor;
        this._cost = cost;
        this._date = date;

    }

    //setters and getters
    public void setID(int id){
        this._id = id;
    }

    public int getId(){
        return this._id;
    }

    public void setCategory(String category){
        this._category = category;
    }

    public String getCategory(){
        return this._category;
    }

    public void setVendor(String vendor){
        this._vendor = vendor;
    }

    public String getVendor(){
        return this._vendor;
    }

    public void setCost(float cost){
        this._cost = cost;
    }

    public float getCost(){
        return this._cost;
    }

    public void setDate(String date){
        this._date = date;
    }
    public String getDate(){
        return this._date;
    }
}
