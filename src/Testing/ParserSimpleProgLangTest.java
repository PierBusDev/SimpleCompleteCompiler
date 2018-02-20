package Testing;

import LexAnalysis.Lexers.Lexer;
import Parsing.ParserSimpleProgLang;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class ParserSimpleProgLangTest {


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
        tc.add(new TestCase("a=5; b=c+7; d = 456 /7; a = b", "input accepted" ));

        tc.add(new TestCase("if a <> b\n" +
                "    then b = 86\n" +
                "    else c = 22 / a", "input accepted" ));

        //last istruction must not have ; separator
        tc.add(new TestCase("if a <> b\n" +
                "    then b = 86\n" +
                "    else c = 22 / a;", "'<-1>' is not in any of the gui(STAT -> ..)" ));

        tc.add(new TestCase("print ( test );\n" +
                "\n" +
                "if b > 55\n" +
                "    then read ( pippo);\n" +
                "\n" +
                "for ( test = 58; test > a) do\n" +
                "    print ( ciao )", "input accepted" ));

        tc.add(new TestCase("begin\n" +
                "    b = 5 + 6 / 56 * 98;\n" +
                "    read( a )\n" +
                "end", "input accepted" ));

        tc.add(new TestCase("a = b + 58;" +
                "\nprint(c)", "input accepted" ));

        //an operation without assignmente is not part of the language (should ask a = instead of the first + and stop there)
        tc.add(new TestCase("a + c * 88 - f", "syntax error should have read = instead of 43" ));

        //can't assign the value of read to a variable
        tc.add(new TestCase("a = read( file )", "'<265, read>' is not in gui(EXPR -> TERM EXPRP)" ));

        //two statements must be separated by a ;
        tc.add(new TestCase("a = b \n c = 118", "'<257, c>' is not in any of gui(TERMP -> ...)" ));


    }

    @Test
    public void prog() {
        for (TestCase elem : tc) {
            Lexer lex = new Lexer();
            String msg = "testing string: " + elem.inputString + " expecting: " + elem.shouldBeResult;
            String resultString = "";

            BufferedReader br = new BufferedReader(new StringReader(elem.inputString));

            ParserSimpleProgLang parser = new ParserSimpleProgLang(lex, br);
            try {
                parser.prog();
            }catch (Error e){
                Assert.assertEquals(elem.shouldBeResult, e.getMessage().substring(e.getMessage().lastIndexOf( ':') + 2) );
                //Assert.fail(e.getMessage().substring(e.getMessage().lastIndexOf(':')));
            }finally {
                System.out.println("=================================================\n\n");
            }
        }
    }
}