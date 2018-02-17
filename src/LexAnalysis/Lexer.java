package LexAnalysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Lexer {
    public static int line = 1;
    private char peek = ' ';

    private void readch(BufferedReader br){
        try{
            peek = (char) br.read();
        }catch (IOException ioe){
            peek = (char) -1; //error
        }
    }

    public Token lexicalScan(BufferedReader br){
        //ignore all spacing chars
        while(peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r'){ //is it a spacing char?
            if(peek == '\n')  //go to next line
            readch(br); //next input
        }

        switch(peek){
            //=============================== Tokens
            case '!':
                peek = ' ';
                return Token.not;
            //=============================== Words
            case '&': //looking for &&
                readch(br);
                if(peek == '&'){
                    peek = ' ';
                    return Word.and;
                }else{
                    System.err.println("Wrong character after & : " + peek );
                    return null;
                }
            //===============================
            case (char) -1:
                return new Token(Tag.EOF);

            default:
                if(Character.isLetter(peek)){
                    //case of identifiers and keywords
                    return null;//TODO remove
                }else if(Character.isDigit(peek)){
                    //case of numbers
                    return null;//TODO remove
                }else{
                    System.err.println("Wrong character: " + peek);
                    return null;
                }
        }
    }

    public static void main(String[] args){
        Lexer lex = new Lexer();
        String path = "./string.txt";
        try{
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do{
                tok = lex.lexicalScan(br);
                System.out.println("Scan: " + tok);
            }while(tok.tag != Tag.EOF);
            br.close();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

}
