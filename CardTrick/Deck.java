package CardTrick;

public class Deck
{
    Card [] deck = new Card [52];
    char [] suit = {'S','D','C','H'};
    public Deck()//initialises the deck of 52 cards
    {
        for (int s = 0 ; s < 4 ; s += 1)
        {
            for (int r = 1 ; r <= 13 ; r += 1)
            {
                deck[(s*13)+r-1] = new Card (r,suit[s]);
            }   
        }
    }

    void print ()//prints all 52 cards 
    {
        for (int r = 1 ; r <= 13 ; r += 1)
        {
            for (int s = 0 ; s < 4 ; s += 1)
            {
                deck[s*13+r-1].display();
                System.out.print ("\t");
            }
            System.out.println("");
        }
    }

    int getRandom(int x, int y)//returns a random integer btween two limits
    {
        return ((int)(Math.random()*10000)%(y-x))+x ;
    }

    void shuffle()//shuffles 52 cards ramdomly
    {
        Card [] newdeck = new Card [52];
        for (int a = 0 ; a < 52 ; )
        {
            int random = getRandom(0,52);
            if (newdeck[random] == null)
            {   newdeck[random] = deck[a];
                a += 1;
            }
        }
        deck = newdeck;
    }
}
