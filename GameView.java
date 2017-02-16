package com.example.joshross.myfirstapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by joshross on 2/7/17.
 */

public class GameView extends View {

    protected boolean actionDown = false;
    protected boolean actionMove = false;
    protected boolean actionUp = false;

    protected float nextX, nextY;

    //Stores the original points
    protected Point[] points;

    //Stores the points in order of the shortest path
    protected ArrayList<Point> shortPath;

    //Stores the previous lines drawn
    protected static ArrayList<Line> lines = new ArrayList<Line>();
    protected static int tail = -1;

    //Stores the previous points of the current line being drawn
    protected ArrayList<Point> currLine;

    //Stores the initial and end point for the current line being drawn
    protected Point initialPoint = new Point();
    protected Point endPoint = new Point();

    //Tells whether or not the line being drawn can be a valid line
    protected boolean isLine = false;
    protected boolean isFinished = false;

    //Keep track of the number of points
    protected static int spinVal;
    protected int numPoints;

    //Used for making Toasts
    protected Context context;

    public GameView(Context c){
        super(c);
        context = c;
        setBackgroundResource(R.drawable.campus);
        numPoints = spinVal;
        points = setPoints(numPoints, c);
        shortPath = ShortestPath.shortestPath(points);
    }

    public GameView(Context c, AttributeSet a){
        super(c,a);
        context = c;
        setBackgroundResource(R.drawable.campus);
        numPoints = spinVal;
        points = setPoints(numPoints, c);
        shortPath = ShortestPath.shortestPath(points);
    }

    public static void setSpinVal(int n) {
        spinVal = n;
    }

    public static boolean undo() {
        if (tail >= 0) {
            lines.remove(tail);
            tail--;
            return true;
        }
        return false;
    }

    public static boolean clear() {
        if (tail >= 0) {
            lines.clear();
            tail = -1;
            return true;
        }
        else {
            return false;
        }
    }

