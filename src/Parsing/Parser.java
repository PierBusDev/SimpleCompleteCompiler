package Parsing;

/*
 * Recursive Descent Parser
 */

import LexAnalysis.Lexers.Lexer;
import LexAnalysis.Tag;
import LexAnalysis.Token;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br){
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
        //move(); //first move is already called in the constructor
        // gui(start -> expr EOF) = { (, NUM }
        if(look.tag == '(' || look.tag == Tag.NUM) {
            expr();
            match(Tag.EOF);
        }else{
            error("syntax error: '" + look + "' is not in gui(start -> expr EOF)" );
        }
    }

    private void expr(){
        //gui(expr -> term exprp) = { (, NUM}
        if(look.tag == '(' || look.tag == Tag.NUM){
            term();
            exprp();
        }else{
            error("syntax error: '" + look + "' is not in gui(expr -> term exprp)" );
        }
    }

    private void exprp(){
        switch(look.tag){
            case '+': //gui(exprp -> + term exprp) = +
                move();
                term();
                exprp();
                break;
            case '-': //gui(exprp -> - term exprp) = -
                move();
                term();
                exprp();
                break;
            case ')': //gui(exprp -> epsilon) = { ), EOF}
            case Tag.EOF:
                //do nothing
                break;
            default:
                error("syntax error: '" + look + "' is not in gui(exprp -> ...)");
        }
    }

    private void term(){
        if(look.tag == '(' || look.tag == Tag.NUM){ //gui(term -> fact termp) = { (, EOF}
            fact();
            termp();
        }else{
            error("syntax error: '" + look + "' is not in gui(term -> fact termp)" );
        }
    }

    private void termp(){
        switch(look.tag){
            case '*': //gui(termp -> * fact termp) = *
                move();
                fact();
                termp();
                break;
            case '/': //gui(termp -> / fact termp) = /
                move();
                fact();
                termp();
                break;
            case '+': //gui(termp -> epsilon) = { +, -, ), EOF}
            case '-':
            case ')':
            case Tag.EOF:
                //do nothing
                break;
            default:
                error("syntax error: '" + look + "' is not in gui(termp -> ...)");
        }
    }

    private void fact(){
        if(look.tag == '('){ // gui(fact ->( expr ) } = (
            move();
            expr();
            match(Token.rpt.tag);
        }else if(look.tag == Tag.NUM){ //gui(fact -> NUM) = NUM
            move();
        }else{
            error("syntax error: '" + look + "' is not in gui(fact-> ...)");
        }

    }


    public static void main(String[] args){
        Lexer lex = new Lexer();
        String path = "src/Parsing/mathOperations.txt";
        try{
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("input ok");
            br.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
}
