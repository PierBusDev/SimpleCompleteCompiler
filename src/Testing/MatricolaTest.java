package Testing;

import DFA.Matricola;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MatricolaTest {

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
        tc.add(new TestCase("123456Bianchi", true));
        tc.add(new TestCase("654321Rossi", true));
        tc.add(new TestCase("654321Bianchi", false));
        tc.add(new TestCase("123456Rossi", false));
        tc.add(new TestCase("2Bianchi", true));
        tc.add(new TestCase("122C", true));
        tc.add(new TestCase("Rossi", false));
        tc.add(new TestCase("2341", false));
        tc.add(new TestCase("2123132 Bianchi", false));
        tc.add(new TestCase("2123132%Bianchi", false));
        tc.add(new TestCase("2123132bianchi", false)); //surnames must start with uppercase
        tc.add(new TestCase("21VErdi", false)); //and not have uppercase after the first letter
    }


    @Test
    public void scan() {
        for (TestCase elem : tc) {
            String msg = "testing string: " + elem.inputString + " expecting: " + elem.shouldBeAccepted;
            Assert.assertEquals(msg, elem.shouldBeAccepted, Matricola.scan(elem.inputString));
        }
    }
}