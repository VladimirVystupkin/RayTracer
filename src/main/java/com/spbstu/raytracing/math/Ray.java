package com.spbstu.raytracing.math;

import com.sun.istack.internal.Nullable;

import java.util.Collection;

/**
 * @author vva
 * @date 16.03.14
 * @description
 */
public class Ray extends Line {


    public Ray(Point3D point1, Point3D point2) {
        super(point1, point2);
    }

    public Ray(Point3D point, Vector directionVector) {
        super(point, directionVector);
    }


    @Nullable
    public Point3D getClosest(Collection<Point3D> points) {
        if (points.size() == 0) {
            return null;
        }
        Point3D crossPoint = null;
        Double minDistance = null;
        for (Point3D localCrossPoint : points) {
            Vector toCrossPoint = new Vector(point, localCrossPoint);
            if (Vector.scalar(toCrossPoint, directionVector) > 0) {
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

    @Nullable
    public Point3D getFurthest(Collection<Point3D> points) {
        if (points.size() == 0) {
            return null;
        }
        Point3D crossPoint = null;
        Double maxDistance = null;
        for (Point3D localCrossPoint : points) {
            Vector toCrossPoint = new Vector(point, localCrossPoint);
            if (Vector.scalar(toCrossPoint, directionVector) > 0) {
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
