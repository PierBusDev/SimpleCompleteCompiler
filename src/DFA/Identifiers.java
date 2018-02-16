package DFA;

/* This DFA should recognize a simple type of identifier names.
 * They are not empty strings that:
 * can start with a letter or symbol '_'
 * cannot contain only the symbol '_'
 * can contain numbers, letters or '_'
 * Note that letters can be both upper and lower case!
 */

public class Identifiers {

    public static boolean scan(String s) {
        int state = 0;
        int i = 0;

        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0:
                    if (ch == '_')
                        state = 1;
                    else if (Character.toString(ch).matches("[a-zA-Z]")) //is a letter
                        state = 2;
                    else state = -1;
                    break;

                case 1:
                    if (ch == '_')
                        state = 1;
                    else if (Character.toString(ch).matches("[a-zA-Z]") || Character.toString(ch).matches("[0-9]")) //is a number or a letter
                        state = 2;
                    else state = -1;
                    break;

                case 2:
                    if (ch == '_' ||
                            Character.toString(ch).matches("[a-zA-Z]") ||
                            Character.toString(ch).matches("[0-9]"))
                        state = 2;
                    else state = -1;
                    break;
            }
        }
        return (state == 2);
    }

        public static void main(String[] args) {
            System.out.println(NotThreeZeros.scan(args[0]) ? "Ok" : "Nope");
        }
}
