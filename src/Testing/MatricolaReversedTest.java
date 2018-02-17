package Testing;

import DFA.MatricolaReversed;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MatricolaReversedTest {

    private List<TestCase> tc = new ArrayList<TestCase>();


    private class TestCase {
        String inputString;
        Boolean shouldBeAccepted;

        TestCase(String inputString, Boolean shouldBeAccepted) {
            this.inputString = inputString;
            this.shouldBeAccepted = shouldBeAccepted;
        }
    }

    @Before
    public void setUp() throws Exception {
        tc.add(new TestCase("Bianchi123456", true));
        tc.add(new TestCase("Rossi654321", true));
        tc.add(new TestCase(" Rossi654321", false));
        tc.add(new TestCase("Rossi654321 ", false));
        tc.add(new TestCase("Bianchi654321", false));
        tc.add(new TestCase("Rossi123456", false));
        tc.add(new TestCase("Bianchi2", true));
        tc.add(new TestCase("C122", true));
        tc.add(new TestCase("Rossi", false));
        tc.add(new TestCase("2341", false));
        tc.add(new TestCase("Bianchi 2123132", false));
        tc.add(new TestCase("Bianchi%2123132", false));
        tc.add(new TestCase("bianchi2123132", false)); //surnames must start with uppercase
        tc.add(new TestCase("VErdi21", false)); //and not have uppercase after the first letter
    }


    @Test
    public void scan() {
        for (TestCase elem : tc) {
            String msg = "testing string: " + elem.inputString + " expecting: " + elem.shouldBeAccepted;
            Assert.assertEquals(msg, elem.shouldBeAccepted, MatricolaReversed.scan(elem.inputString));
        }
    }
}