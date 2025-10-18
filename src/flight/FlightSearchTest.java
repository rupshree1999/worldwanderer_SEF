package flight;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
//Student Name:Rupshree Bhadra-s4131551
/**
 * JUnit 5 tests for FlightSearch.runFlightSearch
 * Spec notes:
 * - All STRING inputs for tests are lowercase (airport codes, seating class).
 * - Dates use strict DD/MM/YYYY and are chosen to be valid unless the case is meant to be invalid.
 * - Each condition C1..C12 has TWO datasets (A = usually boundary-valid, B = boundary-invalid).
 * - The final "all-valid" scenario has FOUR distinct valid datasets.
 * - We print the "Actual" value so you can copy it into your results sheet.
 */
public class FlightSearchTest {

    // Handy constants (future dates so "not in past" holds)
    private static final String D1 = "10/12/2026";
    private static final String R1 = "15/12/2026";
    private static final String D2 = "12/12/2026";
    private static final String R2 = "18/12/2026";
    private static final String SAME = "22/12/2026";
    private static final String PAST = "01/01/2024";

    // =========================
    // C1 — total passengers 1..9
    // =========================
    @Test @DisplayName("C1-A: total passengers lower boundary valid (1)")
    void cond1A_total_lower_valid() {
        FlightSearch f = new FlightSearch();
        boolean actual = f.runFlightSearch(D1,"mel",false,R1,"pvg","economy",1,0,0);
        System.out.println("C1-A Actual=" + actual);
        assertTrue(actual);
    }

    @Test @DisplayName("C1-B: total passengers just above upper invalid (total=10)")
    void cond1B_total_above_invalid() {
        FlightSearch f = new FlightSearch();
        boolean actual = f.runFlightSearch(D2,"mel",false,R2,"pvg","economy",5,4,1); // 10 total
        System.out.println("C1-B Actual=" + actual);
        assertFalse(actual);
    }

    // =========================================
    // C2 — children NOT in emergency / NOT first
    // =========================================
    @Test @DisplayName("C2-A: child valid (economy, no emergency)")
    void cond2A_child_valid() {
        FlightSearch f = new FlightSearch();
        boolean actual = f.runFlightSearch(D1,"mel",false,R1,"pvg","economy",2,1,0);
        System.out.println("C2-A Actual=" + actual);
        assertTrue(actual);
    }

    @Test @DisplayName("C2-B: child invalid (emergency row + child)")
    void cond2B_child_emergency_invalid() {
        FlightSearch f = new FlightSearch();
        boolean actual = f.runFlightSearch(D1,"mel",true,R1,"pvg","economy",2,1,0);
        System.out.println("C2-B Actual=" + actual);
        assertFalse(actual);
    }

    // ==========================================
    // C3 — infants NOT in emergency / NOT business
    // ==========================================
    @Test @DisplayName("C3-A: infant invalid (business + infant)")
    void cond3A_infant_business_invalid() {
        FlightSearch f = new FlightSearch();
        boolean actual = f.runFlightSearch(D1,"mel",false,R1,"pvg","business",2,0,1);
        System.out.println("C3-A Actual=" + actual);
        assertFalse(actual);
    }

    @Test @DisplayName("C3-B: infant valid (economy, no emergency)")
    void cond3B_infant_economy_valid() {
        FlightSearch f = new FlightSearch();
        boolean actual = f.runFlightSearch(D1,"mel",false,R1,"pvg","economy",2,0,1);
        System.out.println("C3-B Actual=" + actual);
        assertTrue(actual);
    }

    // ==========================================
    // C4 — ≤ 2 children per adult
    // ==========================================
    @Test @DisplayName("C4-A: boundary valid (1 adult, 2 children)")
    void cond4A_children_boundary_valid() {
        FlightSearch f = new FlightSearch();
        boolean actual = f.runFlightSearch(D1,"mel",false,R1,"pvg","economy",1,2,0);
        System.out.println("C4-A Actual=" + actual);
        assertTrue(actual);
    }

