package abd.tableau.iterative;

public class Edge {
	 
    protected AndNode a, b;
    protected double weight;
     
    public Edge(AndNode a, AndNode b) {
        this(a, b, Double.POSITIVE_INFINITY);
    }
     
    public Edge(AndNode a, AndNode b, double weight) {
        this.a = a;
        this.b = b;
        this.weight = weight;
    }
     
    public double getWeight() {
        return weight;
    }
     
    public String toString() {
        return a + " ==> " + b;
    }
 
}