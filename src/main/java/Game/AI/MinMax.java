package Game.AI;

import Game.state.Board.Board;
import Game.state.Move;
import Game.state.pieces.Piece;

public class MinMax implements MoveStrategy {
    private final Evaluator evaluator;

    public MinMax(){
        this.evaluator = new mainEvaluator();
    }

    @Override
    public Move getMove(Board board, int depth) throws CloneNotSupportedException {

        Move bestMove = null;
        int HSV = Integer.MIN_VALUE;
        int LSV = Integer.MAX_VALUE;
        int CV;
        Board newBoard;
        for( final Move move : board.getAllPossibleMove(board.currentPlayerColor,false)){
             newBoard = board.customClone(true);
            newBoard.executeMove(move,true);
            CV = board.currentPlayerColor == Piece.Color.WHITE ? min(newBoard,depth - 1) :max(newBoard,depth -  1);
            if(board.currentPlayerColor == Piece.Color.WHITE && CV >= HSV){
                HSV = CV;
                bestMove = move;
            } else if(board.currentPlayerColor == Piece.Color.BLACK && CV <= LSV){
                LSV = CV;
                bestMove = move;
            }
        }

        return bestMove;
    }

    public int max(final Board board , final int depth ) throws CloneNotSupportedException {
        if (depth == 0 || isGameFinished(board)){
            return this.evaluator.evaluate(board,depth);
        }
        int HSV = Integer.MIN_VALUE;
        Board newBoard;
        for (final Move move : board.getAllPossibleMove(board.currentPlayerColor,false)){
            newBoard = board.customClone(true);
            board.executeMove(move,true);
            final int CV = min(newBoard,depth - 1);
            if(CV >= HSV){
                HSV = CV;
            }
        }
        return HSV;
    }
    public int min(final Board board , final int depth ) throws CloneNotSupportedException {
        if (depth == 0 || isGameFinished(board)){
            return this.evaluator.evaluate(board,depth);
        }
        int LSV = Integer.MAX_VALUE;
        Board newBoard;
        for (final Move move : board.getAllPossibleMove(board.currentPlayerColor,false)){
            newBoard = board.customClone(true);
            board.executeMove(move,true);
            final int CV = max(newBoard,depth - 1);
            if(CV <= LSV){
                LSV = CV;
            }
        }
        return LSV;
    }

    private static boolean isGameFinished (Board board){
        return board.lastMoveType == Move.moveType.CHECKMATE ||
               board.lastMoveType == Move.moveType.STALEMATE;
    }
    @Override
    public String toString() {
        return "MinMax";
    }


}
