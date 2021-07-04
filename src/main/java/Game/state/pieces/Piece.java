package Game.state.pieces;

import Game.state.Board.Board;
import Game.state.Move;
import Game.state.Board.cell;

import java.util.ArrayList;

public abstract class Piece {

    protected ArrayList<cell> possibleMoves = new ArrayList<>();
    protected ArrayList<cell> RegularMoves = new ArrayList<>();

    public enum Color {
        WHITE, BLACK
    }

    public enum Type {
        KING, ROOK, BISHOP, QUEEN, KNIGHT, PAWN
    }
    private boolean firstMove = true;

    public boolean isFirstMove() {
        return firstMove;
    }

    public void setFirstMove(boolean firstMove) {
        this.firstMove = firstMove;
    }

    private Color color;
    private Type type;

    public Piece(Color color) {
        this.color = color;

    }

    public abstract boolean validateMove(Move move , Board board);

    public abstract ArrayList<cell> getPossibleMoves(Move move , Board board);

    public abstract int getCode();

    public Color getColor() {
        return color;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ArrayList<cell> getRegularMoves() {
        return this.RegularMoves;
    }

    protected boolean isWithinTheRange (int Rank, int file){
        return 0 <= Rank && Rank <= 7 && 0 <= file && file <= 7;
    }

}
