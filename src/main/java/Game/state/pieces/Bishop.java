package Game.state.pieces;

import Game.state.Board.Board;
import Game.state.Board.cell;
import Game.state.Move;

import java.util.ArrayList;

public class Bishop extends Piece {

    public Bishop(Color color) {
        super(color);
        this.setType(Type.BISHOP);
    }

    @Override
    public boolean validateMove(Move move, Board board) {
        return getPossibleMoves(move,board).contains(move.getToCell());

    }

    @Override
    public ArrayList<cell> getPossibleMoves(Move move, Board board) {
        Color currentPlayerColor;
        int fromCellFile = move.getFromCell().getFile();
        int fromCellRank = move.getFromCell().getRank();
        int iteratorX = 1;
        int iteratorY = 1;

        currentPlayerColor = move.getFromCell().getPiece().getColor();

        this.possibleMoves.clear();
        while(iteratorX + fromCellRank < Board.DIMENSION && iteratorY + fromCellFile < Board.DIMENSION){

              cell possibleCell = board.getCells()[fromCellRank + iteratorX][fromCellFile + iteratorY];
            if ( possibleCell.getPiece() != null) {
                if (possibleCell.getPiece().getColor() != currentPlayerColor) {
                    this.possibleMoves.add(possibleCell);
                }
                break;
            }
            this.possibleMoves.add(possibleCell);
            iteratorX++;
            iteratorY++;
        }
        iteratorX = 1;
        iteratorY = 1;

        while( fromCellRank - iteratorX >= 0 &&  fromCellFile - iteratorY >= 0){

            cell possibleCell =  board.getCells()[fromCellRank - iteratorX][fromCellFile - iteratorY];
            if ( possibleCell.getPiece() != null) {
                if (possibleCell.getPiece().getColor() != currentPlayerColor) {
                    this.possibleMoves.add(possibleCell);
                }
                break;
            }
            this.possibleMoves.add(possibleCell);
            iteratorX++;
            iteratorY++;
        }
         iteratorX = 1;
         iteratorY = 1;

        while( fromCellRank - iteratorX >= 0 &&  fromCellFile + iteratorY < Board.DIMENSION){

            cell possibleCell = board.getCells()[fromCellRank - iteratorX][fromCellFile + iteratorY];
            if ( possibleCell.getPiece() != null) {
                if (possibleCell.getPiece().getColor() != currentPlayerColor) {
                    this.possibleMoves.add(possibleCell);
                }
                break;
            }
            this.possibleMoves.add(possibleCell);
            iteratorX++;
            iteratorY++;
        }

        iteratorX = 1;
        iteratorY = 1;

        while( fromCellRank + iteratorX <Board.DIMENSION &&  fromCellFile - iteratorY >= 0){

            cell possibleCell = board.getCells()[fromCellRank + iteratorX][fromCellFile - iteratorY];
            if ( possibleCell.getPiece() != null) {
                if (possibleCell.getPiece().getColor() != currentPlayerColor) {
                    this.possibleMoves.add(possibleCell);
                }
                break;
            }
            this.possibleMoves.add(possibleCell);
            iteratorX++;
            iteratorY++;
        }


        return this.possibleMoves;
    }


    @Override
    public int getCode() {
        return this.getColor() == Color.WHITE ? 2 : 8;
    }

}
