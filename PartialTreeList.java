package app;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import structures.Arc;
import structures.MinHeap;
import structures.Graph;
import structures.PartialTree;
import structures.Vertex;

/**
 * Stores partial trees in a circular linked list
 * 
 */
public class PartialTreeList implements Iterable<PartialTree> {
    
	/**
	 * Inner class - to build the partial tree circular linked list 
	 * 
	 */
	public static class Node {
		/**
		 * Partial tree
		 */
		public PartialTree tree;
		
		/**
		 * Next node in linked list
		 */
		public Node next;
		
		/**
		 * Initializes this node by setting the tree part to the given tree,
		 * and setting next part to null
		 * 
		 * @param tree Partial tree
		 */
		public Node(PartialTree tree) {
			this.tree = tree;
			next = null;
		}
	}

	/**
	 * Pointer to last node of the circular linked list
	 */
	private Node rear;
	
	/**
	 * Number of nodes in the CLL
	 */
	private int size;
	
	/**
	 * Initializes this list to empty
	 */
    public PartialTreeList() {
    	rear = null;
    	size = 0;
    }

    /**
     * Adds a new tree to the end of the list
     * 
     * @param tree Tree to be added to the end of the list
     */
    public void append(PartialTree tree) {
    	Node ptr = new Node(tree);
    	if (rear == null) {
    		ptr.next = ptr;
    	} else {
    		ptr.next = rear.next;
    		rear.next = ptr;
    	}
    	rear = ptr;
    	size++;
    }

    /**
	 * Initializes the algorithm by building single-vertex partial trees
	 * 
	 * @param graph Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
	public static PartialTreeList initialize(Graph graph) { //TODO
		
		PartialTreeList partials = new PartialTreeList();
		int iterate = 0;
		
		while(iterate < graph.vertices.length) {
			
			Vertex vertices = graph.vertices[iterate];
			PartialTree list = new PartialTree(vertices);			
			Vertex.Neighbor neighbor = vertices.neighbors;
			
			while(neighbor != null) {
				Arc arcs = new Arc(vertices, neighbor.vertex, neighbor.weight);
				list.getArcs().insert(arcs);
				neighbor = neighbor.next;
			}
			
			partials.append(list);
			iterate++;
		}
		//System.out.println(graph.vertices.length);
		
		return partials;
	}
	
	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree list
	 * for that graph
	 * 
	 * @param ptlist Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is irrelevant
	 */
	public static ArrayList<Arc> execute(PartialTreeList ptlist) { //TODO
		
		ArrayList<Arc> everything = new ArrayList<Arc>();
		PartialTreeList templist = ptlist;
		
		for(;templist.size() > 1;) {
			
			PartialTree PTX = templist.remove();
			Arc arcs = PTX.getArcs().deleteMin();
			Vertex two = arcs.getv2();
			
			for(int i = 0; PTX.getArcs().isEmpty() == false; i++) {
				if (PTX.getRoot().equals(two.getRoot())) {
					arcs = PTX.getArcs().deleteMin();
					two = arcs.getv2();
				} else if(i > ptlist.size()) {
					break;
				}
			}
			
			PartialTree PTY = templist.removeTreeContaining(two);
			PTX.merge(PTY);
			templist.append(PTX);
			everything.add(arcs);
			
		}
		
		return everything;

	}
	
    /**
     * Removes the tree that is at the front of the list.
     * 
     * @return The tree that is removed from the front
     * @throws NoSuchElementException If the list is empty
     */
    public PartialTree remove() 
    throws NoSuchElementException {
    			
    	if (rear == null) {
    		throw new NoSuchElementException("list is empty");
    	}
    	PartialTree ret = rear.next.tree;
    	if (rear.next == rear) {
    		rear = null;
    	} else {
    		rear.next = rear.next.next;
    	}
    	size--;
    	return ret;
    		
    }

    /**
     * Removes the tree in this list that contains a given vertex.
     * 
     * @param vertex Vertex whose tree is to be removed
     * @return The tree that is removed
     * @throws NoSuchElementException If there is no matching tree
     */
    public PartialTree removeTreeContaining(Vertex vertex) //TODO
    throws NoSuchElementException {
    	
    	if(size == 0) {
			throw new NoSuchElementException("DIS SHIT EMPTY");
		}
    	
		Node temp1 = rear.next;
		Node temp2 = rear;
		Vertex ptr = vertex;
		
		if(temp1.tree.getRoot().equals(ptr.getRoot())) {
			
			if(ptr.getRoot().equals(rear.tree.getRoot())) {
				temp2.next = rear.next;
				rear = temp2;
				size--;
				return temp1.tree;
			}
			
			if(size == 1) { 
				rear = null;
				size--;
				return temp1.tree;
			}
			
			temp2.next = temp1.next;
			size--;
			return temp1.tree;
		}
		
		temp1 = temp1.next;
		temp2 = temp2.next;
		
		while(temp1 != rear.next){
			
			if(temp1.tree.getRoot().equals(ptr.getRoot())) {
				
				if(ptr.getRoot().equals(rear.tree.getRoot())) {
					temp2.next = rear.next;
					rear = temp2;
					size--;
					return temp1.tree;
				}
				
				if(size == 1) { 
					rear = null;
					size--;
					return temp1.tree;
				}
				
				temp2.next = temp1.next;
				size--;
				return temp1.tree;
			}
			
			temp1 = temp1.next;
			temp2 = temp2.next;
			
		}
		
		return null;
     }
    
    /**
     * Gives the number of trees in this list
     * 
     * @return Number of trees
     */
    public int size() {
    	return size;
    }
    
    /**
     * Returns an Iterator that can be used to step through the trees in this list.
     * The iterator does NOT support remove.
     * 
     * @return Iterator for this list
     */
    public Iterator<PartialTree> iterator() {
    	return new PartialTreeListIterator(this);
    }
    
    private class PartialTreeListIterator implements Iterator<PartialTree> {
    	
    	private PartialTreeList.Node ptr;
    	private int rest;
    	
    	public PartialTreeListIterator(PartialTreeList target) {
    		rest = target.size;
    		ptr = rest > 0 ? target.rear.next : null;
    	}
    	
    	public PartialTree next() 
    	throws NoSuchElementException {
    		if (rest <= 0) {
    			throw new NoSuchElementException();
    		}
    		PartialTree ret = ptr.tree;
    		ptr = ptr.next;
    		rest--;
    		return ret;
    	}
    	
    	public boolean hasNext() {
    		return rest != 0;
    	}
    	
    	public void remove() 
    	throws UnsupportedOperationException {
    		throw new UnsupportedOperationException();
    	}
    	
    }
}


