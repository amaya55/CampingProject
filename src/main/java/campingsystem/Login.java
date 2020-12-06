/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package campingsystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.String.valueOf;
import java.text.ParseException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author amaya.salisbury
 */
public class Login {

    private Scanner sc = new Scanner(System.in);

    int counter = 0;
    int counter2 = 0;

    private String newEmail = "";
    private String newFirstName;
    private String newPassword = "";
    private String newLastName;
    private String newTelNumber;
    private String isAdmin = "Customer";
    private boolean loginError = false;
    private boolean creatingUser = false;
    private Campsite cs = new Campsite();
    private User currentUser;

    protected void startApplication() throws ParseException {
        System.out.println("Welcome to the Booking and VAT System. Please select an option from the menu below to get started:\n");
        System.out.println("1:\tI have an account");
        System.out.println("2:\tI am a new customer");
        System.out.println("3:\tExit");

        byte menuChoice = Byte.parseByte(sc.nextLine());

        switch (menuChoice) {
            case 1:
                login();
                break;
            case 2:
                signUp();
                break;
            case 3:
                System.out.println("Are you sure you would like to exit? (Y/N)");
                String input = sc.nextLine();
                if (input.equalsIgnoreCase("y")) {
                    System.exit(0);
                } else {
                    startApplication();
                }
                break;
            default:
                System.out.println("Input not recognised. Please try again.");
                startApplication();

        }

    }

    private void login() {
        String userAttempt;
        String passwordAttempt;
        String row;
        try {
            BufferedReader br = new BufferedReader(new FileReader("user.csv"));

            br.readLine(); //this removes the header line from the buffer - must be kept in or the file isn't read correctly.

            while ((row = br.readLine()) != null) { //Reads each row within the file as long as there are values within the row
                String[] line = row.split(","); //Separates the values in the row and adds each as values within a String Array. 
                cs.getUsers().add(new User(line[0], line[1], line[2])); //Adds the values from the file into the User ArrayList. Uses the index of the line String Array to determine which variable should be assigned to the variable.
            }

            for (int i = 0; i < 3; i++) { //Used to loop through block of code to allow the user to reattempt their login should it fail. This can be done up to three times. The variable counter is used to determine how many attempts have been made.
                System.out.println("Please enter your email:");
                userAttempt = sc.nextLine();
                System.out.println("Please enter your password:");
                passwordAttempt = sc.nextLine();

                //Loops through all records and checks whether input email and password match an existing record.
                for (User u : cs.getUsers()) {
                    if (u.getEmail().equals(userAttempt) && u.getPassword().equals(passwordAttempt)) {
                        currentUser = u;
                        System.out.println("Login successful");
                        displayMainMenu();
                        break;
                    }
                    else {
                
                counter++;
                        if (counter < 3) {
                    System.out.println("Incorrect details entered. Please try again.");
                    break;
                } else {
                    System.out.println("Incorrect details entered too many times. Session terminated.");
                    System.exit(0);
                    break;
                }
                
                    }}
            }
        } catch (FileNotFoundException fnf) {
            System.out.println("There was an error when trying to find the file: " + fnf);
            loginError = true;
            System.out.println("Recreating login file...");
            createUserFile();
        } catch (IOException io) {
            System.out.println("Exception caused while reading file: " + io);
        }

    }

    private void displayMainMenu() {
        byte userInput;
        String choice;
        System.out.println("Welcome to the Campsite's Online Booking System. Please select an option from the menu below to get started.");
        System.out.println("1:\tBook your stay\n2:\tEdit an existing booking");

        try {
            userInput = sc.nextByte();

            switch (userInput) {
                case 1:
                    bookStay();
                    break;
                case 2:
                    editBooking();
                    break;
                default:
                    System.out.println("That didn't work. Try again.");
            }
        } catch (InputMismatchException ime) {
            System.out.println("Your input was not what was expected. Please only enter numbers - no special characters or letters.\nTry again? (Y/N");
            choice = sc.nextLine();

            if (choice.equalsIgnoreCase("y")) {
                displayMainMenu();
            } else if (choice.equalsIgnoreCase("n")) {
                System.out.println("Very well. Exiting system.");
            } else {
                System.out.println("Input not recognised. Exiting the system.");
                System.exit(0);
            }
        }

    }

