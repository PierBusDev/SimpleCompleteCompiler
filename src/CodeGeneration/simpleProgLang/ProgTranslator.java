package CodeGeneration.simpleProgLang;

import CodeGeneration.CodeGenerator;
import CodeGeneration.OpCode;
import LexAnalysis.Lexers.Lexer;
import LexAnalysis.NumberTok;
import LexAnalysis.Tag;
import LexAnalysis.Token;
import LexAnalysis.Word;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ProgTranslator {

    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    CodeGenerator codeGen;
    SymbolTable st = new SymbolTable();
    int count = 0;

    public ProgTranslator(Lexer l, BufferedReader br, String outputFileName){
        lex = l;
        pbr = br;
        move();
        codeGen = new CodeGenerator(outputFileName);
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
        //move(); //first move is already called in the constructor
        // gui(PROG -> STATLIST eof) = { id, print, read, if, for, begin }
        if(look.tag == Tag.ID
                || look.tag == Word.print.tag
                || look.tag == Word.read.tag
                || look.tag == Word.iftok.tag
                || look.tag == Word.fortok.tag
                || look.tag == Word.begin.tag) {
            int lnext_prog = codeGen.newLabel();
            statlist(lnext_prog);
            codeGen.emitLabel(lnext_prog);
            match(Tag.EOF, "should have read eof instead of " + look.tag);
            try{
                codeGen.toJasmin();
            }catch(IOException ioe){
                System.out.println("Io error \n");
            }

        }else{
            error("syntax error: '" + look + "' is not in gui(PROG -> STATLIST eof)" );
        }

    }

    private void stat(int lnext){

        if(look.tag == Tag.ID) { // STAT -> id = EXPR
            //try to get the address of this id
            int id_addr = st.lookupAddress(((Word)look).lexeme);
            if(id_addr == -1) { // not found
                id_addr = count;
                st.insert(((Word) look).lexeme, count++); //just store it
            }
            move();
            match(Token.assign.tag, "should have read = instead of " + look.tag);
            expr();
            //now i everything i need for the assignment
            codeGen.emit(OpCode.istore, id_addr);

        //========================================================
        }else if(look.tag == Word.print.tag){ // STAT -> print ( EXPR )
            move();
            match(Token.lpt.tag, "should have read ( instead of " + look.tag );
            expr();
                codeGen.emit(OpCode.invokestatic, 1);
            match(Token.rpt.tag, "should have read ) instead of " + look.tag);

        //========================================================
        }else if(look.tag == Word.read.tag){ // STAT -> read ( id )
            move();
            match(Token.lpt.tag, "should have read ( instead of " + look.tag );
            if(look.tag == Tag.ID){
                //let's try to get the address of this specific id
                int read_id_addr = st.lookupAddress(((Word)look).lexeme);
                if(read_id_addr == -1){ // not found
                    read_id_addr = count;
                    st.insert(((Word)look).lexeme, count++); //just store it
                }
                match(Tag.ID, "should have read ID instead of " + look.tag);
                match(Token.rpt.tag, "should have read ) instead of " + look.tag);
                    codeGen.emit(OpCode.invokestatic, 0);
                    codeGen.emit(OpCode.istore, read_id_addr);
            }else{
                error("Error in grammar (stat) after read( with \" + look");
            }

        //===========================================================
        }else if(look.tag == Word.iftok.tag ){ // STAT -> if BEXPR then STAT ELSE_STAT
            move();
            int ltrue = codeGen.newLabel(), lfalse = codeGen.newLabel();
            bexpr(ltrue);
                codeGen.emit(OpCode.GOto, lfalse);
            //if the comparision returned true this is where to go: (aka execute the then subtree)
                codeGen.emitLabel(ltrue);

            match(Tag.THEN, "should have read then instead of " + look.tag);

            stat(lnext);
            codeGen.emit(OpCode.GOto, lnext);

            //if bexpr evaluates as false it will go here
            codeGen.emitLabel(lfalse);
            // if there is an else i will have the code here
            else_stat(lfalse, lnext);

        //============================================================
        }else if(look.tag == Word.fortok.tag){ // STAT -> for (id = EXPR; BEXPR) do STAT
            int begin, btrue, bfalse;
            move();
            match(Token.lpt.tag, "should have read ( instead of " + look.tag );
                begin = codeGen.newLabel();
                btrue = codeGen.newLabel();
                bfalse = codeGen.newLabel();

            int id_addr = st.lookupAddress(((Word)look).lexeme);
            if(id_addr == -1){ // not found
                id_addr = count;
                st.insert(((Word)look).lexeme, count++); //just store it
            }

            match(Tag.ID, "should have read ID instead of " + look.tag);
            match(Token.assign.tag, "should have read = instead of " + look.tag);
            expr();
                codeGen.emit(OpCode.istore, id_addr );
            match(Token.semicolon.tag, "should have read ; instead of " + look.tag);
                codeGen.emitLabel(begin);
            bexpr(btrue);
                //if the expr is false, goto the next of STAT (jumping out of the for)
                codeGen.emit(OpCode.GOto, bfalse);
            codeGen.emitLabel(btrue);
            match(Token.rpt.tag, "should have read ) instead of " + look.tag);
            match(Word.dotok.tag, "should have read do instead of " + look.tag);
            //if I run this STAT it means I am inside the iteration and at the end I have to recheck the conditions
            // so go to the begin label
            int lincrement = codeGen.newLabel();
            stat(lincrement);
                codeGen.emitLabel(lincrement);
                //code to increment the index
                codeGen.emit(OpCode.ldc, 1);
                codeGen.emit(OpCode.iload, id_addr);
                codeGen.emit(OpCode.iadd);
                codeGen.emit(OpCode.istore, id_addr);
                //-----
                codeGen.emit(OpCode.GOto, begin);

            codeGen.emitLabel(bfalse);
        //====================================================================
        }else if(look.tag == Word.begin.tag){ //STAT -> begin STATLIST end
            move();
            statlist(lnext);
            match(Word.end.tag, "should have read end instead of " + look.tag);

        }else{
            error("syntax error: '" + look + "' is not in any of the gui(STAT -> ..)");
        }
    }


    private void else_stat(int lfalse, int lnext){
        if(look.tag == Word.elsetok.tag){ //ELSE_STAT -> else STAT
            move();
            stat(lnext);
            //codeGen.emitLabel(lnext);
        }else if(look.tag == Token.semicolon.tag
                || look.tag == Word.end.tag
                || look.tag == Tag.EOF){ //gui(ELSE_STAT -> epsilon) = { ;, end, eof }
            //do nothing
        }else{
            error("syntax error: '" + look + "' is not in any of the gui(ELSE_STAT -> ..)");
        }
    }


    private void statlist(int lnext){
        if(look.tag == Tag.ID
                || look.tag == Word.print.tag
                || look.tag == Word.read.tag
                || look.tag == Word.iftok.tag
                || look.tag == Word.fortok.tag
                || look.tag == Word.begin.tag){ //STATLIST-> STAT STATLISTP
            int stat_next = codeGen.newLabel();
            stat(stat_next);
            codeGen.emitLabel(stat_next);
            statlistp();
        }else{
            error("syntax error: '" + look + "' is not in gui(STATLIST-> STAT STATLISTP)");
        }
    }

    private void statlistp(){
        if(look.tag == Token.semicolon.tag){ //STATLISTP -> ; STAT STATLISTP
            move();
            int next = codeGen.newLabel();
            stat(next);
            codeGen.emitLabel(next);
            statlistp();
        }else if(look.tag == Tag.EOF
                || look.tag == Word.end.tag){ // STATLISTP -> epsilon
            //do nothing
        }else{
            error("syntax error: '" + look + "' is not in any gui(STATLISTP -> ...)");
        }
    }

    private void bexpr(int ltrue){
        if(look.tag ==  Token.lpt.tag
                || look.tag ==  Tag.NUM
                || look.tag ==  Tag.ID){ // BEXPR -> EXPR relop EXPR
            expr();
            //I need to keep track of the current (potential) relop type
            Token relopToken = look;
            match(Tag.RELOP, "should have read relop instead of " + look.tag );
            expr();

            //now I have both the subtrees of EXPR and the relop token
            //remember not to move(), I already matched, this is an ausiliary variable not "look"
            if(relopToken == Word.eq){ // ==
                codeGen.emit(OpCode.if_icmpeq, ltrue);
            }else if(relopToken == Word.ge){ // >=
                codeGen.emit(OpCode.if_icmpge, ltrue);
            }else if(relopToken == Word.gt){ // >
                codeGen.emit(OpCode.if_icmpgt, ltrue);
            }else if(relopToken == Word.le){ // <=
                codeGen.emit(OpCode.if_icmple, ltrue);
            }else if(relopToken == Word.lt){ // <
                codeGen.emit(OpCode.if_icmplt, ltrue);
            }else if(relopToken == Word.ne){ // <>
                codeGen.emit(OpCode.if_icmpne, ltrue);
            }else{
                error("Expected a relop symbol");
            }


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
            codeGen.emit(OpCode.iadd);
            exprp();
        }else if(look.tag == Token.minus.tag){ // EXPRP -> - TERM EXPRP
            move();
            term();
            codeGen.emit(OpCode.isub);
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
            codeGen.emit(OpCode.imul);
            termp();
        }else if(look.tag == Token.div.tag){ // TERMP -> / FACT TERMP
            move();
            fact();
            codeGen.emit(OpCode.idiv);
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
            codeGen.emit(OpCode.ldc, Integer.valueOf( ((NumberTok) look).lexeme ));
            move();
        }else if(look.tag ==  Tag.ID){ // FACT -> id;
            int id_addr = st.lookupAddress(((Word)look).lexeme);
            if(id_addr == -1){ // not found
                //id_addr = count;
                //st.insert(((Word)look).lexeme, count++); //just store it

                //if he have not found the address here I have a big problem
                //because of the grammar here I am already "using" this identifier in a operation assuming it is initialized
                //but if I end in this IF someone failed to initialize it, big error!
                error("Identifier " + ((Word) look).lexeme + " is not initialized while at this point it needs to be!" );
            }
            codeGen.emit(OpCode.iload, id_addr);
            move();

        }else{
            error("syntax error: '" + look + "' is not in any of gui(FACT -> ...)");
        }
    }


    public static void main(String[] args){
        Lexer lex = new Lexer();
        String path = "src/CodeGeneration/simpleProgLang/prog.txt";
        try{
            BufferedReader br = new BufferedReader(new FileReader(path));
            ProgTranslator translator = new ProgTranslator(lex, br, "src/CodeGeneration/simpleProgLang/Output.j");
            translator.prog();
            System.out.println("input ok");
            br.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }

        lex = new Lexer();
        path = "src/CodeGeneration/simpleProgLang/advProg.txt";
        try{
            BufferedReader br = new BufferedReader(new FileReader(path));
            ProgTranslator translator = new ProgTranslator(lex, br, "src/CodeGeneration/simpleProgLang/advOutput.j");
            translator.prog();
            System.out.println("input ok");
            br.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
}
