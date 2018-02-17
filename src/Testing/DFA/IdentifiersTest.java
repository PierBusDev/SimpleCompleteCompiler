package Testing.DFA;

import DFA.Identifiers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class IdentifiersTest {

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
        tc.add(new TestCase("1", false));
        tc.add(new TestCase("_", false));
        tc.add(new TestCase("___", false));
        tc.add(new TestCase("___1", true));
        tc.add(new TestCase("t", true));
        tc.add(new TestCase("t1000", true));
        tc.add(new TestCase("aaa", true));
        tc.add(new TestCase("000", false));
        tc.add(new TestCase("ab@0", false));
        tc.add(new TestCase("%%", false));
        tc.add(new TestCase("a0a0", true));
    }


    @Test
    public void scan() {
        for (TestCase elem : tc) {
            String msg = "testing string: " + elem.inputString + " expecting: " + elem.shouldBeAccepted;
            Assert.assertEquals(msg, elem.shouldBeAccepted, Identifiers.scan(elem.inputString));
        }
    }
}