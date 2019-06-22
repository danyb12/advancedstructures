/*
DHANUSH SURESHBABU cs435 8544 miniproject google pagerank
 *
 *
 * */
import java.io.*;
import java.lang.Math;
import java.util.*;



public class PageRank_8544 {
    //setting variables
    static final double error_rate = Math.pow(10,-4);
    static final double d = 0.85;
    static final int max=10;
    static int edge;
    static int[][] Edge;
    static int vertex;
    static double[] Vertex;
    static double[] VertexPrev;
    static int[] C; //outdegrees
    static double rank;

    public PageRank_8544(int iterations, int initialvalue, String filename){
        Boolean isLargeGraph = false;
        String line;
        BufferedReader reader;
        FileReader filer;
        try {
            //reading from file

            filer=new FileReader(filename);
            reader = new BufferedReader(filer);

            String[] res = reader.readLine().split(" ");
            vertex = Integer.parseInt(res[0]);
            edge = Integer.parseInt(res[1]);
            Vertex = new double[vertex];
            VertexPrev = new double[vertex];
            Edge = new int[vertex][vertex];
            C = new int[vertex];
            //if vertex more than 10 pages then print message
            if (vertex > max) {
                initialvalue = -1;
                iterations = 0;
                isLargeGraph = true;


                System.out.println("Over 10 web-pages detected in file. Setting iterations to 0 and initialvalue to -1.");
            }
            //rank for different initialvalue
            if (initialvalue == 0) {
                rank = 0.0;

            } else if (initialvalue == 1) {
                rank = 1.0;

            } else if (initialvalue == -1) {
                rank = 1.0 / vertex;

            } else if (initialvalue == -2) {
                rank = (1.0 / (Math.pow(vertex, .5)));
            }
            line = reader.readLine();
            while (line != null) {
                //parsing file
                res = line.split(" ");
                //get Int from String
                int first = Integer.parseInt(res[0]);
                int second = Integer.parseInt(res[1]);
                Edge[first][second] = 1;
                C[first]++;
                line = reader.readLine();
            }
            reader.close();
            Arrays.fill(VertexPrev, rank);
        }catch(IOException edge){
            edge.getMessage();
        }
        //if not large graph
        if(!isLargeGraph){
            System.out.print("Base   :  0 :");
            for(int i = 0; i < vertex; i++){
                System.out.printf("P[%2d]=%1.6f ", i, VertexPrev[i]);
            }
            System.out.println();
        }
        if(iterations >= 1){
            for(int j = 1; j < iterations+1; j++){
                System.out.printf("Iter    : %2d :", j);
                for(int k = 0; k < vertex; k++){
                    Vertex[k] = calcPageRank(k);
                    System.out.printf("P[%2d]=%1.6f ", k, Vertex[k]);
                }
                System.out.println();
                VertexPrev = Arrays.copyOf(Vertex, vertex);
            }
        }
        else{
            Boolean isDiff = false;
            int i = 0;
            //if not different
            while(!isDiff){
                i++;
                //if not large graph
                if(!isLargeGraph){
                    System.out.printf("Iter   : %2d :", i);

                for(int j = 0; j < vertex; j++){
                    Vertex[j] = calcPageRank(j);

                    System.out.printf("P[%2d]=%1.6f ", j, Vertex[j]);

                }


                    System.out.println();
                }
                isDiff = true;
                for(int j = 0; j < vertex; j++){
                    if(Math.abs(Vertex[j]-VertexPrev[j]) > error_rate){
                        isDiff = false;
                    }
                }
                if(!isDiff){
                    VertexPrev = Arrays.copyOf(Vertex, vertex);
                }
            }
            //if large graph
            if(isLargeGraph){
                System.out.printf("Iter    : %2d\n", i+1);
                for(int k = 0; k < vertex; k++){
                    System.out.printf("P[%2d] = %1.6f\n", k, Vertex[k]);
                }
            }
        }
    }
    //calculation for page rank given in miniproject sheet
    public static double calcPageRank(int v){
        double AdjacentPageRanks = 0.0;
        for(int i = 0; i < vertex; i++){
            if(Edge[i][v] == 1){
                AdjacentPageRanks += VertexPrev[i]/C[i];
            }
        }
        return ((1.0-d)/vertex) + ((double)d)*AdjacentPageRanks;
    }


    public static void main(String[] args) throws IOException{


        int iterations = Integer.parseInt(args[0]);
        int initialvalue = Integer.parseInt(args[1]);
        String filename = args[2];

        PageRank_8544 page=new PageRank_8544(iterations, initialvalue,filename);



    }
}




