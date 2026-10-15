
import java.util.*;
import java.io.*;

public class Intercitybus {

    static Scanner sc = new Scanner(System.in);

    // MODULE 4: Interface
    interface Reservation {
        boolean bookSeat(int seatNumber);
        void displaySeats();
    }

    // MODULE 4: Class, constructor and encapsulation
    // Nested Bus class avoids conflicts with an old Bus.class file
    static class Bus implements Reservation {

        private String busNumber;
        private String busName;
        private String from;
        private String to;
        private String timing;

        // MODULE 3: 2D array: 4 rows x 5 columns = 20 seats
        // 0 = Available, 1 = Booked
        private int[][] seats = new int[4][5];

        Bus(String busNumber, String busName, String from,
            String to, String timing) {

            this.busNumber = busNumber;
            this.busName = busName;
            this.from = from;
            this.to = to;
            this.timing = timing;
        }

        String getBusNumber() {
            return busNumber;
        }

        String getBusName() {
            return busName;
        }

        String getFrom() {
            return from;
        }

        String getTo() {
            return to;
        }

        String getTiming() {
            return timing;
        }

        // MODULE 4: Method overriding
        @Override
        public String toString() {
            return busName + " | " + busNumber + " | "
                    + from + " to " + to + " | " + timing;
        }

        void displayDetails() {
            System.out.println("\nBus Name   : " + busName);
            System.out.println("Bus Number : " + busNumber);
            System.out.println("From       : " + from);
            System.out.println("To         : " + to);
            System.out.println("Timing     : " + timing);
        }

        // Display seats using nested loops
        @Override
        public void displaySeats() {
            System.out.println("\nA = Available | B = Booked\n");

            for (int row = 0; row < 4; row++) {
                System.out.print("Row " + (row + 1) + ": ");

                for (int col = 0; col < 5; col++) {
                    int seatNumber = row * 5 + col + 1;

                    if (seats[row][col] == 0) {
                        System.out.printf("[%02d:A] ", seatNumber);
                    } else {
                        System.out.printf("[%02d:B] ", seatNumber);
                    }
                }

                System.out.println();
            }
        }

        @Override
        public boolean bookSeat(int seatNumber) {
            if (seatNumber < 1 || seatNumber > 20) {
                System.out.println("Choose a seat from 1 to 20.");
                return false;
            }

            int row = (seatNumber - 1) / 5;
            int col = (seatNumber - 1) % 5;

            if (seats[row][col] == 1) {
                System.out.println("This seat is already booked.");
                return false;
            }

            seats[row][col] = 1;
            return true;
        }

        int getBookedSeatCount() {
            int count = 0;

            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 5; col++) {
                    if (seats[row][col] == 1) {
                        count++;
                    }
                }
            }

