package Game.state.Board;

import Game.state.Move;
import Game.state.pieces.*;

import java.util.ArrayList;
import java.util.List;

public class Board {

    public static final int DIMENSION = 8;

    public Piece.Color currentPlayerColor = Piece.Color.WHITE;

    private static cell wKingPosition;
    private static cell bKingPosition;

    private ArrayList<Move> moveHistory = new ArrayList<>();

    public static Board currentBoard;

    public  Move.moveType lastMoveType;

    public Piece lastCapturedPiece;

    private cell enPassant;

    private ArrayList<cell> attackedCellsByOpponent = new ArrayList<>();



    private cell[][] cells = new cell[8][8];




    public Board(int[][] cellsCode) {

        build(cellsCode);
        Board.currentBoard = this;
    }
    public Board(){

    }

    public void executeMove(Move move , boolean checkKingMating) {
        move.getFromCell().getPiece().validateMove(move,this);
        lastCapturedPiece = move.getToCell().getPiece();
        // All game states are listed here , which are used to be able to update the board correctly
        // if it's a regular move

        if (lastMoveType == Move.moveType.REGULAR)
        {
           updateKingPositions(move);
           changePiecePosition(move.getFromCell(),move.getToCell());
        }
         else if (lastMoveType == Move.moveType.CASTLE_QUEEN_SIDE){
            updateKingPositions(move);
            changePiecePosition(move.getFromCell(),move.getToCell());
            changePiecePosition(cells[move.getFromCell().getRank()][move.getFromCell().getFile()-4],cells[move.getFromCell().getRank()][move.getFromCell().getFile()-1]);
        }
        else if (lastMoveType == Move.moveType.CASTLE_KING_SIDE){
            updateKingPositions(move);
            changePiecePosition(move.getFromCell(),move.getToCell());
            changePiecePosition(cells[move.getFromCell().getRank()][move.getFromCell().getFile()+3],cells[move.getFromCell().getRank()][move.getFromCell().getFile()+1]);
        }  else if (lastMoveType == Move.moveType.PAWN_EN_PASSANT){
            changePiecePosition(move.getFromCell(),move.getToCell());
            cells[enPassant.getRank()][enPassant.getFile()] = new cell(enPassant.getRank(),enPassant.getFile());
            enPassant = null;
        }

        this.moveHistory.add(move);


        checkPinnedPieces();
        checkIfKingsAreunderCheck(checkKingMating);
        switchPlayersColor();
    }


    public boolean checkIfKingsAreunderCheck(boolean checkKingMating){

        String CheckMateValue = null;
        boolean kingCheck = false;
        for ( cell[] row : cells){
            for (cell cell :row) {
                if (cell.getPiece() != null) {
                    if (cell.getPiece().getType() == Piece.Type.KING) {
                        King king = (King) cell.getPiece();
                        Piece.Color opponentColor = cell.getPiece().getColor() == Piece.Color.WHITE ? Piece.Color.BLACK : Piece.Color.WHITE;
                        king.setChecked(this.getAttackedCellsByOpponent(opponentColor).contains(cell));
                        if(this.getAttackedCellsByOpponent(opponentColor).contains(cell)) kingCheck = true;
                         if(checkKingMating){
                             if(king.isCheckedMatedOrStaleMated(cell,this) != null ){
                                 CheckMateValue = king.isCheckedMatedOrStaleMated(cell,this);
                             }
                         }
                    }
                }
            }
        }

        if ( CheckMateValue != null && CheckMateValue.equals("checkmate")){
            lastMoveType = Move.moveType.CHECKMATE;
        } else if(CheckMateValue != null && CheckMateValue.equals("stalemate")){
            lastMoveType = Move.moveType.STALEMATE;
        }

        return kingCheck;
    }

    // this method is used to undo moves after simulating executing moves on a board for checking if any piece can defend its king while checking if it's checkmated or nor
    public void undoMove(Move move ,boolean oldFirstMove , Move.moveType lastMoveType , cell enPassantCell , boolean BCheck , boolean wCheck){
        this.lastMoveType = lastMoveType;
        this.setEnPassant(enPassantCell);
        move.getFromCell().getPiece().setFirstMove(oldFirstMove);
        move.getFromCell().getPiece().setPinningPiece(null);
        King whiteKing = (King) wKingPosition.getPiece();
        King blackKing = (King) bKingPosition.getPiece();
        whiteKing.setChecked(wCheck);
        blackKing.setChecked(BCheck);
        cells[move.getToCell().getRank()][move.getToCell().getFile()].setPiece(lastCapturedPiece);
        cells[move.getFromCell().getRank()][move.getFromCell().getFile()].setPiece(move.getFromCell().getPiece());
    }


