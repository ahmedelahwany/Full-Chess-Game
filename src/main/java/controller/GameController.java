package controller;

import Game.state.Board.Board;
import Game.state.Board.cell;
import Game.state.Move;
import Game.state.pieces.Piece;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import Game.results.GameResult;
import Game.results.GameResultDao;
import Game.results.TopPlayer;
import Game.results.TopPlayerDao;
import Game.state.Player;
import Game.state.GameState;

import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;

@Slf4j
public class GameController {


    ImageView iv1 = new ImageView();

    private GameState gameState;


    private ArrayList<Image> imagesList = new ArrayList<>();
    private ArrayList<cell> highlightedCells = new ArrayList<>();

    private int stepCountFirstPlayer;
    private int stepCountSecondPlayer;

    public static Piece.Color currentPlayerColor = Piece.Color.WHITE;

    private Player winner = new Player("");



    private ZonedDateTime beginGame;

    //private GameResultDao gameResultDao;
    //private TopPlayerDao topPlayerDao;


    public enum Click {
        FIRST_CLICK, SECOND_CLICK
    }

    public static Click click = Click.FIRST_CLICK;
    public static cell firstClickPiece;

    @FXML
    private GridPane Grid;


    @FXML
    private Label messageLabel;

    @FXML
    private Label usernameLabel1;
    @FXML
    private Label usernameLabel2;

    @FXML
    private Circle player1turn;
    @FXML
    private Circle player2turn;

    @FXML
    private Button EndGameButton;





    /**
     * Sets the name of the players in Start.fxml.
     * @param FirstPlayer the name of the player 1.
     * @param SecondPlayer the name of player 2.
     */
    public void initdata(String FirstPlayer , String SecondPlayer) {
        Player firstPlayer = new Player(FirstPlayer);
        Player secondPlayer = new Player(SecondPlayer);


        usernameLabel1.setText("" + firstPlayer);
        usernameLabel2.setText("" + secondPlayer);

        player1turn.setOpacity(1);
        player2turn.setOpacity(0);
    }

    /**
     * Initializes the game by setting the squares and implementing the colors of the squares on the board.
     * Sets the pieces starting positions on the board.
     */

    @FXML
    public void initialize() {
       // gameResultDao = GameResultDao.getInstance();
       // topPlayerDao = TopPlayerDao.getInstance();

        intiImageList();
        Platform.runLater(this::start);
    }

    private void drawBoard() {
        Grid.getChildren().clear();
        for (int i = 0; i < Grid.getRowCount(); i++) {
            for (int j = 0; j < Grid.getColumnCount(); j++) {
                String color;
               color = getStyleForSquare(i,j);
               StackPane square = createSquare(color);
              Grid.add(square, j, i);
            }
        }
        placePiece();
    }

    private void intiImageList (){
          String Color ;
        for (int i = 0 ; i<2; i++){
            if (i == 0)
                Color = "White";
            else
                Color = "Black";
            imagesList.add(new Image(getClass().getResource("/pictures/"+ Color +"_Pawn.png").toExternalForm()));
            imagesList.add(new Image(getClass().getResource("/pictures/"+ Color +"_Knight.png").toExternalForm()));
            imagesList.add(new Image(getClass().getResource("/pictures/"+ Color +"_Bishop.png").toExternalForm()));
            imagesList.add(new Image(getClass().getResource("/pictures/"+ Color +"_Rook.png").toExternalForm()));
            imagesList.add(new Image(getClass().getResource("/pictures/"+ Color +"_Queen.png").toExternalForm()));
            imagesList.add(new Image(getClass().getResource("/pictures/"+ Color +"_King.png").toExternalForm()));
        }
    }


   public void placePiece(){

       for (int i = 0; i < Grid.getRowCount(); i++) {
           for (int j = 0; j < Grid.getColumnCount(); j++) {
               if (gameState.INITIAL[i][j] < 12)
               {
                   iv1 = new ImageView();
                   iv1.setImage(imagesList.get(gameState.INITIAL[i][j]));
                   iv1.setFitWidth(70);
                   iv1.setFitHeight(70);
                   iv1.setSmooth(true);
                   iv1.setCache(true);
                   GridPane.setHalignment(iv1, HPos.CENTER);
                   GridPane.setValignment(iv1, VPos.CENTER);

                   Grid.add(iv1, j,i);
               }
           }
       }

    }


    private StackPane createSquare(String cls) {
        var square = new StackPane();
        square.getStyleClass().add(cls);
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }
    private void switchPlayerTurn() {

        currentPlayerColor = currentPlayerColor == Piece.Color.WHITE? Piece.Color.BLACK : Piece.Color.WHITE;
        if (player1turn.getOpacity() == 1) {
            player1turn.setOpacity(0);
            player2turn.setOpacity(1);
        } else {
            player1turn.setOpacity(1);
            player2turn.setOpacity(0);
        }
    }

    /**
     * a void method that checks if the user is trying to click on one of the positions,
     * @param mouseEvent a mouse event.
     */

