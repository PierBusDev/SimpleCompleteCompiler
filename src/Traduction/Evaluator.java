package Traduction;

import LexAnalysis.Lexers.Lexer;
import LexAnalysis.NumberTok;
import LexAnalysis.Tag;
import LexAnalysis.Token;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Evaluator {

    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Evaluator(Lexer l, BufferedReader br){
        lex = l;
        pbr = br;
        move();
    }

    void move(){
        look = lex.lexicalScan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s){
        throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t){
        if(look.tag == t){
            if(look.tag != Tag.EOF) move();
        }else error("syntax error");
    }


    public void start(){
        int expr_val = 0;

        // gui(start -> expr EOF) = { (, NUM }
        if(look.tag == '(' || look.tag == Tag.NUM) {
            expr_val = expr();
            match(Tag.EOF);
        }else{
            error("syntax error: '" + look + "' is not in gui(start -> expr EOF)" );
        }

        System.out.println(expr_val);
    }


    private int expr(){
        int term_val, exprp_val = 0;

        //gui(expr -> term exprp) = { (, NUM}
        if(look.tag == '(' || look.tag == Tag.NUM){
            term_val = term();
            exprp_val = exprp(term_val);
        }else{
            error("syntax error: '" + look + "' is not in gui(expr -> term exprp)" );
        }
        return exprp_val;
    }

    private int exprp(int exprp_i){
        int term_val, exprp_val = 0;

        switch(look.tag){
            case '+': //gui(exprp -> + term exprp) = +
                move();
                term_val = term();
                exprp_val = exprp(exprp_i + term_val);
                break;
            case '-': //gui(exprp -> - term exprp) = -
                move();
                term_val = term();
                exprp_val = exprp(exprp_i - term_val);
                break;
            case ')': //gui(exprp -> epsilon) = { ), EOF}
            case Tag.EOF:
                //do nothing
                exprp_val = exprp_i;
                break;
            default:
                error("syntax error: '" + look + "' is not in gui(exprp -> ...)");
        }
        return exprp_val;
    }

    private int term(){
        int fact_val, term_val = 0;

        if(look.tag == '(' || look.tag == Tag.NUM){ //gui(term -> fact termp) = { (, EOF}
            fact_val = fact();
            term_val = termp(fact_val);
        }else{
            error("syntax error: '" + look + "' is not in gui(term -> fact termp)" );
        }

        return term_val;
    }

    private int termp(int termp_i){
        int fact_val, termp_val = 0;
        switch(look.tag){
            case '*': //gui(termp -> * fact termp) = *
                move();
                fact_val = fact();
                termp_val = termp(termp_i * fact_val);
                break;
            case '/': //gui(termp -> / fact termp) = /
                move();
                fact_val = fact();
                termp_val = termp(termp_i / fact_val);
                break;
            case '+': //gui(termp -> epsilon) = { +, -, ), EOF}
            case '-':
            case ')':
            case Tag.EOF:
                //do nothing
                termp_val = termp_i;
                break;
            default:
                error("syntax error: '" + look + "' is not in gui(termp -> ...)");
        }

        return termp_val;
    }

    private int fact(){
        int fact_val = 0;
        if(look.tag == '('){ // gui(fact ->( expr ) } = (
            move();
            fact_val = expr();
            match(Token.rpt.tag);
        }else if(look.tag == Tag.NUM){ //gui(fact -> NUM) = NUM
            fact_val = Integer.valueOf( ((NumberTok) look).lexeme );
            move();
        }else{
            error("syntax error: '" + look + "' is not in gui(fact-> ...)");
        }

        return fact_val;
    }

    public static void main(String[] args){
        Lexer lex = new Lexer();
        String path = "src/Traduction/mathOperations.txt";
        try{
            BufferedReader br = new BufferedReader(new FileReader(path));
            Evaluator parser = new Evaluator(lex, br);
            parser.start();
            System.out.println("input ok");
            br.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
}
