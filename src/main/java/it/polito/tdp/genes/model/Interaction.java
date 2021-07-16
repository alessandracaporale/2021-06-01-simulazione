package it.polito.tdp.genes.model;

public class Interaction implements Comparable<Interaction> {
	
	private String gene1;
	private String gene2;
	private String type;
	private double ec;
	private double peso;
	
	public Interaction(String gene1, String gene2, String type, double ec)  {
		super();
		this.gene1 = gene1;
		this.gene2 = gene2;
		this.type = type;
		this.ec = ec;
	}
	
	public String getGene1() {
		return gene1;
	}
	public void setGene1(String gene1) {
		this.gene1 = gene1;
	}
	public String getGene2() {
		return gene2;
	}
	public void setGene2(String gene2) {
		this.gene2 = gene2;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getEc() {
		return ec;
	}
	public void setEc(double ec) {
		this.ec = ec;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	public int compareTo (Interaction i) {
		String s1 = String.valueOf(this.getPeso());
		String s2 = String.valueOf(i.getPeso());
		return s2.compareTo(s1);
	}
	
}
