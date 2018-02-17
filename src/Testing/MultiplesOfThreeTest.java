package Testing;

import DFA.MultiplesOfThree;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MultiplesOfThreeTest {

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
        tc.add(new TestCase("11", true)); //3
        tc.add(new TestCase("110", true));//6
        tc.add(new TestCase("1001", true));//9
        tc.add(new TestCase("1100", true));//12
        tc.add(new TestCase("1111", true));//15
        tc.add(new TestCase("10010", true));//18
        tc.add(new TestCase("0", true));
        tc.add(new TestCase("1", false));
        tc.add(new TestCase("10", false));//2
        tc.add(new TestCase("111", false));//7
    }


    @Test
    public void scan() {
        for(TestCase elem : tc){
            String msg = "testing string: " + elem.inputString + " expecting: " + elem.shouldBeAccepted;
            Assert.assertEquals(msg, elem.shouldBeAccepted, MultiplesOfThree.scan(elem.inputString));
        }
    }
}