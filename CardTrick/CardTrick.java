import java.util.*;
public class CardTrick
{
    Deck deck;//stores a deck
    Card [] cards21 = new Card [21]; //single dimension array to store cards
    Card [] [] columns = new Card [7][3];//double dimension array for representation
    Scanner sc = new Scanner (System.in);
    
    void demo ()//runs the card trick till the user wants
    {
        int play = 1;
        while (play != 0)
        {
            cardTrick();
            System.out.println();
            System.out.println("Enter 1 to play, 0 to stop : ");
            play = sc.nextInt();
        }
    }
    public CardTrick()//initializes a deck of 21 cards
    {
        deck = new Deck ();
        deck.shuffle();
        for (int a = 0 ; a < 21 ; a += 1)
        {
            cards21[a] = deck.deck[a];
        }
    }

    void arrange ()//puts cards from cards21 to colunms
    {
        int x = 0;
        for (int r = 0 ; r < 7 ; r += 1)
        {
            for (int c= 0 ; c < 3 ; c += 1)
            {
                columns[r][c] = cards21 [x];
                x++;
            }
        }
    }

    void print ()//prints columns
    {
        System.out.println("C1\tC2\tC3");
        for (int r = 0 ; r < 7 ; r += 1)
        {
            for (int c= 0 ; c < 3 ; c += 1)
            {
                columns[r][c].display();
                System.out.print("\t");
            }
            System.out.println();
        }
    }

    void cardTrick()//runs the algorithm to guess the correct card
    {
        Card temp = new Card() ;
        for (int times = 0 ; times < 3 ; times += 1)
        {
            arrange();
            print();
            int r = interact();
            for (int x = 0 ; x < 7 ; x += 1)
            {
                temp = columns [x][1];
                columns [x][1] = columns[x][r-1];
                columns[x][r-1] = temp;  
            }
            rearrange();
        }
        cards21[10].display();
    }

    int interact ()//asks the user for colunm no. of their card
    {
        System.out.println("Which Column is your Card in ??");
        int response = sc.nextInt(); 
        return response;
    }

    void rearrange ()//puts cards from columns to cards21
    {
        int x = 0;
        for (int c= 0 ; c < 3 ; c += 1)
        {
            for (int r = 0 ; r < 7 ; r += 1)
            {
                cards21 [x] = columns[r][c];
                x++;
            }
        }
    }
}
