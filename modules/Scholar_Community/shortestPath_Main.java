package org.apache.lucene.demo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.text.html.HTMLDocument.Iterator;
import javax.xml.crypto.Data;

/*
 * Author: ywei
 * Function: 
 * Time: 1/10/2013
 * Refer to:
 * Thanks to:
 */
	
public class shortestPath_Main {
	
	final static String _SPLIT = "\t";
	final static int PATH_LENGTH = 8;
	
	public static String _scholarStr;
	public static String[] _scholarArr;
	public static int _scholarlength;
	public static String _scholarFile;
	
	public static String[] _single_scholar;
	
	//存放学者关系数组
	public static int[][] _scholarMatrix;
	
	public shortestPath_Main(String scholarFile) throws IOException{
		_scholarFile = scholarFile;
		parseScholarMatrx(_scholarFile);
	}
	
	public static String[] getScholarArr(){
		return _scholarArr;
	}
	
	public static int getScholarlength(){
		return _scholarlength;
	}
	
	public static int getScholarInt(String scholar){
		int scholarInt = -1;
		for( int i = 0; i < _scholarlength; i++ ){
			if( _scholarArr[i].equals(scholar)){
				scholarInt = i;
				return scholarInt;
			}
		}
		return scholarInt;
	}
	
	public static String getScholarString(int scholarInt){
		String scholarStr = "";
		if( scholarInt < _scholarlength && scholarInt >=0 ){
			scholarStr = _scholarArr[scholarInt];
		}
		return scholarStr;
	}
	
	public static Map<String, Integer> getRelativeScholar(String scholar){
		String[] scholarArr = getScholarArr();
		int scholarInt = -1;
	    Map<String, Integer> relativeScholar = new HashMap<String, Integer>();
	    scholarInt = getScholarInt(scholar);

		if( -1 != scholarInt){
			for( int i = 0; i < _scholarlength; i++ ){
				if( _scholarMatrix[scholarInt][i] > 0){
					System.out.println(scholarArr[i] + "   " + _scholarMatrix[scholarInt][i]);
					relativeScholar.put(scholarArr[i], _scholarMatrix[scholarInt][i]);
				}
			}
		}else{
			System.out.println("There is no relative scholar");
		}
		return relativeScholar;
	}
	
	public static void parseScholarMatrx(String scholarFile) throws IOException{
		
		java.util.Date start = new java.util.Date();
		
		//gain scholar
		File scholarFH = new File(scholarFile);
		FileReader scholarFR = new FileReader(scholarFH);
		BufferedReader scholarBR = new BufferedReader(scholarFR);
		_scholarStr = scholarBR.readLine();
		//System.out.println("HERE" + _scholarStr);
		_scholarArr = _scholarStr.split(_SPLIT);
		_scholarlength = _scholarArr.length;
		
		//for debug
		for( int i = 0; i < _scholarlength; i++){
			//System.out.println(scholarArr[i]);
		}
		
		//init scholar matrix
		_scholarMatrix = new int[_scholarlength][_scholarlength];
		for( int i = 0; i < _scholarlength; i++){
			_scholarMatrix[i][i] = 0;
		}
		for(int i = 0; i < _scholarlength; i++){
        	for(int j = i+1; j < _scholarlength; j++)
        	{
        		_scholarMatrix[i][j] = -1;
        		_scholarMatrix[j][i] = -1;
        	}
		}
		
		//Create scholar matrix
		String scholarRelationStr = "";
		String[] scholarScore;
		int flag = 0;
		while( null != (scholarRelationStr = scholarBR.readLine()) ){
			scholarScore = scholarRelationStr.split(_SPLIT);
			for( int j = 0; j < _scholarlength; j++){
				_scholarMatrix[flag][j] = Integer.parseInt(scholarScore[j]);
			}
			flag++;
			//System.out.println(scholarRelationStr);
		}
		
		// print scholar matrix
		for(int i = 0; i < _scholarlength; i++){
        	for(int j = 0; j < _scholarlength; j++)
        	{
        		if( _scholarMatrix[i][j] > 0){
        			//System.out.print(scholarMatrix[i][j]);
        		}
        		//System.out.print(scholarMatrix[i][j]);
        	}
		}
		
		//retrieve resource
		scholarBR.close();
		java.util.Date end = new java.util.Date();
		System.out.println( end.getTime() - start.getTime());
	}
	
	public static int[][] getStandardScholarMatrix(int[][] scholarMatrix){
		int[][] standardScholarMatrix = new int[scholarMatrix[0].length][scholarMatrix[0].length];
		for(int i = 0; i < scholarMatrix[0].length; i++){
        	for(int j = 0; j < scholarMatrix[0].length; j++) {
        		if( scholarMatrix[i][j] > 1) {
        			standardScholarMatrix[i][j] = 1;
        		}
        		else{
        			standardScholarMatrix[i][j] = scholarMatrix[i][j];
        		}
        	}
		}
		return standardScholarMatrix;
	}
	
	//����ѧ����ص�����ѧ��
	public static Map<String, Integer> searchRelativeScholar(String scholar) throws IOException{
		//String scholarFile = "C:/Users/Think/Desktop/author.txt";
		//int[][] scholarMatrix = parseScholarMatrx(_scholarFile);
		Map<String, Integer> relativeScholarResult = getRelativeScholar(scholar);
		return relativeScholarResult;
	}
	