    public void onDraw(Canvas canvas){

        //Set the paint for the points
        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setStrokeWidth(25);

        //Draw the points on the canvas
        for (int i = 0; i < points.length; i++) {
            canvas.drawPoint(points[i].x, points[i].y, p);
        }

        //Draw the previous lines on the canvas
        p.setStrokeWidth(15);
        for (int i = 0; i < lines.size(); i++) {
            Line curr = lines.get(i);
            Point p1 = curr.getP1();
            Point p2 = curr.getP2();
            canvas.drawLine(p1.x, p1.y, p2.x, p2.y, p);
        }

        //Initial movement when the mouse is first put down
        //Checks if the initial point of the mouse is one of the points and if so it stores that point
        if(actionDown){
            p.setColor(Color.YELLOW);
            p.setStrokeWidth(15);
            currLine = new ArrayList<Point>();
            canvas.drawPoint(nextX, nextY, p);
            currLine.add(new Point((int) nextX, (int) nextY));
            actionDown = false;

            for (int i = 0; i < numPoints; i++) {
                if (Math.abs(points[i].x - nextX) <= 25 && Math.abs(points[i].y - nextY) <= 25) {
                    initialPoint.set(points[i].x, points[i].y);
                    isLine = true;
                    break;
                }
            }
        }

        if(actionMove){
            p.setColor(Color.YELLOW);
            p.setStrokeWidth(15);
            int index = 0;
            Point currPoint;
            Point nextPoint;
            while (index < currLine.size() - 1) {
                currPoint = currLine.get(index);
                nextPoint = currLine.get(index + 1);
                canvas.drawLine(currPoint.x, currPoint.y, nextPoint.x, nextPoint.y, p);
                index++;
            }
            canvas.drawLine(currLine.get(index).x, currLine.get(index).y, nextX, nextY, p);
            currLine.add(new Point((int) nextX, (int) nextY));
            actionMove = false;
        }

        if(actionUp) {
            actionUp = false;
            if (isLine) {
                for (int i = 0; i < numPoints; i++) {
                    if (Math.abs(points[i].x - nextX) <= 25 && Math.abs(points[i].y - nextY) <= 25) {
                        endPoint.set(points[i].x, points[i].y);
                        isFinished = true;
                        break;
                    }
                }
            }
            if (isFinished) {
                Line newLine = new Line(initialPoint, endPoint);
                boolean isNewLine = true;
                for (int i = 0; i < lines.size(); i++) {
                    if (newLine.equals(lines.get(i))) {
                        isNewLine = false;
                        break;
                    }
                }
                if (isNewLine) {
                    lines.add(newLine);
                    canvas.drawLine(initialPoint.x, initialPoint.y, endPoint.x, endPoint.y, p);
                    tail++;
                }
            }

            isLine = false;
            isFinished = false;
            initialPoint = new Point();
            endPoint = new Point();

            if (lines.size() == numPoints) {
                int duration = Toast.LENGTH_LONG;
                CharSequence text;
                if (checkForCircuit(lines)) {
                    if (getPercentOff(lines) == 0 || checkForShortestPath(lines)) {
                        //Display message to user telling them they got the shortest path
                        text = "Congrats! You Have Found The Shortest Path!";
                    }
                    else {
                        int percent = getPercentOff(lines);
                        //Display message to user telling them by what percent off their path is
                        text = "Close! Your Path Is Off By " + percent + "% Trying Hitting The Undo" +
                                " Or Clear Buttons To Fix It";
                    }
                }
                else {
                    //Display message to the user saying they have formed an invalid circuit
                    text = "This Is An Invalid Circuit! Try Hitting The Undo Or Clear Buttons To Fix It";
                }
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

        }

//        //On action up we need to do the following:
//        // 1. Erase the Yellow line that has been drawn !
//        // 2. Check if two points are being connected !
//        // 2a. If the two points are not being connected then do nothing !
//        // 2b. If the two points are already connected then do nothing, otherwise move to step 3 !
//        // 3. Draw a straight red line between the 2 connected points !
//        // 4. If the number of lines == the number of points make sure a circuit has been formed !
//        // 4a. If a circuit has not been formed then present a message to the user !
//        // 4b. If a circuit has been formed proceede to step 5 !
//        // 5. Check if the circuit is the shortest path !
//        // 5a. If the circuit is the shortest path then present a congrats message to the user !
//        // 5b. If the circuit is not the shortest path then find by what % of it is and tell the user this !

    }

    private int getPercentOff(ArrayList<Line> circuit) {
        int distShort = 0;
        //Calculate the length of the shortPath
        for (int i = 0; i < shortPath.size() - 1; i++) {
            Point p1 = shortPath.get(i);
            Point p2 = shortPath.get(i + 1);
            int x = (int) Math.pow(p2.x - p1.x, 2);
            int y = (int) Math.pow(p2.y - p1.y, 2);
            distShort += (int) Math.sqrt(x + y);
        }

        Point start = shortPath.get(0);
        Point end = shortPath.get(shortPath.size() - 1);
        distShort += (int) Math.sqrt(Math.pow(end.x - start.x, 2) + Math.pow(end.y - start.y, 2));


        int distCirc = 0;
        //Calculate the length of the circuit
        for (int i = 0; i < circuit.size(); i++) {
            Point p1 = circuit.get(i).getP1();
            Point p2 = circuit.get(i).getP2();
            int x = (int) Math.pow(p2.x - p1.x, 2);
            int y = (int) Math.pow(p2.y - p1.y, 2);
            distCirc += (int) Math.sqrt(x + y);
        }

        if (distCirc == distShort) {
            return 0;
        }
        else {
            return (int) (((distCirc - distShort) / (1.0 * distShort)) * 100);
        }
    }

    private boolean checkForCircuit(ArrayList<Line> circuit) {
        int[] pointReach = new int[numPoints];
        for (int i = 0; i < circuit.size(); i++) {
            for (int j = 0; j < points.length; j++) {
                if (circuit.get(i).getP1().equals(points[j])) {
                    pointReach[j] += 1;
                }
                if (circuit.get(i).getP2().equals(points[j])) {
                    pointReach[j] += 1;
                }
            }
        }

        for (int i = 0; i < pointReach.length; i++) {
            if (pointReach[i] != 2) {
                return false;
            }
        }
        return true;
    }

    //Need to find starting point for path and match that with starting point for shortPath
    //Need to check both counter clockwise and clockwise
    private boolean checkForShortestPath(ArrayList<Line> path) {
        int startInd = 0;
        for (int i = 0; i < shortPath.size(); i++) {
            if (shortPath.get(i).equals(path.get(0).getP1())) {
                startInd = i;
            }
        }

        //Check Clockwise
        boolean cwCheck = true;
        int cw = startInd;
        for (int count = 0; count < path.size(); count++) {
            if (shortPath.get(cw).equals(path.get(count).getP1())) {
                cw++;
                if (cw == shortPath.size()) {
                    cw = 0;
                }
            }
            else {
                cwCheck = false;
                break;
            }
        }

        if (cwCheck) {
            return true;
        }

        //Check Counter Clockwise
        boolean ccwCheck = true;
        int ccw = startInd;
        int pathInd = 0;
        for (int count = 0; count < path.size(); count++) {
            if (shortPath.get(ccw).equals(path.get(pathInd).getP1())) {
                ccw++;
                pathInd--;
                if (ccw == shortPath.size()) {
                    ccw = 0;
                }
                if (pathInd == -1) {
                    pathInd = path.size() - 1;
                }
            }
            else {
                ccwCheck = false;
                break;
            }
        }

        return ccwCheck;
    }

    public Point[] setPoints(int n, Context c) {
        Point[] origins = new Point[n];
        DisplayMetrics displayMetrics = c.getResources().getDisplayMetrics();
        double width = displayMetrics.widthPixels;
        double height = displayMetrics.heightPixels;

        for (int i = 0; i < n; i++) {
            int randX = (int) (Math.random() * width);
            int randY = (int) (Math.random() * height * 0.75);
            origins[i] = new Point(randX, randY);
        }
        return origins;
    }

    public boolean onTouchEvent(MotionEvent e) {
        if(e.getAction() == MotionEvent.ACTION_DOWN){
            nextX = (int) e.getX();
            nextY = (int) e.getY();
            actionDown = true;
            invalidate();
            return true;
        }

        if(e.getAction() == MotionEvent.ACTION_MOVE) {
            nextX = (int) e.getX();
            nextY = (int) e.getY();
            actionMove = true;
            invalidate();
            return true;
        }

        if(e.getAction() == MotionEvent.ACTION_UP) {
            nextX = (int) e.getX();
            nextY = (int) e.getY();
            actionUp = true;
            invalidate();
            return true;
        }

        return false;
    }
}
