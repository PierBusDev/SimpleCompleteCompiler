package Testing.DFA;

import DFA.OnlyComments;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class OnlyCommentsTest {
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
        tc.add(new TestCase("/****/", true));
        tc.add(new TestCase("/***/", true));
        tc.add(new TestCase("/*a*a*/", true));
        tc.add(new TestCase("/*a/**/", true));
        tc.add(new TestCase("/**a///a/a**/", true));
        tc.add(new TestCase("/**/", true));
        tc.add(new TestCase("/*/*/", true));
        tc.add(new TestCase("/*/", false));
        tc.add(new TestCase("/**/***/", false));
        tc.add(new TestCase("//", false));
    }


    @Test
    public void scan() {
        for(TestCase elem : tc){
            String msg = "testing string: " + elem.inputString + " expecting: " + elem.shouldBeAccepted;
            Assert.assertEquals(msg, elem.shouldBeAccepted, OnlyComments.scan(elem.inputString));
        }
    }
}