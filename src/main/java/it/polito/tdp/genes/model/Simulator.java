package it.polito.tdp.genes.model;

import java.util.*;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.*;
import it.polito.tdp.genes.model.*;

public class Simulator {

	//eventi
	private PriorityQueue<Event> queue;
	
	//parametri di simulazione
	private int TMAX = 36;
	private double probStessoGene = 0.3;
	
	//stato del sistema
	private Genes geneIniz;
	private int N;
	private Graph<Genes, DefaultWeightedEdge> graph;
	
	//misure in uscita
	List<Genes> geniStudiati;
	Map<Genes, Integer> mappaIngGenes;
	
	public void init(int N, Graph<Genes, DefaultWeightedEdge> graph, Genes start) {
		this.N=N;
		this.graph=graph;
		this.geneIniz=start;
		
		this.queue = new PriorityQueue<>();
		
		//controllo che il gene di partenza non sia isolato
		if(this.graph.degreeOf(geneIniz) == 0) {
			throw new IllegalArgumentException("Vertice di partenza isolato!");
		}
		
		//inizializzazione della coda
		for(int n=0; n<N; n++) {
			this.queue.add(new Event(0, n));
		}
		
		//inizializzazione del modello del mondo
		this.geniStudiati = new ArrayList<>();
		for(int n=0; n<N; n++) {
			this.geniStudiati.add(geneIniz);
		}
	}
	
	public void run() {
		while (!this.queue.isEmpty()) {
			Event e = queue.poll();
			
			int T = e.getT();
			int nIng = e.getnIng();
			Genes g = this.geniStudiati.get(nIng);
			
			if(T<this.TMAX) {
				//cosa studia nIng al mese T+1?
				if(Math.random()<this.probStessoGene) {
					//mantiene il gene
					this.queue.add(new Event(T+1, nIng));
				}
				else {
					double S=0; //somma dei pesi adiacenti
					for(DefaultWeightedEdge edge : this.graph.edgesOf(g)) {
						S += this.graph.getEdgeWeight(edge);
					}
					//estraggo num casuale R tra 0 e S
					Double R = Math.random()*S;
					//confronto R con le somme parziali dei pesi
					Genes nuovo = null;
					double somma =0;
					for(DefaultWeightedEdge edge : this.graph.edgesOf(g)) {
						somma += this.graph.getEdgeWeight(edge);
						if(somma > R) {
							nuovo = Graphs.getOppositeVertex(graph, edge, g);
							break;
						}
					}
					this.geniStudiati.set(nIng, nuovo);
					this.queue.add(new Event(T+1, nIng));
				}
			}
		}
	}
	
	public Map<Genes, Integer> getGeniStudiati() {
		Map<Genes, Integer> studiati = new HashMap<>();
		for(int n=0; n<N; n++) {
			Genes gene = this.geniStudiati.get(n);
			if(studiati.containsKey(gene)) {
				//incremento il contatore
				studiati.put(gene, studiati.get(gene)+1);
			}
			else {
				studiati.put(gene, 1);
			}
		}
		return studiati;
	}
}
