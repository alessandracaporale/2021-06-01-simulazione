package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.genes.model.*;


public class GenesDao {
	
	public List<Genes> getAllGenes(){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM genes";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
				result.add(genes);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	

	public List<Genes> getEssentialGenes() {
		String sql = "SELECT distinct GeneID, Essential, Chromosome "
				+ "FROM genes "
				+ "WHERE Essential = ? "
				+ "ORDER BY GeneID";
		List<Genes> result = new ArrayList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, "Essential");
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				//boolean esiste = false;
				Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
				/*
				for (Genes g : result) {
					if(g.getGeneId().equals(genes.getGeneId())) {
						esiste = true;
					}
				}
				if(!esiste) {
					result.add(genes);
				}*/
				result.add(genes);
			}
			res.close();
			st.close();
			conn.close();
			
			return result;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	
	public List<Interaction> getInteractions() {
		String sql = "SELECT * "
				+ "FROM Interactions "
				+ "WHERE GeneID1 <> GeneID2";
		List<Interaction> result = new ArrayList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				boolean f1 = false;
				boolean f2 = false;
				Interaction i = new Interaction(res.getString("GeneID1"), res.getString("GeneID2"), res.getString("Type"), res.getDouble("Expression_Corr"));
				for(Genes g : this.getEssentialGenes()) {
					if(g.getGeneId().equals(res.getString("GeneID1"))) {
						f1 = true;
					}
					if(g.getGeneId().equals(res.getString("GeneID2"))) {
						f2 = true;
					}
				}
				if(f1 && f2) {
					result.add(i);
				}
				
			}
			conn.close();
			return result;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	
}
