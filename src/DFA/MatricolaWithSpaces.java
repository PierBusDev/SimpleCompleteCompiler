package DFA;

/*
 * This particular dfa must accept strings formed by a string of number (of any dimension) followed by a string of letters (of any dimension) based on:
 * if the string of number is even the string of letters MUST start with a-kA-k
 * if is odd the string of letters MUST start with l-zL-z
 * the string is a surname (first letter uppercase, all the rest lower case)
 * the surname can be composed, eg "De La Vega"
 * there can be any number of spaces: in between the matricola and the surname
 * before the numbers
 * after the surname
 * inside the surname if it is composed (how to recognize it: each part must start with an uppercase, eg "De La Vega" is ok, "De la Vega" is not
 */

public class MatricolaWithSpaces {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;

        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0:
                    if( ch == ' ')
                        state = 0;
                    else if (!Character.isLetter(ch) && Character.getNumericValue(ch) % 2 == 0) //even
                        state = 1;
                    else if (!Character.isLetter(ch) && Character.getNumericValue(ch) % 2 != 0) //odd
                        state = 2;
                    else state = -1;
                    break;

                case 1:
                    if(ch == ' ')
                        state = 3;
                    else if (Character.toString(ch).matches("[A-K]"))
                        state = 5;
                    else if (!Character.isLetter(ch) && Character.getNumericValue(ch) % 2 == 0) //even
                        state = 1;
                    else if (!Character.isLetter(ch) && Character.getNumericValue(ch) % 2 != 0) //odd
                        state = 2;
                    else state = -1;
                    break;

                case 2:
                    if(ch == ' ')
                        state = 4;
                    else if (Character.toString(ch).matches("[L-Z]"))
                        state = 5;
                    else if (!Character.isLetter(ch) && Character.getNumericValue(ch) % 2 == 0) //even
                        state = 1;
                    else if (!Character.isLetter(ch) && Character.getNumericValue(ch) % 2 != 0) //odd
                        state = 2;
                    else state = -1;
                    break;

                case 3:
                    if(Character.toString(ch).matches("[A-K]"))
                        state = 5;
                    else if (ch == ' ')
                        state = 3;
                    else
                        state = -1;
                    break;

                case 4:
                    if(Character.toString(ch).matches("[L-Z]"))
                        state = 5;
                    else if(ch == ' ')
                        state = 3;
                    else
                        state = -1;
                    break;

                case 5:
                    if(Character.toString(ch).matches("[a-z]"))
                        state = 5;
                    else if(ch == ' ')
                        state = 6;
                    else
                        state = -1;
                    break;

                case 6:
                    if(Character.toString(ch).matches("[A-Z]"))
                        state = 5;
                    else if(ch == ' ')
                        state = 7;
                    else
                        state = -1;
                    break;

                case 7:
                    if(ch == ' ')
                        state = 7;
                    else
                        state = -1;
                    break;
            }
        }
        return (state == 5 || state == 7);
    }

    public static void main(String[] args) {
        System.out.println(NotThreeZeros.scan(args[0]) ? "Ok" : "Nope");
    }
}
