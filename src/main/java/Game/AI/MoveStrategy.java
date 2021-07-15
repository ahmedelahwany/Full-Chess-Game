package Game.AI;

import Game.state.Board.Board;
import Game.state.Move;

public interface MoveStrategy {
    Move getMove(Board board, int depth) throws CloneNotSupportedException;
}
