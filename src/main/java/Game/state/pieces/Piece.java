package Game.state.pieces;

import Game.state.Board.Board;
import Game.state.Move;
import Game.state.Board.cell;

import java.util.ArrayList;

public abstract class Piece {

    protected ArrayList<cell> possibleMoves = new ArrayList<>();
    protected ArrayList<cell> RegularMoves = new ArrayList<>();
    private cell pinningPiece ;

    public enum Color {
        WHITE, BLACK
    }

    public cell getPinningPiece() {
        return this.pinningPiece;
    }

    public void setPinningPiece(cell pinningPiece) {
        this.pinningPiece = pinningPiece;
    }

    public enum Type {
        KING, ROOK, BISHOP, QUEEN, KNIGHT, PAWN
    }
    private boolean firstMove;




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
        this.pinningPiece = null;
        this.firstMove = true;
    }

    public abstract void validateMove(Move move , Board board);

    // to check if bishop,Rock or Queen pin a specific opponent's piece to the opponent's king
    // pinned pieces can't move at all
    public void checkPinnedPieces(cell position ,Board board){}


    protected  void setPinnedPiecesForBishop(cell position , Board board) {
        ArrayList<cell> cells = new ArrayList<>();
        cell opponentKing = this.getColor() == Color.WHITE ? board.getbKingPosition() : board.getwKingPosition();
        Color opponentColor = opponentKing.getPiece().getColor();
        int OpponentKingRank = opponentKing.getRank();
        int OpponentKingFile = opponentKing.getFile();

        int directionRank = OpponentKingRank > position.getRank() ? 1 : -1;
        int directionFile = OpponentKingFile > position.getFile() ? 1 : -1;

        for (int i = directionRank * position.getRank(), j = directionFile * position.getFile(); i < OpponentKingRank * directionRank && j < OpponentKingFile * directionFile; i++, j++) {
            if (board.getCells()[Math.abs(i)][Math.abs(j)].getPiece() != null && board.getCells()[Math.abs(i)][Math.abs(j)].getPiece().getColor() == opponentColor) {
                cells.add(board.getCells()[Math.abs(i)][Math.abs(j)]); // pinned pieces as a first element in the array
            }
        }
        if (cells.size() == 1) {
            cells.get(0).getPiece().setPinningPiece(position);
            // for a piece to be pinned to a king , it has to be the only piece in the way from the attacking piece to the king.
        }
    }
    protected  void setPinnedPiecesForRock(cell position , Board board) {
        ArrayList<cell> cells = new ArrayList<>();
        cell opponentKing = this.getColor() == Color.WHITE ? board.getbKingPosition() : board.getwKingPosition();
        Color opponentColor = opponentKing.getPiece().getColor();
        int OpponentKingRank = opponentKing.getRank();
        int OpponentKingFile = opponentKing.getFile();

        int directionRank = OpponentKingRank > position.getRank() ? 1 : -1;
        int directionFile = OpponentKingFile > position.getFile() ? 1 : -1;

        if(OpponentKingFile == position.getFile())
       {
           for (int i = directionRank * position.getRank(); i < OpponentKingRank * directionRank ; i++) {
               if (board.getCells()[Math.abs(i)][Math.abs(OpponentKingFile)].getPiece() != null && board.getCells()[Math.abs(i)][Math.abs(OpponentKingFile)].getPiece().getColor() == opponentColor) {
                cells.add(board.getCells()[Math.abs(i)][Math.abs(OpponentKingFile)]); // pinned pieces as a first element in the array
            }
        }
       }
        if(OpponentKingRank == position.getRank())
        {
            for (int j = directionFile * position.getFile(); j < OpponentKingFile * directionFile ; j++) {
                if (board.getCells()[Math.abs(OpponentKingRank)][Math.abs(j)].getPiece() != null && board.getCells()[Math.abs(OpponentKingRank)][Math.abs(j)].getPiece().getColor() == opponentColor) {
                    cells.add(board.getCells()[Math.abs(OpponentKingRank)][Math.abs(j)]); // pinned pieces as a first element in the array
                }
            }
        }
        if (cells.size() == 1) {
            cells.get(0).getPiece().setPinningPiece(position);
            // for a piece to be pinned to a king , it has to be the only piece in the way from the attacking piece to the king.
        }
    }

    protected boolean checkIfPieceDefendKing(Board board , Move possibleMove ,cell KingPosition){
        boolean oldFirstMove = possibleMove.getFromCell().getPiece().isFirstMove();
        cell currentEnpasantCell = board.getEnPassant();
        Move.moveType lastMoveType = board.lastMoveType;
        King whiteKing = (King)board.getwKingPosition().getPiece();
        King blackKing = (King)board.getbKingPosition().getPiece();
        boolean oldWhiteKingChecked = whiteKing.isChecked();
        boolean oldBlackKingChecked = blackKing.isChecked();
        boolean pieceDefend = false;
            board.executeMove(possibleMove,false);
            King king = (King) KingPosition.getPiece();
            if(!king.isChecked()){
                pieceDefend = true;
            }
            board.undoMove(possibleMove,oldFirstMove,lastMoveType,currentEnpasantCell, oldBlackKingChecked, oldWhiteKingChecked); // resetting every thing in the board lastMoveType,enpassant
            return pieceDefend;
    }
    // adding possible move is possible only if the king is not under check or if the king is under check while the piece can defend it
    protected void AddPossibleMove (Board board ,Move move , cell possibleCell ,boolean possible) {
        cell ownKing = this.getColor() == Color.WHITE ? board.getwKingPosition() : board.getbKingPosition();
        King king = (King) ownKing.getPiece();
      if (possible) { if (king.isChecked()) {
            if (this.checkIfPieceDefendKing(board, new Move(move.getFromCell(), possibleCell), ownKing)) {
                this.possibleMoves.add(possibleCell);
            }
        } else {
            this.possibleMoves.add(possibleCell);
        }}
    }
    protected boolean addPiece(Board board,int rank ,int file , Piece.Color currentPlayerColor ,cell OpponentKing,boolean possible ,Move move) {
        cell possibleCell = board.getCells()[rank][file];
        if (possibleCell.getPiece() != null) {
            this.RegularMoves.add(possibleCell);
            if (possibleCell.getPiece().getColor() != currentPlayerColor) {
                this.AddPossibleMove(board,move,possibleCell,possible);
            }
            if(possible || !(possibleCell.equals(OpponentKing)) ) return true;

        }
        this.RegularMoves.add(possibleCell);
        this.AddPossibleMove(board,move,possibleCell,possible);
        return false;
    }
    public abstract ArrayList<cell> getPossibleMoves(Move move , Board board , boolean possible);

    public abstract Piece clone();
    public abstract int getCode();
    public abstract int getValue();

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
