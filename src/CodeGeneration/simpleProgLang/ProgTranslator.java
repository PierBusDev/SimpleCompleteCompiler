package CodeGeneration.simpleProgLang;

import CodeGeneration.CodeGenerator;
import CodeGeneration.OpCode;
import LexAnalysis.Lexers.Lexer;
import LexAnalysis.Tag;
import LexAnalysis.Token;
import LexAnalysis.Word;

import java.io.BufferedReader;
import java.io.IOException;

public class ProgTranslator {

    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    CodeGenerator codeGen = new CodeGenerator("src/CodeGeneration/simpleProgLang/Output.j");
    SymbolTable st = new SymbolTable();
    int count = 0;

    public ProgTranslator(Lexer l, BufferedReader br){
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


    public void prog(){
        //TODO
        int lnext_prog = codeGen.newLabel();
        statlist(lnext_prog);
        codeGen.emitLabel(lnext_prog);
        match(Tag.EOF);
        try{
            codeGen.toJasmin();
        }catch(IOException ioe){
            System.out.println("Io error \n");
        }
        //TODO
    }

    public void stat(int lnext){
        switch(look.tag){
            //todo
            case Tag.PRINT:
                match(Tag.PRINT);
                match('(');
                expr();
                code.emit(OpCode.invokestatic, 1);
                match(')');
                break;
            case Tag.READ:
                match(Tag.READ);
                match('(');
                if(look.tag == Tag.ID){
                    int read_id_addr = st.lookupAddress(((Word)look).lexeme);
                    if(read_id_addr == -1){ // not found
                        read_id_addr = count;
                        st.insert(((Word)look).lexeme, count++);
                    }
                    match(Tag.ID);
                    match(')');
                    codeGen.emit(OpCode.invokestatic, 0);
                    codeGen.emit(OpCode.istore, read_id_addr);
                }else{
                    error("Error in grammar (stat) after read( with \" + look");
                }
                break;
            //TODO
        }
    }


    //TODO

    private void b_expr(int ltrue, int lfalse){
        //TODO
        expr();
        if(look == Word.eq){
            match(Tag.RELOP);
            expr();
            //TODO
        }
        //TODO
    }

    //TODO

    private void exprp(){
        switch(look.tag){
            case '+':
                match('+');
                term();
                codeGen.emit(OpCode.iadd);
                exprp();
                break;
            //TODO
        }
    }

    //TODO
}
