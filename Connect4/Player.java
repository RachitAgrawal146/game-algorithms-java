/**
 * Represents a Connect 4 player.
 * Can be either a human player or an AI opponent.
 */
public class Player
{
    private String name;
    private int piece;
    private boolean isAI;

    /**
     * Creates a new player.
     * @param name  the display name of the player
     * @param piece the piece number (1 or 2)
     * @param isAI  true if this player is controlled by the AI
     */
    public Player (String name, int piece, boolean isAI)
    {
        this.name = name;
        this.piece = piece;
        this.isAI = isAI;
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