    @Test @DisplayName("C4-B: just over invalid (1 adult, 3 children)")
    void cond4B_children_over_invalid() {
        FlightSearch f = new FlightSearch();
        boolean actual = f.runFlightSearch(D2,"mel",false,R2,"pvg","economy",1,3,0);
        System.out.println("C4-B Actual=" + actual);
        assertFalse(actual);
    }

    // ==========================================
    // C5 — infants ≤ adults (one per adult)
    // ==========================================
    @Test @DisplayName("C5-A: boundary valid (2 adults, 2 infants)")
    void cond5A_infants_equal_valid() {
        FlightSearch f = new FlightSearch();
        boolean actual = f.runFlightSearch(D1,"mel",false,R1,"pvg","economy",2,0,2);
        System.out.println("C5-A Actual=" + actual);
        assertTrue(actual);
    }

    @Test @DisplayName("C5-B: invalid (1 adult, 2 infants)")
    void cond5B_infants_more_invalid() {
        FlightSearch f = new FlightSearch();
        boolean actual = f.runFlightSearch(D1,"mel",false,R1,"pvg","economy",1,0,2);
        System.out.println("C5-B Actual=" + actual);
        assertFalse(actual);
    }

    // ==========================================
    // C6 — departure date not in the past
    // ==========================================
    @Test @DisplayName("C6-A: valid future departure")
    void cond6A_departure_future_valid() {
        FlightSearch f = new FlightSearch();
        boolean actual = f.runFlightSearch(D1,"mel",false,R1,"pvg","economy",1,0,0);
        System.out.println("C6-A Actual=" + actual);
        assertTrue(actual);
    }

    @Test @DisplayName("C6-B: invalid past departure")
    void cond6B_departure_past_invalid() {
        FlightSearch f = new FlightSearch();
        boolean actual = f.runFlightSearch(PAST,"mel",false,R2,"pvg","economy",1,0,0);
        System.out.println("C6-B Actual=" + actual);
        assertFalse(actual);
    }

    // =================================================
    // C7 — strict dd/MM/yyyy + leap year validation
    // =================================================
    @Test @DisplayName("C7-A: 29/02/2026 invalid (non-leap)")
    void cond7A_leap_nonLeap_invalid() {
        FlightSearch f = new FlightSearch();
        boolean actual = f.runFlightSearch("29/02/2026","mel",false,"01/03/2026","pvg","economy",1,0,0);
        System.out.println("C7-A Actual=" + actual);
        assertFalse(actual);
    }

    @Test @DisplayName("C7-B: 29/02/2028 valid (leap)")
    void cond7B_leap_valid() {
        FlightSearch f = new FlightSearch();
        boolean actual = f.runFlightSearch("29/02/2028","mel",false,"02/03/2028","pvg","economy",1,0,0);
        System.out.println("C7-B Actual=" + actual);
        assertTrue(actual);
    }

    // =================================================
    // C8 — return date must be >= departure (two-way only)
    // =================================================
    @Test @DisplayName("C8-A: boundary valid (return == departure)")
    void cond8A_return_equal_valid() {
        FlightSearch f = new FlightSearch();
        boolean actual = f.runFlightSearch(SAME,"mel",false,SAME,"pvg","economy",1,0,0);
        System.out.println("C8-A Actual=" + actual);
        assertTrue(actual);
    }

    @Test @DisplayName("C8-B: invalid (return before departure)")
    void cond8B_return_before_invalid() {
        FlightSearch f = new FlightSearch();
        boolean actual = f.runFlightSearch(D2,"mel",false,"11/12/2026","pvg","economy",1,0,0);
        System.out.println("C8-B Actual=" + actual);
        assertFalse(actual);
    }

    // ==========================================
    // C9 — allowed seating classes only
    // ==========================================
    @Test @DisplayName("C9-A: valid (premium economy)")
    void cond9A_class_premium_valid() {
        FlightSearch f = new FlightSearch();
        boolean actual = f.runFlightSearch(D1,"mel",false,R1,"pvg","premium economy",1,0,0);
        System.out.println("C9-A Actual=" + actual);
        assertTrue(actual);
    }

    @Test @DisplayName("C9-B: invalid (unknown class)")
    void cond9B_class_unknown_invalid() {
        FlightSearch f = new FlightSearch();
        boolean actual = f.runFlightSearch(D1,"mel",false,R1,"pvg","super",1,0,0);
        System.out.println("C9-B Actual=" + actual);
        assertFalse(actual);
    }

