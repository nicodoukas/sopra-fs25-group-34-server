package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class PlacementMessage {
    private String gameId;
    private Integer placement;

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setPlacement(Integer placement) {
        this.placement = placement;
    }

    public Integer getPlacement() {
        return placement;
    }

}