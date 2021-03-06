package Game.state;

import Game.state.Board.Board;
import Game.state.Board.cell;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;


/**
 * Class representing the state of the puzzle.
 */
@Data
@Slf4j
public class GameState {



    private  Player FirstPlayer;

    private  Player SecondPlayer;


    private  Player currentPlayer ;
    int Type = 0;


    /**
     *  an array that sets the position of the cream and brown colors of the squares on the board.
     */
    public  int[][] INITIAL ;

    public static int[][] Basic = {
            {9,7,8,10,11,8,7,9},
            {6,6,6,6,6,6,6,6},
            {30,30,30,30,30,30,30,30},
            {30,30,30,30,30,30,30,30},
            {30,30,30,30,30,30,30,30},
            {30,30,30,30,30,30,30,30},
            {0,0,0,0,0,0,0,-0},
            {3,1,2,4,5,2,1,3}
    };

    /**
     * The array storing the current configuration of the board.
     */
    private Board board;



    public GameState() {
        this(Basic , new Player("john") , new Player("Doe"));
    }

    /**
     * Creates a {@code GameState} object that is initialized it with
     * the specified array.
     *
     * @param a an array of size 8&#xd7;8 representing the initial configuration
     *          of the board
     */
    public GameState(int[][] a , Player FirstPlayer, Player SecondPlayer) {
        this.FirstPlayer = FirstPlayer;
        this.SecondPlayer = SecondPlayer;
        this.currentPlayer = FirstPlayer;
        initBoard(a);
        INITIAL = cloneArray(Basic);
    }



    private void initBoard(int[][] a) {
        board =  new Board(a);
    }

    public void executeMove(Move move) {


        move.getFromCell().getPiece().validateMove(move,board);

        if (board.lastMoveType == Move.moveType.REGULAR)
       {
           changePiecePositionInState(move.getFromCell(),move.getToCell());
       }
        else if (board.lastMoveType == Move.moveType.CASTLE_QUEEN_SIDE){
            changePiecePositionInState(move.getFromCell(),move.getToCell());
            changePiecePositionInState(board.getCells()[move.getFromCell().getRank()][move.getFromCell().getFile()-4],
                                       board.getCells()[move.getFromCell().getRank()][move.getFromCell().getFile()-1]);
        }
        else if (board.lastMoveType == Move.moveType.CASTLE_KING_SIDE){
            changePiecePositionInState(move.getFromCell(),move.getToCell());
            changePiecePositionInState(board.getCells()[move.getFromCell().getRank()][move.getFromCell().getFile()+3],
                                       board.getCells()[move.getFromCell().getRank()][move.getFromCell().getFile()+1]);
        } else if (board.lastMoveType == Move.moveType.PAWN_EN_PASSANT){
            changePiecePositionInState(move.getFromCell(),move.getToCell());
            INITIAL[board.getEnPassant().getRank()][board.getEnPassant().getFile()] = 30;
        }

        board.executeMove(move,true);
    }





    private void changePiecePositionInState ( cell fromPos , cell toPos){
        INITIAL[fromPos.getRank()][fromPos.getFile()] = 30;
        INITIAL[toPos.getRank()][toPos.getFile()] =  fromPos.getPiece().getCode();
    }
    /**
     * Checks whether the game is finished.
     */
    public String isGameFinished(){
        if(board.lastMoveType == Move.moveType.CHECKMATE ){
            return "Check Mate";
        } else if(board.lastMoveType == Move.moveType.STALEMATE){
            return  "Stale Mate";
        } else {
            return null;
        }
    }



    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (cell[] row : board.getCells()) {
            for (cell cell : row) {
                sb.append(cell).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }


    public int[][] cloneArray(int [][] a){
        return Arrays.stream(a)
                .map(int[]::clone)
                .toArray(int[][]::new);
    }

}
