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
    public void validateMove(Move move, Board board) {
        board.lastMoveType = Move.moveType.REGULAR;
        board.setEnPassant(null);
    }

    @Override
    public ArrayList<cell> getPossibleMoves(Move move, Board board , boolean possible) {

       Color currentPlayerColor = move.getFromCell().getPiece().getColor();
        if(possible)this.possibleMoves.clear();
        this.RegularMoves.clear();
     if(this.getPinningPiece() == null){

         for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int x = Math.abs(move.getFromCell().getRank() - i);
                int y = Math.abs(move.getFromCell().getFile() - j);
                Color opponentPlayerColor = board.getCells()[i][j].getPiece() == null ? null : board.getCells()[i][j].getPiece().getColor();
                if(x * y == 2) this.RegularMoves.add(board.getCells()[i][j]);
                if (x * y == 2 && (opponentPlayerColor != currentPlayerColor) )
                    {
                        cell possibleCell = board.getCells()[i][j];
                        this.AddPossibleMove(board,move,possibleCell,possible);
                    }
            }
        }
     }
        return this.possibleMoves;
    }

    @Override
    public Piece clone() {
        Knight Knight = new Knight(this.getColor());
        Knight.setFirstMove(this.isFirstMove());
        Knight.setPinningPiece(this.getPinningPiece().clonei());
        return Knight;
    }
    @Override
    public int getCode() {
        return this.getColor() == Color.WHITE ? 1 : 7;
    }

    @Override
    public int getValue() {
        return 300;
    }

}
