package Game.state;

public class Timer {

    private String minutes;
    private String seconds;

    public Timer(int min , int sec ) {
        this.setMinutes(min);
        this.setSeconds(sec);
    }



    public void setMinutes(int min) {
        if (min < 10) {
            this.minutes = "0" + min;
        } else {
            this.minutes = min + "";
        }
    }

    public void setSeconds(int sec) {
        if (sec < 10) {
            this.seconds = "0" + sec;
        } else if(sec > 59){
          setMinutes(Integer.parseInt( this.minutes)+1);
          int newSec = 60 - sec;
          setSeconds(Math.abs(newSec));
        } else {
            this.seconds = sec+"";
        }
    }

    public String toString() {
        return this.minutes + ":" + this.seconds;
    }

}
