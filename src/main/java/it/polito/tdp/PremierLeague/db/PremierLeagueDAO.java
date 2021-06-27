package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public void listAllPlayers(Map<Integer,Player>idMap){
		String sql = "SELECT * FROM Players";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				if(!idMap.containsKey(res.getInt("PlayerID"))) {
				Player p = new Player(res.getInt("PlayerID"), res.getString("Name"));
				idMap.put(p.getPlayerID(), p);
				}
				
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Player>getVertici(Map<Integer,Player>idMap,double goal){
		String sql="SELECT p.PlayerID,p.Name "
				+ "FROM actions AS a,players AS p "
				+ "WHERE a.PlayerID=p.PlayerID "
				+ "GROUP BY a.PlayerID "
				+ "HAVING AVG(a.Goals)>?";
		List<Player>vertici=new ArrayList<Player>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setDouble(1, goal);
		    ResultSet res = st.executeQuery() ;
		    while(res.next()) {
		    	if(idMap.containsKey(res.getInt("p.PlayerID"))) {
		    		vertici.add(idMap.get(res.getInt("p.PlayerID")));
		    	}
		    }
		    conn.close();
		    return vertici;
		}catch(SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	public List<Adiacenza>getAdiacenze(Map<Integer,Player>idMap){
		String sql="SELECT p1.PlayerID,p2.PlayerID,SUM(a1.TimePlayed-a2.TimePlayed) AS peso "
				+ "FROM players AS p1,players AS p2,actions AS a1,actions AS a2 "
				+ "WHERE p1.PlayerID>p2.PlayerID AND p1.PlayerID=a1.PlayerID AND "
				+ "p2.PlayerID=a2.PlayerID AND a1.TeamID!=a2.TeamID AND "
				+ "a1.`Starts`=1 AND a2.`Starts`=1 AND a1.MatchID=a2.MatchID "
				+ "GROUP BY p1.PlayerID,p2.PlayerID";
		List<Adiacenza>adiacenze=new ArrayList<Adiacenza>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
		    ResultSet res = st.executeQuery() ;
		    while(res.next()) {
		    	adiacenze.add(new Adiacenza(idMap.get(res.getInt("p1.PlayerID")),idMap.get(res.getInt("p2.PlayerID")),res.getDouble("peso")));
		    }
		    conn.close();
		    return adiacenze;
		}catch(SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
}
