package Mastermind;
import java.util.*;

public class Guess
{
    Scanner sc = new Scanner (System.in);
    int black = 0, white = 0;

    void checkGuess(String guess, String code)//checks the guess against the code and sets the values of black and white
    {
        for (int i = 0 ; i < 4 ; i ++)
        {
            char element = guess.charAt(i);
            for (int j = 0 ; j < 4 ; j++)
            {
                if ((element == code.charAt(j))&&(j==i))
                    black+=1;
                else if (element == code.charAt(j))
                    white+=1;
            }
        }
    }
    

}
