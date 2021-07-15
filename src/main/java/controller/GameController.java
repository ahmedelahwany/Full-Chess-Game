package controller;

import Game.state.Board.Board;
import Game.state.Board.cell;
import Game.state.*;
import Game.state.pieces.King;
import Game.state.pieces.Piece;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;

@Slf4j
public class GameController {


    ImageView iv1 = new ImageView();

    private GameState gameState;

    private BooleanProperty gameFinished = new SimpleBooleanProperty(false);

    private ArrayList<Image> imagesList = new ArrayList<>();
    private ArrayList<cell> highlightedCells = new ArrayList<>();


    private  boolean secondPlayerFirstMove = true;

    public static Piece.Color currentPlayerColor = Piece.Color.WHITE;


    Player firstPlayer ;
    Player secondPlayer;

    private String initialMinutes;
    private Timeline countDownPlayerOne = null;
    private Timeline countDownPlayerTwo = null;

    private IntegerProperty minutesLeftPlayerOne = new SimpleIntegerProperty();
    private IntegerProperty secondsLeftPlayerOne = new SimpleIntegerProperty();

    private IntegerProperty minutesLeftPlayerTwo = new SimpleIntegerProperty();
    private IntegerProperty secondsLeftPlayerTwo = new SimpleIntegerProperty();

    private int incrementValue;

    private MoveLogger moveLogger;

    public GameController() {
    }

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
    private Label StopWatchPlayerOne;
    @FXML
    private Label StopWatchPlayerTwo;

    @FXML
    private  Label moveLogging;

    @FXML
    private Button WhiteResigns;
    @FXML
    private Button BlackResigns;

    @FXML
    private Button EndGameButton;



    /**
     * Sets the name of the players in Start.fxml.
     * @param FirstPlayer the name of the player 1.
     * @param SecondPlayer the name of player 2.
     */
    public void initdata(String incrementValue,String minutes , boolean isPlayerOneWhite,String FirstPlayer , String SecondPlayer) {
         firstPlayer = isPlayerOneWhite ? new Player(FirstPlayer) :new Player(SecondPlayer);
         secondPlayer = isPlayerOneWhite ? new Player(SecondPlayer) :new Player(FirstPlayer);

        initialMinutes = minutes;
        this.minutesLeftPlayerOne.set(Integer.parseInt(minutes));
        this.minutesLeftPlayerTwo.set(Integer.parseInt(minutes));
        this.secondsLeftPlayerOne.set(0);
        this.secondsLeftPlayerTwo.set(0);

        this.incrementValue = Integer.parseInt(incrementValue);

        addListeners(secondsLeftPlayerTwo ,minutesLeftPlayerOne);
        addListeners(secondsLeftPlayerTwo ,minutesLeftPlayerTwo);
        usernameLabel1.setText("" + firstPlayer);
        usernameLabel2.setText("" + secondPlayer);

        player1turn.setOpacity(1);
        player2turn.setOpacity(0);
    }

