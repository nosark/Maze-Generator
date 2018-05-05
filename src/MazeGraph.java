/**
 * Created by kylenosar on 11/20/14.
 */
import java.util.*;
import java.io.*;

public class MazeGraph implements Graph {

    private int width,height; // keeps track of rows and columns in maze
    // vertices = v = r * W + c
     // for |v| use Math.abs(v) to get the absolute value
    // to get grid position for row , row = v/W; for column , column = v % W

    private int [] dx = {1,-1,0,0}; // change in x
    private int [] dy = {0,0,1,-1}; //change in y

    // constructor: creates the maze with specified dimensions
    public MazeGraph(int W,int H) {
        this.width = W;
        this.height = H;
    }
    @Override
    public int numVerts() {return width * height;} // returns the num of vertices based on graph dimensions

    @Override
    public ArrayList<Integer> adjacents(int vertices) {
        int column =  vertices % width;
        int row = vertices / width;

        ArrayList<Integer> adj = new ArrayList<Integer>();
        for(int i = 0; i < 4; i++) {
            int someRow = row + dx[i];
            int someColumn = column + dy[i];

            if(someRow >= 0 && someRow < height && someColumn >=0 && someColumn < width) {
                adj.add(someRow * width + someColumn); // handles the encoding
            }
        }
        return adj; //returns the list of possible directional choices
    }
}
