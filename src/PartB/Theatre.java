package PartB;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Theatre {

    // Declare a 2D array to record the seat availability in each row
    static int[][] seatsArray = new int[3][];
    // Declare an ArrayList to store tickets
    static ArrayList<Ticket> ticketsArray = new ArrayList<>();

    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        // Initialize seatArray with 3 arrays for each row of seats
        seatsArray[0] = new int[12];
        seatsArray[1] = new int[16];
        seatsArray[2] = new int[20];

        System.out.println("Welcome to the New Theatre");

        // Initialize all seats in the seatArray as free(0) using a nested for loop.
        for (int[] row : seatsArray) {
            for (int seat : row) {
                row[seat] = 0;
            }
        }

        int option = -1;

        // Do-while loop to keep looping until the user selects option 0
        do {
            System.out.println("-------------------------------------------------");
            System.out.println("Please select an option:");
            System.out.println("1) Buy a ticket");
            System.out.println("2) Print seating area");
            System.out.println("3) Cancel ticket");
            System.out.println("4) List available seats");
            System.out.println("5) Save to file");
            System.out.println("6) Load from file");
            System.out.println("7) Print ticket information and total price");
            System.out.println("8) Sort tickets by price");
            System.out.println("0) Quit");
            System.out.println("-------------------------------------------------");

            System.out.print("Enter option: ");

            try {
                option = input.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Try again.");
                input.nextLine();
                continue;
            }

            // Switch statement to call methods based on the user's selected option
            switch (option) {
                case 1 :
                    buy_ticket();
                    break;
                case 2 :
                    print_seating_area();
                    break;
                case 3 :
                    cancel_ticket();
                    break;
                case 4 :
                    show_available();
                    break;
                case 5 :
                    save("seatInformation.txt");
                    break;
                case 6 :
                    load("seatInformation.txt");
                    break;
                case 7 :
                    show_tickets_info(ticketsArray);
                    break;
                case 8 :
                    ArrayList<Ticket> sortedTicketsArray = sort_tickets(ticketsArray);
                    show_tickets_info(sortedTicketsArray);
                    break;
                case 0 :
                    System.out.print("Thank you for visiting the New Theatre. Goodbye!");
                    break;
                default :
                    System.out.println("Invalid option. Try again.");
                    break;
            }
        }
        while (option != 0);
    }

    // Method to buy a ticket when the user selects ‘1’ in the main menu
    private static void buy_ticket() {

        System.out.print("Enter your first name: ");
        String name = input.next();
        System.out.print("Enter your last name: ");
        String surname = input.next();
        System.out.print("Enter your email address: ");
        String email = input.next();
        Person person = new Person(name, surname, email);

        try {
            int rowNum = getRow();
            int seatNum = getSeat(rowNum);

            if (seatsArray[rowNum - 1][seatNum - 1] == 1) {
                System.out.println("Sorry, seat is unavailable");
                buy_ticket();
            } else {
                System.out.print("Enter price of the ticket: ");
                double price = input.nextDouble();

                seatsArray[rowNum - 1][seatNum - 1] = 1;
                System.out.println("Ticket purchased successfully");

                Ticket ticket = new Ticket(rowNum, seatNum, price, person);
                ticketsArray.add(ticket);
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Try again");
            input.nextLine();
            buy_ticket();
        }
    }

    // Method to get user input for selecting a row number
    private static int getRow () {

        System.out.print("Select row number (1-3): ");
        int rowNum = input.nextInt();

        if (rowNum < 1 || rowNum > 3) {
            System.out.println("Invalid row number");
            return getRow();
        } else {
            return rowNum;
        }

    }

    // Method to get user input for selecting a seat number
    private static int getSeat (int rowNum) {

        System.out.print("Select seat number (1-" + seatsArray[rowNum - 1].length + "): ");
        int seatNum = input.nextInt();

        if (seatNum < 1 || seatNum > seatsArray[rowNum - 1].length) {
            System.out.println("Invalid seat number");
            return getSeat(rowNum);
        } else {
            return seatNum;
        }
    }


    // Method to print a visual representation of the seating area when the user selects ‘2’ in the main menu
    private static void print_seating_area() {

        System.out.println("-------------------------------------------------");
        System.out.println("     ***********");
        System.out.println("     *  STAGE  *");
        System.out.println("     ***********");

        for (int row = 0; row < seatsArray.length; row++) {
            for (int space = 0; space < (4 - row * 2); space++) {
                System.out.print(" ");
            }
            for (int seat = 0; seat < seatsArray[row].length / 2; seat++) {
                if (seatsArray[row][seat] == 0) {
                    System.out.print("O");
                } else {
                    System.out.print("X");
                }
            }
            System.out.print(" ");

            for (int seat = seatsArray[row].length / 2; seat < seatsArray[row].length; seat++) {
                if (seatsArray[row][seat] == 0) {
                    System.out.print("O");
                } else {
                    System.out.print("X");
                }
            }
            System.out.println();
        }
    }

    // Method to cancel a ticket when the user selects ‘3’ in the main menu
    private static void cancel_ticket() {

        try {
            int rowNum = getRow();
            int seatNum = getSeat(rowNum);

            if (seatsArray[rowNum - 1][seatNum - 1] == 0) {
                System.out.println("The seat is not booked. Please try again.");
                cancel_ticket();
            } else {
                seatsArray[rowNum - 1][seatNum - 1] = 0;
                System.out.println("Ticket cancelled successfully");

                for (Ticket ticket : ticketsArray) {
                    ticketsArray.remove(ticket);
                    break;
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Try again");
            input.nextLine();
            cancel_ticket();
        }
    }

    // Method to display available seats in each row when the user selects ‘4’ in the main menu
    private static void show_available() {

        for (int row = 0; row < seatsArray.length; row++) {
            System.out.print("Seats available in row " + (row + 1) + ": ");

            // Initialize a boolean to keep track of whether program is printing the first seat number of the row
            boolean firstPrint = true;

            for (int seat = 0; seat < seatsArray[row].length; seat++) {
                if (seatsArray[row][seat] == 0) {
                    if (!firstPrint) {
                        System.out.print(", ");
                    }
                    System.out.print((seat + 1));
                    firstPrint = false;
                }
            }
            System.out.println(".");
        }
    }

    // Method to save current state of the seatArray to a file when the user selects ‘5’ in the main menu
    public static void save(String filename) {

        try {
            FileWriter fileWriter = new FileWriter(filename);

            for (int[] row : seatsArray) {
                for (int seat : row) {
                    fileWriter.write(seat + " ");
                }
                fileWriter.write("\n");
            }
            fileWriter.close();
            System.out.println("Seats saved to file: " + filename);
        }
        catch (IOException e) {
            System.out.println("An error occurred while saving the seats");
        }
    }

    // Method to load the seatArray from the saved file when the user selects ‘6’ in the main menu
    public static void load(String filename) {

        try {
            File file = new File(filename);
            Scanner readFile = new Scanner(file);

            for (int row = 0; row < seatsArray.length; row++) {
                String rowLine = readFile.nextLine();
                String[] rowArray = rowLine.split(" ");

                for (int seat = 0; seat < seatsArray[row].length; seat++) {
                    seatsArray[row][seat] = Integer.parseInt(rowArray[seat]);
                }
            }
            readFile.close();
            System.out.println("Seats loaded from file: " + filename);
        }
        catch (IOException e) {
            System.out.println("An error occurred while loading the seats");
        }
    }

    // Method to print information about all purchased tickets and their total price when the user selects ‘7’ in the main menu
    private static void show_tickets_info(ArrayList<Ticket> ticketsArray) {

        if (ticketsArray.size() == 0) {
            System.out.println("No ticket found");
        } else {
            double totalPrice = 0.0;

            for (int index = 0; index < ticketsArray.size(); index++) {
                System.out.println("-------------------------------------------------");
                Ticket ticket = ticketsArray.get(index);
                System.out.println("Ticket " + (index + 1));
                ticket.print();
                System.out.println("-------------------------------------------------");

                totalPrice += ticket.getPrice();
            }
            System.out.println("Total price: £" + totalPrice);
            System.out.println("-------------------------------------------------");
        }
    }

    // Method to sort tickets by price to a new ArrayList and return it when the user selects ‘8’ in the main menu
    private static ArrayList<Ticket> sort_tickets(ArrayList<Ticket> ticketsArray) {

        ArrayList<Ticket> sortedTicketsArray = new ArrayList<>(ticketsArray);

        // Sort the ArrayList using selection sort algorithm
        for (int start = 0; start < sortedTicketsArray.size() - 1; start++) {
            int minIndex = start;

            for (int i = start + 1; i < sortedTicketsArray.size(); i++) {
                if (sortedTicketsArray.get(i).getPrice() < sortedTicketsArray.get(minIndex).getPrice()) {
                    minIndex = i;
                }
            }
            Ticket  temp = sortedTicketsArray.get(start);
            sortedTicketsArray.set(start, sortedTicketsArray.get(minIndex));
            sortedTicketsArray.set(minIndex, temp);
        }
        return sortedTicketsArray;
    }
}

