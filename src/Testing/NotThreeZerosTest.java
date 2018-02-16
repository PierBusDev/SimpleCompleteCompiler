package Testing;

import DFA.NotThreeZeros;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class NotThreeZerosTest {
    private List<TestCase> tc = new ArrayList<TestCase>();

    private class TestCase {
        String inputString;
        Boolean shouldBeAccepted;

        TestCase(String inputString, Boolean shouldBeAccepted){
            this.inputString = inputString;
            this.shouldBeAccepted = shouldBeAccepted;
        }
    }

    @Before
    public void setUp() throws Exception {
        tc.add(new TestCase("010101", true));
        tc.add(new TestCase("0001", false));
        tc.add(new TestCase("1100011001", false));
        tc.add(new TestCase("10214", false));
        tc.add(new TestCase("10002000", false));
        tc.add(new TestCase("1001010100", true));
        tc.add(new TestCase("000", false));
    }


    @Test
    public void scan() {
        for(TestCase elem : tc){
            String msg = "testing string: " + elem.inputString + " expecting: " + elem.shouldBeAccepted;
            Assert.assertEquals(msg, elem.shouldBeAccepted, NotThreeZeros.scan(elem.inputString));
        }
    }
}