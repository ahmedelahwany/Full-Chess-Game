package Game.state;

import Game.state.Board.cell;

public class Move {
    private cell fromCell;
    private cell ToCell;



    public Move(cell fromCell, cell toCell) {
        this.fromCell = fromCell;
        ToCell = toCell;
    }

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
