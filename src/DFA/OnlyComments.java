package DFA;

/* here we build the DFA capable of recognizing all string of comments starting with / * (with no spaces) and ending with * / (with no spaces)
 * Alphabet = {/, *, a}
 * note that / * / (without spaces) is not a valid comment
 */


public class OnlyComments {
    public static boolean scan(String s){
        int state = 0;
        int i = 0;

        while(state >= 0 && i < s.length()){
            final char ch = s.charAt(i++);

            switch (state){
                case 0:
                    if (ch == '/')
                        state = 1;
                    else state = -1;
                    break;

                case 1:
                    if(ch == '*')
                        state = 2;
                    else state = -1;
                    break;

                case 2:
                    if(ch == 'a' || ch == '/')
                        state = 2;
                    else if(ch == '*')
                        state = 3;
                    else state = -1;
                    break;

                case 3:
                    if(ch == '*')
                        state = 3;
                    else if(ch == '/')
                        state = 4;
                    else if(ch == 'a')
                        state = 2;
                    else state = -1;
                    break;

                case 4:
                    state = -1;
                    break;
            }
        }
        return (state == 4);
    }



    public static void main(String[] args) {
        System.out.println(OnlyComments.scan(args[0]) ? "Ok" : "Nope");
    }

}