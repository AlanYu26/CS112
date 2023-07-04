package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {

	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;

	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;

	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}

	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 *
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile)
	throws FileNotFoundException {
		//use keywordsIndex.put dawg

		String temp = "";
		HashMap<String, Occurrence> freq = new HashMap<String, Occurrence>();
		Scanner sc = new Scanner(new File(docFile));


		while(sc.hasNext()) {
			String word = sc.next();
			word = getKeyword(word);
			temp = word;
			if(temp == null) {
				continue; //not a keyword
			} if(freq.containsKey(temp)) {
				freq.replace(temp, freq.get(temp), new Occurrence(docFile, freq.get(temp).frequency + 1));
				} else {
				freq.put(temp, new Occurrence(docFile, 1));
				}
		}
		sc.close();



		System.out.println(freq.size());

		return freq;
	}

	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table.
	 * This is done by calling the insertLastOccurrence method.
	 *
	 * @param kws Keywords hash table for a document
	 */

	public void mergeKeywords(HashMap<String,Occurrence> kws) { // TODO

		Set<String> keywords = kws.keySet();
		Iterator<String> iterate = keywords.iterator();

		for(;iterate.hasNext();) {

			String target = iterate.next();
			Occurrence occurrences = kws.get(target);
			ArrayList<Occurrence> times = keywordsIndex.get(target);
			if(times != null){
				continue;
			}
			if(times == null) { // first occurrence, add to map
				times = new ArrayList<Occurrence>();
				keywordsIndex.put(target, times);
			}

			times.add(occurrences);
			insertLastOccurrence(times);
		}
		System.out.println(keywordsIndex.size());

	}
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 *
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 *
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 *
	 * See assignment description for examples
	 *
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 * @throws FileNotFoundException
	 */
	public String getKeyword(String word) {
		word = word.toLowerCase(); // ignore case baybeeee
		String temp = "";
		String temp2 = "";
		ArrayList <Character> c = new ArrayList<Character>();

		if(word.length() <= 1) {
			return null;
		}
		if(word.substring(0,1).contains("(")) {
			return null;
		}
		if(word.substring(word.length() - 2, word.length() - 1).contains(")")) {
			return null;
		}

		for(int i = 0; i < word.length(); i++) { // checks numbers and only these punctuation cases
			if(!Character.isAlphabetic(word.charAt(i))) {
				      if(!(word.charAt(i) == ','
						|| word.charAt(i) == '.'
						|| word.charAt(i) == '?'
						|| word.charAt(i) == ':'
						|| word.charAt(i) == ';'
						|| word.charAt(i) == '!')) {
					return null;
				}
			}
		}
		int j = 0;
		while(j < word.length()) {
			c.add(word.charAt(j));
			j++;// store all characters into the list and compare them one at a time later
		}

		for(int i = 0; i < c.size(); i++) {
			        if(c.get(i) == ','
					|| c.get(i) == '.'
					|| c.get(i) == '?'
					|| c.get(i) == ':'
					|| c.get(i) == ';'
					|| c.get(i) == '!') { // gathers punctuations
				temp += c.get(i);
			} else { // else gather the letters
				temp2 += c.get(i);
			}
		}

		if(temp2.length() <= 1) {
			return null; // omits initials and single letter words as well as single letter noise words
		}

		for(int i = 0; i < noiseWords.size(); i++) {
			if(noiseWords.contains(temp2)) {
				return null;
			}
		}

		if((temp2 + temp).equals(word) == false) { //checks if the word and the punctuation make the original world, if so returns word minus punctuations
			return null;
		} else {
			return temp2;
		}
	}



	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 *
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */


	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		if(occs.size() < 2) { return null; } // checks size if less than 2, returns null

		ArrayList<Integer> index = new ArrayList<>();
		ArrayList<Integer> arrays = new ArrayList<>();

		int lo = 0;
		int hi = occs.size() - 2;
		int value = occs.get(occs.size() - 1).frequency;

		for(int i = 0; i < occs.size(); i++) {
			arrays.add(occs.get(i).frequency); // store all into array list
		}

		while(hi >= lo) { // begin binary search
			int middle = (hi + lo) / 2;
			index.add(middle);
			if(arrays.get(middle) < value) {
				hi = middle - 1;
				continue;
			} if(arrays.get(middle) > value) {
				lo = middle + 1;
				if(hi <= middle) {
					middle = middle + 1;
					continue;
				}
			} else {
				break;
			}

		}

		int next = occs.get(occs.size() - 2).frequency;
		if(index.size() == 0) {
			return null;
	}
		if((value < next) == false){
			Occurrence temp = occs.remove(occs.size() - 1);
			int position = index.get(index.size() - 1);
			occs.add(position, temp);
		}

		for(int i = 0; i < index.size(); i++) {
			System.out.println(index.get(i));
		}
		for(int i = 0; i < arrays.size(); i++) {
			System.out.print(arrays.get(i) + " ");
		}
		return index;
	}

	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 *
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile)
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}

		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}

	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies.
	 *
	 * Note that a matching document will only appear once in the result.
	 *
	 * Ties in frequency values are broken in favor of the first keyword.
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same
	 * frequency f1, then doc1 will take precedence over doc2 in the result.
	 *
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 *
	 * See assignment description for examples
	 *
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches,
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2)
	{
		ArrayList<String> output = new ArrayList<String>();
		ArrayList<Occurrence> first = new ArrayList<Occurrence>();
		ArrayList<Occurrence> second = new ArrayList<Occurrence>();
		ArrayList<Occurrence> total = new ArrayList<Occurrence>();
		int firstloop = 0;
		int secondloop = 1;
		int dup1 = 0;
		int dup2 = 0;

		if (keywordsIndex.containsKey(kw1)) { //store the first word to fine
			first = keywordsIndex.get(kw1);
		}

		if (keywordsIndex.containsKey(kw2)) { //store the first word to fine
			second = keywordsIndex.get(kw2);
		}

		total.addAll(first);
		total.addAll(second);

		if (first.isEmpty() && second.isEmpty()) { //no need to check through if both is empty list
			return null;
			} else if(first.isEmpty() == false && second.isEmpty() == false) {

			while(firstloop < total.size() - 1) {

				for(secondloop = 1; secondloop < total.size() - firstloop; secondloop++) {

					if(total.get(secondloop - 1).frequency > total.get(secondloop).frequency) {
						continue;
					}

					if(total.get(secondloop - 1).frequency < total.get(secondloop).frequency) {
						Occurrence temp = total.get(secondloop - 1);
						total.set(secondloop - 1, total.get(secondloop));
						total.set(secondloop, temp);
						continue;
					}
				}
				firstloop++;
				continue;
			}

			while(dup1 < total.size() - 1) { // remove DOOOOOOOOOPS dawg
				dup2 = dup1 + 1;
				while(dup2 < total.size()) {
					if(total.get(dup1).document == total.get(dup2).document) {
						total.remove(dup2);
						continue;
					}
					dup2++;
				}
				dup1++;
			}
		}

		// Top 5
		while (total.size() > 5) {
			total.remove(total.size() - 1);
		}

		System.out.println(total);

		for(Occurrence oc : total) {
			output.add(oc.document);
		}

		return output;
	}
}