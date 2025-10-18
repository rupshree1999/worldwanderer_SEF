package flight; // keep lowercase package

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.util.Set;

public class FlightSearch {

    // ====== Instance variables (state) ======
    private String  departureDate;            // dd/MM/yyyy (lowercase enforced globally)
    private String  departureAirportCode;     // one of allowed airports (lowercase)
    private boolean emergencyRowSeating;      // true only allowed with economy
    private String  returnDate;               // dd/MM/yyyy (two-way only; mandatory)
    private String  destinationAirportCode;   // allowed airports (lowercase)
    private String  seatingClass;             // "economy","premium economy","business","first"
    private int     adultPassengerCount;
    private int     childPassengerCount;
    private int     infantPassengerCount;

    // ====== Constants ======
    private static final Set<String> ALLOWED_AIRPORTS =
            Set.of("syd","mel","lax","cdg","del","pvg","doh");

    private static final Set<String> ALLOWED_CLASSES =
            Set.of("economy", "premium economy", "business", "first");

    /** Strict dd/MM/yyyy with leap-year rules (ResolverStyle.STRICT). */
    private static final DateTimeFormatter STRICT_DDMMYYYY =
            new DateTimeFormatterBuilder()
                    .parseStrict()
                    .appendPattern("dd/MM/uuuu")
                    .toFormatter()
                    .withResolverStyle(ResolverStyle.STRICT);

    // ====== Core method (Activity 1.2) ======
    public boolean runFlightSearch(
            String departureDate,
            String departureAirportCode,
            boolean emergencyRowSeating,
            String returnDate,
            String destinationAirportCode,
            String seatingClass,
            int adultPassengerCount,
            int childPassengerCount,
            int infantPassengerCount
    ) {
        // ---- Precondition (spec note): all strings are provided in lowercase ----
        if (!isLower(departureDate)
                || !isLower(departureAirportCode)
                || !isLower(returnDate)
                || !isLower(destinationAirportCode)
                || !isLower(seatingClass)) {
            return false;
        }

        // We'll parse dates via helper when needed (keeps state untouched on failure).
        LocalDate dep = null;
        LocalDate ret = null;

        // ====== Condition 1: total passengers must be in [1..9] ======
        int total = adultPassengerCount + childPassengerCount + infantPassengerCount;
        if (total < 1 || total > 9) return false;

        // ====== Condition 2: children cannot be in emergency row OR first class ======
        if ((emergencyRowSeating && childPassengerCount > 0)
                || ("first".equals(seatingClass) && childPassengerCount > 0)) {
            return false;
        }

        // ====== Condition 3: infants cannot be in emergency row OR business class ======
        if ((emergencyRowSeating && infantPassengerCount > 0)
                || ("business".equals(seatingClass) && infantPassengerCount > 0)) {
            return false;
        }

        // ====== Condition 4: up to 2 children per adult ======
        if (childPassengerCount < 0 || childPassengerCount > adultPassengerCount * 2) return false;

        // ====== Condition 5: each infant must have an adult (infants <= adults) ======
        if (infantPassengerCount < 0 || infantPassengerCount > adultPassengerCount) return false;

        // ====== Condition 6: departure date cannot be in the past ======
        // (Requires a valid departure date; if parsing fails, validation fails.)
        dep = parseStrict(departureDate);
        if (dep == null) return false;
        if (dep.isBefore(LocalDate.now())) return false;

        // ====== Condition 7: STRICT date format dd/MM/yyyy, valid combinations ======
        // (Both dates must parse under STRICT rules; return is mandatory.)
        ret = parseStrict(returnDate);
        if (ret == null) return false;

        // ====== Condition 8: flights are two-way; return date cannot be before departure ======
        if (ret.isBefore(dep)) return false;

        // ====== Condition 9: seating class must be one of ("economy","premium economy","business","first") ======
        if (!ALLOWED_CLASSES.contains(seatingClass)) return false;

        // ====== Condition 10 (updated): only economy class can have an emergency row ======
        // (All classes can be non-emergency.)
        if (emergencyRowSeating && !"economy".equals(seatingClass)) return false;

        // ====== Condition 11: allowed airports only; origin != destination ======
        if (!ALLOWED_AIRPORTS.contains(departureAirportCode)) return false;
        if (!ALLOWED_AIRPORTS.contains(destinationAirportCode)) return false;
        if (departureAirportCode.equals(destinationAirportCode)) return false;

        // ---- All good: initialise state atomically (no mutation before this point) ----
        this.departureDate            = departureDate;
        this.departureAirportCode     = departureAirportCode;
        this.emergencyRowSeating      = emergencyRowSeating;
        this.returnDate               = returnDate;
        this.destinationAirportCode   = destinationAirportCode;
        this.seatingClass             = seatingClass;
        this.adultPassengerCount      = adultPassengerCount;
        this.childPassengerCount      = childPassengerCount;
        this.infantPassengerCount     = infantPassengerCount;

        return true;
    }

    // ====== Helpers ======
    private static boolean isLower(String s) {
        return s != null && !s.isEmpty() && s.equals(s.toLowerCase());
    }

    /** Parse a date string with STRICT dd/MM/yyyy rules; returns null on any failure. */
    private static LocalDate parseStrict(String ddMMyyyy) {
        try {
            return LocalDate.parse(ddMMyyyy, STRICT_DDMMYYYY);
        } catch (Exception e) {
            return null;
        }
    }

    // ====== Getters (for tests / Note 7) ======
    public String  getDepartureDate()          { return departureDate; }
    public String  getDepartureAirportCode()   { return departureAirportCode; }
    public boolean isEmergencyRowSeating()     { return emergencyRowSeating; }
    public String  getReturnDate()             { return returnDate; }
    public String  getDestinationAirportCode() { return destinationAirportCode; }
    public String  getSeatingClass()           { return seatingClass; }
    public int     getAdultPassengerCount()    { return adultPassengerCount; }
    public int     getChildPassengerCount()    { return childPassengerCount; }
    public int     getInfantPassengerCount()   { return infantPassengerCount; }
}
