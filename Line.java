package com.example.joshross.myfirstapp;

import android.graphics.Point;

/**
 * Created by joshross on 2/11/17.
 */

public class Line {

    protected Point p1;
    protected Point p2;

    public Line(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Point getP1() {
        return this.p1;
    }

    public Point getP2() {
        return this.p2;
    }

    public void setPoints(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Line) {
            Line ln = (Line) o;
            if (this.p1.equals(ln.getP1()) && this.p2.equals(ln.getP2())) {
                return true;
            }
            else if (this.p1.equals(ln.getP2()) && this.p2.equals(ln.getP1())) {
                return true;
            }
        }
        return false;
    }

}
