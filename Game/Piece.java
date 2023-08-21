package Game;

public class Piece {
    public String playerType;
    public int value;
    
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setPlayerType(String playerType){
        this.playerType = playerType;
    }
    
    public String getPlayerType(){
        return this.playerType;
    }
}