package Game.state;

import Game.state.Board.Board;
import Game.state.Board.cell;

import java.util.Objects;

public class Move implements Cloneable{
    private cell fromCell;
    private cell ToCell;



    public Move(cell fromCell, cell toCell) {
        this.fromCell = fromCell;
        ToCell = toCell;
    }


    public int getMoveEffect() {
        if(this.getToCell().getPiece() != null ){
            return getToCell().getPiece().getValue() - this.getFromCell().getPiece().getValue() - 1000000;
        }
        return 10000 - this.getFromCell().getPiece().getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Move)) return false;
        Move move = (Move) o;
        return getFromCell().equals(move.getFromCell()) &&
               getToCell().equals(move.getToCell());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFromCell(), getToCell());
    }

    public boolean isThreateningKing(Board board) {
        Board newBoard = board.customClone(false);
        newBoard.executeMove(this,true);
      return newBoard.checkIfKingsAreunderCheck(true);
     }


    public boolean isCastlingMove(Board board) {
        Board newBoard = board.customClone(false);
        newBoard.executeMove(this,true);
        return board.lastMoveType == moveType.CASTLE_KING_SIDE || board.lastMoveType == moveType.CASTLE_QUEEN_SIDE;
    }
    public boolean isAttackingMove() {
        return this.getToCell().getPiece()!= null;
    }

    // these type are useful for updating the board after every move ( model and view)
    public enum moveType {
        PAWN_PROMOTION,
        CASTLE_KING_SIDE,
        CASTLE_QUEEN_SIDE,
        PAWN_EN_PASSANT,
        REGULAR,
        CHECKMATE,
        STALEMATE
    }

    public void setFromCell(cell fromCell) {
        this.fromCell = fromCell;
    }

    public void setToCell(cell toCell) {
        ToCell = toCell;
    }

    @Override
    public Object clone() {
        Move clone;
        try
        {
            clone = (Move) super.clone();
            clone.setFromCell(this.getFromCell().clonei());
            clone.setToCell(this.getToCell().clonei());
        }
        catch (CloneNotSupportedException e)
        {
            throw new RuntimeException(e);
        }
        return clone;
    }

    public cell getFromCell() {
        return fromCell;
    }


    public cell getToCell() {
        return ToCell;
    }

}
