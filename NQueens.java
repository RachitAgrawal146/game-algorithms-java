
public class NQueens
{
    int ri=0;
    
    int [] [] nQueens (int [] [] board, int r, int c, int n)
    {
        if (n==0)
        {
            printBoard(board);
            return board;   
        }     
        
        if (isSafe(board,r,c)==true)
            board = nQueens(placeQueen(board,r,c),0,c+1,n-1);  
        else if ((r==board.length-1)&&(c==board.length-1)&&(isSafe(board,r,c)==false))
            board = nQueens(startBoard(board.length),ri+1,0,board.length);
        else if ((r==board.length-1)&&(isSafe(board,r,c)==false))
            board = nQueens(shiftDown(board,r,c),0,c,n);
        else if (isSafe(board,r,c)==false)  
            board = nQueens(board,r+1,c,n);
        return board;
    }

    void printBoard (int [] [] board)
    {
        for (int i = 0 ; i < board.length ; i += 1)
        {
            for (int j = 0 ; j < board.length ; j += 1)
            {
                if ((board[i][j]== 0)&&((i+j)%2==0))
                    System.out.print("\u25a1" + " ");
                if ((board[i][j]== 0)&&((i+j)%2==1))
                    System.out.print("\u25a0" + " ");
                if (board[i][j]==1)
                    System.out.print("\u265b" + " ");
            }
            System.out.println();
        }
    }
    
        int [] [] startBoard (int n)
        {
        int [] [] board = new int [n] [n];
        return board;
    }

    int [] place1d (int [] arr, int c)
    {
        int [] ans = new int [arr.length];
        for (int i = 0; i < arr.length ; i += 1)
        {
            if (i == c)
                ans[i] = 1;
            else
                ans[i] = arr[i];
        }
        return ans;
    }

    int [] [] placeQueen (int [] [] board, int r, int c)
    {
        int [] [] ansBoard = new int [board.length] [board.length];
        for (int i = 0 ; i < board.length; i+=1)
        {
            if (i==r)
                ansBoard[i] = place1d (board[r],c);
            else
                ansBoard[i] = board[i];
        }
        return ansBoard;
    }

    int countRow (int [] [] board, int row)
    {
        int sum = 0;
        for (int i = 0 ; i < board.length ; i++)
        {
            if (board [row][i]==1)
                sum += 1;
        }
        return sum;
    }

    int countColumn (int [] [] board, int column)
    {
        int sum = 0;
        for (int i = 0 ; i < board.length ; i++)
        {
            if (board [i][column]==1)
                sum += 1;
        }
        return sum;
    }

    boolean checkDiagonals (int [] [] board, int r, int c)
    {
        int leftDiag = 0;
        int rightDiag = 0;
        int j=c;
        for (int i = r ; ((i < board.length)&&(j<board.length)) ; i ++)
        {
            if (board[i][j]==1)
                leftDiag++;
            j++;
        }
        j=c;
        for (int i = r ; ((i >= 0)&&(j>=0)) ; i-- )
        {
            if (board[i][j]==1)
                leftDiag++;
            j--;
        }
        j=c;
        for (int i = r ; (i>=0)&&(j<board.length) ; i--)
        {
            if (board[i][j]==1)
                rightDiag++ ;
            j++;
        }
        j=c;
        for (int i = r ; (j>=0)&&(i<board.length) ; i++)
        {
            if (board[i][j]==1)
                rightDiag++ ;
            j--;
        }
        if ((leftDiag==0)&&(rightDiag==0))
            return true;
        else
            return false;
    }

    boolean isSafe(int [] [] board, int r, int c)
    {
        if (((checkDiagonals(board,r,c)==true)&&(countColumn(board,c)==0)&&(countRow(board,r)==0)))
            return true;
        else    
            return false;
    }

    int [] [] shiftDown(int [] [] board, int r, int c)
    {
        int lQueen = 0;
        for (int i = 0 ; i < board.length ; i ++)
        {
            if (board [i][c-1]==1)
                lQueen = i;
        }
        board[lQueen][c-1] = 0;
        board[lQueen+1][c-1] = 1;
        return board;
    }
}