    // ==========================================
    // C10 — emergency row only for economy
    // ==========================================
    @Test @DisplayName("C10-A: valid (economy + emergency)")
    void cond10A_emergency_economy_valid() {
        FlightSearch f = new FlightSearch();
        boolean actual = f.runFlightSearch(D1,"mel",true,R1,"pvg","economy",2,0,0);
        System.out.println("C10-A Actual=" + actual);
        assertTrue(actual);
    }

    @Test @DisplayName("C10-B: invalid (business + emergency)")
    void cond10B_emergency_business_invalid() {
        FlightSearch f = new FlightSearch();
        boolean actual = f.runFlightSearch(D1,"mel",true,R1,"pvg","business",2,0,0);
        System.out.println("C10-B Actual=" + actual);
        assertFalse(actual);
    }

    // ==========================================
    // C11 — airports allowed & not the same
    // ==========================================
    @Test @DisplayName("C11-A: valid (mel -> pvg)")
    void cond11A_airports_valid() {
        FlightSearch f = new FlightSearch();
        boolean actual = f.runFlightSearch(D1,"mel",false,R1,"pvg","economy",1,0,0);
        System.out.println("C11-A Actual=" + actual);
        assertTrue(actual);
    }

    @Test @DisplayName("C11-B: invalid (mel -> mel)")
    void cond11B_airports_same_invalid() {
        FlightSearch f = new FlightSearch();
        boolean actual = f.runFlightSearch(D1,"mel",false,R1,"mel","economy",1,0,0);
        System.out.println("C11-B Actual=" + actual);
        assertFalse(actual);
    }

    // ==========================================
    // C12 — lowercase strings only (spec note)
    // ==========================================
    @Test @DisplayName("C12-A: valid (all lowercase)")
    void cond12A_lowercase_valid() {
        FlightSearch f = new FlightSearch();
        boolean actual = f.runFlightSearch(D1,"cdg",false,R1,"doh","business",2,0,0);
        System.out.println("C12-A Actual=" + actual);
        assertTrue(actual);
    }

    @Test @DisplayName("C12-B: invalid (uppercase airport/class)")
    void cond12B_uppercase_invalid() {
        FlightSearch f = new FlightSearch();
        boolean actual = f.runFlightSearch(D1,"MEL",false,R1,"pvg","ECONOMY",1,0,0);
        System.out.println("C12-B Actual=" + actual);
        assertFalse(actual);
    }

    // =================================================
    // All-valid scenario — four distinct valid combos
    // =================================================
    @Test @DisplayName("Valid-A: economy (syd -> lax)")
    void validA_economy() {
        FlightSearch f = new FlightSearch();
        boolean actual = f.runFlightSearch(D1,"syd",false,R1,"lax","economy",1,0,0);
        System.out.println("Valid-A Actual=" + actual);
        assertTrue(actual);
    }

    @Test @DisplayName("Valid-B: economy + emergency (mel -> pvg)")
    void validB_economy_emergency() {
        FlightSearch f = new FlightSearch();
        boolean actual = f.runFlightSearch(D2,"mel",true,R2,"pvg","economy",2,0,0);
        System.out.println("Valid-B Actual=" + actual);
        assertTrue(actual);
    }

    @Test @DisplayName("Valid-C: premium economy + children within ratio (cdg -> doh)")
    void validC_premium_children() {
        FlightSearch f = new FlightSearch();
        boolean actual = f.runFlightSearch("14/12/2026","cdg",false,"20/12/2026","doh","premium economy",2,2,0);
        System.out.println("Valid-C Actual=" + actual);
        assertTrue(actual);
    }

    @Test @DisplayName("Valid-D: business (no infants) (del -> syd)")
    void validD_business_noInfants() {
        FlightSearch f = new FlightSearch();
        boolean actual = f.runFlightSearch("16/12/2026","del",false,"22/12/2026","syd","business",2,0,0);
        System.out.println("Valid-D Actual=" + actual);
        assertTrue(actual);
    }
}
