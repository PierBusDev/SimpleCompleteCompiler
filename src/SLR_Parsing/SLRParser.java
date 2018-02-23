package SLR_Parsing;

import LexAnalysis.Lexers.Lexer;
import LexAnalysis.Tag;
import LexAnalysis.Token;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SLRParser {
        private enum typeOfActions{ shift, reduce, accept};
        private class Action {
            public typeOfActions type;
            public int value;

            public Action(typeOfActions t, int value){
                type =t;
                this.value =value;
            }
        }

        private enum NonTerm{ EXPR, TERM, FACT};

        private Lexer lex;
        private BufferedReader pbr;
        private Token look;
        private Stack<Integer> stack = new Stack<>();

        private Map<Integer, Action> actions[];
        private Map<Integer, Integer> gotoTable[];

    public SLRParser(Lexer l, BufferedReader br){
        lex = l;
        pbr = br;
        move();

        stack.push(0); //initial state
        actions = initializeActions(actions);
        gotoTable = initializeGoto(gotoTable);

    }

    private Map<Integer, Action>[] initializeActions(Map<Integer, Action>[] actions){
        actions = new HashMap[16];
        //state 0
        actions[0] = new HashMap<Integer,Action>();
        actions[0].put(Tag.NUM, new Action(typeOfActions.shift, 5));
        actions[0].put(Token.lpt.tag, new Action(typeOfActions.shift, 4));
        //state 1
        actions[1] = new HashMap<Integer,Action>();
        actions[1].put(Tag.EOF, new Action(typeOfActions.accept, -1));
        actions[1].put(Token.plus.tag, new Action(typeOfActions.shift, 6));
        actions[1].put(Token.minus.tag, new Action(typeOfActions.shift, 7));
        //state 2
        actions[2] = new HashMap<Integer,Action>();
        actions[2].put(Tag.EOF, new Action(typeOfActions.reduce, 3));
        actions[2].put(Token.rpt.tag, new Action(typeOfActions.reduce, 3));
        actions[2].put(Token.plus.tag, new Action(typeOfActions.reduce, 3));
        actions[2].put(Token.minus.tag, new Action(typeOfActions.reduce, 3));
        actions[2].put(Token.mult.tag, new Action(typeOfActions.shift, 10));
        actions[2].put(Token.div.tag, new Action(typeOfActions.shift, 11));
        //state 3
        actions[3] = new HashMap<Integer,Action>();
        actions[3].put(Token.rpt.tag, new Action(typeOfActions.reduce, 6));
        actions[3].put(Tag.EOF, new Action(typeOfActions.reduce, 6));
        actions[3].put(Token.plus.tag, new Action(typeOfActions.reduce, 6));
        actions[3].put(Token.minus.tag, new Action(typeOfActions.reduce, 6));
        actions[3].put(Token.mult.tag, new Action(typeOfActions.reduce, 6));
        actions[3].put(Token.div.tag, new Action(typeOfActions.reduce, 6));
        //state 4
        actions[4] = new HashMap<Integer,Action>();
        actions[4].put(Tag.NUM, new Action(typeOfActions.shift, 5));
        actions[4].put(Token.lpt.tag, new Action(typeOfActions.shift, 4));
        //state 5
        actions[5] = new HashMap<Integer,Action>();
        actions[5].put(Token.rpt.tag, new Action(typeOfActions.reduce, 8));
        actions[5].put(Tag.EOF, new Action(typeOfActions.reduce, 8));
        actions[5].put(Token.plus.tag, new Action(typeOfActions.reduce, 8));
        actions[5].put(Token.minus.tag, new Action(typeOfActions.reduce, 8));
        actions[5].put(Token.mult.tag, new Action(typeOfActions.reduce, 8));
        actions[5].put(Token.div.tag, new Action(typeOfActions.reduce, 8));
        //state 6
        actions[6] = new HashMap<Integer,Action>();
        actions[6].put(Tag.NUM, new Action(typeOfActions.shift, 5));
        actions[6].put(Token.lpt.tag, new Action(typeOfActions.shift, 4));
        //state 7
        actions[7] = new HashMap<Integer,Action>();
        actions[7].put(Tag.NUM, new Action(typeOfActions.shift, 5));
        actions[7].put(Token.lpt.tag, new Action(typeOfActions.shift, 4));
        //state 8
        actions[8] = new HashMap<Integer,Action>();
        actions[8].put(Tag.EOF, new Action(typeOfActions.reduce, 1));
        actions[8].put(Token.rpt.tag, new Action(typeOfActions.reduce, 1));
        actions[8].put(Token.plus.tag, new Action(typeOfActions.reduce, 1));
        actions[8].put(Token.minus.tag, new Action(typeOfActions.reduce, 1));
        actions[8].put(Token.mult.tag, new Action(typeOfActions.shift, 10));
        actions[8].put(Token.div.tag, new Action(typeOfActions.shift, 11));
        //state 9
        actions[9] = new HashMap<Integer,Action>();
        actions[9].put(Tag.EOF, new Action(typeOfActions.reduce, 2));
        actions[9].put(Token.rpt.tag, new Action(typeOfActions.reduce, 2));
        actions[9].put(Token.plus.tag, new Action(typeOfActions.reduce, 2));
        actions[9].put(Token.minus.tag, new Action(typeOfActions.reduce, 2));
        actions[9].put(Token.mult.tag, new Action(typeOfActions.shift, 10));
        actions[9].put(Token.div.tag, new Action(typeOfActions.shift, 11));
        //state 10
        actions[10] = new HashMap<Integer,Action>();
        actions[10].put(Tag.NUM, new Action(typeOfActions.shift, 5));
        actions[10].put(Token.lpt.tag, new Action(typeOfActions.shift, 4));
        //state 11
        actions[11] = new HashMap<Integer,Action>();
        actions[11].put(Tag.NUM, new Action(typeOfActions.shift, 5));
        actions[11].put(Token.lpt.tag, new Action(typeOfActions.shift, 4));
        //state 12
        actions[12] = new HashMap<Integer,Action>();
        actions[12].put(Token.rpt.tag, new Action(typeOfActions.reduce, 5));
        actions[12].put(Tag.EOF, new Action(typeOfActions.reduce, 5));
        actions[12].put(Token.plus.tag, new Action(typeOfActions.reduce, 5));
        actions[12].put(Token.minus.tag, new Action(typeOfActions.reduce, 5));
        actions[12].put(Token.mult.tag, new Action(typeOfActions.reduce, 5));
        actions[12].put(Token.div.tag, new Action(typeOfActions.reduce, 5));
        //state 13
        actions[13] = new HashMap<Integer,Action>();
        actions[13].put(Token.rpt.tag, new Action(typeOfActions.reduce, 4));
        actions[13].put(Tag.EOF, new Action(typeOfActions.reduce, 4));
        actions[13].put(Token.plus.tag, new Action(typeOfActions.reduce, 4));
        actions[13].put(Token.minus.tag, new Action(typeOfActions.reduce, 4));
        actions[13].put(Token.mult.tag, new Action(typeOfActions.reduce, 4));
        actions[13].put(Token.div.tag, new Action(typeOfActions.reduce, 4));
        //state 14
        actions[14] = new HashMap<Integer,Action>();
        actions[14].put(Token.rpt.tag, new Action(typeOfActions.shift, 15));
        actions[14].put(Token.plus.tag, new Action(typeOfActions.shift, 6));
        actions[14].put(Token.minus.tag, new Action(typeOfActions.shift, 7));
        //state 15
        actions[15] = new HashMap<Integer,Action>();
        actions[15].put(Token.rpt.tag, new Action(typeOfActions.reduce, 7));
        actions[15].put(Tag.EOF, new Action(typeOfActions.reduce, 7));
        actions[15].put(Token.plus.tag, new Action(typeOfActions.reduce, 7));
        actions[15].put(Token.minus.tag, new Action(typeOfActions.reduce, 7));
        actions[15].put(Token.mult.tag, new Action(typeOfActions.reduce, 7));
        actions[15].put(Token.div.tag, new Action(typeOfActions.reduce, 7));

        return actions;
    }

    public Map<Integer, Integer>[] initializeGoto(Map<Integer, Integer>[] gotoTable){
        gotoTable = new HashMap[16];
        //0
        gotoTable[0] = new HashMap<>();
        gotoTable[0].put(NonTerm.EXPR.ordinal(), 1);
        gotoTable[0].put(NonTerm.TERM.ordinal(), 2);
        gotoTable[0].put(NonTerm.FACT.ordinal(), 3);
        //1
        //2
        //3
        //4
        gotoTable[4] = new HashMap<>();
        gotoTable[4].put(NonTerm.EXPR.ordinal(), 14);
        gotoTable[4].put(NonTerm.TERM.ordinal(), 2);
        gotoTable[4].put(NonTerm.FACT.ordinal(), 3);
        //5
        //6
        gotoTable[6] = new HashMap<>();
        gotoTable[6].put(NonTerm.TERM.ordinal(), 8);
        gotoTable[6].put(NonTerm.FACT.ordinal(), 3);
        //7
        gotoTable[7] = new HashMap<>();
        gotoTable[7].put(NonTerm.TERM.ordinal(), 9);
        gotoTable[7].put(NonTerm.FACT.ordinal(), 3);
        //8
        //9
        //10
        gotoTable[10] = new HashMap<>();
        gotoTable[10].put(NonTerm.FACT.ordinal(), 13);
        //11
        gotoTable[11] = new HashMap<>();
        gotoTable[11].put(NonTerm.FACT.ordinal(), 12);
        //12
        //13
        //14
        //15

        return gotoTable;

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
        while(true){
            if( actions[stack.peek()].get(look.tag).type == typeOfActions.shift){ //SHIFTING
                int newState = actions[stack.peek()].get(look.tag).value;
                stack.push(newState);
                move();
            }else if( actions[stack.peek()].get(look.tag).type == typeOfActions.reduce ){
                int production = actions[stack.peek()].get(look.tag).value;
                switch(production){
                    case 1:
                        stack.pop(); stack.pop(); stack.pop();
                        if(gotoTable[stack.peek()].containsKey(NonTerm.EXPR.ordinal())){
                            stack.push(gotoTable[stack.peek()].get(NonTerm.EXPR.ordinal()));
                        }else{
                            error("NonTerm EXPR has no goto for state " + stack.peek());
                        }
                        break;

                    case 2:
                        stack.pop(); stack.pop(); stack.pop();
                        if(gotoTable[stack.peek()].containsKey(NonTerm.EXPR.ordinal())){
                            stack.push(gotoTable[stack.peek()].get(NonTerm.EXPR.ordinal()));
                        }else{
                            error("NonTerm EXPR has no goto  for state " + stack.peek());
                        }
                        break;

                    case 3:
                        stack.pop();
                        if(gotoTable[stack.peek()].containsKey(NonTerm.EXPR.ordinal())){
                            stack.push(gotoTable[stack.peek()].get(NonTerm.EXPR.ordinal()));
                        }else{
                            error("NonTerm EXPR has no goto for state " + stack.peek());
                        }
                        break;

                    case 4:
                        stack.pop(); stack.pop(); stack.pop();
                        if(gotoTable[stack.peek()].containsKey(NonTerm.TERM.ordinal())){
                            stack.push(gotoTable[stack.peek()].get(NonTerm.TERM.ordinal()));
                        }else{
                            error("NonTerm TERM has no goto for state " + stack.peek());
                        }
                        break;

                    case 5:
                        stack.pop(); stack.pop(); stack.pop();
                        if(gotoTable[stack.peek()].containsKey(NonTerm.TERM.ordinal())){
                            stack.push(gotoTable[stack.peek()].get(NonTerm.TERM.ordinal()));
                        }else{
                            error("NonTerm TERM has no goto for state " + stack.peek());
                        }
                        break;

                    case 6:
                        stack.pop();
                        if(gotoTable[stack.peek()].containsKey(NonTerm.TERM.ordinal())){
                            stack.push(gotoTable[stack.peek()].get(NonTerm.TERM.ordinal()));
                        }else{
                            error("NonTerm TERM has no goto for state " + stack.peek());
                        }
                        break;

                    case 7:
                        stack.pop(); stack.pop(); stack.pop();
                        if(gotoTable[stack.peek()].containsKey(NonTerm.FACT.ordinal())){
                            stack.push(gotoTable[stack.peek()].get(NonTerm.FACT.ordinal()));
                        }else{
                            error("NonTerm FACT has no goto for state " + stack.peek());
                        }
                        break;

                    case 8:
                        stack.pop();
                        if(gotoTable[stack.peek()].containsKey(NonTerm.FACT.ordinal())){
                            stack.push(gotoTable[stack.peek()].get(NonTerm.FACT.ordinal()));
                        }else{
                            error("NonTerm FACT has no goto for state " + stack.peek());
                        }
                        break;
                }
            }else if(actions[stack.peek()].get(look.tag).type == typeOfActions.accept){
                System.out.println("STRING CORRECT!");
                break; //from the while
            }else{
                error("error in the outer while loop!");
            }
        }
    }

    public static void main(String[] args){
        Lexer lex = new Lexer();
        String path = "src/SLR_Parsing/mathOperations.txt";
        try{
            BufferedReader br = new BufferedReader(new FileReader(path));
            SLRParser parser = new SLRParser(lex, br);
            parser.start();
            System.out.println("input ok");
            br.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
}