    private void build(int[][] cellsCode){
        Piece.Color color;
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                if(cellsCode[i][j]<12){
                    if(cellsCode[i][j]<6){
                       color = Piece.Color.WHITE;
                    } else{
                        color= Piece.Color.BLACK;
                    }
                    cells[i][j] = new cell (i,j, getPieceBasedOnCode(cellsCode[i][j],color));
                    if (cells[i][j].getPiece().getType() == Piece.Type.KING) {
                        if (cells[i][j].getPiece().getColor() == Piece.Color.WHITE) {
                            setwKingPosition(cells[i][j]);
                        } else {
                            setbKingPosition(cells[i][j]);
                        }
                    }
                } else{
                    cells[i][j] = new cell (i,j);
                }
            }
        }
        lastMoveType = Move.moveType.REGULAR;
        enPassant = null;
        lastCapturedPiece = null;
    }

    public ArrayList<cell> getAttackedCellsByOpponent(Piece.Color playerColor) {
        this.attackedCellsByOpponent.clear();
        for ( cell[] row : cells){
            for (cell cell :row){
                if (cell.getPiece() != null)
                {
                    if(cell.getPiece().getColor() == playerColor){
                        if (cell.getPiece().getType() == Piece.Type.KING) {
                            King king = (King) cell.getPiece();
                            this.attackedCellsByOpponent.addAll(king.getRegularMoves(new Move(cell,new cell (0,0)),this));
                        } else {
                            cell.getPiece().getPossibleMoves(new Move(cell,new cell (0,0)),this,false);
                            this.attackedCellsByOpponent.addAll(cell.getPiece().getRegularMoves());
                        }
                    }
                }
            }
        }

        return attackedCellsByOpponent;
    }

    public List<Move> getNumFromMoveHistory(int n){
        List<Move> numberOfMoves = new ArrayList<>(this.moveHistory.subList(this.moveHistory.size() - n - 1, this.moveHistory.size() - 1));
        numberOfMoves.add(this.moveHistory.get(this.moveHistory.size()-1));
        return numberOfMoves;
    }

    public ArrayList<Move> getAllPossibleMove(Piece.Color playerColor , boolean attack) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        for ( cell[] row : this.getCells()) {
            for (cell cell : row) {
                if (cell.getPiece() != null && cell.getPiece().getColor() == playerColor) {
                   for(cell move : cell.getPiece().getPossibleMoves(new Move(cell,new cell(0,0)),this,true)) {
                       if (!attack || cell.getPiece().getType() != Piece.Type.PAWN) possibleMoves.add(new Move(cell,move));
                   }
                   if(attack && cell.getPiece().getType() == Piece.Type.PAWN){
                       for (cell move : cell.getPiece().getRegularMoves()){
                           possibleMoves.add(new Move(cell,move));
                       }
                   }
                }
            }
        }
        return possibleMoves;
    }

    // method to check if there are any pinned pieces for a every player ; this method should be executed after every move
    private void checkPinnedPieces (){
        restPinnedPiece();
        for ( cell[] row : cells) {
            for (cell cell : row) {
                if (cell.getPiece() != null) {
                             cell.getPiece().checkPinnedPieces(cell,this); // checking if there is any pinning piece for every piece after every move over the board
                }
            }
        }
    }

    private void restPinnedPiece(){
        for ( cell[] row : cells) {
            for (cell cell : row) {
                if (cell.getPiece() != null) {
                    cell.getPiece().setPinningPiece(null); // resetting pinning piece property for all pieces
                }
            }
        }
    }

    // updating the king positions props in the board class
    private void updateKingPositions(Move move) {
        if (move.getFromCell().getPiece().getType() == Piece.Type.KING) {
            if (move.getFromCell().getPiece().getColor() == Piece.Color.WHITE) {
                setwKingPosition(move.getToCell());
            } else {
                setbKingPosition(move.getToCell());
            }
        }
    }

    private void changePiecePosition ( cell fromPos , cell toPos){
        cells[fromPos.getRank()][fromPos.getFile()] = new cell(fromPos.getRank(),fromPos.getFile());
        fromPos.getPiece().setFirstMove(false);
        cells[toPos.getRank()][toPos.getFile()].setPiece(fromPos.getPiece());
    }

    private Piece getPieceBasedOnCode(int i, Piece.Color color) {
        if(i == 0 || i == 6) return new Pawn(color);
        if(i == 3 || i == 9) return new Rock(color);
        if(i == 2 || i == 8) return new Bishop(color);
        if(i == 4 || i == 10) return new Queen(color);
        if(i == 1 || i == 7) return new Knight(color);
        return new King(color);
    }


    public cell getwKingPosition() {
        return wKingPosition;
    }


    public cell getbKingPosition() {
        return bKingPosition;
    }


    public  void setwKingPosition(cell wKingPosition) {
        Board.wKingPosition = wKingPosition;
    }

    public  void setbKingPosition(cell bKingPosition) {
        Board.bKingPosition = bKingPosition;
    }


    public cell[][] getCells() {
        return cells;
    }


    public cell getEnPassant() {
        return enPassant;
    }

    public void setEnPassant(cell enPassant) {
        this.enPassant = enPassant;
    }
    private void switchPlayersColor (){
        currentPlayerColor = currentPlayerColor == Piece.Color.WHITE ? Piece.Color.BLACK : Piece.Color.WHITE;
    }


    // deep cloning of board
    public Board customClone (boolean updateCurrent) {
        Board board = new Board();
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                board.getCells()[i][j] = this.getCells()[i][j].clonei();
            }
        }
        board.setEnPassant(this.enPassant.clonei());
        board.lastCapturedPiece = this.lastCapturedPiece.clone();
        board.lastMoveType = this.lastMoveType;
        board.setwKingPosition(this.getwKingPosition().clonei());
        board.setbKingPosition(this.getbKingPosition().clonei());
        board.currentPlayerColor = this.currentPlayerColor;
        // deep copy of board
        for (Move move : this.moveHistory){
            board.moveHistory.add((Move) move.clone());
        }
       if(updateCurrent) Board.currentBoard = board;
        return board;
    }

    @Override
    public String toString() {
            StringBuilder sb = new StringBuilder();
            for (cell[] row : this.getCells()) {
                for (cell cell : row) {
                    sb.append(cell).append(' ');
                }
                sb.append('\n');
            }
            return sb.toString();
    }





}