    public void handleMouseClick(MouseEvent mouseEvent) {


        int column = GridPane.getColumnIndex((Node) mouseEvent.getSource());
        int row = GridPane.getRowIndex((Node) mouseEvent.getSource());

         cell clickedCell = gameState.getBoard().getCells()[row][column];

         if (click == Click.SECOND_CLICK || ( click == Click.FIRST_CLICK && clickedCell.getPiece().getColor() == currentPlayerColor))
         {
             if(click == Click.FIRST_CLICK ){
                 if(gameState.INITIAL[row][column] != 30)
                 {
                    clickFirstPiece(clickedCell);
                 }
             } else {
                 if(gameState.INITIAL[row][column] != 30)
                 {
                     if(firstClickPiece.getPiece().getColor() == clickedCell.getPiece().getColor()){
                         unhighlightPossibleMoves();
                         clickFirstPiece(clickedCell);
                     } else {
                         if(firstClickPiece.getPiece().getPossibleMoves(new Move (firstClickPiece, clickedCell),gameState.getBoard(),true).contains(clickedCell)){
                             gameState.executeMove(new Move (firstClickPiece, clickedCell));
                             click = Click.FIRST_CLICK;
                             if(gameState.isGameFinished()!= null){
                                 messageLabel.setText(gameState.isGameFinished());
                             }
                             System.out.println(gameState.isGameFinished());
                             switchPlayerTurn();
                         } else {
                             click = Click.FIRST_CLICK;
                         }
                         drawBoard();
                     }
                 } else {
                     if(firstClickPiece.getPiece().getPossibleMoves(new Move (firstClickPiece, clickedCell),gameState.getBoard(),true).contains(clickedCell)){
                         gameState.executeMove(new Move (firstClickPiece, clickedCell));
                         click = Click.FIRST_CLICK;
                         if(gameState.isGameFinished()!= null){
                             messageLabel.setText(gameState.isGameFinished());
                         }
                         System.out.println(gameState.isGameFinished());
                         switchPlayerTurn();
                     } else {
                         click = Click.FIRST_CLICK;
                     }
                     drawBoard();
                 }
            }
         }

    }

    private void clickFirstPiece(cell clickedCell) {
        Move move = new Move (clickedCell,new cell(0,0));
        highlightPossibleMoves (move);
        firstClickPiece = clickedCell;
        click = Click.SECOND_CLICK;
    }

    private void highlightPossibleMoves( Move move) {

        ArrayList<cell> possibleMoves = move.getFromCell().getPiece().getPossibleMoves(move,gameState.getBoard(),true);
        for (cell possibleMove : possibleMoves) {
            if ( possibleMove.getPiece() == null){
                Grid.getChildren().get(possibleMove.getFile() + possibleMove.getRank() * Board.DIMENSION).getStyleClass().set(0,"possibleMove");
            } else{
                Grid.getChildren().get(possibleMove.getFile() + possibleMove.getRank() * Board.DIMENSION).getStyleClass().set(0,"possibleCapture");
            }
            highlightedCells.add(possibleMove);
        }
    }

    private void unhighlightPossibleMoves() {

        for (cell cell : highlightedCells) {
                String style = getStyleForSquare(cell.getRank(),cell.getFile());
                Grid.getChildren().get(cell.getFile() + cell.getRank() * Board.DIMENSION).getStyleClass().set(0,style);
        }
        highlightedCells.clear();
    }
    private String getStyleForSquare (int rank , int file){
        if(rank%2==0) {
            if (file % 2 == 0)
                return "square-black";
            else
                return "square";
        }
        else
        {  if(file%2==0)
            return "square";
        else
            return "square-black";
        }
    }

    public void start() {
        stepCountFirstPlayer = 0;
        stepCountSecondPlayer = 0;
        player1turn.setOpacity(1);
        player2turn.setOpacity(0);
       // messageLabel.setText("Player " + this.FirstPlayer.getName()+ " has the first turn");
       // gameState = new GameState(this.FirstPlayer,this.SecondPlayer);
        gameState = new GameState();
        currentPlayerColor = Piece.Color.WHITE;
        firstClickPiece = null;
        click = Click.FIRST_CLICK;
        drawBoard();
        beginGame = ZonedDateTime.now();
        log.info("Game start.");
    }
    /**
     * Resets the game to a starting state.
     * @param actionEvent An action which is sent when a button is pressed.
     */
    public void resetGame(ActionEvent actionEvent)  {
        log.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        log.info("Resetting game...");

        start();
    }

    private GameResult getResult() {

        GameResult result;
        result = GameResult.builder()
                                    .FirstPlayer(gameState.getFirstPlayer().getName())
                                    .SecondPlayer(gameState.getSecondPlayer().getName())
                                    .winner(winner.getName())
                                    .stepsFirstPlayer(stepCountFirstPlayer)
                                    .stepsSecondPlayer(stepCountSecondPlayer)
                                    .duration(Duration.between(beginGame, ZonedDateTime.now()))
                                    .build();
        return result;
    }

    public void endGame(ActionEvent actionEvent) throws IOException {
//

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/highscores.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        log.info("Finished game, loading Top five scene.");
    }
}