            return count;
        }
    }

    // MODULE 6: Collections
    static ArrayList<Bus> buses = new ArrayList<Bus>();
    static HashMap<String, String> bookings =
            new HashMap<String, String>();
    static HashSet<String> bookedSeats = new HashSet<String>();

    static int bookingCounter = 1;

    public static void main(String[] args) {

        addBusRoutes();
        loadBookings();

        System.out.println("AVAILABLE CITIES:");
        System.out.println("Hyderabad, Chennai, Vijaywada, Bengaluru");

        System.out.print("\nEnter from: ");
        String from = sc.nextLine().trim();

        System.out.print("Enter to: ");
        String to = sc.nextLine().trim();

        // MODULE 6: Stream filter
        ArrayList<Bus> matchingBuses = new ArrayList<Bus>();

        buses.stream()
             .filter(bus ->
                 bus.getFrom().equalsIgnoreCase(from)
                 && bus.getTo().equalsIgnoreCase(to))
             .forEach(bus -> matchingBuses.add(bus));

        if (matchingBuses.isEmpty()) {
            System.out.println("No buses available for this route.");
        } else {
            for (Bus bus : matchingBuses) {
                bus.displayDetails();
                bus.displaySeats();

                System.out.println("Available seats: "
                        + (20 - bus.getBookedSeatCount()));

                bookingMenu(bus);
            }
        }

        sc.close();
    }

    // Original 10 routes
    static void addBusRoutes() {
        buses.add(new Bus("TS08HK9990", "KK Travels",
                "Hyderabad", "Chennai", "10:30 PM"));

        buses.add(new Bus("NL09HJ2090", "BVR Travels",
                "Hyderabad", "Vijaywada", "9:00 PM - 6:30 AM"));

        buses.add(new Bus("APO9RR1209", "Diwakar Travels",
                "Hyderabad", "Bengaluru", "9:00 PM - 6:30 AM"));

        buses.add(new Bus("TS08HK9989", "RR Travels",
                "Bengaluru", "Hyderabad", "11:00 PM - 6:30 AM"));

        buses.add(new Bus("AP39KL9999", "RRR Travels",
                "Vijaywada", "Hyderabad", "11:00 PM - 6:30 AM"));

        buses.add(new Bus("KA29HH9898", "KR Travels",
                "Chennai", "Hyderabad", "8:00 PM - 6:30 AM"));

        buses.add(new Bus("AP39KL9090", "RRR Travels",
                "Chennai", "Vijaywada", "11:00 PM - 6:30 AM"));

        buses.add(new Bus("KA09YY7895", "Yash Travels",
                "Chennai", "Bengaluru", "9:00 PM - 6:30 AM"));

        buses.add(new Bus("AP39KL9990", "KR Travels",
                "Bengaluru", "Vijaywada", "11:00 PM - 6:30 AM"));

        buses.add(new Bus("TN09JK8888", "Vijay Travels",
                "Bengaluru", "Chennai", "9:00 PM - 6:30 AM"));
    }

    static void bookingMenu(Bus bus) {
        while (true) {
            System.out.println("\n1. Book a seat");
            System.out.println("2. Display seats");
            System.out.println("3. View bookings");
            System.out.println("4. Finish");
            System.out.print("Enter choice: ");

            int choice;

            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    bookTicket(bus);
                    break;
                case 2:
                    bus.displaySeats();
                    break;
                case 3:
                    showBookings();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Choose from 1 to 4.");
            }
        }
    }

    static void bookTicket(Bus bus) {
        if (bus.getBookedSeatCount() == 20) {
            System.out.println("All seats are booked.");
            return;
        }

        bus.displaySeats();
        System.out.print("Enter seat number (1-20): ");

        try {
            int seatNumber = Integer.parseInt(sc.nextLine().trim());

            if (seatNumber < 1 || seatNumber > 20) {
                System.out.println("Invalid seat number.");
                return;
            }

            // MODULE 5: StringBuilder
            StringBuilder builder = new StringBuilder();
            builder.append(bus.getBusNumber());
            builder.append("-");
            builder.append(seatNumber);

            String seatKey = builder.toString();

            // MODULE 6: HashSet prevents duplicate bookings
            if (bookedSeats.contains(seatKey)) {
                System.out.println("This seat is already booked.");
                return;
            }

            if (bus.bookSeat(seatNumber)) {
                String bookingId = "BK" + bookingCounter++;

                String details = "Booking ID: " + bookingId
                        + " | Bus: " + bus.getBusName()
                        + " | Route: " + bus.getFrom() + " to "
                        + bus.getTo()
                        + " | Seat: " + seatNumber;

                bookings.put(bookingId, details);
                bookedSeats.add(seatKey);

                saveBooking(bus, seatNumber, bookingId);

                System.out.println("\nBOOKING SUCCESSFUL!");
                System.out.println(details);
                bus.displaySeats();
            }

        } catch (NumberFormatException e) {
            System.out.println("Enter a valid seat number.");
        }
    }

    // MODULE 5: File writing
    static void saveBooking(Bus bus, int seatNumber,
                            String bookingId) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("bookings.txt", true))) {

            writer.write(bookingId + "|"
                    + bus.getBusNumber() + "|"
                    + seatNumber + "|"
                    + bus.getFrom() + "|"
                    + bus.getTo() + "|"
                    + bus.getBusName());

            writer.newLine();

        } catch (IOException e) {
            System.out.println("Could not save booking: "
                    + e.getMessage());
        }
    }

    // MODULE 5: File reading
    static void loadBookings() {
        File file = new File("bookings.txt");

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader =
                new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|", -1);

                if (data.length != 6) {
                    continue;
                }

                String bookingId = data[0];
                String busNumber = data[1];

                try {
                    int seatNumber = Integer.parseInt(data[2]);

                    if (seatNumber < 1 || seatNumber > 20) {
                        continue;
                    }

                    for (Bus bus : buses) {
                        if (bus.getBusNumber().equals(busNumber)) {
                            String key = busNumber + "-" + seatNumber;

                            if (bookedSeats.add(key)) {
                                bus.bookSeat(seatNumber);

                                String details =
                                        "Booking ID: " + bookingId
                                        + " | Bus: " + data[5]
                                        + " | Route: " + data[3]
                                        + " to " + data[4]
                                        + " | Seat: " + seatNumber;

                                bookings.put(bookingId, details);
                            }

                            break;
                        }
                    }

                    // MODULE 5: substring()
                    if (bookingId.startsWith("BK")) {
                        int number = Integer.parseInt(
                                bookingId.substring(2));

                        if (number >= bookingCounter) {
                            bookingCounter = number + 1;
                        }
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid booking record.");
                }
            }

        } catch (IOException e) {
            System.out.println("Could not read bookings file.");
        }
    }

    // MODULE 6: HashMap
    static void showBookings() {
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        System.out.println("\n--- BOOKING RECORDS ---");

        for (String id : bookings.keySet()) {
            System.out.println(bookings.get(id));
        }
    }
}
