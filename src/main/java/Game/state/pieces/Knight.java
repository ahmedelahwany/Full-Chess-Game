package Game.state.pieces;

import Game.state.Board.Board;
import Game.state.Board.cell;
import Game.state.Move;

import java.util.ArrayList;

public class Knight extends Piece {

    public Knight(Color color) {
        super(color);
        this.setType(Type.KNIGHT);
    }

    @Override
    public boolean validateMove(Move move, Board board) {
        Board.lastMoveType = Move.moveType.REGULAR;
        board.setEnPassant(null);
        return getPossibleMoves(move,board).contains(move.getToCell());
    }

    @Override
    public ArrayList<cell> getPossibleMoves(Move move, Board board) {

       Color currentPlayerColor = move.getFromCell().getPiece().getColor();
        this.possibleMoves.clear();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int x = Math.abs(move.getFromCell().getRank() - i);
                int y = Math.abs(move.getFromCell().getFile() - j);
                Color opponentPlayerColor = board.getCells()[i][j].getPiece() == null ? null : board.getCells()[i][j].getPiece().getColor();
                if (x * y == 2 && (opponentPlayerColor != currentPlayerColor))
                    {
                        this.possibleMoves.add(board.getCells()[i][j]);
                    }
            }
        }
        System.out.println(this.possibleMoves.size());
        return this.possibleMoves;
    }

    @Override
    public int getCode() {
        return this.getColor() == Color.WHITE ? 1 : 7;
    }

}
