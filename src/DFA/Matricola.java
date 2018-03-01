package DFA;

/*
 * This particular dfa must accept strings formed by a string of number (of any dimension) followed by a string of letters (of any dimension) based on:
 * if the string of number is even the string of letters MUST start with A-k
 * if is odd the string of letters MUST start with L-z
 * the string is a surname (first letter uppercase, all the rest lower case)
 */

public class Matricola {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;

        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0:
                    if (Character.isDigit(ch) && Character.getNumericValue(ch) % 2 == 0) //even
                        state = 1;
                    else if (Character.isDigit(ch) && Character.getNumericValue(ch) % 2 != 0) //odd
                        state = 2;
                    else state = -1;
                    break;

                case 1:
                    if (Character.toString(ch).matches("[A-K]"))
                        state = 3;
                    else if (Character.isDigit(ch) && Character.getNumericValue(ch) % 2 == 0) //even
                        state = 1;
                    else if (Character.isDigit(ch) && Character.getNumericValue(ch) % 2 != 0) //odd
                        state = 2;
                    else state = -1;
                    break;

                case 2:
                    if (Character.toString(ch).matches("[L-Z]"))
                        state = 3;
                    else if (Character.isDigit(ch) && Character.getNumericValue(ch) % 2 == 0) //even
                        state = 1;
                    else if (Character.isDigit(ch) && Character.getNumericValue(ch) % 2 != 0) //odd
                        state = 2;
                    else state = -1;
                    break;

                case 3:
                    if(Character.toString(ch).matches("[a-z]"))
                        state = 3;
                    else
                        state = -1;
            }
        }
        return (state == 3);
    }

    public static void main(String[] args) {
        System.out.println(Matricola.scan(args[0]) ? "Ok" : "Nope");
    }
}
