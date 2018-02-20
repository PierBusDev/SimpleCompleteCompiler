package Testing;

import LexAnalysis.Lexers.Lexer;
import Parsing.Parser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class ParserTest {


    private List<TestCase> tc = new ArrayList<TestCase>();


    private class TestCase {
        String inputString;
        String shouldBeResult;

        TestCase(String inputString, String shouldBeResult) {
            this.inputString = inputString;
            this.shouldBeResult = shouldBeResult;
        }
    }

    @Before
    public void setUp() throws Exception {
        tc.add(new TestCase("(5+2)*872387 + 76 - (6-7-8-9) + 245 - 99 /234 *1 -2", "input accepted" ));
        // = is not a valid char
        tc.add(new TestCase("(5+2)*44=23+2", "'<61>' is not in gui(termp -> ...)" ));
        // nor are ids
        tc.add(new TestCase("pippo = 889899", "'<257, pippo>' is not in gui(start -> expr EOF)" ));

        //balanced par
        tc.add(new TestCase("( (5+2) /34 - (99-8-1) * (2 *(5-5)) )", "input accepted" ));
        //not balanced par
        tc.add(new TestCase(" (5+2) /34 - (99-8-1) * (2 *(5-5)) )", "syntax error" ));
        tc.add(new TestCase("( (5+2) /34 - (99-8-1) * (2 *(5-5)) ", "syntax error" ));
        tc.add(new TestCase("( (5+2) /34 - (99-8-1 * (2 *(5-5)) )", "syntax error" ));

        tc.add(new TestCase("5+3\n -4+5+23\t /2+4", "input accepted" ));

        //no spaces in between numbers
        tc.add(new TestCase("5+3 4 - 23", "'<256, 4>' is not in gui(termp -> ...)" ));
        //no operator as last input (incomplete operation)
        tc.add(new TestCase("5+3-", "'<-1>' is not in gui(term -> fact termp)" ));





    }

    @Test
    public void start() {
        for (TestCase elem : tc) {
            Lexer lex = new Lexer();
            String msg = "testing string: " + elem.inputString + " expecting: " + elem.shouldBeResult;
            String resultString = "";

            BufferedReader br = new BufferedReader(new StringReader(elem.inputString));

            Parser parser = new Parser(lex, br);
            try {
                parser.start();
            }catch (Error e){
                Assert.assertEquals(elem.shouldBeResult, e.getMessage().substring(e.getMessage().lastIndexOf( ':') + 2) );
                //Assert.fail(e.getMessage().substring(e.getMessage().lastIndexOf(':')));
            }finally {
                System.out.println("=================================================\n\n");
            }
        }
    }
}