	//��������ѧ�����·��
	public static String[] searchScholarPath(String scholarStart, String scholarEnd) throws IOException{
		
		long startTime = System.currentTimeMillis();
		//int[][] scholarMatrix = parseScholarMatrx(_scholarFile);
		int[][] standardScholarMatrix = getStandardScholarMatrix(_scholarMatrix);
		int start = getScholarInt(scholarStart);
		int end = getScholarInt(scholarEnd);
		int[] path = new int[_scholarlength];
		for ( int i = 0; i < _scholarlength; i++){
			path[i] = -1;
		}
		int flag = 0;
		String[] pathStr = new String[PATH_LENGTH];
		for ( int i = 0; i < PATH_LENGTH; i++){
			pathStr[i] = "";
		}

		if( 1 == shortestPath.shortestDistance(standardScholarMatrix, path, start, end)){	
			//System.out.println("They are relative");
			for( int i = 0; i < _scholarlength; i++ ){
				if( path[i] >= 0 ){
					//System.out.println("scholar path: " + getScholarString(path[i]));
					pathStr[flag++] = getScholarString(path[i]);
				}
			}
			/*if( 0 == flag){
				pathStr[0] = scholarStart;
				pathStr[1] = scholarEnd;
			}else
			{
				pathStr[flag+1] = scholarEnd;
				for( int i = (flag-1); i >= 0; i-- ){
					pathStr[i+1] = pathStr[i];
				}
				pathStr[0] = scholarStart;
			}*/
			
		}
		long endTime = System.currentTimeMillis();
		//System.out.println("Search Time: " + (endTime - startTime));
		return pathStr;
	}
	
	public static void print_RelativeAuthor(){
		for(int i = 0; i < _scholarMatrix[0].length; i++){
        	for(int j = 0; j < _scholarMatrix[0].length; j++) {
        		if( _scholarMatrix[i][j] > 0){
        			System.out.print(_scholarMatrix[i][j] + _SPLIT);
        		}
        	}
        	System.out.println(getScholarString(i));
		}
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		shortestPath_Main sp = new shortestPath_Main("C:/Users/Think/Desktop/author.diagram");
		//int[][] scholarMatrix = parseScholarMatrx("C:/Users/Think/Desktop/author.diagram");
	    Map<String, Integer> result1 = shortestPath_Main.searchRelativeScholar("颜祥林");
	    if( result1.isEmpty() ){
	    	System.out.println("There is no relative scholar!");
	    }else{
	    	java.util.Iterator<Entry<String, Integer>> it = result1.entrySet().iterator();
	    	while( it.hasNext() ){
	    		Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)it.next();
	    		Object key = entry.getKey();
	    		Object value = entry.getValue();
	    		System.out.println(key + "  +  " + value);
	    	}
	    }
	    
	    System.out.println(sp.getScholarInt("耿志杰")+"	"+sp.getScholarInt("王婷婷") + "    " + sp.getScholarInt("杜雯") );
	    System.out.println(sp.getScholarString(24));
	    		
	    /*String[] pathStr = shortestPath_Main.searchScholarPath("耿志杰", "杜雯");
		for( int i = 0; i < pathStr.length; i++ ){
			if( !pathStr[i].equals("") ) {
				//System.out.println();
				System.out.print(pathStr[i] + "->");
			}
		}*/
		//print_RelativeAuthor();
	    int max = 0;
	    String p1 = "";
	    String p2 = "";
	    String[] scholarArr = getScholarArr();
	    for (int k = 22; k < getScholarlength(); k++){
	    	System.out.println("here" + k + "    " + sp.getScholarString(k));
	    	for( int j = 0; j < getScholarlength(); j++){
	    		String[] pathStr = shortestPath_Main.searchScholarPath(scholarArr[k], scholarArr[j]);
	    		if( !pathStr[0].equals("")){
	    			//System.out.println(scholarArr[k] + "    " + scholarArr[j]);
	    			for( int i = 0; i < pathStr.length; i++ ){
	    				if( !pathStr[i].equals("") ) {
	    					//System.out.println();
	    					System.out.print(pathStr[i] + "->");
	    				}
	    			}
	    			if( pathStr.length > max)
	    			{
	    				max = pathStr.length;
	    				p1 = scholarArr[k];
	    				p2 = scholarArr[j];
	    			}
	    			System.out.println();
	    		}
	    	}
	    	
	    }
	    System.out.println(max + "    " + p1 + "    " + p2);
	    
	    
		
		/*String scholarFile = "C:/Users/Think/Desktop/author.txt";
		int[][] scholarMatrix = parseScholarMatrx(scholarFile);
		int[] path = new int[scholarMatrix[0].length];
		
		int[][] standardScholarMatrix = getStandardScholarMatrix(scholarMatrix);
		
		//shortestPath sp = new shortestPath();
		int start = 0;
		int end = 4;
		if( 1 == shortestPath.shortestDistance(standardScholarMatrix, path, start, end)){
			System.out.println(start + "-->" + end);
			System.out.print(end + "   ");
			for(int i = path[end]; i > -1; i = path[i])
				System.out.print( i + "   ");
			System.out.println(start);
		}
		
		//��ȡָ��ѧ�ߵĹ�ϵȦ�Լ���ϵֵ
		Map<String, Integer> relativeScholarResult = getRelativeScholar(scholarMatrix, "��@��");*/
	}
}
