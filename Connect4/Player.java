/**
 * Player.java
 *
 * Represents a player in Connect4.
 * Can be either a human player (reads input from console)
 * or an AI player (delegates move selection to AI.java).
 */
public class Player
{
    String name;
    int piece;          // 1 = player one, 2 = player two
    boolean isAI;

    /** Creates a human player. */
    public Player (String name, int piece)
    {
        this.name  = name;
        this.piece = piece;
        this.isAI  = false;
    }

    /** Creates a player and specifies whether it is AI-controlled. */
    public Player (String name, int piece, boolean isAI)
    {
        this.name  = name;
        this.piece = piece;
        this.isAI  = isAI;
    }
}
