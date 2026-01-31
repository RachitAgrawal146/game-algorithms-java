package Connect4;
import java.util.Scanner;

public class Connect4
{
    Board b = new Board ();

    public static void main (String [] args)
    {
        Scanner sc = new Scanner(System.in);
        Connect4 c = new Connect4 ();
        Player p1 = new Player ();
        Player p2 = new Player ();
        boolean chance = true;
        while (c.check()==0)
        {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println();
            c.b.print();
            if (chance == true)
            {
                System.out.println("Player 1 : enter the Column in which you want to place: ");
                int column = sc.nextInt();
                c.move(column,1);
                System.out.println();
                chance = false;
            }
            else
            {
                System.out.println("Player 2 : enter the Column in which you want to place: ");
                int column = sc.nextInt();
                c.move(column,2);
                chance = true;
            }
        }
        if (c.check()==1)
            System.out.println("Congratulations Player 1 !!");
        else if (c.check()==2)
            System.out.println("Congratulations Player 2 !!");
    }

    void initialize() 
    {
        b.board = new int[][] {
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 2, 0, 0, 0},
            {0, 0, 0, 2, 0, 0, 0},
            {1, 1, 0, 2, 0, 0, 0},
            {1, 2, 1, 2, 1, 0, 0}};
    }

    int check()
    {
        for (int i = 0 ; i < 6 ; i ++)
        {
            if (checkRow(i) != 0)
                return checkRow(i);
        }
        for (int i = 0 ; i < 7 ; i++)
        {
            if (checkColumn(i) != 0)
                return checkColumn(i);
        }
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                if (j <= 3)
                {
                    int res = checkLeftDiagonal(i,j);
                    if (res != 0) 
                        return res;
                }
                if (j >= 3)
                {
                    int res = checkRightDiagonal(i,j);
                    if (res != 0) 
                        return res;
                }
            }
        }
        return 0;
    }

    int checkRow (int row)
    {
        int count1 = 0 ;
        int count2 = 0 ;
        for (int j = 0 ; j < 7 ; j ++)
        {
            if (count1==4)
                return(1);
            if (count2==4)
                return(2);
            if (b.board[row][j] == 1)
            {
                count1 +=1;
                count2 = 0;
            }
            else if (b.board[row][j] == 2)
            {
                count2 +=1;
                count1 = 0;
            }
            else if (b.board[row][j] == 0)
            {
                count1=0;
                count2=0;
            }
        }
        if (count1==4)
            return(1);
        if (count2==4)
            return(2);
        return 0;
    }

    int checkColumn (int column)
    {
        int count1 = 0;
        int count2 = 0;
        for (int j = 0; j < 6; j++)
        {
            if (b.board[j][column] == 1)
            {
                count1++;
                count2 = 0;
            }
            else if (b.board[j][column] == 2)
            {
                count2++;
                count1 = 0;
            }
            else
            {
                count1 = 0;
                count2 = 0;
            }
            if (count1 == 4) 
                return 1;
            if (count2 == 4) 
                return 2;
        }
        return 0;
    }

    int checkLeftDiagonal (int row, int column)
    {
        int count1 = 0;
        int count2 = 0;

        int r = row;
        int c = column;
        while (r < 6 && c < 6)
        {
            if (b.board[r][c] == 1)
            {
                count1++;
                count2 = 0;
            }
            else if (b.board[r][c] == 2)
            {
                count2++;
                count1 = 0;
            }
            else
            {
                count1 = 0;
                count2 = 0;
            }
            if (count1 == 4) 
                return 1;
            if (count2 == 4) 
                return 2;

            r++;
            c++;
        }
        return 0;
    }

    int checkRightDiagonal (int row, int column)
    {
        int count1 = 0;
        int count2 = 0;

        int r = row;
        int c = column;
        while (r < 6 && c >= 0)
        {
            if (b.board[r][c] == 1)
            {
                count1++;
                count2 = 0;
            }
            else if (b.board[r][c] == 2)
            {
                count2++;
                count1 = 0;
            }
            else
            {
                count1 = 0;
                count2 = 0;
            }

            if (count1 == 4) 
                return 1;
            if (count2 == 4) 
                return 2;
            r++;
            c--;
        }
        return 0;
    }

    void move(int column, int tag)
    {
        int col = column - 1;
        int targetRow = -1;
        for (int i = 5; i >= 0; i--)
        {
            if (b.board[i][col] == 0)
            {
                targetRow = i;
                break;
            }
        }

        if (targetRow == -1)
            return;

        final long DELAY = 150;
        long lastTime = System.currentTimeMillis();

        String coin = (tag == 1) ? "\u26AA" : "\u26AB";
        System.out.println();
        // -------- ABOVE BOARD FRAME --------
        while (true)
        {
            long now = System.currentTimeMillis();
            if (now - lastTime >= DELAY)
            {
                System.out.println();
                System.out.print("\033[H\033[2J");
                System.out.flush();
                for (int c = 0; c < 7; c++)
                {
                    if (c == col)
                        System.out.print(" " + coin);
                    else
                        System.out.print("   ");
                }
                System.out.println();

                b.print();
                lastTime = now;
                break;
            }
        }
        long start = System.currentTimeMillis();
        long delay = 1000;   // change this value freely

        while (System.currentTimeMillis() - start < delay)
        {
            // do nothing — just wait
        }

        // -------- FALL INSIDE BOARD --------
        for (int r = 0; r <= targetRow; r++)
        {
            long now;
            do {
                now = System.currentTimeMillis();
            } while (now - lastTime < DELAY);

            if (r > 0)
                b.board[r - 1][col] = 0;

            b.board[r][col] = tag;

            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println();
            b.print();

            lastTime = now;
        }
    }

}
