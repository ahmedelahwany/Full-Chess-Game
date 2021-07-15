package Game.state;

import Game.state.Board.Board;
import Game.state.Board.cell;

import java.util.Objects;

public class Move {
    private cell fromCell;
    private cell ToCell;



    public Move(cell fromCell, cell toCell) {
        this.fromCell = fromCell;
        ToCell = toCell;
    }

    public boolean isCastlingMove(Board board) {
        board.executeMove(this,true);
        return board.lastMoveType == moveType.CASTLE_KING_SIDE || board.lastMoveType == moveType.CASTLE_QUEEN_SIDE;
    }

    public int getMoveEffect(Board board) {
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
      board.executeMove(this,true);
      return board.checkIfKingsAreunderCheck(true);
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

    public cell getFromCell() {
        return fromCell;
    }


    public cell getToCell() {
        return ToCell;
    }

}
