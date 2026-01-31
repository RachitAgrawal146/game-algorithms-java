package Mastermind;
import java.util.*;
public class Board
{
    // Reset
    public static final String ANSI_RESET = "\u001B[0m";

    // Regular Colors
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_GREY = "\u001B[90m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    void printBoard(int black , int white, String guess)//prints the board
    {
        System.out.println("-----------------------------------");
        System.out.print("|");
        for (int i = 0 ; i < 4-black-white ; i++)
        {
            System.out.print(" " + ANSI_BLACK + "\u25CF" + ANSI_RESET + " ");
        }
        for (int b = 0 ; b < black ; b ++)
            System.out.print(" " + ANSI_GREY + "\u25CF" + ANSI_RESET + " ");
        for (int w = 0 ; w < white ; w ++)
            System.out.print( " " + ANSI_WHITE + "\u25CF" + ANSI_RESET + " ");  
        System.out.print("|"); 
        for (int j = 0 ; j < guess.length() ; j++)
        {
            char element = guess.charAt(j);
            switch(element)
            {
                case '1':
                    System.out.print( "  "+ANSI_RED + "1" + ANSI_RESET + "  ");
                    break;
                case '2':
                    System.out.print( "  " + ANSI_GREEN + "2" + ANSI_RESET+ "  ");
                    break;
                case '3':
                    System.out.print( "  " + ANSI_YELLOW + "3" + ANSI_RESET+ "  ");
                    break;
                case '4':
                    System.out.print( "  " + ANSI_BLUE + "4" + ANSI_RESET+ "  ");
                    break;
                case '5':
                    System.out.print( "  " + ANSI_PURPLE + "5" + ANSI_RESET+ "  ");
                    break;
                case '6':
                    System.out.print( "  " + ANSI_CYAN + "6" + ANSI_RESET+ "  ");
                    break;
            }
        }
        System.out.println("|");
        System.out.println("-----------------------------------");
    }
}