    private void addListeners(IntegerProperty seconds , IntegerProperty minutes) {
        seconds.addListener((observable ,oldValue,newValue) ->{
            if(newValue.doubleValue() + minutes.get()  == 0){
                gameFinished.setValue(true);
                messageLabel.setText("Time Out ... " + (currentPlayerColor == Piece.Color.WHITE ? "Black" : "White") + " is victorious");

            }
        });
        gameFinished.addListener(((observableValue, oldValue, newValue) -> {
            if(newValue){
                countDownPlayerOne.stop();
                countDownPlayerTwo.stop();
                disableBtns(true);
            }
        }));
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
        Platform.runLater(()-> this.start(false));
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
        highKingifChecked(gameState.getBoard());
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
        if (player1turn.getOpacity() == 1) { //black   switching opacity in UI and stopping the countdown for one player and starting other after incrementing the seconds value.
            countDownPlayerOne.stop();
            StopWatchPlayerOne.setText(String.valueOf(new Timer(minutesLeftPlayerOne.get(), secondsLeftPlayerOne.get() + incrementValue)));
            if(secondPlayerFirstMove)
           {
               createCountDownTimers(countDownPlayerTwo,minutesLeftPlayerTwo,secondsLeftPlayerTwo,StopWatchPlayerTwo,false,false);
               secondPlayerFirstMove = false;
           } else {
                secondsLeftPlayerTwo.set(secondsLeftPlayerTwo.get() + incrementValue);
                createCountDownTimers(countDownPlayerTwo,minutesLeftPlayerTwo,secondsLeftPlayerTwo ,StopWatchPlayerTwo,false,false);
            }
          player1turn.setOpacity(0);
            player2turn.setOpacity(1);
        } else { //white
            countDownPlayerTwo.stop();
            StopWatchPlayerTwo.setText(String.valueOf(new Timer(minutesLeftPlayerTwo.get(), secondsLeftPlayerTwo.get() + incrementValue)));
            secondsLeftPlayerOne.set(secondsLeftPlayerOne.get() + incrementValue);
            createCountDownTimers(countDownPlayerOne,minutesLeftPlayerOne,secondsLeftPlayerOne,StopWatchPlayerOne,false,true);
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

         if(!gameFinished.get())
         {if (click == Click.SECOND_CLICK || ( click == Click.FIRST_CLICK && clickedCell.getPiece()!= null && clickedCell.getPiece().getColor() == currentPlayerColor))
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
                             finishGameOrSwitch(clickedCell);
                         } else {
                             click = Click.FIRST_CLICK;
                         }
                         drawBoard();
                     }
                 } else {
                     if(firstClickPiece.getPiece().getPossibleMoves(new Move (firstClickPiece, clickedCell),gameState.getBoard(),true).contains(clickedCell)){
                         gameState.executeMove(new Move (firstClickPiece, clickedCell));
                         click = Click.FIRST_CLICK;
                         finishGameOrSwitch(clickedCell);
                     } else {
                         click = Click.FIRST_CLICK;
                     }
                     drawBoard();
                 }
            }
         }}

    }

    private void highKingifChecked(Board board) {
        cell whiteKing = board.getwKingPosition();
        cell blackKing = board.getbKingPosition();

        King wKing = (King)whiteKing.getPiece();
        King bKing = (King)blackKing.getPiece();

        if(wKing.isChecked()){
            Grid.getChildren().get(whiteKing.getFile() + whiteKing.getRank() * Board.DIMENSION).getStyleClass().set(0,"possibleCapture");
        } else if(bKing.isChecked()){
            Grid.getChildren().get(blackKing.getFile() + blackKing.getRank() * Board.DIMENSION).getStyleClass().set(0,"possibleCapture");
        } else{
            String wstyle = getStyleForSquare(whiteKing.getRank(),whiteKing.getFile());
            String bstyle = getStyleForSquare(blackKing.getRank(),blackKing.getFile());
            Grid.getChildren().get(whiteKing.getFile() + whiteKing.getRank() * Board.DIMENSION).getStyleClass().setAll(wstyle);
            Grid.getChildren().get(blackKing.getFile() + blackKing.getRank() * Board.DIMENSION).getStyleClass().setAll(bstyle);
        }
    }

    private void finishGameOrSwitch(cell clickedCell){
        if(gameState.isGameFinished()!=null )
        {
            gameFinished.setValue(true);
            if (gameState.isGameFinished().equals("Check Mate")) messageLabel.setText("Checkmate  " + currentPlayerColor + " is victorious");
            if (gameState.isGameFinished().equals("Stale Mate")) messageLabel.setText("Stalemate  ");
        }
        else switchPlayerTurn();
        moveLogger.addMove(new Move (firstClickPiece, clickedCell),gameState.getBoard());
        moveLogging.setText(moveLogger.getValue());

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
                Grid.getChildren().get(possibleMove.getFile() + possibleMove.getRank() * Board.DIMENSION).getStyleClass().add("possibleMove");
            } else{
                Grid.getChildren().get(possibleMove.getFile() + possibleMove.getRank() * Board.DIMENSION).getStyleClass().set(0,"possibleCapture");
            }
            highlightedCells.add(possibleMove);
        }
    }

    private void unhighlightPossibleMoves() {

        for (cell cell : highlightedCells) {
            String style = getStyleForSquare(cell.getRank(),cell.getFile());
            Grid.getChildren().get(cell.getFile() + cell.getRank() * Board.DIMENSION).getStyleClass().setAll(style);
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

    public void start(boolean restart) {
        if(restart) initdata(String.valueOf(incrementValue),initialMinutes,true,firstPlayer.getName(),secondPlayer.getName());
        gameState = new GameState();
        currentPlayerColor = Piece.Color.WHITE;
        firstClickPiece = null;
        click = Click.FIRST_CLICK;
        gameFinished.setValue(false);
        secondPlayerFirstMove = true;
        moveLogger = new MoveLogger();
        createCountDownTimers(countDownPlayerOne,minutesLeftPlayerOne,secondsLeftPlayerOne,StopWatchPlayerOne,false,true);
        createCountDownTimers(countDownPlayerTwo,minutesLeftPlayerTwo,secondsLeftPlayerTwo,StopWatchPlayerTwo,secondPlayerFirstMove,false);
        drawBoard();
        log.info("Game start.");
    }


    public void createCountDownTimers(Timeline animation , IntegerProperty minutes , IntegerProperty seconds , Label stopWatchLabel , boolean firstMove , boolean firstPlayerTimer ){
        Timer playerTime = new Timer(0,0) ;
        if (animation!=null) animation.stop();
        if(firstMove){
            CountDown(playerTime,minutes,seconds,stopWatchLabel);
        }
        else {
            CountDown(playerTime,minutes,seconds,stopWatchLabel);
            animation = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), e -> CountDown(playerTime,minutes,seconds,stopWatchLabel)));
            animation.setCycleCount(Timeline.INDEFINITE);
            animation.play();
            if(firstPlayerTimer){
                countDownPlayerOne =animation;
            } else{
                countDownPlayerTwo = animation;
            }
        }
    }

    private void CountDown(Timer defaultTime, IntegerProperty minutes, IntegerProperty seconds, Label timer) {
        if (seconds.get() == 0) {
            minutes.set(minutes.get()-1);
            seconds.set(59);
        } else {
            seconds.set(seconds.get()-1);
        }
        defaultTime.setMinutes(minutes.get());
        defaultTime.setSeconds(seconds.get());
        timer.setText(defaultTime.toString());
    }

