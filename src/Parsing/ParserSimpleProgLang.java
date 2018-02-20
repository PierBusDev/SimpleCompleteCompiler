package Parsing;

/*
 * Recursive Descent Parser
 */

import LexAnalysis.Lexers.Lexer;
import LexAnalysis.Tag;
import LexAnalysis.Token;
import LexAnalysis.Word;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ParserSimpleProgLang {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public ParserSimpleProgLang(Lexer l, BufferedReader br){
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

    void match(int t, String extraMessage){
        if(look.tag == t){
            if(look.tag != Tag.EOF) move();
        }else error("syntax error " + extraMessage );
    }

    //start here
    public void prog(){
        //move(); //first move is already called in the constructor
        // gui(PROG -> STATLIST eof) = { id, print, read, if, for, begin }
        if(look.tag == Tag.ID
                || look.tag == Word.print.tag
                || look.tag == Word.read.tag
                || look.tag == Word.iftok.tag
                || look.tag == Word.fortok.tag
                || look.tag == Word.begin.tag) {
            statlist();
            match(Tag.EOF, "should have read eof instead of " + look.tag);
        }else{
            error("syntax error: '" + look + "' is not in gui(PROG -> STATLIST eof)" );
        }
    }

    private void stat(){
        if(look.tag == Tag.ID) { // STAT -> id = EXPR
            move();
            match(Token.assign.tag, "should have read = instead of " + look.tag);
            expr();

        }else if(look.tag == Word.print.tag){ // STAT -> print ( EXPR )
            move();
            match(Token.lpt.tag, "should have read ( instead of " + look.tag );
            expr();
            match(Token.rpt.tag, "should have read ) instead of " + look.tag);

        }else if(look.tag == Word.read.tag){ // STAT -> read ( id )
            move();
            match(Token.lpt.tag, "should have read ( instead of " + look.tag );
            match(Tag.ID, "should have read ID instead of " + look.tag);
            match(Token.rpt.tag, "should have read ) instead of " + look.tag);

        }else if(look.tag == Word.iftok.tag ){ // STAT -> if BEXPR then STAT ELSE_STAT
            move();
            bexpr();
            match(Tag.THEN, "should have read then instead of " + look.tag);
            stat();
            else_stat();

        }else if(look.tag == Word.fortok.tag){ // STAT -> for (id = EXPR; BEXPR) do STAT
            move();
            match(Token.lpt.tag, "should have read ( instead of " + look.tag );
            match(Tag.ID, "should have read ID instead of " + look.tag);
            match(Token.assign.tag, "should have read = instead of " + look.tag);
            expr();
            match(Token.semicolon.tag, "should have read ; instead of " + look.tag);
            bexpr();
            match(Token.rpt.tag, "should have read ) instead of " + look.tag);
            match(Word.dotok.tag, "should have read do instead of " + look.tag);
            stat();

        }else if(look.tag == Word.begin.tag){ //STAT -> begin STATLIST end
            move();
            statlist();
            match(Word.end.tag, "should have read end instead of " + look.tag);

        }else{
            error("syntax error: '" + look + "' is not in any of the gui(STAT -> ..)");
        }
    }


    private void else_stat(){
        if(look.tag == Word.elsetok.tag){ //ELSE_STAT -> else STAT
            move();
            stat();
        }else if(look.tag == Token.semicolon.tag
                || look.tag == Word.end.tag
                || look.tag == Tag.EOF){ //gui(ELSE_STAT -> epsilon) = { ;, end, eof }
            //do nothing
        }else{
            error("syntax error: '" + look + "' is not in any of the gui(ELSE_STAT -> ..)");
        }
    }


    private void statlist(){
        if(look.tag == Tag.ID
                || look.tag == Word.print.tag
                || look.tag == Word.read.tag
                || look.tag == Word.iftok.tag
                || look.tag == Word.fortok.tag
                || look.tag == Word.begin.tag){ //STATLIST-> STAT STATLISTP
            stat();
            statlistp();
        }else{
            error("syntax error: '" + look + "' is not in gui(STATLIST-> STAT STATLISTP)");
        }
    }

    private void statlistp(){
        if(look.tag == Token.semicolon.tag){ //STATLISTP -> ; STAT STATLISTP
            move();
            stat();
            statlistp();
        }else if(look.tag == Tag.EOF
            || look.tag == Word.end.tag){ // STATLISTP -> epsilon
            //do nothing
        }else{
            error("syntax error: '" + look + "' is not in any gui(STATLISTP -> ...)");
        }
    }

    private void bexpr(){
        if(look.tag ==  Token.lpt.tag
                || look.tag ==  Tag.NUM
                || look.tag ==  Tag.ID){ // BEXPR -> EXPR relop EXPR
            expr();
            match(Tag.RELOP, "should have read relop instead of " + look.tag );
            expr();
        }else{
            error("syntax error: '" + look + "' is not in gui(BEXPR -> EXPR relop EXPR)");
        }
    }

    private void expr(){
        if(look.tag ==  Token.lpt.tag
                || look.tag ==  Tag.NUM
                || look.tag ==  Tag.ID) {// EXPR -> TERM EXPRP
            term();
            exprp();
        }else{
            error("syntax error: '" + look + "' is not in gui(EXPR -> TERM EXPRP)");
        }
    }

    private void exprp(){
        if(look.tag == Token.plus.tag){ // EXPRP -> + TERM EXPRP
            move();
            term();
            exprp();
        }else if(look.tag == Token.minus.tag){ // EXPRP -> - TERM EXPRP
            move();
            term();
            exprp();
        }else if(look.tag == Word.elsetok.tag
                || look.tag ==  Token.semicolon.tag
                || look.tag ==  Word.end.tag
                || look.tag ==  Tag.EOF
                || look.tag ==  Token.rpt.tag
                || look.tag == Word.then.tag
                || look.tag ==  Tag.RELOP){ // EXPRP -> epsilon
            //do nothing
        }else{
            error("syntax error: '" + look + "' is not in gui(EXPRP -> ...)");
        }
    }

    private void term(){
        if(look.tag ==  Token.lpt.tag
                || look.tag ==  Tag.NUM
                || look.tag ==  Tag.ID) { // TERM -> FACT TERMP
            fact();
            termp();
        }else{
            error("syntax error: '" + look + "' is not in gui(TERM -> FACT TERMP)");
        }
    }

    private void termp(){
        if(look.tag == Token.mult.tag){ //TERMP -> * FACT TERMP
            move();
            fact();
            termp();
        }else if(look.tag == Token.div.tag){ // TERMP -> / FACT TERMP
            move();
            fact();
            termp();
        }else if(look.tag == Token.plus.tag
                || look.tag == Token.minus.tag
                || look.tag == Word.elsetok.tag
                || look.tag ==  Token.semicolon.tag
                || look.tag ==  Word.end.tag
                || look.tag ==  Tag.EOF
                || look.tag ==  Token.rpt.tag
                || look.tag == Word.then.tag
                || look.tag ==  Tag.RELOP){ // TERMP -> epsilon
            //do nothing
        }else{
            error("syntax error: '" + look + "' is not in any of gui(TERMP -> ...)");
        }
    }

    private void fact(){
        if(look.tag ==  Token.lpt.tag) { //FACT -> ( EXPR )
            move();
            expr();
            match(Token.rpt.tag, "should have read ) instead of " + look.tag );
        }else if(look.tag ==  Tag.NUM){ // FACT -> num;
            move();
        }else if(look.tag ==  Tag.ID){ // FACT -> id;
            move();
        }else{
            error("syntax error: '" + look + "' is not in any of gui(FACT -> ...)");
        }
    }


    //++++++++++++++++++++



    public static void main(String[] args){
        Lexer lex = new Lexer();
        String path = "src/Parsing/simplePrograms.txt";
        try{
            BufferedReader br = new BufferedReader(new FileReader(path));
            ParserSimpleProgLang parser = new ParserSimpleProgLang(lex, br);
            parser.prog();
            System.out.println("input ok");
            br.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
}
