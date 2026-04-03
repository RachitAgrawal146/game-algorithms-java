public class Card
{
    int rank;
    char suit;
    public Card ()//default constructor
    {
        rank = 1;
        suit = 'D';
    }
    public Card (int rank, char suit)//parameterised constructor
    {
        this.rank = rank;
        if (suit == 'D' || suit == 'C' || suit == 'H' || suit == 'S')
            this.suit = suit;
        else
            this.suit = 'S';
    }
    void display ()//prints the rank and suit of a card
    {
        if (suit == 'C')
            System.out.print (rank + "\u2663" );
        if (suit == 'D')
            System.out.print (rank + "\u2666" );
        if (suit == 'H')
            System.out.print (rank + "\u2665" );
        if (suit == 'S')
            System.out.print (rank + "\u2660" );
    }
}
