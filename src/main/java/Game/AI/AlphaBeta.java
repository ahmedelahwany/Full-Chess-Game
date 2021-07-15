package Game.AI;

import Game.state.Board.Board;
import Game.state.Move;
import Game.state.pieces.Piece;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

import java.util.Collection;
import java.util.Comparator;
import java.util.Observable;

public class AlphaBeta extends Observable  implements MoveStrategy {
    private final Evaluator evaluator;
    private int quiescenceCount;
    private static final int MAX_QUIESCENCE = 5000 * 5;




    public AlphaBeta() {
        this.evaluator = new mainEvaluator();
        this.quiescenceCount = 0;
    }

    @Override
    public String toString() {
        return "AlphaBeta";
    }



    @Override
    public Move getMove(Board board ,  int depth) {
        Move bestMove = null;

        int HSV = Integer.MIN_VALUE;
        int LSV = Integer.MAX_VALUE;
        int CV;
        for (final Move move : MoveSorter.Custom.sort(board.getAllPossibleMove(board.currentPlayerColor,false))) {
            board.executeMove(move,true);
            board.executeMove(move,true);
            CV = board.currentPlayerColor == Piece.Color.WHITE ? min(board, depth - 1,HSV,LSV) :max(board, depth - 1,HSV,LSV);
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


    private int max(final Board board,
                    final int depth,
                    final int highest,
                    final int lowest) {
        if (depth == 0 || isGameFinished(board)) {
            return this.evaluator.evaluate(board, depth);
        }
        int currentHighest = highest;
        for (final Move move : MoveSorter.Basic.sort(board.getAllPossibleMove(board.currentPlayerColor,false))) {
            board.executeMove(move,true);
              currentHighest = Math.max(currentHighest, min(board,QuiescenceSearch(board, depth), currentHighest, lowest));
                if (currentHighest >= lowest) {
                    return lowest;
                }

        }
        return currentHighest;
    }

    private int min(final Board board,
                    final int depth,
                    final int highest,
                    final int lowest) {
        if (depth == 0 || isGameFinished(board)) {
            return this.evaluator.evaluate(board, depth);
        }
        int currentLowest = lowest;
        for (final Move move : MoveSorter.Basic.sort(board.getAllPossibleMove(board.currentPlayerColor,false))) {
             board.executeMove(move,true);
                currentLowest = Math.min(currentLowest, max(board, QuiescenceSearch(board, depth), highest, currentLowest));
                if (currentLowest <= highest) {
                    return highest;
                }
        }
        return currentLowest;
    }

    private static boolean isGameFinished (Board board){
        return board.lastMoveType == Move.moveType.CHECKMATE ||
               board.lastMoveType == Move.moveType.STALEMATE;
    }
    private int QuiescenceSearch(final Board board,
                                         final int depth) {
        if(depth == 1 && this.quiescenceCount < MAX_QUIESCENCE) {
            int activityMeasure = 0;
            if (board.checkIfKingsAreunderCheck(true)) {
                activityMeasure += 1;
            }
            for(final Move move: board.getNumFromMoveHistory(2)) {
                if(move.isAttackingMove()) {
                    activityMeasure += 1;
                }
            }
            if(activityMeasure >= 2) {
                this.quiescenceCount++;
                return 2;
            }
        }
        return depth - 1;
    }

    private enum MoveSorter {

        Basic {
            @Override
            Collection<Move> sort(final Collection<Move> moves) {
                return Ordering.from((Comparator<Move>) (move1, move2) -> ComparisonChain.start()
                        .compareTrueFirst(move1.isCastlingMove(Board.currentBoard), move2.isCastlingMove(Board.currentBoard))
                        .compare(move2.getMoveEffect(Board.currentBoard), move1.getMoveEffect(Board.currentBoard))
                        .result()).immutableSortedCopy(moves);
            }
        },
        Custom {
            @Override
            Collection<Move> sort(final Collection<Move> moves ) {
                return Ordering.from((Comparator<Move>) (move1, move2) -> ComparisonChain.start()
                        .compareTrueFirst(move1.isThreateningKing(Board.currentBoard), move2.isThreateningKing(Board.currentBoard))
                        .compareTrueFirst(move1.isCastlingMove(Board.currentBoard), move2.isCastlingMove(Board.currentBoard))
                        .compare(move2.getMoveEffect(Board.currentBoard), move1.getMoveEffect(Board.currentBoard))
                        .result()).immutableSortedCopy(moves);
            }
        };

        abstract  Collection<Move> sort(Collection<Move> moves);
    }
}
