package flight;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Main {
    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd/MM/uuuu");
    private static String d(int plusDays) {
        return LocalDate.now().plusDays(plusDays).format(DMY);
    }

    public static void main(String[] args) {
        FlightSearch f = new FlightSearch();

        // Valid economy two-way, all lowercase (should be true)
        boolean ok = f.runFlightSearch(d(10), "mel", false, d(15), "pvg", "economy", 1, 0, 0);
        System.out.println("Valid economy → " + ok);

        // Invalid: return before departure (should be false, state should stay as previous)
        boolean bad = f.runFlightSearch(d(10), "mel", false, d(9), "pvg", "economy", 1, 0, 0);
        System.out.println("Return before departure → " + bad);

        // Show current object state (useful to see that failed call didn’t mutate)
        System.out.println("Current state after calls:");
        System.out.println("dep=" + f.getDepartureDate()
                + ", depIata=" + f.getDepartureAirportCode()
                + ", emerg=" + f.isEmergencyRowSeating()
                + ", ret=" + f.getReturnDate()
                + ", destIata=" + f.getDestinationAirportCode()
                + ", class=" + f.getSeatingClass()
                + ", adults=" + f.getAdultPassengerCount()
                + ", children=" + f.getChildPassengerCount()
                + ", infants=" + f.getInfantPassengerCount());
    }
}
