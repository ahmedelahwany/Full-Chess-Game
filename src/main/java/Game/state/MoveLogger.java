package Game.state;

import Game.state.Board.Board;

import java.util.ArrayList;

public class MoveLogger {
     ArrayList<String> value =  new ArrayList<>();
     Row currentRow = new Row();

    public MoveLogger() {

    }

    public void addMove (Move move, Board board){
        String moveAnnotation = getMoveAnnotation(move,board);
       if(currentRow.getWhiteMove().equals("")){
           currentRow.setWhiteMove(moveAnnotation);
           this.value.add(currentRow.toString());
       } else {
           currentRow.setBlackMove(moveAnnotation);
           this.value.remove(this.value.size()-1);
           this.value.add(currentRow.toString());
           currentRow.setMoveNumber(currentRow.getMoveNumber() + 1);
           currentRow.setWhiteMove("");
           currentRow.setBlackMove("");
       }
    }


    private String getMoveAnnotation(Move move , Board board) {
        if(Board.lastMoveType == Move.moveType.CASTLE_QUEEN_SIDE){
            return "O-O-O";
        } else if(Board.lastMoveType == Move.moveType.CASTLE_KING_SIDE){
            return "O-O";
        }
        return move.getFromCell() + (board.lastCapturedPiece == null ? "" : "x") + (Board.lastMoveType == Move.moveType.PAWN_EN_PASSANT ? "x" : "") +
               getCharForNumber(move.getToCell().getFile() + 1) + (8 - move.getToCell().getRank()) + checkIfkingIsCheckedOrMated(board);
    }

    private String checkIfkingIsCheckedOrMated(Board board) {
        if(Board.lastMoveType == Move.moveType.CHECKMATE){
            return "#";
        } else if(board.checkIfKingsAreunderCheck(false)){
            return "+";
        } else{
            return "";
        }
    }


    private static class Row {
        private int moveNumber;
        private String whiteMove;
        private String blackMove;

        public Row() {
            this.moveNumber = 1;
            this.whiteMove = "";
            this.blackMove = "";
        }

        public int getMoveNumber() {
            return moveNumber;
        }

        public void setMoveNumber(int moveNumber) {
            this.moveNumber = moveNumber;
        }

        public String getWhiteMove() {
            return whiteMove;
        }

        public void setWhiteMove(String whiteMove) {
            this.whiteMove = whiteMove;
        }


        public void setBlackMove(String blackMove) {
            this.blackMove = blackMove;
        }

        @Override
        public String toString() {
            return moveNumber + " -  " + whiteMove + "  ||  " + blackMove + "\n";
        }
    }
    public String getValue() {
        StringBuilder moves = new StringBuilder();
        for (String s : value) {
            moves.append(s);
        }
        return moves.toString();
    }


    private String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char)(i + 'a' - 1)) : null;
    }
}
