/**
 * Created by kylenosar on 11/20/14.
 */

import java.io.*;
import java.util.*;

public class MazeGen
{
    // statics to encode the file as to which direction i am taking in the maze
   final static int EAST = 1; //0001
   final static int WEST = 4; //0100
   final static int NORTH = 2; //0010
   final static int SOUTH = 8; //1000


    public static int[]mazeDFS(Graph someGraph,int sourceNode)
    {
        Random rand = new Random(); // create a random to use to determine direction traveled between current node and next
        int v = someGraph.numVerts();
        boolean [] visited = new boolean[v]; //keep track of the visited nodes
        int [] parent = new int[v];

        for (int i = 0; i < v; i++)
        {
            visited[i] = false;
        }
            visited[sourceNode] = true;
            parent[sourceNode] = -1;
            int visitCount = 1;

        Stack<Integer> myStack = new Stack<Integer>();
        int s = sourceNode;
        while(visitCount < v)
        {
            ArrayList<Integer> neighbors = someGraph.adjacents(sourceNode);
            ArrayList<Integer> unvisited = new ArrayList<Integer>();

            for(Integer item: neighbors)
            {
                if(!visited[item]) // if the item in the list array hasn't been visited: add to unvisited
                {
                    unvisited.add(item);
                }
            }

            if(unvisited.size() > 0)
            {
                int randomNeighbor = unvisited.get(rand.nextInt(unvisited.size()));
                visited[randomNeighbor] = true;
                visitCount++;
                parent[randomNeighbor] = sourceNode;
                myStack.push(randomNeighbor);
                sourceNode = randomNeighbor;
            }
            else
            {
                sourceNode = myStack.pop();
            }
        }
        return parent;
    }

    public static void main(String[]args)
    {

        int width = 0; // width and height are instanced at zero to allow access outside the try block.
        int height = 0;
        String outFile = "";
        Random rand = new Random();
        if(args.length > 0 && args.length < 4)
        {
            if(Integer.parseInt(args[0]) > 5 && Integer.parseInt(args[1]) > 5)
            {
                try
                {
                    width = Integer.parseInt(args[0]); // reads in the width
                    height = Integer.parseInt(args[1]); // reads in the height
                    outFile = args[2]; // file to write to
                } catch (Exception e)
                {
                    System.out.println("Sorry but there was an error parsing the arguments. \n Please try again");
                    System.exit(1);
                }
            }
            else
            {
                System.out.println("your maze had bogus dimensions");
            }
        }


        Graph graph = new MazeGraph(width,height);

        int start = rand.nextInt(graph.numVerts());
        int [] parent = mazeDFS(graph,start);

        int cells[][] = new int[height][width];
        for(int r = 0; r < height; r++ )
        {
            for(int c = 0; c < width; c++)
            {
                cells[r][c] = 0xF; // the bit code for 1111
            }
        }

        for(int r = 0; r < height; r++)
        {
            for(int c = 0; c < width; c++)
            {
                int v = r * width + c;
                int w = parent[v];

                if(w >= 0) // if current node has a parent
                {
                    int row = w / width;
                    int column = w % width;
                    if(column == c+1) // if going right
                    {
                        cells[r][c] &= ~EAST; //clear east
                        cells[row][column] &= ~WEST; //clear west
                    }
                    if(column == c-1) // if going left
                    {
                        cells[r][c] &= ~WEST;
                        cells[row][column] &= ~EAST;
                    }
                    if(row == r+1) // if going down
                    {
                        cells[r][c] &= ~SOUTH; // clear south
                        cells[row][column] &= ~NORTH; // clear north
                    }
                    if(row == r-1) // if going up
                    {
                        cells[r][c] &= ~NORTH; // clear northl
                        cells[row][column] &= ~SOUTH; //clear south
                    }
                }
            }
        }

        int startingRow = rand.nextInt(height); //store beginning row of the maze
        int endingRow = rand.nextInt(height); // and store the end of the maze

        // last thing, remove the walls from start to finish
        cells[startingRow][0] &= ~WEST; // clear the west wall to enter the maze
        cells[endingRow][width-1] &= ~EAST; //clear the ending wall

        try
        {
           PrintStream myStream = new PrintStream(new File(outFile));
            myStream.println(width + " " + height); // dimensions of the maze
            for (int r = 0; r < height; r++)
            {
                for (int c = 0; c < width; c++)
                {
                    myStream.print(cells[r][c] + " ");
                }
                myStream.println();
            }
            myStream.close(); // close the stream
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Im sorry but the file you specified cannot be located, \n" +
                    "Please re run the program and try again! \n ERROR: "+e);
            System.exit(1);
        }



    }
}
