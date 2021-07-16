package it.polito.tdp.genes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.genes.db.*;

public class Model {
	
	GenesDao dao = new GenesDao();
	Graph<Genes, DefaultWeightedEdge> graph;

	public Model() {
	}
	
	public List<Genes> getAllGenes(){
		return dao.getAllGenes();
	}
	
	public List<Genes> getEssentialGenes() {
		return dao.getEssentialGenes();
	}
	
	public List<Interaction> getInteractions() {
		return dao.getInteractions();
	}
	
	public Genes getGenesById(String id) {
		Genes gene = null;
		for(Genes g : this.getEssentialGenes()) {
			if(g.getGeneId().equals(id)) {
				gene = g;
			}
		}
		return gene;
	}
	
	public void creaGrafo() {
		graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//vertici
		Graphs.addAllVertices(graph, this.getEssentialGenes());
		
		//archi
		for (Interaction i : this.getInteractions()) {
			Genes g1 = this.getGenesById(i.getGene1());
			Genes g2 = this.getGenesById(i.getGene2());
			
			if(g1.getChromosome()!=g2.getChromosome()) {
				i.setPeso(Math.abs(i.getEc()));
			}
			else {
				i.setPeso(2*Math.abs(i.getEc()));
			}
			if(this.graph.getEdge(g1, g2) == null) {
				Graphs.addEdge(graph, g1, g2, i.getPeso());
			}
		}
		/*
		for(Interaction i : this.getInteractions()) {
			Genes g1 = this.getGenesById(i.getGene1());
			Genes g2 = this.getGenesById(i.getGene2());
			if(this.graph.getEdge(g1, g2) == null) {
				Graphs.addEdge(graph, g1, g2, i.getPeso());
			}
		}*/
		System.out.println("Numero vertici: "+graph.vertexSet().size());
		System.out.println("Numero archi: "+graph.edgeSet().size());
		
	}
	
	
	public String getGeniAdiacenti(Genes gene) {
		if(this.graph==null) {
			return "Creare il grafo!";
		}
		String s = "";
		List<Interaction> lista = new ArrayList<>();
		/*for(Genes g : this.graph.vertexSet()) {
			if(g.equals(gene)) {
				for(DefaultWeightedEdge e : this.graph.edgesOf(g)) {
					//s += this.graph.getEdgeTarget(e)+" "+this.graph.getEdgeWeight(e)+"\n";
					lista.add(e);
				}
			}
		}*/
		for (Interaction i : this.getInteractions()) {
			Genes g1 = this.getGenesById(i.getGene1());
			Genes g2 = this.getGenesById(i.getGene2());
			
			if(g1.getChromosome()!=g2.getChromosome()) {
				i.setPeso(Math.abs(i.getEc()));
			}
			else {
				i.setPeso(2*Math.abs(i.getEc()));
			}
			if(i.getGene1().equals(gene.getGeneId()) || i.getGene2().equals(gene.getGeneId())) {
				lista.add(i);
			}
		}
		Collections.sort(lista);
		for(Interaction i : lista) {
			if(i.getGene1().equals(gene.getGeneId())) {
				s += i.getGene2()+" "+i.getPeso()+"\n";
			}
			else {
				s += i.getGene1()+" "+i.getPeso()+"\n";
			}
		}
		
		return s;
	}

	public Map<Genes, Integer> simulaIngegneri(Genes start, int n) {
		try {
			Simulator sim = new Simulator();
			sim.init(n, graph, start);
			sim.run();
			return sim.getGeniStudiati();
		}
		catch(IllegalArgumentException e) {
			return null;
		}
	}
}