    private void bookStay() {
        Scanner sc = new Scanner(System.in);

        DateTimeFormatter dform = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // UK date formatting. Reference:https://howtodoinjava.com/java/date-time/localdate-parse-string/
        DateTimeFormatter dform2 = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy"); //UK date formatting plus day of the week

        byte accommodation;
        byte days = 0;
        byte stayDuration;
        double price;
        double totalPrice;
        double vat;
        int weekdayDiscount = 0;
        LocalDate bookingStartDate = null;
        LocalDate bookingEndDate = null;
        LocalDate todayDate = LocalDate.now(); //This finds the current date and is then used to check whether the user's chosen date is in the future.
        String accommodationChoice = null;
        String bookingStart;

        System.out.println("Thank you for choosing to book your stay with us. To begin, please enter the date that you would like to stay with us e.g. 01/12/2020:");
        bookingStart = sc.nextLine();

        try {
            bookingStartDate = LocalDate.parse(bookingStart, dform);
        } catch (DateTimeException dte) {
            //
            System.out.println("Whoops, that didn't work! The following error occurred as a result of incorrect entry. Please try again using the format dd/mm/yyyy. E.g. " + todayDate.format(dform));
            System.out.println(dte);
            bookStay();
        }

        if (!bookingStartDate.isAfter(todayDate)) { //this ensures that bookings are only arranged for dates in the future so nobody can accidentally book for a time in the past.
            System.out.println("Error. You must only select a date in the future to arrange your booking.\n");
            bookStay();
        }

        System.out.println("You have chosen to book in with us on " + bookingStartDate + ". Next, please choose how long you would like to stay with us:");

        System.out.println("1:\t7 days\n2:\t14 days");
        stayDuration = Byte.parseByte(sc.nextLine());
        switch (stayDuration) {
            case 1:
                days = 7;
                weekdayDiscount = 5;
                break;
            case 2:
                days = 14;
                weekdayDiscount = 10;
                break;
            default:
                System.out.println("Incorrect option entered, please try again.");
                bookStay();
                break;
        }

        //Uses the input start date and adds the chosen amount of days to find the end date.
        bookingEndDate = bookingStartDate.plusDays(days);

        System.out.println("Thank you. Next, please select which accomodation option you would like to stay in:");
        System.out.println("1:\tCottage\n2:\tLog Cabin\n3:\tLarge Tent\n4:\tMedium Tent\n5:\tSmall Tent");
        accommodation = Byte.parseByte(sc.nextLine());

        try {
            switch (accommodation) {
                case 1:
                    accommodationChoice = "Cottage";
                    break;
                case 2:
                    accommodationChoice = "Log Cabin";
                    //Log cabin
                    break;
                case 3:
                    accommodationChoice = "Large Tent";
                    //Large Tent
                    break;
                case 4:
                    accommodationChoice = "Medium Tent";
                    //Medium Tent
                    break;
                case 5:
                    accommodationChoice = "Small Tent";
                    //Small Tent
                    break;
                default:
                    System.out.println("Incorrect option selected. Please try again.");
                    bookStay();
            }
        } catch (NumberFormatException nfe) {
            System.out.println("An error was encountered due to an incorrect selection. Please ensure that when selecting from the accommodation menu, only a number is chosen.\n" + nfe);
            bookStay();
        }

        System.out.println("To confirm, you have chosen to stay in the " + accommodationChoice + " for a total of " + days + " days, commencing " + bookingStartDate.format(dform2) + ". Is that correct? (Y/N)");

        String userInput = sc.nextLine();

        AccommodationOption selectedOption = null; //This stores the matching version of AccommodationOption from the for loop below so it can easily be accessed outside of the loop.

        if (userInput.equalsIgnoreCase("Y")) {
            for (AccommodationOption ao : cs.getAccommodationOptions()) {
                if (ao.getType().equals(accommodationChoice)) {
                    selectedOption = ao;
                }
            }

            price = ((selectedOption.getPricePerDay() * days) - ((selectedOption.getPricePerDay() * 0.05)) * weekdayDiscount);
            vat = selectedOption.getPricePerDay() * 0.025;
            totalPrice = (price + vat + selectedOption.getCard());

            System.out.println("The total price of your stay, inclusive of VAT, card payment fees and weekday discounts is: \n£" + totalPrice);
            System.out.println("Would you like to view a full breakdown of costs? (Y/N)");

            userInput = sc.nextLine();

            if (userInput.equalsIgnoreCase("y")) {
                System.out.println("\nTotal cost:\t£" + totalPrice);
                System.out.println("\nCost per day:\t + £" + selectedOption.getPricePerDay());
                System.out.println("Total VAT:\t + £" + vat);
                System.out.println("Card fee:\t + £" + selectedOption.getCard());
                System.out.println("\nTotal Weekday discounts:\t- £" + (selectedOption.getPricePerDay() * 0.05) * weekdayDiscount);

            } else if (userInput.equalsIgnoreCase("n")) {
                System.out.println("No problem at all. If you would like to view a full breakdown of costs in future, please choose the option 'View My Booking' in the Main Menu.");
            } else {
                System.out.println("Sorry, I didn't understand that, but if you would like to view a full breakdown of costs, this can be done by choosing 'View My Booking' in the Main Menu.");
            }

            System.out.println("\nPlease review the information below. Are you happy to proceed with the booking? (Y/N)");
            System.out.println("\nBooking overview:");
            System.out.println("----------------------");
            System.out.println("Accommodation Choice:\t" + selectedOption.getType());
            System.out.println("Length of Stay:\t" + days);
            System.out.println("Booking start date:\t" + bookingStartDate.format(dform2));
            /*//Below calculates the end date using the booking start date. It adds the amount of days selected using the days variable created previously. 
                It's then formatted to be consistent with the other dates.*/
            System.out.println("Booking end date:\t" + (bookingEndDate.format(dform2)));
            System.out.println("Total cost:\t£" + totalPrice);

            userInput = sc.nextLine();

            if (userInput.equalsIgnoreCase("y")) {
                System.out.println("Congratulations, you're one step closer to your holiday! Please wait while we generate your booking...");
                cs.getBookings().add(new Booking(currentUser.getEmail(), selectedOption.getType(), bookingStartDate, bookingEndDate, totalPrice));

                createBookingFile();

            }

        } else if (userInput.equalsIgnoreCase("N")) {
            //this could be made better by amending a specific detail.

            System.out.println("Would you like to try to book again or would you prefer to return to the main menu?");
            System.out.println("1:\tContinue with booking\n2:\tReturn to main menu\n3:\tExit system");

            try {

            } catch (NumberFormatException nfe) {
                System.out.println("An error was encountered due to an incorrect selection. Please ensure that when selecting from a numbered menu, only a number is input.\n" + nfe);
                System.out.println("Returning to the main menu.");
                displayMainMenu();
            }
        } else {
            System.out.println("Sorry, that wasn't the answer we were looking for.");
            bookStay();

        }
    }

