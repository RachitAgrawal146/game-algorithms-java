/**
 * Player.java
 *
 * Represents a player in Connect4.
 * Can be either a human player (reads input from console)
 * or an AI player (delegates move selection to AI.java).
 */
public class Player
{
    private String name;
    private int piece;          // 1 = player one, 2 = player two
    private boolean isAI;

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

    /** Returns the player's display name. */
    public String getName ()
    {
        return name;
    }

    /** Returns the player's piece number (1 or 2). */
    public int getPiece ()
    {
        return piece;
    }

    /** Returns true if this player is an AI. */
    public boolean isAI ()
    {
        return isAI;
    }
}
