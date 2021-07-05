package Game.state.pieces;

import Game.state.Board.Board;
import Game.state.Board.cell;
import Game.state.Move;

import java.util.ArrayList;

public class Queen extends Piece {

    public Queen(Color color) {
        super(color);
        this.setType(Type.QUEEN);
    }

    @Override
    public boolean validateMove(Move move, Board board) {
        Board.lastMoveType = Move.moveType.REGULAR;
        board.setEnPassant(null);
        return getPossibleMoves(move,board).contains(move.getToCell());

    }

    @Override
    public ArrayList<cell> getPossibleMoves(Move move, Board board) {
        Color currentPlayerColor;
        int fromCellFile = move.getFromCell().getFile();
        int fromCellRank = move.getFromCell().getRank();
        int iterator = 1;
        int iteratorX = 1;
        int iteratorY = 1;
        this.possibleMoves.clear();
        this.RegularMoves.clear();
        currentPlayerColor = move.getFromCell().getPiece().getColor();

        while(iterator+fromCellFile < Board.DIMENSION ){

            cell possibleCell = board.getCells()[fromCellRank][fromCellFile+iterator];
            if ( possibleCell.getPiece() != null) {
                this.RegularMoves.add(possibleCell);
                if (possibleCell.getPiece().getColor() != currentPlayerColor) {
                    this.possibleMoves.add(possibleCell);
                }
                break;
            }
            this.RegularMoves.add(possibleCell);
            this.possibleMoves.add(possibleCell);
            iterator++;
        }
        iterator = 1;

        while(fromCellFile - iterator >= 0){

            cell possibleCell = board.getCells()[fromCellRank][fromCellFile-iterator];
            if ( possibleCell.getPiece() != null) {
                this.RegularMoves.add(possibleCell);
                if (possibleCell.getPiece().getColor() != currentPlayerColor) {
                    this.possibleMoves.add(possibleCell);
                }
                break;
            }
            this.RegularMoves.add(possibleCell);
            this.possibleMoves.add(possibleCell);
            iterator++;
        }

        iterator = 1;

        while(fromCellRank + iterator < Board.DIMENSION){

            cell possibleCell = board.getCells()[fromCellRank + iterator][fromCellFile];
            if ( possibleCell.getPiece() != null) {
                this.RegularMoves.add(possibleCell);
                if (possibleCell.getPiece().getColor() != currentPlayerColor) {
                    this.possibleMoves.add(possibleCell);
                }
                break;
            }
            this.RegularMoves.add(possibleCell);
            this.possibleMoves.add(possibleCell);
            iterator++;
        }
        iterator = 1;

        while(fromCellRank - iterator >= 0){
            cell possibleCell = board.getCells()[fromCellRank - iterator][fromCellFile];
            if ( possibleCell.getPiece() != null) {
                this.RegularMoves.add(possibleCell);
                if (possibleCell.getPiece().getColor() != currentPlayerColor) {
                    this.possibleMoves.add(possibleCell);
                }
                break;
            }
            this.RegularMoves.add(possibleCell);
            this.possibleMoves.add(possibleCell);
            iterator++;
        }

        while(iteratorX + fromCellRank<Board.DIMENSION && iteratorY + fromCellFile < Board.DIMENSION){

            cell possibleCell = board.getCells()[fromCellRank + iteratorX][fromCellFile + iteratorY];
            if ( possibleCell.getPiece() != null) {
                this.RegularMoves.add(possibleCell);
                if (possibleCell.getPiece().getColor() != currentPlayerColor) {
                    this.possibleMoves.add(possibleCell);
                }
                break;
            }
            this.RegularMoves.add(possibleCell);
            this.possibleMoves.add(possibleCell);
            iteratorX++;
            iteratorY++;
        }
        iteratorX = 1;
        iteratorY = 1;

        while( fromCellRank - iteratorX >= 0 &&  fromCellFile - iteratorY >= 0){

            cell possibleCell =  board.getCells()[fromCellRank - iteratorX][fromCellFile - iteratorY];
            if ( possibleCell.getPiece() != null) {
                this.RegularMoves.add(possibleCell);
                if (possibleCell.getPiece().getColor() != currentPlayerColor) {
                    this.possibleMoves.add(possibleCell);
                }
                break;
            }
            this.RegularMoves.add(possibleCell);
            this.possibleMoves.add(possibleCell);
            iteratorX++;
            iteratorY++;
        }
        iteratorX = 1;
        iteratorY = 1;

        while( fromCellRank - iteratorX >= 0 &&  fromCellFile + iteratorY < Board.DIMENSION){

            cell possibleCell = board.getCells()[fromCellRank - iteratorX][fromCellFile + iteratorY];
            if ( possibleCell.getPiece() != null) {
                this.RegularMoves.add(possibleCell);
                if (possibleCell.getPiece().getColor() != currentPlayerColor) {
                    this.possibleMoves.add(possibleCell);
                }
                break;
            }
            this.RegularMoves.add(possibleCell);
            this.possibleMoves.add(possibleCell);
            iteratorX++;
            iteratorY++;
        }

        iteratorX = 1;
        iteratorY = 1;

        while( fromCellRank + iteratorX <Board.DIMENSION &&  fromCellFile - iteratorY >= 0){

            cell possibleCell = board.getCells()[fromCellRank + iteratorX][fromCellFile - iteratorY];
            if ( possibleCell.getPiece() != null) {
                this.RegularMoves.add(possibleCell);
                if (possibleCell.getPiece().getColor() != currentPlayerColor) {
                    this.possibleMoves.add(possibleCell);
                }
                break;
            }
            this.RegularMoves.add(possibleCell);
            this.possibleMoves.add(possibleCell);
            iteratorX++;
            iteratorY++;
        }
        return this.possibleMoves;
    }

    @Override
    public void checkPinnedPieces(cell position, Board board) {
        cell opponentKing = this.getColor() == Color.WHITE ? board.getbKingPosition() :board.getwKingPosition();
        int OpponentKingRank = opponentKing.getRank();
        int OpponentKingFile = opponentKing.getFile();

        if ((Math.abs(OpponentKingRank - position.getRank()) == 1 && Math.abs(OpponentKingFile - position.getFile()) == 1) ||
            (OpponentKingRank == position.getRank() || OpponentKingFile == position.getFile())) {
             setPinnedPieces(position,board);
        }
    }

    @Override
    public int getCode() {
        return this.getColor() == Color.WHITE ? 4 : 10;
    }
}