    private void signUp() {
        Pattern numbers = Pattern.compile(".*[0-9].*");
        Pattern letters = Pattern.compile(".*[a-z].*");

        String userInput;

        System.out.println("Please enter your email address. This will be used to login to your account:");
        newEmail = sc.nextLine();

        if (!newEmail.contains("@")) {
            System.out.println("Sorry, the information you entered was incorrect. Please ensure you enter a valid email address.");
            signUp();
        }

        System.out.println("Please re-enter your email address to confirm:");
        String tempEmailAddress = sc.nextLine();

        if (!newEmail.equals(tempEmailAddress)) {
            System.out.println("The email addresses that you entered didn't match. Would you like to try again? (Y/N)");
            userInput = sc.nextLine();

            if (userInput.equalsIgnoreCase("y")) {
                signUp();
            } else {
                System.out.println("Returning you to the main menu.");
                displayMainMenu();
            }
        }
        System.out.println("Please choose a password to use with your account. Your password should be at least 8 characters and contain both numbers and letters:");

        for (int i = 0; i <= 3; i++) {

            newPassword = sc.nextLine();
            Matcher matcherN = numbers.matcher(newPassword);
            Matcher matcherL = letters.matcher(newPassword);
            boolean passwordIsStrong = ((matcherN.find() && matcherL.find()) && (newPassword.length() > 7));

            /*This for loop is intended to help users to create a more secure password.
               The if statement will check if the user's input includes both letters and numbers*/
            if (passwordIsStrong) {
                System.out.println("You have chosen a strong password.");
                break;
            } else {
                counter2++;

                if (counter2 == 3) {
                    System.out.println("You've entered an incorrect format a few times. Returning you to the main menu.");
                    displayMainMenu();
                } else if (counter2 < 3) {
                    System.out.println("That password isn't strong enough. Please use both numbers and letters to make your account more secure.");
                }
            }
        }

        System.out.println("Next, please enter your first name:");
        newFirstName = sc.nextLine();

        System.out.println("Please enter your last name:");
        newLastName = sc.nextLine();

        System.out.println("You're almost there! Finally, please enter your phone number:");
        newTelNumber = sc.nextLine();

        System.out.println("Thanks " + newFirstName + ", creating your account now.");

        cs.getUsers().add(new User(newEmail, newPassword, "Customer"));
        cs.getCustomers().add(new Customer(newEmail, newFirstName, newLastName, newTelNumber));

        creatingUser = true;

        createCustomerFile();

    }

