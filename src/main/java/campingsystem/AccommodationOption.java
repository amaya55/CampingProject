/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package campingsystem;

import java.util.ArrayList;

/**
 *
 * @author amaya.salisbury
 */
public class AccommodationOption {
    
    private String type;
    private double card;
    private int weekdayDiscount;
    private double pricePerDay;
    private double vat;

    public AccommodationOption(String type, double card, int weekdayDiscount, double pricePerDay, double vat) {
        this.type = type;
        this.card = card;
        this.weekdayDiscount = weekdayDiscount;
        this.pricePerDay = pricePerDay;
        this.vat = vat;
    }
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the card
     */
    public double getCard() {
        return card;
    }

    /**
     * @param card the card to set
     */
    public void setCard(double card) {
        this.card = card;
    }

    /**
     * @return the weekdayDiscount
     */
    public int getWeekdayDiscount() {
        return weekdayDiscount;
    }

    /**
     * @param weekdayDiscount the weekdayDiscount to set
     */
    public void setWeekdayDiscount(int weekdayDiscount) {
        this.weekdayDiscount = weekdayDiscount;
    }

    /**
     * @return the pricePerDay
     */
    public double getPricePerDay() {
        return pricePerDay;
    }

    /**
     * @param pricePerDay the pricePerDay to set
     */
    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    /**
     * @return the vat
     */
    public double getVat() {
        return vat;
    }

    /**
     * @param vat the vat to set
     */
    public void setVat(double vat) {
        this.vat = vat;
    }
}
