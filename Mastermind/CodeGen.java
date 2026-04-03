/**
 * Generates random Mastermind secret codes using digits 1-6.
 */
public class CodeGen
{
    /**
     * Creates a random 4-digit code where repeats are allowed.
     * @return a 4-character string of digits 1-6
     */
    String codeGen ()
    {
        String code = "";
        for (int i = 0 ; i < 4 ; i += 1)
        {
            int number = (int)(Math.random() * 6) + 1;
            code = code + number;
        }
        return code;
    }

    /**
     * Creates a random 4-digit code without repeating any digit.
     * @return a 4-character string of distinct digits from 1-6
     */
    String codeGenNoRep ()
    {
        String code = "";
        while (code.length() < 4)
        {
            int number = (int)(Math.random() * 6) + 1;
            if (isAbsent(code, (char)(number + 48)))
                code = code + number;
        }
        return code;
    }

    /**
     * Checks if a given character is absent in a given string.
     * @param s the string to search
     * @param c the character to look for
     * @return true if the character is not found
     */
    boolean isAbsent (String s, char c)
    {
        for (int i = 0 ; i < s.length() ; i += 1)
        {
            if (s.charAt(i) == c)
                return false;
        }
        return true;
    }
}