    private void editBooking() {

        File tempFile = new File("booking.csv");
        boolean bFileExists = tempFile.exists();
        byte bookingNumber = 0;
        String row;
        String startDateString;
        String endDateString;
        DateTimeFormatter dform = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // UK date formatting. Reference:https://howtodoinjava.com/java/date-time/localdate-parse-string/

        Booking selectedBooking;
        
        if (!bFileExists) {
            System.out.println("File does not currently exist. Creating file...");
            createBookingFile();
        }


        try {
            BufferedReader br = new BufferedReader(new FileReader("booking.csv"));
            br.readLine(); //this removes the header line from the buffer - must be kept in or the file isn't read correctly.

            while ((row = br.readLine()) != null) {
                //Reads each row within the file as long as there are values within the row
                br.readLine(); 
                
                //this removes the header line from the buffer - must be kept in or the file isn't read correctly.
                String[] line = row.split(","); 
                
                //Separates the values in the row and adds each as values within a String Array.
                startDateString = line[2];
                endDateString = line[3];
                LocalDate startDate = LocalDate.parse(startDateString, dform);
                LocalDate endDate = LocalDate.parse(endDateString, dform);
                Double tc = Double.parseDouble(line[4]);
                cs.getBookings().add(new Booking(line[0], line[1], startDate, endDate, tc)); //Adds the values from the file into the User ArrayList. Uses the index of the line String Array to determine which variable should be assigned to the variable.
                //System.out.println("Booking read successfully: " + line[1] + ", "+ startDate);
            }

            System.out.println("You have the following holidays booked in with us: \n");

            for (Booking b : cs.getBookings()) {
                if (currentUser.getEmail().equals(b.getCustomerEmail())) {
                    bookingNumber++;
                    System.out.println(bookingNumber + ":\tAccommodation Choice: " + b.getAccommodationChoice() + ", Arrival Date: " + b.getBookingStartDate());
                } else {
                    System.out.println("No bookings currently exist on your account. Returning you to the main menu.");
                    displayMainMenu();
                }
            }

        } catch (FileNotFoundException fnf) {
            System.out.println("Exception caught due to file not being found: " + fnf);
            System.out.println("Returning to the main menu.");
            displayMainMenu();
        } catch (IOException io) {
            System.out.println("Exception caught during input/output operations: " + io);
            System.out.println("Returning to the main menu.");
            displayMainMenu();
        }

    }
    private void createCustomerFile() {
        File customerFile = new File("customer.csv");
        FileWriter fw;

        boolean isSuccessful = true;

        try {
            if (customerFile.createNewFile()) {
                System.out.println("Created new file:  " + customerFile);
                fw = new FileWriter("customer.csv");
                fw.append("Email");
                fw.append(",");
                fw.append("First Name");
                fw.append(",");
                fw.append("Last Name");
                fw.append(",");
                fw.append("Telefone");
                fw.append("\n");
                fw.flush();
                fw.close();
            }

        } catch (IOException io) {
            System.out.println("Exception caused while creating the file " + customerFile + ":\n" + io);
            isSuccessful = false;
        } catch (Exception e) {
            System.out.println("Unexpected exception: " + e);
            isSuccessful = false;
        }

        if (isSuccessful) {

            writeToCustomerFile();
        }

    }

    private void writeToCustomerFile() {
        Customer customerToAdd = null;
        boolean cIsSuccessful = false;

        //loops through customer arraylist to identify current customer. Assigns this to a variable to reference in later code.
        for (Customer c : cs.getCustomers()) {
            if (c.getEmail().equals(newEmail)) {
                customerToAdd = c;
            }
        }

        if (customerToAdd != null) {
            //adds headers to the new file.
            try ( FileWriter fw = new FileWriter("customer.csv", true)) {

                for (Customer record : cs.getCustomers()) {
                    fw.append(customerToAdd.getEmail());
                    fw.append(",");
                    fw.append(customerToAdd.getFirstName());
                    fw.append(",");
                    fw.append(customerToAdd.getLastName());
                    fw.append(",");
                    fw.append(customerToAdd.getTelNumber());
                }
                fw.append("\n");

                fw.flush();
                fw.close();

                System.out.println("Step 2 complete.");

                cIsSuccessful = true;

            } catch (IOException io) {
                System.out.println("Exception when updating file: " + io);
                System.out.println("Returning to main menu.");
                displayMainMenu();
            }
        } else {
            System.out.println("Error fetching customer details.");
        }

        if (cIsSuccessful && creatingUser) {
            createUserFile();
        }
    }

