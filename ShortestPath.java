package com.example.joshross.myfirstapp;

import android.graphics.Point;
import java.util.ArrayList;
/**
 * This is a bruteforce solution to the Traveling Salesman Problem.
 *
 */
public class ShortestPath {

    /*
     * The input is an array of all the Points in the graph.
     * This method then calculates the distance of the circuit through
     * all permutations of the Points, and then returns an ArrayList
     * containing the Points in their order in the shortest path.
     */
    public static ArrayList<Point> shortestPath(Point[] points) {

        // get the permutations of the indices
        ArrayList<ArrayList<Integer>> indices = permute(points.length);

        double shortest = Double.MAX_VALUE;
        ArrayList<Integer> shortestPathIndices = null;

        for (ArrayList<Integer> p : indices) {
            double total = 0;

            // get the distance between the intermediate points
            for (int i = 0; i < p.size()-1; i++) {
                Point p1 = points[p.get(i)];
                Point p2 = points[p.get(i+1)];
                double dx = p1.x - p2.x;
                double dy = p1.y - p2.y;
                double dist = Math.sqrt(dx*dx + dy*dy);
                total += dist;
            }

            // then need to go back to the beginning
            Point p1 = points[p.get(p.size()-1)];
            Point p2 = points[p.get(0)];
            double dist = dist(p1, p2);
            total += dist;

            // see if this is the shortest  so far
            if (total < shortest) {
                shortest = total;
                shortestPathIndices = p;
            }
        }

        // reconstruct the shortest path using the indices
        ArrayList<Point> shortestPath = new ArrayList<Point>();
        for (int k : shortestPathIndices)
            shortestPath.add(points[k]);


        return shortestPath;
    }


    /*
     * from http://www.programcreek.com/2013/02/leetcode-permutations-java/
     */
    private static ArrayList<ArrayList<Integer>> permute(int vals) {

        int[] num = new int[vals];
        for (int i = 0; i < vals; i++) num[i] = i;

        ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();

        //start from an empty list
        result.add(new ArrayList<Integer>());

        for (int i = 0; i < num.length; i++) {
            //list of list in current iteration of the array num
            ArrayList<ArrayList<Integer>> current = new ArrayList<ArrayList<Integer>>();

            for (ArrayList<Integer> l : result) {
                // # of locations to insert is largest index + 1
                for (int j = 0; j < l.size()+1; j++) {
                    // + add num[i] to different locations
                    l.add(j, num[i]);

                    ArrayList<Integer> temp = new ArrayList<Integer>(l);

                    current.add(temp);

                    // - remove num[i] add
                    l.remove(j);
                    System.gc();
                }
            }

            //result = new ArrayList<ArrayList<Integer>>(current);
            result.clear();
            for (ArrayList<Integer> k : current) {
                result.add(k);
            }
        }

        return result;
    }

    public static double dist(Point p1, Point p2) {
        double dx = p1.x - p2.x;
        double dy = p1.y - p2.y;
        double dist = Math.sqrt(dx*dx + dy*dy);
        return dist;

    }
}