//    private GameResult getResult() {
//
//        GameResult result;
//        result = GameResult.builder()
//                                    .FirstPlayer(gameState.getFirstPlayer().getName())
//                                    .SecondPlayer(gameState.getSecondPlayer().getName())
//                                    .winner(winner.getName())
//                                    .stepsFirstPlayer(stepCountFirstPlayer)
//                                    .stepsSecondPlayer(stepCountSecondPlayer)
//                                    .duration(Duration.between(beginGame, ZonedDateTime.now()))
//                                    .build();
//        return result;
//    }

    /**
     * Resets the game to a starting state.
     * @param actionEvent An action which is sent when a button is pressed.
     */
    public void resetGame(ActionEvent actionEvent)  {
        log.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        log.info("Resetting game...");
        disableBtns(false);
        messageLabel.setText("");
        moveLogging.setText("");
        start(true);
    }


    public void endGame() {
     this.gameFinished.setValue(true);
     messageLabel.setText("Game is Draw by agreement");
     disableBtns(true);
    }
    public void WhiteResigns() {
        this.gameFinished.setValue(true);
        messageLabel.setText("White Resigned ... Black is victorious");
        moveLogging.setText("");
        disableBtns(true);
    }
    public void BlackResigns() {
        this.gameFinished.setValue(true);
        messageLabel.setText("Black Resigned ... White is victorious");
        moveLogging.setText("");
        disableBtns(true);
    }
    private void disableBtns(boolean disable){
        WhiteResigns.setDisable(disable);
        BlackResigns.setDisable(disable);
        EndGameButton.setDisable(disable);
    }

}
