/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package campingsystem;

import java.time.LocalDate;

/**
 *
 * @author amaya.salisbury
 */
public class Booking {

    private String customerEmail;
    private String accommodationChoice;
    private LocalDate bookingStartDate;
    private LocalDate bookingEndDate;
    private double totalPrice;

    public Booking(String customerEmail, String accommodationChoice, LocalDate bookingStartDate, LocalDate bookingEndDate, double totalPrice) {
        this.customerEmail = customerEmail;
        this.accommodationChoice = accommodationChoice;
        this.bookingStartDate = bookingStartDate;
        this.bookingEndDate = bookingEndDate;
        this.totalPrice = totalPrice;
    }

    /**
     * @return the customer
     */
    protected String getCustomerEmail() {
        return customerEmail;
    }

    /**
     * @param customer the customer to set
     */
    protected void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    /**
     * @return the accommodationChoice
     */
    protected String getAccommodationChoice() {
        return accommodationChoice;
    }

    /**
     * @param accommodationChoice the accommodationChoice to set
     */
    protected void setAccommodationChoice(String accommodationChoice) {
        this.accommodationChoice = accommodationChoice;
    }

    /**
     * @return the bookingStartDate
     */
    protected LocalDate getBookingStartDate() {
        return bookingStartDate;
    }

    /**
     * @param bookingStartDate the bookingStartDate to set
     */
    protected void setBookingStartDate(LocalDate bookingStartDate) {
        this.bookingStartDate = bookingStartDate;
    }

    /**
     * @return the bookingEndDate
     */
    protected LocalDate getBookingEndDate() {
        return bookingEndDate;
    }

    /**
     * @param bookingEndDate the bookingEndDate to set
     */
    protected void setBookingEndDate(LocalDate bookingEndDate) {
        this.bookingEndDate = bookingEndDate;
    }

    /**
     * @return the totalPrice
     */
    protected double getTotalPrice() {
        return totalPrice;
    }

    /**
     * @param totalPrice the totalPrice to set
     */
    protected void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
