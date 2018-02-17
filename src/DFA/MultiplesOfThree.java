package DFA;

/* here we build the DFA capable of recognizing any binary string representing numbers multiples of three
 * basic idea: recognizing the remainders
 */


public class MultiplesOfThree {
    public static boolean scan(String s){
        int state = 0;
        int i = 0;

        while(state >= 0 && i < s.length()){
            final char ch = s.charAt(i++);

            switch (state){
                case 0:
                    if (ch == '0')
                        state = 0;
                    else if(ch == '1')
                        state = 1;
                    else state = -1;
                    break;

                case 1:
                    if(ch == '0')
                        state = 2;
                    else if(ch == '1')
                        state = 0;
                    else state = -1;
                    break;

                case 2:
                    if(ch == '0')
                        state = 1;
                    else if(ch == '1')
                        state = 2;
                    else state = -1;
                    break;
            }
        }
        return (state == 0);
    }



    public static void main(String[] args) {
        System.out.println(MultiplesOfThree.scan(args[0]) ? "Ok" : "Nope");
    }

}