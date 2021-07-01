package Game.state;


import lombok.Getter;
import lombok.Setter;

/**
 * Class representing the players in the game.
 */
public class Player {

    /**
     *  fields of the stone class.
     *
     *  name field represents player's name;
     */

    @Getter @Setter private String name;


    public Player(String name) {
        this.name=name;
    }


    public String toString() {
        return name;
    }

}
