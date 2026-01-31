package Mastermind;

public class CodeBreaker
{
    int [] set = new int [360];
    void initializeSet()
    {
        int index = 0;
        for (int a = 1 ; a < 7 ; a++)
        {
            for (int b = 1 ; b < 7 ; b++)
            {
                if (b==a)
                    continue;
                for (int c = 1 ; c < 7 ; c++)
                {
                    if ((c==b)||(c==a))
                        continue;
                    for (int d = 1 ; d < 7 ; d++)
                    {
                        if ((d==a)||(d==b)||(d==c))
                            continue;
                        set[index] = a*1000 + b*100 + c*10 + d;
                        index++;
                    }
                }
            }
        }

        for (int n = 0; n < 360 ; n ++)
            System.out.println(set[n]);
    }

    void remove(int black, int white,int attempt )
    {
        int possibilities = 0;
        for (int i = 0 ; i < 360 ; i++)
        {
            if (set[i]==0)
                continue;
            Guess guess  = new Guess ();
            guess.checkGuess(String.valueOf(attempt), String.valueOf(set[i]));
            if ((guess.black!=black)||(guess.white!=white))
                set[i] = 0;
        }
        for (int n = 0 ; n < 360 ; n++)
        {
            if (set[n]!=0)
            {
                System.out.println(set[n]);
                possibilities +=1;
            }
        }
        System.out.println("Possibilities = " + possibilities);
    }
}
