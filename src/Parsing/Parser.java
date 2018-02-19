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
        move();
        expr();
        match(Tag.EOF);
        //TODO
    }

    private void expr(){

    }

    private void exprp(){
        switch(look.tag){
            case '+':
                //TODO
        }
    }

    private void term(){

    }

    private void termp(){

    }

    private void fact(){

    }


    public static void main(String[] args){
        Lexer lex = new Lexer();
        String path = "";
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
