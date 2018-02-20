package Traduction;

import LexAnalysis.Lexers.Lexer;
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
        int expr_val;
        // TODO
        expr_val = expr();
        match(Tag.EOF);

        System.out.println(expr_val);
    }

    private int expr(){
        int term_val, exprp_val;
        // TODO
        term_val = term();
        exprp_val = exprp(term_val);
        // TODO

        return exprp_val;
    }

    private int exprp(int exprp_i){
        int term_val, exprp_val;

        switch (look.tag){
            case '+':
                match('+');
                term_val = term();
                exprp_val = exprp(exprp_i + term_val);
                break;
                //TODO
        }
    }

    private int term(){

    }

    private int termp(int termp_i){

    }

    private int fact(){

    }

    public static void main(String[] args){
        Lexer lex = new Lexer();
        String path = "src/Parsing/mathOperations.txt";
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
