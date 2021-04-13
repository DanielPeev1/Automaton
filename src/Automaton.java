import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.HashMap;
import java.io.File;

public class Automaton {
	
	int N, K, M;
	ArrayList <Arrow> arrows = new ArrayList <Arrow> ();
	HashSet <Integer> endNodes = new HashSet <Integer> ();
	//За улеснение на обхожданията
	ArrayList <Arrow> nodes [];
	
	public Automaton (Scanner in) {
		
		N = in.nextInt();
		K = in.nextInt ();
		in.nextLine ();
		for (int i = 0; i < K; i ++) {
			endNodes.add (in.nextInt ());
			
		}
		M = in.nextInt ();
		nodes = new ArrayList [N];
		
		for (int i = 0; i < N; i ++) {
			nodes [i] = new ArrayList <Arrow> ();
		}
		for (int i = 0; i < M; i ++) {
			int f = in.nextInt ();
			int t = in.nextInt ();
			String v = in.nextLine ();
			Arrow a = new Arrow (f, t, v);
			arrows.add(a);
			nodes [f].add(a);
		}
	}
	public boolean isPos (int a) {
		return endNodes.contains(a);
	}
	public boolean isWord (String s) {
		
		return DFS (s, 0);
	}
	public boolean DFS (String s, int curr) {
		
		if (s.length () == 0 && endNodes.contains(curr)) return true;
		else if (s.length() == 0) return false;
		int cc = 0;
		for (int i = 0; i < nodes [curr].size (); i ++) {
			
			int to = nodes [curr].get(i).getTo ();
			String val = nodes [curr].get(i).getVal ();
			
			if (val.substring(1,2).equals(s.substring(0, 1))) {
				cc ++;
				if (DFS (s.substring(1, s.length()), to)) {
				return true;
				}
			}
		}
		 return false;
	}
	public void showAllWords (int len) {
		
		HashSet <String> usedWords = new HashSet <String> ();
		ArrayList <String> list = new ArrayList <String> ();
		DFSGen (list, 0, len, "", usedWords);
		System.out.println(list);
	}
	public void DFSGen (ArrayList <String> list, int curr, int len, String currS, HashSet <String> usedWords) {
		
		if (currS.length() == len && !usedWords.contains(currS) && endNodes.contains(curr)) {
			list.add(currS);
			usedWords.add(currS);
		}
		else if (currS.length() > len) {}
		else {
			for (int i = 0; i < nodes [curr].size (); i ++) {
				DFSGen (list, nodes [curr].get(i).getTo(), len, currS + nodes [curr].get(i).getVal().substring(1,2), usedWords);
			}
		}
	}
	public boolean isDetermined () {
		HashSet <String> arrowsFromCurrentNode = new HashSet <String> ();
		for (int i = 0; i < nodes.length; i ++) {
			for (int j = 0; j < nodes [i].size (); j ++) {
				if (arrowsFromCurrentNode.contains (nodes[i].get(j).getVal())) return false;
				arrowsFromCurrentNode.add(nodes [i].get(j).getVal());
			}
			arrowsFromCurrentNode.clear();
		}
		return true;
	}
	static ArrayList <String> distinctArrows = new ArrayList <String> ();
	static int currentHighest = 0;
	static ArrayList <Integer> finalNodesList = new ArrayList <Integer> ();
	
	public void generateDetermined () {
		
		if (endNodes.contains(0)) finalNodesList.add(0);
		/* Запазвам всеки един от върховете на новия граф като map от String във формата на n1|n2|...|,
		 *  където n1, n2....са номерата на върховете на недетерминирания граф, които са включени в един 
		 *  нов връх на детерминирания граф, към Integer, който е номера на върха в новия граф.
		 */
		HashMap <String, Integer> map = new HashMap <String, Integer> ();
		HashSet <String> distinctMap = new HashSet <String> ();
		ArrayList <Arrow> ans = new ArrayList <Arrow> ();
		HashSet <Integer> aux = new HashSet <Integer> ();
		
		for (int i = 0; i < arrows.size (); i ++) {
			
			String val = arrows.get(i).getVal();
			
			if (!distinctMap.contains(val)) {
				distinctMap.add(val);
				distinctArrows.add(val);//Множеството от различните букви, които се съдържат в думите, генерирани от автомата
			}
		}
		distinctArrows.sort((String s1, String s2) -> s1.compareTo(s2));
		map.put("0|", 0);
		detAux (map, ans, 0, "0|");
		
		Collections.sort(finalNodesList);
		ArrayList <Integer> actualLastNodes = new ArrayList <Integer> ();//Крайните върхове в новия, детерминиран автомат
		for (int i = 0; i < finalNodesList.size (); i ++) {
			if (!aux.contains(finalNodesList.get(i)))  {
				aux.add(finalNodesList.get(i));
				actualLastNodes.add(finalNodesList.get(i));
			}
		}
		System.out.print(currentHighest + 1 + " " + actualLastNodes.size ());
		System.out.println();
		
		for (int i = 0; i < actualLastNodes.size (); i ++) {
			System.out.print(actualLastNodes.get(i) + " ");
		}
		aux.clear ();
		System.out.println();
		System.out.println(ans.size ());
		for (int i = 0; i < ans.size (); i ++) {
			System.out.println(ans.get(i));
		}
	}
	void detAux (HashMap <String, Integer> map, ArrayList <Arrow> ans, int curr, String currS) {
		String next = "";
		String temp = "";
		ArrayList <Integer> nextNode = new ArrayList <Integer> ();
		
		for (int i = 0; i < distinctArrows.size (); i ++) {
			
			if (currS.length()== 0) break;
			temp += currS.charAt(0);
			
			for (int j = 1; j < currS.length (); j ++) {
				
				if (currS.charAt(j) != '|' && j != currS.length() - 1) {
					temp += currS.charAt (j);//Извличам номерата на върховете от стария граф от низа във формата n1|n2...|
				}
				else if (temp.length()!= 0){
					int cuPr = Integer.parseInt (temp);
					if (endNodes.contains(cuPr)) finalNodesList.add(map.get(currS));// Ако един от тези върхове е краен, то и новия, който ги обединява е краен
					for (int k = 0; k < nodes [cuPr].size (); k ++) {
						if (nodes [cuPr].get (k).getVal().equals(distinctArrows.get(i))) {
							if (!nextNode.contains(nodes [cuPr].get(k).getTo())) nextNode.add(nodes [cuPr].get (k).getTo ());
						}
					}
					temp = "";
				}
			}
			temp = "";
			Collections.sort(nextNode);
			HashSet <Integer> auxSet = new HashSet <Integer> ();
			for (int d = 0; d < nextNode.size (); d ++) {
				if (auxSet.contains(nextNode.get(d))) nextNode.remove(d);
				else auxSet.add(nextNode.get(d));
			}
			auxSet.clear();		
			next = "";
			for (int m = 0; m < nextNode.size (); m ++) {
				next += nextNode.get(m);
				if (m <= nextNode.size() - 1) next += "|";
			}
			if (map.containsKey(next)) {
				nextNode.clear();
				ans.add(new Arrow (curr, map.get(next), distinctArrows.get (i)));
			}
			else if (next.length()!=0){
				currentHighest ++;
				map.put(next, currentHighest);
				nextNode.clear();
				ans.add(new Arrow(curr, currentHighest, distinctArrows.get (i)));
				detAux (map, ans, currentHighest, next);
			}
			nextNode.clear();
		}
	}
}
