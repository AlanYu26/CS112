package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	public void build() {
		/** COMPLETE THIS METHOD **/
		String line = sc.nextLine();
		String caseType;
		String close = "";
		int index = 0;
		
		TagNode temp = null;
		TagNode tempmake = null;
		TagNode newc = null;
		TagNode start = null;
		
		Stack<TagNode> tags = new Stack<TagNode>();
		Stack<String> cases = new Stack<String>();
		Stack<TagNode> storage = new Stack<TagNode>();
		
		if(line.substring(0,1).contains("<")) { // begins with html tag
			while(close.contains(">") == false) {
				index++;
				close = line.substring(index, index + 1);
			}
			start = new TagNode(line.substring(1,index), null, null);
			root = start;
			tags.push(root);
			storage.push(root);
			cases.push("zero");
			
		}	else if(line.substring(0,1).contains("<") == false) return;	// is not a proper html file
		
			while(tags.isEmpty() == false) {
				line = sc.nextLine();
				if( line.substring(0,1).contains("<") && line.substring(1,2).contains("/") == false) { // checks first if tag, and if it is checks if it is a closing or not
					caseType = "zero";
				} else if(line.substring(0,1).contains("<")) {
					caseType = "tags";
				} else if(line.substring(0,1).contains("<") != true){
					caseType = "words";
				} else {
					return;
				}
				
				if(caseType.contains("zero") != false ) {
					close = "";
					index = 0;
				while(close.contains(">") != true ) {
					index++;
					close = line.substring(index,index + 1);
				}
				tempmake = new TagNode(line.substring(1,index), null, null);
				temp = storage.peek();
				
			if (cases.peek() == ("tags")) {
				temp.sibling = tempmake;
			} else if(cases.peek() == ("words")) {
				temp.sibling = tempmake;
			} else if(cases.peek() != ("tags")){
				temp.firstChild = tempmake;
			} else if(cases.peek() != "words") {
				temp.firstChild = tempmake;
			}
			
				storage.push(tempmake);
				tags.push(tempmake);
				cases.push("zero");
		} else if(caseType.contains("tags") == true) {
			while(storage.peek().equals(tags.peek()) == false) {
				storage.pop();
			}
				tags.pop();
				cases.push("tags");
		} else if(caseType.contains("tags") == false) {
			temp = storage.peek();
			newc = new TagNode(line, null, null);
			if(cases.peek() != ("zero")) {
				temp.sibling = newc;
			} else {
				temp.firstChild = newc;	
			} 
				cases.push("words");
				storage.push(newc);
		}
	}
}		
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	
	private static void replacing(String oldTag, String newTag, TagNode temproot) {
		
		if(temproot == null) return;
		
		for(TagNode iterate = temproot; iterate != null; iterate = iterate.sibling) {
		
			if(iterate.firstChild != null) {
			  	replacing(oldTag, newTag, iterate.firstChild);
			  } 
			while(iterate.tag.equals(oldTag)) {
				  iterate.tag = newTag;
			  }
		}
	}
	
	public void replaceTag(String oldTag, String newTag) {
		replacing(oldTag, newTag, root);

	}

	private void bolding(String oldTag, TagNode root, ArrayList<TagNode> storage) {
		if(root == null) return;
		
		for(TagNode iterate = root; iterate != null; iterate = iterate.sibling) {
			bolding(oldTag, iterate.firstChild, storage);
			  if(iterate.tag.equals(oldTag)) {
				  storage.add(root);
				  bolding(oldTag, iterate.sibling, storage);
			  }
		}
	}
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		
		ArrayList<TagNode> storage = new ArrayList<TagNode>();
		TagNode temp1 = null;
		TagNode temp2 = null;
		int rownumber = 1; //starts from 1 not 0
		
		bolding("tr", root, storage); // setting up with storage
		
		
		if(storage.isEmpty()) return; // if empty no point on bolding empty
		if(storage.size() < row) return; // check if the row number is even possible
		
		temp1 = storage.get(0);
		
		while(rownumber < row) {
			if(temp1 != null) {
				rownumber += 1;
				temp1 = temp1.sibling;
			}
		}
		for(temp1 = temp1.firstChild; temp1 != null; temp1 = temp1.sibling) {
			temp2 = new TagNode("b", temp1.firstChild, null);
			temp2.firstChild = temp1.firstChild;
			temp1.firstChild = temp2;
			
		}
	}
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	private void casepemb(String target, TagNode root) { // p, em, b
		
		TagNode temp = null;
		
		if(root == null) return; // check if file is full or not
		
		if(root.firstChild != null) {
			if(root.tag.equals(target)) {
			root.tag = root.firstChild.tag;
			if(root.firstChild.sibling != null) {
				temp = root.firstChild;
				while(temp.sibling != null) {
					temp = temp.sibling;
				}
					temp.sibling = root.sibling;
					root.sibling = root.firstChild.sibling;	
				}
				root.firstChild = root.firstChild.firstChild;
			}
		}
		casepemb(target, root.firstChild);
		casepemb(target, root.sibling);
	}
	
	public void removeTag(String tag) { 
		if(tag.equals("ol")) {
			caseolul(tag, root);
		} else if(tag.equals("ul")) {
			caseolul(tag, root);
		} else if(tag.equals("p")) {
			casepemb(tag, root);
		} else if(tag.equals("em")) { 
			casepemb(tag, root);
		} else if(tag.equals("b")) {
			casepemb(tag, root);
		} else {
			return;
		}
	}
	
	private void caseolul(String target, TagNode root) { // ol, ul
		
		TagNode temp = null;
		
		if(root == null) return; // agane check if root has anything in the file
		
		while(root.tag.equals(target) ) {
		if(root.firstChild != null) {
			root.tag = "p";
			for(temp = root.firstChild; temp.sibling != null; temp = temp.sibling) { 
				temp.tag = "p";
				continue;
			}
				temp.tag = "p";
				temp.sibling = root.sibling;
				root.sibling = root.firstChild.sibling;
				root.firstChild = root.firstChild.firstChild;
			}
		}
			caseolul(target, root.firstChild);
			caseolul(target, root.sibling);
	}
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	  public void addTag(String word, String tag) {
          /** COMPLETE THIS METHOD **/
          add(root, word, tag);
         
  }
	  private void add(TagNode root, String word, String tag) {
		  
		  if(root == null) return;
		  
		  add(root.firstChild, word.toLowerCase(), tag);
		  add(root.sibling, word.toLowerCase(), tag);
		  
		  int i = 0;
		  int check;
		  String tagging;
		  String remainder;
		  StringBuilder Stringbuild;
		  
		  if(root.firstChild == null) {
			  while(root.tag.toLowerCase().contains(word)) {
				  String[] words = root.tag.split(" ");
				  check = 1;
				  tagging = "";
				  Stringbuild = new StringBuilder(root.tag.length());
				  for(i = 0; i < words.length; i++) {
					  if(words[i].toLowerCase().matches(word + "[.]?")) { // checks . 
						  check = 2;
						  tagging = words[i];
						 for(int j = i + 1; j < words.length; j++) 
							 Stringbuild.append(words[j] + " " );
							 break;
					  } else if(words[i].toLowerCase().matches(word + "[?]?")) { // checks ?
						  check = 2;
						  tagging = words[i];
						 for(int j = i + 1; j < words.length; j++) 
							 Stringbuild.append(words[j] + " " );
							 break;
					  } else if(words[i].toLowerCase().matches(word + "[!]?")) { // checks !
						  check = 2;
						  tagging = words[i];
						 for(int j = i + 1; j < words.length; j++) 
							 Stringbuild.append(words[j] + " " );
							 break;
					  } else if(words[i].toLowerCase().matches(word + "[,]?")) { // checks ,
						  check = 2;
						  tagging = words[i];
						 for(int j = i + 1; j < words.length; j++) 
							 Stringbuild.append(words[j] + " " );
							 break;
					  } else if(words[i].toLowerCase().matches(word + "[;]?")) { // checks ;
						  check = 2;
						  tagging = words[i];
						 for(int j = i + 1; j < words.length; j++) 
							 Stringbuild.append(words[j] + " " );
							 break;
					  } else if(words[i].toLowerCase().matches(word + "[:]?")) { // checks :
						  check = 2;
						  tagging = words[i];
						 for(int j = i + 1; j < words.length; j++) 
							 Stringbuild.append(words[j] + " " );
							 break;
					  }
				  }
				  if(check == 1) return; // only if item was not found
				  
				  remainder = Stringbuild.toString().trim();
				  
				  if(i == 0) {
					  root.firstChild = new TagNode(tagging, null, null);
					  root.tag = tag;
					  if(remainder.equals("") == true) {
						  return; // done with it
					  }
						  else if(remainder.equals("") == false) {
						  root.sibling = new TagNode(remainder, null, root.sibling);
						  root = root.sibling;
						  }
					  } else {
						  TagNode NodeTag = new TagNode(tagging, null, null);
						  TagNode NodeTag2 = new TagNode(tag, NodeTag, root.sibling);
						  root.sibling = NodeTag2;
						  root.tag = root.tag.replaceFirst(" " + tagging,  "");
						  if(remainder.equals("") == true) {
							  return; // done with it
						  } else if(remainder.equals("") == false) {
							  root.tag = root.tag.replace(remainder,  "");
							  NodeTag2.sibling = new TagNode(remainder, null, NodeTag2.sibling);
							  root = NodeTag2.sibling;
					  }
				  }
			  }
		  }
	  }
	
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}
	
	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|----");
			} else {
				System.out.print("     ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}

/*
 * for(TagNode ptr = root; ptr!= null; ptr = ptr.sibling) {
 * if(ptr.tag.equals(oldTag) {
 * ptr.tag = newTag;
 * }
 * if(ptr.firstChild != null) {
 * call method again with whatever arguements so:
 * 	method(ptr.firstChild, oldTag, NewTag);
 * }
 * }
 * */
