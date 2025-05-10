package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class PlacementPutDTO {
    private Integer placement;
    private String player;

    public void setPlacement(Integer placement) {
        this.placement = placement;
    }

    public Integer getPlacement() {
        return placement;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }
}