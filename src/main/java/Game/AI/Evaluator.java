package Game.AI;

import Game.state.Board.Board;

public interface Evaluator {

    int evaluate(Board board , int depth);
}
