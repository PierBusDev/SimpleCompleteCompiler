package LexAnalysis.Lexers;

import LexAnalysis.NumberTok;
import LexAnalysis.Tag;
import LexAnalysis.Token;
import LexAnalysis.Word;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class LexerWithAdvancedIdentifiersAndComments {
    public static int line = 1;
    private char peek = ' ';

    //this will be useful to check if a string on the input is a lang keyword
    private HashMap<String, Word> tokenKeywordsHS = new HashMap<>();

    public LexerWithAdvancedIdentifiersAndComments(){

        tokenKeywordsHS.put(Word.iftok.lexeme, Word.iftok);
        tokenKeywordsHS.put(Word.then.lexeme, Word.then);
        tokenKeywordsHS.put(Word.elsetok.lexeme, Word.elsetok);
        tokenKeywordsHS.put(Word.fortok.lexeme, Word.fortok);
        tokenKeywordsHS.put(Word.dotok.lexeme, Word.dotok);
        tokenKeywordsHS.put(Word.print.lexeme, Word.print);
        tokenKeywordsHS.put(Word.read.lexeme, Word.read);
        tokenKeywordsHS.put(Word.begin.lexeme, Word.begin);
        tokenKeywordsHS.put(Word.end.lexeme, Word.end);
    }

    private void readch(BufferedReader br){
        try{
            peek = (char) br.read();
        }catch (IOException ioe){
            peek = (char) -1; //error
        }
    }

    private void checkForComments(BufferedReader br){

    }

    public Token lexicalScan(BufferedReader br){
        //ignore all spacing chars
        while(peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r'){ //is it a spacing char?
            if(peek == '\n') {
                System.out.println();
                line++; //go to next line
            }
            readch(br); //next input
        }

        if(peek == '/')
            checkForComments(br);

        switch(peek){
            //=============================== Tokens
            case '/':
                //===== FIRST here we have to check for comments!
                readch(br);
                if(peek == '/'){ //case of // comment
                    //ignore everything until EOF or \n
                    readch(br);
                    while(peek != (char) -1 && peek != '\n' ){
                        readch(br);
                    }
                    //go forward
                    return this.lexicalScan(br);
                }else if(peek == '*'){ //case /*
                    readch(br);
                    if(peek == (char) -1){
                        System.err.println("Error, a comment starting with /* was not closed correctly before the eof");
                        return null;
                    }
                    while(true){
                        if(peek == '*'){
                            readch(br);
                            if(peek == '/'){ //end of comment
                                readch(br);
                                //go forward
                                return this.lexicalScan(br);
                            }else{ //it was just an isolated *
                                readch(br);
                            }
                        }else{ //we are still inside the comment, skip the char
                            readch(br);
                        }
                    }
                }else{
                    return Token.div;
                }
            case '!':
                peek = ' ';
                return Token.not;
            case '(':
                peek = ' ';
                return Token.lpt;
            case ')':
                peek = ' ';
                return Token.rpt;
            case '+':
                peek = ' ';
                return Token.plus;
            case '-':
                peek = ' ';
                return Token.minus;
            case '*':
                peek = ' ';
                return Token.mult;
            case ';':
                peek = ' ';
                return Token.semicolon;
            // careful here, = can be both a simple '=' or a operator '=='
            case '=':
                readch(br);
                if(peek == '='){ // ==
                    peek = ' ';
                    return Word.eq;
                }else{ // just =
                    return Token.assign;
                }

            //=============================== Operators (defined in class Word)
            case '&': //looking for &&
                readch(br);
                if(peek == '&'){
                    peek = ' ';
                    return Word.and;
                }else{
                    System.err.println("Wrong character after & : " + peek );
                    return null;
                }
            case '|': //looking for ||
                readch(br);
                if(peek == '|'){
                    peek = ' ';
                    return Word.or;
                }else{
                    System.err.println("Wrong character after | : " + peek );
                    return null;
                }
            case '<': //looking for '<', '<=', '<>'
                readch(br);
                if(peek == '=') { // <=
                    peek = ' ';
                    return Word.le;
                }else if(peek == '>'){ // <>
                    peek = ' ';
                    return Word.ne;
                }else{ //just <
                    return Word.lt;
                }
            case '>': //looking for '>', '>='
                readch(br);
                if(peek == '='){ // <=
                    peek = ' ';
                    return Word.ge;
                }else{ // just <
                    return Word.gt;
                }

            //===============================
            case (char) -1:
                return new Token(Tag.EOF);

            default:
                if(Character.isLetter(peek) || peek == '_'){
                    //=========================== case of IDENTIFIERS and KEYWORDS
                    //let's read until the next space
                    String tempStr = "" + peek;
                    readch(br);
                    while(Character.isLetter(peek) || Character.isDigit(peek) || peek == '_'){
                        tempStr += peek;
                        readch(br);
                    }
                    if(tokenKeywordsHS.containsKey(tempStr)){
                        //KEYWORD
                        return tokenKeywordsHS.get(tempStr);
                    }else{
                        //IDENTIFIER
                        if(DFA.Identifiers.scan(tempStr))
                            return new Word(Tag.ID, tempStr); //257 is the code for identifiers
                        else{
                            System.err.println("Not a valid identifier name: " + tempStr );
                            return null;
                        }

                    }

                }else if(Character.isDigit(peek)){
                    //=========================== case of NUMBERS
                    String formingNumber = "" + peek;
                    readch(br);
                    while(Character.isDigit(peek)){ //keep adding numbers to the string until possible
                        formingNumber += peek;
                        readch(br);
                    }

                    //now I have also to check that this is not something in the form string_of_numbers+string_of_letters (like: 234abc, 2pippo)
                    //because in that case is just an invalid identifier
                    // so, is the next char a letter?
                    if(Character.isLetter(peek)){
                        System.err.println("Found a not valid identifier name: " + formingNumber + peek );
                        return null;
                    }else
                        return new NumberTok(formingNumber);
                }else{
                    System.err.println("Wrong character: " + peek);
                    return null;
                }
        }
    }




    public static void main(String[] args){
        LexerWithAdvancedIdentifiersAndComments lex = new LexerWithAdvancedIdentifiersAndComments();
        String path = "src/LexAnalysis/stringAdvIdentifiersAndComments.txt";
        try{
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do{
                tok = lex.lexicalScan(br);
                System.out.print(tok + " ");
            }while(tok.tag != Tag.EOF);
            br.close();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

}
