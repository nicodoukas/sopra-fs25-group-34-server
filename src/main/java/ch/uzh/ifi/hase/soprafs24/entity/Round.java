package ch.uzh.ifi.hase.soprafs24.entity;
import java.io.Serializable;
public class Round implements Serializable {
    private Player activePlayer;
    private SongCard songCard;
    private int roundNr;
    private int activePlayerPlacement;
    private int challengerPlacement;
    private Player challenger;
    private String previewURL;

    public Player getActivePlayer(){
        return this.activePlayer;
    }
    public void setActivePlayer(Player player){
        this.activePlayer = player;
    }
    public SongCard getSongCard(){
        return this.songCard;
    }
    public void setSongCard(Player player){
        this.activePlayer = player;
    }
    public int getActivePlayerPlacement(){
        return this.activePlayerPlacement;
    }
    public void setActivePlayerPlacement(int placement){
        this.activePlayerPlacement = placement;
    }
    public int getChallengerPlacement(){
        return this.challengerPlacement;
    }
    public void setChallengerPlacement(int placement){
        this.challengerPlacement = placement;
    }
    public Player getChallenger(){
        return this.challenger;
    }
    public void setChallenger(Player player){
        this.challenger = player;
    }
    public String getPreviewURL(){
        return this.previewURL;
    }
    public void setPreviewURL(int placement){
        this.previewURL = placement;
    } // phikell: what's going on here?

}
