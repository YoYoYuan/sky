package org.apache.lucene.demo;

/*
 * Author: ywei
 * Function: to find the shortest distance and path between specified two points in a undirected graph
 * Time: 1/5/2013
 * Refer to: http://sbp810050504.blog.51cto.com/2799422/690803
 * Thanks to: ZhangXiaoqing
 */

public class shortestPath {
	
	public static int flag = 0;
	/**
	 * @param  relationMatrix 节点之间的矩阵
	 *         start 开始节点
	 *         end   目标节点
	 * @return -1  输入参数问题
	 *         0     不可达
	 *         1     找到两节点之间的距离
	 *         path 节点之间的路径
	 */
	public static int shortestDistance(int[][] relationMatrix, int[] path, int start, int end){
		
		long startTime = System.currentTimeMillis();
		//System.out.println("matrix length: " + relationMatrix[0].length);
		if( start == end ){
			//System.out.println("The first item is the same to the end item!");
			return -1;
		}
		
		if( start >= relationMatrix[0].length || start < 0){
			System.out.println( start + " is not in the length scope!");
			return -1;
		}
		
		if( end >= relationMatrix[0].length || end < 0){
			System.out.println( end + " is not in the length scope!");
			return -1;
		}
		
		boolean[] isLabel = new boolean[relationMatrix[0].length];
		int[] indexs = new int[relationMatrix[0].length];
		//path = new int[relationMatrix[0].length]; //所有标号的点的下标集合  
		for(int i = 0; i < path.length; i++)
			path[i] = -1;
		
		int i_count = -1;
		int[] distance = relationMatrix[start].clone();
		int[][] all_path = new int[relationMatrix[0].length][relationMatrix[0].length];
		for( int k = 0; k < relationMatrix[0].length; k++){
			for(int j = 0; j < relationMatrix[0].length; j++){
				all_path[k][j] = -1;
			}
			//System.out.println( "k: " + distance[k]);
			if( distance[k] > 0){
				all_path[k][0] = start;
				all_path[k][1] = k;
			}
		}
		
		int index = start;
		int presentShortest = 0;
		indexs[++i_count] = index;
		isLabel[index] = true;
		
		while (i_count < relationMatrix[0].length-1) {
			double min = Integer.MAX_VALUE;
			for (int i = 0; i < distance.length; i++) {
				if (!isLabel[i] && distance[i] != -1 && i != index) {
					// 如果到这个点有边,并且没有被标号
					if (distance[i] < min) {
						min = distance[i];
						index = i;
					}
				}
			}
			
			if (index == end) {
				break;
			}
			
			isLabel[index] = true;
			//System.out.println(index);
			indexs[++i_count] = index;
			if (relationMatrix[indexs[i_count - 1]][index] == -1 
					|| presentShortest + relationMatrix[indexs[i_count - 1]][index] > distance[index]) {
				presentShortest = distance[index];
			} else{
				presentShortest += relationMatrix[indexs[i_count - 1]][index];
			}
			
			//System.out.println();
			for (int i = 0; i < distance.length; i++) {
				if (distance[i] == -1 && relationMatrix[index][i] != -1) {
					distance[i] = presentShortest + relationMatrix[index][i];
					//path[i] = index;
					for( int m = 0; m < distance.length; m++){
						//System.out.println("index: " + index);
						if( all_path[index][m] < 0){
							all_path[i][m] = i;
							break;
						}
						all_path[i][m] = all_path[index][m];
					}
					//System.out.println("here: " + i + "    " + index);
				} else if(relationMatrix[index][i] != -1  && presentShortest + relationMatrix[index][i] < distance[i]) {
					distance[i] = presentShortest + relationMatrix[index][i];
					for( int m = 0; m < distance.length; m++){
						if( all_path[index][m] <= 0){
							all_path[i][m] = i;
							break;
						}
						all_path[i][m] = all_path[index][m];
					}
					//path[i] = index;
					//System.out.println("there: " + i + "    " + index);
				}
			 }
		}
		
		//两节点之间路径不可达
		if( (distance[end] - distance[start]) <= 0 ){
			//System.out.println("It can not find the avaiable path for " + start + "->" + end + "!");
			return 0;
		}
		
		for(int i = 0; i < distance.length; i++){
			path[i] = all_path[end][i];
		}
		/*
		System.out.println(start+"-->"+end);
	    System.out.print(end+"   ");
	    for(int i = path[end]; i > -1; i = path[i])
	    	System.out.print( i + "   ");
	    System.out.println(start);*/
	    //return distance[end] - distance[start];
		long endTime = System.currentTimeMillis();
		//System.out.println("Find path time: " + (endTime - startTime));
		
	    return 1;
	}
	
	/**
	 * @param args
	 * @Function 这个程序用来求一个图的最短路径矩阵
	*/
	public static int[][] getShortestPathMatrix(int[][] W) {
		int[][] SPM = new int[W.length][W.length];
		int[] path = {};
		for (int i = 0; i < W.length; i++) {
			for (int j = i + 1; j < W.length; j++) {
				SPM[i][j] =shortestDistance(W, path, i, j);
				SPM[j][i] = SPM[i][j];
			}
		}
		return SPM;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		double[][] W1 = {
				{ 0, 1, 0.25, -1, -1, -1 },
				{ 1, 0, 0.5, 0.142857, 0.2, -1 },
				{ 0.25, 0.5, 0, -1, 1, -1 },
				{ -1, 0.142857, -1, 0, 0.333333, 0.5 },
				{ -1, 0.2, 1, 0.333333, 0, 0.166667 },
				{ -1, -1, -1, 0.5, 0.166667, 0 } };
		int[][] W2 = {
				{ 0, 1, 1, -1, -1, -1 },
				{ 1, 0, 1, 1, 1, -1 },
				{ 1, 1, 0, -1, 1, -1 },
				{ -1, 1, -1, 0, 1, 1 },
				{ -1, 1, 1, 1, 0, 1 },
				{ -1, -1, -1, 1, 1, 0 } };
		int[][] W3 = {
				{ 0, 1, -1},
				{ 1, 0, -1},
				{-1, -1, 0} };
		int[] path = new int[W1[0].length];
		int start = 0;
		int end = 5;
		if( 1 == shortestDistance(W2, path, start, end))
		{
			System.out.println(start+"-->"+end);
			System.out.print(end+"   ");
			//for(int i = path[end]; i > -1; i = path[i])
			for(int i = (path.length-1); i > -1; i--)
				System.out.print( path[i] + "   ");
			System.out.println(start);
		}
		/*int[][] D = getShortestPathMatrix(W1);
		for (int i = 0; i < D.length; i++) {
			for (int j = 0; j < D[i].length; j++) {
				System.out.print(D[i][j] + " ");
			}
			System.out.println();
		}*/
	}
}
