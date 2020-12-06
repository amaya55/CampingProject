/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package campingsystem;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amaya.salisbury
 */
public class Campsite {
    
        private List<AccommodationOption> accommodationOptions = new ArrayList<>();
        private List<Booking> bookings = new ArrayList<>();
        private List<Customer> customers = new ArrayList<>();
        private List<User> users = new ArrayList<>();
        
        
    public Campsite() {
        //this constructor adds the accommodation options specified in addAccommodationOptions to Campsite.
        AddAccommodationOptions();
    }
    
    private void AddAccommodationOptions() {
        accommodationOptions.add(new AccommodationOption("Log Cabin",2.00,5,90,0.25));
        accommodationOptions.add(new AccommodationOption("Large Tent", 1.80, 5, 80, 0.25));
        accommodationOptions.add(new AccommodationOption("Medium Tent", 1.80, 5, 70,0.25));
        accommodationOptions.add(new AccommodationOption("Small Tent", 1.80, 10, 45, 0.25));
        accommodationOptions.add(new AccommodationOption("Cottage", 4.40, 5, 200, 0.25));
    }
    
    /**
     * @return the accommodationOption
     */
    public List<AccommodationOption> getAccommodationOptions() {
        return accommodationOptions;
    }
    
    public List<Booking> getBookings() {
        return bookings;
    }
    public List<User> getUsers() {
        return users;
    }

    /**
     * @return the customers
     */
    public List<Customer> getCustomers() {
        return customers;
    }
    
}
