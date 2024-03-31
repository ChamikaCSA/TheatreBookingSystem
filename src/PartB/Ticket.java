package PartB;

// Ticket class to represent a single ticket
public class Ticket {

    private int row;
    private int seat;
    private double price;
    private Person person;

    public Ticket(int row, int seat, double price, Person person) {
        this.row = row;
        this.seat = seat;
        this.price = price;
        this.person = person;
    }

    public double getPrice() {
        return price;
    }

    public void print() {
        System.out.println("Full Name: " + person.getName() + " " + person.getSurname());
        System.out.println("Email Address: " + person.getEmail());
        System.out.println("Row: " + row + "  Seat: " + seat);
        System.out.println("Price: £" + price);
    }
}


