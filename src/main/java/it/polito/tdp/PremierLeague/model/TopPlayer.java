package it.polito.tdp.PremierLeague.model;

import java.util.List;

public class TopPlayer {

	private Player player;
	List<Avversario>avversari;
	
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public List<Avversario> getAvversari() {
		return avversari;
	}
	public void setAvversari(List<Avversario> avversari) {
		this.avversari = avversari;
	}
	@Override
	public String toString() {
		return player.getPlayerID()+" "+player.getName();
	}
}
