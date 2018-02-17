package Testing;

import DFA.MatricolaWithSpaces;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MatricolaWithSpacesTest {
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
        // should still accept all those of the simple Matricola dfa
        tc.add(new TestCase("123456Bianchi", true));
        tc.add(new TestCase("654321Rossi", true));
        tc.add(new TestCase("654321Bianchi", false));
        tc.add(new TestCase("123456Rossi", false));
        tc.add(new TestCase("2Bianchi", true));
        tc.add(new TestCase("122C", true));
        tc.add(new TestCase("Rossi", false));
        tc.add(new TestCase("2341", false));
        tc.add(new TestCase("2123132%Bianchi", false));
        tc.add(new TestCase("2123132bianchi", false)); //surnames must start with uppercase
        tc.add(new TestCase("21VErdi", false)); //and not have uppercase after the first letter

        //specific test cases for this automa
        tc.add(new TestCase("654321 Rossi", true)); //and not have uppercase after the first letter
        tc.add(new TestCase("123456 Bianchi", true));
        tc.add(new TestCase("123456                            Bianchi", true));
        tc.add(new TestCase("       123456 Bianchi", true));
        tc.add(new TestCase("123456 Bianchi      ", true));
        tc.add(new TestCase("123456 Bianchi    De", false)); //to many spaces in between surname parts
        tc.add(new TestCase("123 456Bianchi", false));
        tc.add(new TestCase("123456 Bian chi", false));
        tc.add(new TestCase("123456De Gasperi", true));
        tc.add(new TestCase("123456 De Gasperi", true));
        tc.add(new TestCase("123456De La Vega  ", true));


    }


    @Test
    public void scan() {
        for (TestCase elem : tc) {
            String msg = "testing string: " + elem.inputString + " expecting: " + elem.shouldBeAccepted;
            Assert.assertEquals(msg, elem.shouldBeAccepted, MatricolaWithSpaces.scan(elem.inputString));
        }
    }
}