package com.comp380.csun.comp380;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by gdfairclough on 3/21/15.
 */
public class Goal implements Serializable {

    private String name;
    private double targetAmount;
    private double currentAmount;
    private Date dateCreated;
    private Date lastAccessed;

    public Goal(String newName, double newTarget){
        this.name = newName;
        this.targetAmount = newTarget;
        this.dateCreated = new Date();
        this.currentAmount = 0;

    }

    public Goal(String newName, double newTarget, Date newDate){

        this.name = newName;
        this.targetAmount = newTarget;
        this.dateCreated = newDate;
        this.currentAmount = 0;

    }

    public Goal(String newName, double newTarget, double newCurrentAmount, Date newDate){

        this.name = newName;
        this.targetAmount = newTarget;
        this.dateCreated = newDate;
        this.currentAmount = newCurrentAmount;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(double currentAmount) {
        if(currentAmount > targetAmount){

            this.currentAmount = targetAmount;
        }else{

            this.currentAmount = currentAmount;
        }
    }


    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }



    public Date getLastAccessed() {
        return lastAccessed;
    }

    public int getLastAccessedMonth() {

        //create a new new calendar based on the date object
        Calendar cal = Calendar.getInstance();
        cal.setTime(lastAccessed);
        //0 indexed month field
        return cal.get(Calendar.MONTH);

    }

    public void setLastAccessed(Date lastAccessed) {
        this.lastAccessed = lastAccessed;
    }



    public int getProgressPercent(){

        return (int)(currentAmount/targetAmount * 100);

    }



}
