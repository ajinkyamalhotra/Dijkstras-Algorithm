import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Dijkstras {
	
	private static Vertex[] vertices;
	private static Edge[] edges;
	private static int infinity;
	
	public static void main(String[] args) throws FileNotFoundException {
		
		Graph graph = new Graph();
		
		//Getting data from graph
		vertices = graph.getAllVertices();
		edges = graph.getAllEdges();
		infinity = graph.getInfinityValue();
		
		Dijkstras(graph, graph.getSource());
				
		//Optional:
		//graph.printInput();
		//printStats();
		//printOutput();		
		
	}
	
	//printing Stats to console-->
	public static void printStats() { }
	
	//Writes the output to a .txt file
	public static void printOutput() throws FileNotFoundException { }
	
	//This part of the code finds the shortest path from the given source
	public static void Dijkstras(Graph G, Vertex source) {
			
		//initializing the distance of all vertices to infinity or in this case sum of all edge weights + 1
		for(int i=0; i<vertices.length; ++i)
			vertices[i].setDistance(infinity);
			
		//Setting source distance to 0
		source.setDistance(0);
			
		PriorityQueue<Vertex> PQueue = new PriorityQueue<Vertex>();
		
		//adding all the vertices in Priority Queue
		for(int i=0; i<vertices.length; ++i) {
			PQueue.add(vertices[i]);
			//System.out.println("Adding vertex: "+vertices[i].getVertexID());
		}
			
		while(!PQueue.isEmpty()) {
				
			Vertex v = PQueue.remove();
			//System.out.println("\nRemoving Vertex: "+v.getVertexID());
			ArrayList<Vertex> neighbourList = v.getneighbours();
			
			for(int i=0; i<neighbourList.size(); ++i) {
				
				//Getting current edge weight
				int edgeWeight = -1;
				
				for(int j=0; j<edges.length; ++j) {	
					//getting distance of the edge that connects v(source) to the specific neighbour
					edgeWeight = edges[j].getWeight(v, neighbourList.get(i));
					if(edgeWeight != -1) break;
				}
				//Current EdgeWeight calculation done
				
				//decrease key function
				if(v.getDistance() + edgeWeight < neighbourList.get(i).getDistance()) {		
					neighbourList.get(i).setDistance(v.getDistance() + edgeWeight);
					neighbourList.get(i).setPi(v);
					//Adjusting the priority queue after changing the source distance
					Vertex temp = PQueue.remove();
					PQueue.add(temp);
				}
				
			}
			
		}
		
	}
	
}

class Graph {

	private Vertex[] vertices;
	private Edge[] edges;
	private int numberOfVertices;
	private int numberOfEdges;
	private int infinity = 0;
	private String FilePath = "";

	public Graph() {
		
		FileReader inputFile = null;
		
		//Checking if the file exits
		try {	
			
			inputFile = new FileReader(FilePath);
		}
		catch (FileNotFoundException e) {
			
			//System.out.println("FILE NOT FOUND.\nPLEASE READ THE INSTRUCTIONS PROVIDED IN FILE \"Driver.java\" FROM LINE 20.");
			System.exit(1);
		}
		
		Scanner kb = new Scanner(inputFile);
		numberOfVertices = kb.nextInt();
		numberOfEdges = kb.nextInt();
		vertices = new Vertex[numberOfVertices];
		edges = new Edge[numberOfEdges];
		
		//Initializing all the vertices and storing them in array
		for(int i=0; i<numberOfVertices; ++i) {	
			if(i==0)
				vertices[i] = new Vertex(i, true);
			
			else
				vertices[i] = new Vertex(i);
			
		}
		
		//Initializing all the edges and storing them in array
		for(int i=0; i<numberOfEdges; ++i) {
			
			int from = kb.nextInt();
			int to = kb.nextInt();
			int weight = kb.nextInt();
			edges[i] = new Edge(i, weight, vertices[from], vertices[to]);
		}
		kb.close();
		
		//Calculating infinity for initialization of distances of each vertex
		for(int i=0; i<numberOfEdges; ++i)
			infinity += edges[i].getWeight();

		//infinity+1
		infinity++;
		
		//Setting neighbors---> 
		for(int i=0; i<numberOfEdges; ++i) {
			Vertex from = edges[i].getConnectedFrom();
			Vertex to = edges[i].getConnectedTo();
			from.setneighbour(to);
			
		}
	
	}
	
	//Prints out the input
	public void printInput() { }
	
	public int getNumberOfVertices() {
		return numberOfVertices;
	}
	
	public int getNumberOfEgdes() {
		return numberOfEdges;
	}
	
	public Edge[] getAllEdges() {
		return edges;
	}
	
	public Vertex[] getAllVertices() {
		return vertices;
	}
	
	public int getInfinityValue() {
		return infinity;
	}
	public Vertex getSource() {
		return vertices[0];
	}
}

class Edge {
	
	private int edgeWeight;
	private Vertex connectionFrom;
	private Vertex connectionTo;
	
	public Edge(int edgeID, int edgeWeight, Vertex connectionFrom, Vertex connectionTo) {

		this.edgeWeight = edgeWeight;
		this.connectionFrom = connectionFrom;
		this.connectionTo = connectionTo;
	}
	
	public int getWeight() {	
		return this.edgeWeight;
	}
	
	public Vertex getConnectedFrom() {
		return this.connectionFrom;
	}
	
	public Vertex getConnectedTo() {
		return this.connectionTo;
	}
	
	private int getConnectedFromID() {
		return this.connectionFrom.getVertexID();
	}
	
	private int getConnectedToID() {
		return this.connectionTo.getVertexID();
	}
	
	//will return -1 if that edge does not connects vertex v and v1
	//will return actual edge weight if that edge connects vertex v and v1
	public int getWeight(Vertex v, Vertex v1) {
		
		if(isConnected(v, v1))
			return this.getWeight();		
		
		else
			return -1;
	
	}
	
	//will return false if that edge does not connects vertex v1 and v2
	//will return true if that edge connects vertex v1 and v2
	private boolean isConnected(Vertex v1, Vertex v2) {
		
		if(this.getConnectedFromID() == v1.getVertexID() && this.getConnectedToID() == v2.getVertexID())
			return true;
			
		return false;
	}
}

class Vertex implements Comparable<Vertex>{

	private int id;
	private int distance = 0;
	private boolean source = false;
	private Vertex parent;
	private ArrayList<Vertex> neighbour = new ArrayList<Vertex>();
	
	//Constructor for ALL other vertices except for source
	public Vertex(int id) {
		
		this.id = id;	
		this.setPi(null);
		
	}
	
	//Constructor for ONLY source
	public Vertex(int id, boolean source) {
		
		this.id = id;
		this.source = source;
		this.distance = 0;
		this.setPi(null);
		
	}
	
	public int getVertexID() {
		return this.id;
	}
	
	public void setneighbour(Vertex v) {
		this.neighbour.add(v);
	}
	
	public ArrayList<Vertex> getneighbours() {
		return this.neighbour;
	}
	
	public void setDistance(int dist) {
		this.distance = dist;
	}
	
	public int getDistance() {
		return this.distance;
	}
	
	public void setPi(Vertex v) {
		this.parent = v;
	}
	
	public int getPi() {
		
		//for all the vertices except for source
		if(this.parent != null) 
			return this.parent.getVertexID();
		
		//will return -1 only in case where we try to get source's parent
		return -1;
	}
	
	public boolean getSourceFlag() {
		return this.source;
	}
	
	@Override
	public int compareTo(Vertex v){
		return this.distance-v.distance;		
	}

}
