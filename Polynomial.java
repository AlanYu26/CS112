package poly;

import java.io.IOException;
import java.util.Scanner;


/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		Node temp = null;
		Node ptr1 = poly1;
		Node ptr2 = poly2;
		if(poly1 == null) return poly2;
		if(poly2 == null) return poly1;
		while(ptr1 != null ) {
			while(ptr2 != null) {
				if(ptr2.term.degree == ptr1.term.degree) {
					float total = ptr1.term.coeff + ptr2.term.coeff;
					temp = new Node(total,ptr2.term.degree,temp);
					if(temp.term.coeff == 0) {
						ptr1 = ptr1.next;
						ptr2 = ptr2.next;
						temp = temp.next;
					} else if(temp.term.coeff > 0 || temp.term.coeff < 0){
					ptr1 = ptr1.next;
					ptr2 = ptr2.next;
					}
				}   else if(ptr1.term.degree < ptr2.term.degree) {
						temp = new Node(ptr1.term.coeff,ptr1.term.degree,temp);
						if(temp.term.coeff == 0) {
							ptr1 = ptr1.next;
							temp = temp.next;
						} else if(temp.term.coeff > 0 || temp.term.coeff < 0){
						ptr1 = ptr1.next;
						}
					}
					else if(ptr1.term.degree > ptr2.term.degree) {
						temp = new Node(ptr2.term.coeff,ptr2.term.degree,temp);
						if(temp.term.coeff == 0) {
							ptr2 = ptr2.next;
							temp = temp.next;
						} else if(temp.term.coeff > 0 || temp.term.coeff < 0){
						ptr2 = ptr2.next;
						}
					}
				if(ptr1 == null && ptr2 !=null) {
					temp = new Node(ptr2.term.coeff,ptr2.term.degree,temp);
					if(temp.term.coeff == 0) {
						ptr2 = ptr2.next;
						temp = temp.next;
					} else if(temp.term.coeff > 0 || temp.term.coeff < 0) {
						ptr2 = ptr2.next;
					}
				}
			}
				if(ptr1 != null && ptr2 == null) {
					temp = new Node(ptr1.term.coeff,ptr1.term.degree,temp);
					if(temp.term.coeff == 0) {
						ptr1 = ptr1.next;
						temp = temp.next;
					} else if(temp.term.coeff > 0 || temp.term.coeff < 0) {
						ptr1 = ptr1.next;
					}
				}
			}
		
		Node reverse = temp;
		Node finals = null;
		while (reverse != null) {
				finals = new Node(reverse.term.coeff,reverse.term.degree,finals);
				reverse = reverse.next;
				
		}
		if (finals == null) return null;
		else { 
			temp = finals;
			return temp;	
		}
	} //this thing is actually 69 lines long brotherman Sesh
	/*
	...............,┤пнн`,
	.........,┤п`,....нн/
	....../п/.../..../
	..../../.../..../нн..,-----,
	../../.../....//нн┤...........`.
	./../.../..../нн......../┤п\....\
	('.('..('....('....нн...|.....'._.'
	.\.................нн...`\.../┤...)
	...\...............нн......V...../
	.....\.............нн............/
	.......`Х............нн.......Х┤
	..........|.........нн........|

	*/
	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
	
		if(poly1 == null) return null;
		if (poly2 == null) return null;
		Node ptr1 = poly1;
		Node ptr2 = poly2;
		Node temp = null;
		Node newtemp = null;
		float totalcoeff = 0;
		int totaldegree = 0;
		int highest = 0;
		while(ptr1 != null) {
			while(ptr2 != null) {
				totalcoeff = ptr1.term.coeff * ptr2.term.coeff;
				totaldegree = ptr1.term.degree + ptr2.term.degree;
				temp = new Node(totalcoeff,totaldegree, temp);
				if(totaldegree > highest) 
					highest = totaldegree;
					ptr2 = ptr2.next;
			}
			ptr1 = ptr1.next;
			ptr2 = poly2;
		}	
			for(int traverse = 0; traverse <= highest; traverse++) {
				Node another = temp;
				float pi = 0;
				while(another != null) {
					if(another.term.degree == traverse) 
						pi += another.term.coeff;
						another = another.next;
				}
				if(pi != 0) 
					newtemp = new Node(pi, traverse, newtemp);
			}
			Node reverse = newtemp;
			Node finals = null;
			while (reverse != null) {
					finals = new Node(reverse.term.coeff,reverse.term.degree,finals);
					reverse = reverse.next;
					
			}
			if (finals == null) return null;
			else { 
				newtemp = finals;
				return newtemp;
				}
			}

		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		float total = 0;
			while(poly != null) {
				if(poly.term.coeff == 0) {
				poly = poly.next;
				} else if(poly.term.coeff > 0 || poly.term.coeff < 0) {
					total += poly.term.coeff * Math.pow(x, poly.term.degree);
					poly = poly.next;
				}
			}
		return total;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}
