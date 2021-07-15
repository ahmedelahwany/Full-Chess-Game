package Game.AI;

import Game.state.Board.Board;
import Game.state.Board.cell;
import Game.state.Move;
import Game.state.pieces.King;
import Game.state.pieces.Piece;

import java.util.ArrayList;

public class mainEvaluator implements Evaluator {
    private int whiteScore = 0; // for giving a score for check and check mate states
    private int blackScore = 0;

    @Override
    public int evaluate(Board board, int depth) {
         int whitePiecesScore = EvalPlayer(Piece.Color.WHITE,board,depth);
         int blackPiecesScore = EvalPlayer(Piece.Color.BLACK,board,depth);
         int whiteFinalScore = whitePiecesScore + whiteScore;
         int blackFinalScore = blackPiecesScore + blackScore;

        return  whiteFinalScore - blackFinalScore;
    }

    private int EvalPlayer(Piece.Color PlayerColor , Board board , int depth){
        int evalScorePieces = 0;
        int MobilityScore ;
        int pawnStructureScore;
        int TwoBishopBonus;
        int attackScore ;

        int numOfBishops = 0;
        whiteScore = 0;
        blackScore = 0;

        for(cell[] cells : board.getCells() ){
            for(cell cell : cells){
                if(cell.getPiece() != null && cell.getPiece().getColor() == PlayerColor){

                    // evaluate the value of the material
                    evalScorePieces += cell.getPiece().getValue();
                    if(cell.getPiece().getType() == Piece.Type.QUEEN) {numOfBishops++;}
                    if(cell.getPiece().getType() == Piece.Type.KING){
                        King king = (King) cell.getPiece();
                        // evaluate if the king is checked and give a score accordingly
                         whiteScore = king.isChecked() && PlayerColor == Piece.Color.BLACK  ? 50 : 0; // evaluate if the king is checked
                         blackScore = king.isChecked() && PlayerColor == Piece.Color.WHITE  ? 50 : 0;

                        // evaluate if the king is checkmated and give a score accordingly
                        if(king.isCheckedMatedOrStaleMated(cell, board).equals("checkmate") && PlayerColor == Piece.Color.BLACK){
                             whiteScore += 10000 * depthEvalValue(depth);
                         }
                        if(king.isCheckedMatedOrStaleMated(cell, board).equals("checkmate") && PlayerColor == Piece.Color.WHITE){
                            blackScore += 10000 * depthEvalValue(depth);
                        }

                        // evaluate if the king is castled and give a score accordingly
                        if(king.isCastled && PlayerColor == Piece.Color.BLACK){
                            whiteScore += 25;
                        }
                        if(king.isCastled && PlayerColor == Piece.Color.WHITE){
                            blackScore += 25;
                        }
                    }
                }
            }
        }
        MobilityScore = board.getAllPossibleMove(PlayerColor,false).size(); // to calc how many possible moves a player have in a valid position
        pawnStructureScore = new pawnStructureEvaluator().getPawnStructureScore(board,PlayerColor); // to calc pawn structure score
        TwoBishopBonus = numOfBishops == 2 ? 25 : 0;
        attackScore = getAttackScore(board,PlayerColor);

        return evalScorePieces + MobilityScore + pawnStructureScore + TwoBishopBonus +  attackScore;
    }

    private int getAttackScore(Board board, Piece.Color playerColor) {
        int numOfAttackingMoves = 0;
        for(Move move : board.getAllPossibleMove(playerColor,true)){
            if (move.isAttackingMove()){
                numOfAttackingMoves++;
            }
        }
        return numOfAttackingMoves;
    }

    private static class pawnStructureEvaluator {
        public pawnStructureEvaluator() {
        }
        public int getPawnStructureScore (Board board , Piece.Color PlayerColor){
            ArrayList<cell> pawns = getAllPawns(board,PlayerColor);
            return  getDoublePawnScore(pawns) + getIsolatedPawnScore(pawns) + getBackwardPawnsScore(pawns ,board , PlayerColor);
        }

         // no defending pawns can void being lost if moving forward
        private int getBackwardPawnsScore(ArrayList<cell> pawns , Board board , Piece.Color PlayerColor) {
            int NumOfBackWardPawns = 0;
            Piece.Color opponentColor = PlayerColor == Piece.Color.WHITE ? Piece.Color.BLACK : Piece.Color.WHITE;
            for (cell pawn : pawns){
                int numOfAdjacentPawns = 0;
                for (cell adjacentPawn :pawns){
                    if((adjacentPawn.getFile() == pawn.getFile() - 1 || adjacentPawn.getFile() == pawn.getFile() + 1)
                       && adjacentPawn.getRank() <=pawn.getRank()){
                        numOfAdjacentPawns++;
                    }
                }
                if(numOfAdjacentPawns == 0 && board.getCells()[pawn.getRank()+1][pawn.getFile()].getPiece() == null
                  && board.getAllPossibleMove(opponentColor,true).contains(new Move(pawn,new cell(pawn.getRank()+1,pawn.getFile())))){
                    NumOfBackWardPawns ++;
                }
            }
           return NumOfBackWardPawns * -12;
        }

        private int getIsolatedPawnScore(ArrayList<cell> pawns) {
            int [] numOfPawnInCols = new int[8];
            int NumOfIsolatedPawns = 0;
            for (cell pawn : pawns){
                numOfPawnInCols[pawn.getFile()]++;
            }

            // checking if there are no pawns in the right and left columns
            for(int i = 1; i < numOfPawnInCols.length - 1; i++) {
                if((numOfPawnInCols[i-1] == 0 && numOfPawnInCols[i+1] == 0)) {
                    NumOfIsolatedPawns += numOfPawnInCols[i];
                }
            }
            // check fo the first column
            if(numOfPawnInCols[0] > 0 && numOfPawnInCols[1] == 0) {
                NumOfIsolatedPawns += numOfPawnInCols[0];
            }
            // check for the last column
            if(numOfPawnInCols[7] > 0 && numOfPawnInCols[6] == 0) {
                NumOfIsolatedPawns += numOfPawnInCols[7];
            }
            return NumOfIsolatedPawns * -33;
        }

        private int getDoublePawnScore(ArrayList<cell> pawns) {
            int [] numOfPawnInCols = new int[8];
            int NumOfDoublePawns = 0;
            for (cell pawn : pawns){
                numOfPawnInCols[pawn.getFile()]++;
            }
            // check if there are more than one pawn in every column
            for (int numOfPawnInCol : numOfPawnInCols) {
                if(numOfPawnInCol > 1){
                    NumOfDoublePawns =+ numOfPawnInCol;
                }
            }
            return NumOfDoublePawns * -30;
        }

        private ArrayList<cell> getAllPawns(Board board, Piece.Color playerColor) {
            ArrayList<cell> pawns = new ArrayList<>();
            for ( cell[] row : board.getCells()) {
                for (cell cell : row) {
                    if (cell.getPiece() != null && cell.getPiece().getColor() == playerColor && cell.getPiece().getType() == Piece.Type.PAWN) {
                        pawns.add(cell);
                    }
                }
            }
            return pawns;
        }
    }
    private int depthEvalValue(int depth){
        return depth == 0 ? 1: 100 * depth;
    }
}
