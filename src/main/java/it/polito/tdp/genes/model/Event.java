package it.polito.tdp.genes.model;

public class Event implements Comparable<Event> {
	
	private int T; //mesi da 1 a 36
	private int nIng; //numero ingegnere
	
	public Event(int t, int nIng) {
		T = t;
		this.nIng = nIng;
	}

	public int getT() {
		return T;
	}
	public void setT(int t) {
		T = t;
	}
	public int getnIng() {
		return nIng;
	}
	public void setnIng(int nIng) {
		this.nIng = nIng;
	}

	@Override
	public int compareTo(Event e) {
		return T-e.getT();
	}
}
