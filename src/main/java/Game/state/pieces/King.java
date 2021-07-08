package Game.state.pieces;

import Game.state.Board.Board;
import Game.state.Board.cell;
import Game.state.Move;


import java.util.ArrayList;

public class King extends Piece {



    public King(Color color) {
        super(color);
        this.setType(Type.KING);
    }

    boolean isChecked ;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public void validateMove(Move move, Board board) {

        if(this.getPossibleMoves(move,board,true).contains(move.getToCell())){
            if( move.getToCell().getFile() - move.getFromCell().getFile() == 2){
                Board.lastMoveType = Move.moveType.CASTLE_KING_SIDE;
                board.setEnPassant(null);
            } else if (move.getFromCell().getFile() - move.getToCell().getFile() == 2){
                Board.lastMoveType = Move.moveType.CASTLE_QUEEN_SIDE;
                board.setEnPassant(null);
            } else {
                Board.lastMoveType = Move.moveType.REGULAR;
                board.setEnPassant(null);
            }

        }
    }



    @Override
    public ArrayList<cell> getPossibleMoves(Move move, Board board, boolean possible) {
        Color opponentColor = move.getFromCell().getPiece().getColor() == Color.WHITE ? Color.BLACK:Color.WHITE;
        int KingRank = move.getFromCell().getRank();
        int KingFile = move.getFromCell().getFile();
        if(possible)this.possibleMoves.clear();

        this.RegularMoves.clear();
        int defaultRookRank = move.getFromCell().getPiece().getColor() == Color.WHITE ? 7 : 0;

        int[] possibleRankPositions ={KingRank,KingRank, KingRank + 1, KingRank + 1, KingRank + 1, KingRank - 1, KingRank - 1, KingRank - 1};
        int[] possibleFilePositions ={KingFile - 1, KingFile + 1, KingFile - 1,KingFile, KingFile + 1, KingFile - 1,KingFile, KingFile + 1};

         ArrayList<cell> attackedCellByOpponent = board.getAttackedCellsByOpponent(opponentColor);
        // regular moves of the king; we check if the king will be under threat by opponent pieces or if there are any pieces of same color in any possible cell (8 directions)
        for(int i=0;i<8;i++)
            {
                if(isWithinTheRange(possibleRankPositions[i],possibleFilePositions[i]))
              {
                    cell possibleCell = board.getCells()[possibleRankPositions[i]][possibleFilePositions[i]];
                    this.RegularMoves.add(possibleCell);
                    if((possibleCell.getPiece() == null || possibleCell.getPiece().getColor() != this.getColor())
                       && !attackedCellByOpponent.contains(possibleCell))
                        {
                            this.possibleMoves.add(possibleCell);
                        }
              }
            }

        // check for castling moves (King side  and queen side )
        int [][] castlingDataForIteration = {{7,2},{0,3}};
        for (int[] ints : castlingDataForIteration) {
            int direction = ints[0] == 7 ? 1 : -1;
            boolean castleValid = false;
            Piece rockPosition = board.getCells()[defaultRookRank][ints[0]].getPiece();
            if (rockPosition != null) {
                if (this.isFirstMove() && rockPosition.isFirstMove() && !this.isChecked()) {
                    castleValid = true;
                    for (int j = 1; j <= ints[1]; j++) {
                        cell interveningCell = board.getCells()[defaultRookRank][KingFile + direction * j];  // cell between king and rock has to be empty and not under attack by any opponent piece
                        if (attackedCellByOpponent.contains(interveningCell) || interveningCell.getPiece() != null) {
                            castleValid = false;
                        }
                    }
                }
            }
            if (castleValid) {
                this.possibleMoves.add(new cell(defaultRookRank, KingFile + direction * 2));

            }
        }

        return this.possibleMoves;

    }



    public String isCheckedMatedOrStaleMated (cell KingPosition ,Board board){
        // check if king is checkMated (it needs to be checked , can't move and there is no any piece of same color that can defend it)

        if(this.isChecked() &&
           this.getPossibleMoves(new Move(KingPosition,new cell (0,0)),board,true).size() == 0
           && board.getAllPossibleMove(KingPosition.getPiece().getColor()).size() == 0){
            return "checkmate";
        }
        // check if king is staleMated ( it needs to be not under check and none of its pieces can't move)
        if(!this.isChecked() &&
             board.getAttackedCellsByOpponent(this.getColor()).size() == 0){
            Board.lastMoveType = Move.moveType.STALEMATE;
            return "stalemate";
        }
            return null;
    }


    public ArrayList<cell> getRegularMoves(Move move, Board board) {
        int KingRank = move.getFromCell().getRank();
        int KingFile = move.getFromCell().getFile();
        this.possibleMoves.clear();

        int[] possibleRankPositions ={KingRank,KingRank, KingRank + 1, KingRank + 1, KingRank + 1, KingRank - 1, KingRank - 1, KingRank - 1};
        int[] possibleFilePositions ={KingFile - 1, KingFile + 1, KingFile - 1,KingFile, KingFile + 1, KingFile - 1,KingFile, KingFile + 1};

        // regular moves of the king; we check if the king will be under threat by opponent pieces or if there are any pieces of same color in any possible cell (8 directions)
        for(int i=0;i<8;i++)
        {
            if(isWithinTheRange(possibleRankPositions[i],possibleFilePositions[i]))
            {
                cell possibleCell = board.getCells()[possibleRankPositions[i]][possibleFilePositions[i]];
                this.RegularMoves.add(possibleCell);
            }
        }
        return this.possibleMoves;
    }

    @Override
    public int getCode() {
        return this.getColor() == Color.WHITE ? 5 : 11;
    }

}
