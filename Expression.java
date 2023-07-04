package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    
    	String temp = "";
    	String ops = "+-*/()]";
    	expr = expr.replaceAll("\\s+","");
    	
    	for(int i = 0; i < expr.length(); i++) {
    		if(Character.isDigit(expr.charAt(i)) == true){
    			continue;
    		}
    		if(delims.contains(Character.toString(expr.charAt(i))) != true) {
    			temp += expr.charAt(i);
    		} else if(ops.contains(Character.toString(expr.charAt(i)))) {
    			Variable temp2 = new Variable(temp);
    			if(vars.contains(temp2) != true && temp != "") {
    				vars.add(temp2);
    				System.out.println(vars);
    			}
    		temp = "";
    		} else if(expr.charAt(i) == '[') {
    			if(temp != "") {
    				Array temp2 = new Array(temp);
    				arrays.add(temp2);
    				temp = "";
    				System.out.println(arrays);
    			}
    		}
    	}
    	Variable temp2 = new Variable(temp);
    	if((vars.contains(temp2)) != true && delims.contains(temp) != true) {
    		vars.add(temp2);
    	}
    	System.out.println(vars);
    	System.out.println(vars.size());
    	System.out.println(arrays);
    	System.out.println(arrays.size());
    }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
   /* private static void solver(Stack<Character> one, float var1, float var2) {
    	char cased = one.pop();
    	float finals = 0;
    	Stack<Float> numbers = new Stack<>();
    	
    	if(cased == '*') {
    		finals = var1 * var2;
    	} else if(cased == '/') {
    		finals = var2 / var1;
    	} else if(cased == '+') {
    		finals =  var1 + var2;
    	} else if(cased == '-') {
    		finals = var2 - var1;
    	}
    	numbers.push(finals);
    	
    }*/

    private static void solving(Stack<Character> one, Stack<Float> two) { // Popping operations from first stack and solving it with two characters.
    	Float finals = null;
    	if(one.size() > 0 && two.size() > 1) {
    		Float var1 = two.pop(); Float var2 = two.pop(); char cased = one.pop();
    	if(cased == '*') {
    		finals = var1 * var2;
    	} else if(cased == '/') {
    		finals = var2 / var1;
    	} else if(cased == '+') {
    		finals =  var1 + var2;
    	} else if(cased == '-') {
    		finals = var2 - var1;
    	}
    	two.push(finals);
    	} else if(two.size() > 0 ) {
    		finals = two.pop();
    		two.push(two.pop());
    	}
    }
    
    private static boolean order(char opone, char optwo) {
    	if((opone == '*' || opone == '/') && (optwo == '+' || optwo == '-')){
    		return false;
    	}
    	return true;
   }
    
/**
 * Evaluates the expression.
 * 
 * @param vars The variables array list, with values for all variables in the expression
 * @param arrays The arrays array list, with values for all array items
 * @return Result of evaluation
 */

    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	// following line just a placeholder for compilation
    	
    	
		float tmpfloat = 0;
		int i = 0;
		int next = 0;
		String contained = " +-/*)]"; // check for delims minus beginning paren/brackets
		expr = expr.replaceAll("\\s+","");
		String operators = "+-/*";
		Float answer = null;
		String opensided = "([";
		
		Stack<Character> operations = new Stack<>();
        Stack<Float> numbers = new Stack<>();
        Stack<String> strings = new Stack<>();
        
        StringBuffer Tokens = new StringBuffer("");
        
    	 for(i = 0; i < expr.length(); i++) {
    			if(expr.charAt(i) == '(') {
    				operations.push(expr.charAt(i));
    	 }
    			else if(expr.charAt(i) == ')') {
					while(operations.isEmpty()  == false && numbers.isEmpty() == false && opensided.contains(Character.toString(operations.peek())) != true) {   				
						solving(operations, numbers);
					}
    				if(opensided.contains(Character.toString(operations.peek()))) {
	   	   				operations.pop();
    				}
    			 }
    			else if(expr.charAt(i) == '[') {
    				strings.push(Tokens.toString());
		            Tokens.setLength(0);    							    					
    				operations.push(expr.charAt(i));
    			
    			}
    			else if(expr.charAt(i) == ']') {
					while(operations.isEmpty() == false && numbers.isEmpty()  == false && opensided.contains(Character.toString(operations.peek()))) {   				
						solving(operations, numbers);
					}
    				if(opensided.contains(Character.toString(operations.peek()))) {
	   	   				operations.pop();
    				}
    				int vari = numbers.pop().intValue();
     				Iterator<Array> increase = arrays.iterator();
     				while (increase.hasNext()) {
     					Array arrayd = increase.next();
     					if(arrayd.name.equals(strings.peek())) {
     						numbers.push((float) arrayd.values[vari]);   
         				    strings.pop();
     					}
     				}    		
    	 }
    		
    			else if(operators.contains(Character.toString(expr.charAt(i)))) {
    				while(operations.isEmpty() == false && opensided.contains(Character.toString(operations.peek())) != true && order(expr.charAt(i), operations.peek())){
						solving(operations, numbers);						
					}
					operations.push(expr.charAt(i));    						
    				}
    		
	    			if(Character.isLetter(expr.charAt(i))) {
	    				Tokens.append(expr.charAt(i));
	    				next = i+1;
	    				if( next  < expr.length()) {
	    					 if(contained.contains(Character.toString(expr.charAt(next)))) {
		    		            Variable value = new Variable(Tokens.toString());
		    		            int spot = vars.indexOf(value);
		    		            tmpfloat = vars.get(spot).value;
			    				numbers.push(tmpfloat);
		    		            Tokens.setLength(0);
	    					}     					
	    				}
	    				else {
	    		            Variable value = new Variable(Tokens.toString());
	    		            int spot = vars.indexOf(value);
	    		            tmpfloat = vars.get(spot).value;
		    				numbers.push(tmpfloat);
	    		            Tokens.setLength(0);    							    					
	    				}
	    			}
	    			else if(Character.isDigit(expr.charAt(i))) {
	    				Tokens.append(expr.charAt(i));
	    				next = i+1;
	    				if( next  < expr.length() ) {
	    					if (contained.contains(Character.toString(expr.charAt(next)))) {
		    					tmpfloat = Integer.parseInt(Tokens.toString());
			    				numbers.push(tmpfloat);
		    					Tokens.setLength(0);
	    					} 
	    					
	    				}
	    				else {
	    					tmpfloat = Float.parseFloat(Tokens.toString());
		    				numbers.push(tmpfloat);
	    					Tokens.setLength(0);
	    				}
	    			}	    				
    	}
    	    	
    	  if(i == expr.length()) {
			while(operations.size() > 0 &&  numbers.size() > 1) {
				solving(operations, numbers);
			}
			if(numbers.size() > 0) {
				answer = numbers.pop(); 
			}
		}
    	return answer;
    }
}

