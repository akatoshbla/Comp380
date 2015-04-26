package com.comp380.csun.comp380;

/**
 * Created by David on 4/25/2015.
 */
public class Income {
    private int id;
    private String source;
    private float amount;
    private String date;

    public Income() {

    }

    public Income(String source, float amount) {
        this.source = source;
        this.amount = amount;
    }

    public Income(String source, float amount, String date) {
        this.source = source;
        this.amount = amount;
        this.date = date;
    }

    //setters and getters
    public void setID(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setSource(String source){
        this.source = source;
    }

    public String getSource(){
        return source;
    }

    public void setAmount(float amount){
        this.amount = amount;
    }

    public float getAmount(){
        return amount;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getDate(){

        if(date != null){
            return date;
        }else
            return null;

    }
}
