package Testing.LexAnalysis;

import LexAnalysis.Lexers.LexerWithAdvancedIdentifiersAndComments;
import LexAnalysis.Tag;
import LexAnalysis.Token;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class LexerWithAdvancedIdentifiersAndCommentsTest {


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
        //it must work with everything of the standard lexer
        tc.add(new TestCase("d=300;", "<257, d> <61> <256, 300> <59> <-1>" ));
        tc.add(new TestCase("print(d*t);", "<264, print> <40> <257, d> <42> <257, t> <41> <59> <-1>" ));
        tc.add(new TestCase("if x>y then x=0", "<259, if> <257, x> <258, >> <257, y> <260, then> <257, x> <61> <256, 0> <-1>" ));
        tc.add(new TestCase("for (ifx=1; ifx<=printread) do ifx=ifx+1", "<262, for> <40> <257, ifx> <61> <256, 1> <59> <257, ifx> <258, <=> <257, printread> <41> <263, do> <257, ifx> <61> <257, ifx> <43> <256, 1> <-1>" ));
        tc.add(new TestCase("&&&", "input is not part of the language"));
        tc.add(new TestCase("17&5", "input is not part of the language"));

        //and recognize advanced identifiers
        tc.add(new TestCase("_ = 153", "input is not part of the language"));
        tc.add(new TestCase("a*7 + ______ / 2", "input is not part of the language"));
        tc.add(new TestCase("123bc = 118", "input is not part of the language"));
        tc.add(new TestCase("pap_8 = 99", "<257, pap_8> <61> <256, 99> <-1>"));
        tc.add(new TestCase("_pap_8 = 99", "<257, _pap_8> <61> <256, 99> <-1>"));
        tc.add(new TestCase("_99 +/* __troll > pap_8 ", "input is not part of the language")); // comment not closed
        // ^^^^ btw in the previous version (without comments) it is a correct string!


        tc.add(new TestCase("/*calcolare la velocita‘ */\n" +
                "d=300; // distanza\n" +
                "t=10; // tempo\n" +
                "print(d*t)", "<257, d> <61> <256, 300> <59> <257, t> <61> <256, 10> <59> <264, print> <40> <257, d> <42> <257, t> <41> <-1>"));

        tc.add(new TestCase("d=300;/*@#?*/d=300;//test", "<257, d> <61> <256, 300> <59> <257, d> <61> <256, 300> <59> <-1>" ));
        tc.add(new TestCase("d=300;/*distanza*//*prova*/", "<257, d> <61> <256, 300> <59> <-1>" ));
        tc.add(new TestCase("x*/y", "<257, x> <42> <47> <257, y> <-1>" ));
        tc.add(new TestCase("x/***/y", "<257, x> <257, y> <-1>" ));

    }

    @Test
    public void lexicalScan() {
        for (TestCase elem : tc) {
            LexerWithAdvancedIdentifiersAndComments lex = new LexerWithAdvancedIdentifiersAndComments();
            String msg = "testing string: " + elem.inputString + " expecting: " + elem.shouldBeResult;
            String resultString = "";

            BufferedReader br = new BufferedReader(new StringReader(elem.inputString));
            Token tok;
            do {
                tok = lex.lexicalScan(br);
                resultString += tok + " ";
                if(tok == null) // the lexer found an error
                    break;
            } while (tok.tag != Tag.EOF);

            if(tok != null)
                Assert.assertEquals(msg, elem.shouldBeResult, resultString.substring(0,resultString.length() - 1) );
            else
                Assert.assertEquals(msg, elem.shouldBeResult, "input is not part of the language");

        }
    }
}
