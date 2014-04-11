package com.spbstu.raytracing.math;

import com.sun.istack.internal.Nullable;
import com.sun.javafx.beans.annotations.NonNull;

import java.util.Collection;

/**
 * 3D Ray class
 *
 * @author vva
 */
public class Ray {


    final Point point;

    final Vector directionVector;

    /**
     * Constructor which makes ray from 2 points
     * @param point1 ray start point
     * @param point2 point to identify ray direction
     */
    public Ray(@NonNull final Point point1, @NonNull final Point point2) {
        point = new Point(point1.x, point1.y, point1.z);
        directionVector = new Vector(point1.x - point2.x, point1.y - point2.y, point1.z - point2.z).getNormalized();
    }

    /**
     * Constructor which makes ray from start point and direction vector
     * @param point ray start point
     * @param directionVector ray direction vector
     */
    public Ray(@NonNull final Point point,@NonNull final Vector directionVector) {
        this.point = point;
        this.directionVector = directionVector.getNormalized();
    }

    /**
     * Returns ray start point
     * @return ray start point
     */
    @NonNull
    public Point getPoint() {
        return point;
    }

    /**
     * Returns normalized ray direction vector
     * @return normalized ray direction vector
     */
    @NonNull
    public Vector getDirectionVector() {
        return directionVector;
    }

    /**
     * Returns the closest to ray start point on the ray direction
     * and null if fo every point P in collection angle between ray direction vector
     * and vector {P.x-point.x,P.y-point.y,P.z-point.z} less than 0 where point is ray is start point
     * @param points collection of points to identify the closest
     * @return the closest to ray start point on the ray direction
     */
    @Nullable
    public Point getClosest(@NonNull final Collection<Point> points) {
        if (points.size() == 0) {
            return null;
        }
        Point crossPoint = null;
        Double minDistance = null;
        for (Point localCrossPoint : points) {
            Vector toCrossPoint = new Vector(point, localCrossPoint);
            if (Vector.scalar(toCrossPoint, directionVector) >= 0) {
                if (minDistance == null) {
                    crossPoint = localCrossPoint;
                    minDistance = toCrossPoint.length();
                } else {
                    double localCrossPointDistance = toCrossPoint.length();
                    if (minDistance > localCrossPointDistance) {
                        minDistance = localCrossPointDistance;
                        crossPoint = localCrossPoint;
                    }
                }
            }
        }
        return crossPoint;
    }

    /**
     * Returns the furthest to ray start point on the ray direction
     * and null if fo every point P in collection angle between ray direction vector
     * and vector {P.x-point.x,P.y-point.y,P.z-point.z} less than 0 where point is ray is start point
     * or collection of points is empty
     * @param points collection of points to identify the furthest
     * @return the furthest to ray start point on the ray direction
     */
    @Nullable
    public Point getFurthest(@NonNull final Collection<Point> points) {
        if (points.size() == 0) {
            return null;
        }
        Point crossPoint = null;
        Double maxDistance = null;
        for (Point localCrossPoint : points) {
            Vector toCrossPoint = new Vector(point, localCrossPoint);
            if (Vector.scalar(toCrossPoint, directionVector) >= 0) {
                if (maxDistance == null) {
                    crossPoint = localCrossPoint;
                    maxDistance = toCrossPoint.length();
                } else {
                    double localCrossPointDistance = toCrossPoint.length();
                    if (maxDistance < localCrossPointDistance) {
                        maxDistance = localCrossPointDistance;
                        crossPoint = localCrossPoint;
                    }
                }
            }
        }
        return crossPoint;
    }
}
