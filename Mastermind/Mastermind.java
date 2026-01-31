package Mastermind;
import java.util.Scanner;
public class Play
{
    int compScore = 0;
    int personScore = 0;
    Scanner sc = new Scanner (System.in);
    String guess = "";

    public static void main (String [] args)//main method to play mastermind
    {
        String code; 
        Scanner sc = new Scanner (System.in);
        Play p = new Play ();
        Guess g = new Guess();
        Board b = new Board();
        CodeGen c = new CodeGen();
        code = c.codeGenNoRep();
        System.out.println(code);
        System.out.println("Enter your guess in numbers where :");
        System.out.println("1 => RED :");
        System.out.println("2 => GREEN :");
        System.out.println("3 => YELLOW :");
        System.out.println("4 => BLUE :");
        System.out.println("5 => PURPLE :");
        System.out.println("6 => CYAN :");
        for (int n = 0 ; n < 10 ; n ++)
        {
            String guess = sc.next();
            g.black = 0;
            g.white = 0;
            g.checkGuess(guess, code);
            if (g.black==4)
            {
                b.printBoard(g.black,g.white, guess);
                System.out.println("Congratulations, You have cracked the code!!");
                break;
            }
            b.printBoard(g.black,g.white, guess);
        }
        if (g.black!=4)
        {
            System.out.println("Oops, Better luck next time :( ");
            System.out.println("The correct code was " + code);
        }
    }

    // Enter your guess in numbers where :
    // 1 => RED :
    // 2 => GREEN :
    // 3 => YELLOW :
    // 4 => BLUE :
    // 5 => PURPLE :
    // 6 => CYAN :
    // 1234
    // -----------------------------------
    // | ●  ●  ●  ● |  1    2    3    4  |
    // -----------------------------------
    // 3456
    // -----------------------------------
    // | ●  ●  ●  ● |  3    4    5    6  |
    // -----------------------------------
    // 2345
    // -----------------------------------
    // | ●  ●  ●  ● |  2    3    4    5  |
    // -----------------------------------
    // 3541
    // -----------------------------------
    // | ●  ●  ●  ● |  3    5    4    1  |
    // -----------------------------------
    // 1465
    // -----------------------------------
    // | ●  ●  ●  ● |  1    4    6    5  |
    // -----------------------------------
    // 5462
    // -----------------------------------
    // | ●  ●  ●  ● |  5    4    6    2  |
    // -----------------------------------

    
    // cd ~/class_11                   
    // javac Mastermind/*.java         
    // java Mastermind.Play
}