    private void createUserFile() {
        File userFile = new File("user.csv");
        FileWriter fw;

        try {
            if (userFile.createNewFile()) {
                System.out.println("Created new file:  " + userFile);
                fw = new FileWriter("user.csv");
                fw.append("Email");
                fw.append(",");
                fw.append("Password");
                fw.append(",");
                fw.append("Role");
                fw.append("\n");
                fw.flush();
                fw.close();
            }
        } catch (IOException io) {
            System.out.println("Exception caused while creating the file " + userFile + ":\n" + io);
        }

        writeToUserFile();

    }

    protected void writeToUserFile() {
        User userToAdd = null;
        boolean isSuccessful = false;

        //loops through user arraylist to identify current user. Assigns this to a variable to reference in later code.
        for (User u : cs.getUsers()) {
            if (u.getEmail().equals(newEmail)) {
                userToAdd = u;
            }
        }

        if (userToAdd != null) {
            //adds headers to the new file.
            try ( FileWriter fw = new FileWriter("user.csv", true)) {

                for (User record : cs.getUsers()) {
                    fw.append(newEmail);
                    fw.append(",");
                    fw.append(userToAdd.getPassword());
                    fw.append(",");
                    fw.append(userToAdd.getIsAdmin());
                }
                fw.append("\n");

                fw.flush();
                fw.close();

                isSuccessful = true;

            } catch (IOException io) {
                System.out.println("Exception when updating file: " + io);
                System.out.println("Returning to main menu.");
                displayMainMenu();
            }
        } else {
            System.out.println("Error fetching customer details.");
        }

        if (isSuccessful) {

            if (loginError) {
                System.out.println("File has now been successfully created. Please try logging in again.");
                login();
            }

            System.out.println("Your account has been successfully created! Please login using your details to add your booking.");
            login();
        }
    }

    private void createBookingFile() {
        File bookingFile = new File("booking.csv");
        FileWriter fw;

        try {
            if (bookingFile.createNewFile()) {
                System.out.println("Created new file:  " + bookingFile);
                fw = new FileWriter("booking.csv");
                fw.append("Customer Email");
                fw.append(",");
                fw.append("Accommodation");
                fw.append(",");
                fw.append("Arrival Date");
                fw.append(",");
                fw.append("Departure Date");
                fw.append(",");
                fw.append("Total Cost");
                fw.append("\n");
                fw.flush();
                fw.close();
            }
        } catch (IOException io) {
            System.out.println("Exception caused while creating the file " + bookingFile + ":\n" + io);
        }

        writeToBookingFile();

    }

    protected void writeToBookingFile() {
        Booking bookingToAdd = null;
        boolean isSuccessful = false;

        //loops through booking arraylist to identify current booking. Assigns this to a variable to reference in later code.
        for (Booking b : cs.getBookings()) {
            if (b.getCustomerEmail().equals(currentUser.getEmail())) {
                bookingToAdd = b;
            }
        }

        if (bookingToAdd != null) {
            String d = valueOf(bookingToAdd.getTotalPrice());
            //adds headers to the new file.
            try ( FileWriter fw = new FileWriter("booking.csv", true)) {

                for (Booking record : cs.getBookings()) {
                    fw.append(bookingToAdd.getCustomerEmail());
                    fw.append(",");
                    fw.append(bookingToAdd.getAccommodationChoice());
                    fw.append(",");
                    fw.append(bookingToAdd.getBookingStartDate().toString());
                    fw.append(",");
                    fw.append(bookingToAdd.getBookingEndDate().toString());
                    fw.append(",");
                    fw.append(d);
                }
                fw.append("\n");

                fw.flush();
                fw.close();

                isSuccessful = true;

            } catch (IOException io) {
                System.out.println("Exception when updating file: " + io);
                System.out.println("Returning to main menu.");
                displayMainMenu();
            }
        } else {
            System.out.println("Error fetching customer details.");
        }

        if (isSuccessful) {
            System.out.println("Your booking has been added! We're looking forward to seeing you on the " + bookingToAdd.getBookingStartDate() + ". Returning you to the main menu.");
            displayMainMenu();
        }
    }

}
