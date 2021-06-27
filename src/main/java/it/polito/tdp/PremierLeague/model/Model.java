package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {

	PremierLeagueDAO dao;
	Map<Integer,Player>idMap;
	Graph<Player,DefaultWeightedEdge>grafo;
	
	public Model() {
		dao=new PremierLeagueDAO();
		idMap=new HashMap<>();
		this.dao.listAllPlayers(idMap);
	}
	public void creaGrafo(double goal) {
		grafo=new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(idMap, goal));
		for(Adiacenza a:this.dao.getAdiacenze(idMap)) {
			if(this.grafo.containsVertex(a.getP1()) && this.grafo.containsVertex(a.getP2())) {
				if(a.getPeso()>0) {
					Graphs.addEdgeWithVertices(this.grafo, a.getP1(), a.getP2(), a.getPeso());
				}else if(a.getPeso()<0) {
					Graphs.addEdgeWithVertices(this.grafo, a.getP2(), a.getP1(),((double)-1)*a.getPeso());
				}
			}
		}
	}
	public int vertexNumber() {
		return this.grafo.vertexSet().size();
	}
	
	public int edgeNumber() {
		return this.grafo.edgeSet().size();
	}
	public TopPlayer getTopPlayer() {
		if(grafo==null)
			return null;
		Player best=null;
		Integer maxDegree=0;
		for(Player p:this.grafo.vertexSet()) {
			if(grafo.outDegreeOf(p)>maxDegree) {
				maxDegree=grafo.outDegreeOf(p);			
				best=p;
			}
		}
		TopPlayer topPlayer = new TopPlayer();
		topPlayer.setPlayer(best);
		List<Avversario> avversari = new ArrayList<>();
		for(DefaultWeightedEdge edge : grafo.outgoingEdgesOf(topPlayer.getPlayer())) {
			avversari.add(new Avversario(grafo.getEdgeTarget(edge), (int) grafo.getEdgeWeight(edge)));
			}
		Collections.sort(avversari);
		topPlayer.setAvversari(avversari);
		return topPlayer;
	}
}
