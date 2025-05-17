package ch.uzh.ifi.hase.soprafs24.entity;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Round implements Serializable {
    private Player activePlayer;
    private SongCard songCard;
    private int roundNr;
    private int activePlayerPlacement;
    private int challengerPlacement;
    private Player challenger;
    private String previewURL;
    private Set<Long> declinedChallenge = new HashSet<>(); //Id of players that declined challenge

    public Player getActivePlayer(){
        return this.activePlayer;
    }
    public void setActivePlayer(Player player){
        this.activePlayer = player;
    }
    public SongCard getSongCard(){
        return this.songCard;
    }
    public void setSongCard(SongCard songCard){
        this.songCard = songCard;
    }
    public int getRoundNr(){
        return this.roundNr;
    }
    public void setRoundNr(int roundNr){
        this.roundNr = roundNr;
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
    public void setPreviewURL(String previewURL){
        this.previewURL = previewURL;
    }
    public Set<Long> getDeclinedChallenge(){return this.declinedChallenge;}
    public void setDeclinedChallenge(Set<Long> declinedChallenge){this.declinedChallenge = declinedChallenge;}
    public void userDeclinesChallenge(Long userId){
        this.declinedChallenge.add(userId);
    }

}