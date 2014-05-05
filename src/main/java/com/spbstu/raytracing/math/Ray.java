package com.spbstu.raytracing.math;

import com.spbstu.raytracing.Relation;
import com.spbstu.raytracing.sceneObject.IntersectionInfo;
import com.spbstu.raytracing.sceneObject.SceneObject;



import java.util.*;

/**
 * @author vva
 */
public class Ray {


    final Point point;

    final Vector directionVector;

    public Ray(final Point point1, final Point point2) {
        point = new Point(point1.x, point1.y, point1.z);
        directionVector = new Vector(point2.x - point1.x, point2.y - point1.y, point2.z - point1.z).getNormalized();
    }

    public Ray(final Point point, final Vector directionVector) {
        this.point = point;
        this.directionVector = directionVector.getNormalized();
    }


    public Point getPoint() {
        return point;
    }


    public Vector getDirectionVector() {
        return directionVector;
    }


    public IntersectionInfo getClosest(final Collection<IntersectionInfo> intersectionInfos) {
        if (intersectionInfos.size() == 0) {
            return null;
        }
        IntersectionInfo closestInfo = null;
        Double minDistance = null;
        for (IntersectionInfo info : intersectionInfos) {
            Vector toCrossPoint = new Vector(point, info.getPoint());
            if (Vector.scalar(toCrossPoint, directionVector) >= 0) {
                if (minDistance == null) {
                    closestInfo = info;
                    minDistance = toCrossPoint.length();
                } else {
                    double localCrossPointDistance = toCrossPoint.length();
                    if (minDistance > localCrossPointDistance) {
                        minDistance = localCrossPointDistance;
                        closestInfo = info;
                    }
                }
            }
        }
        return closestInfo;
    }


    public IntersectionInfo getFurthest(final Collection<IntersectionInfo> infoCollection) {
        if (infoCollection.size() == 0) {
            return null;
        }
        IntersectionInfo furthestInfo = null;
        Double maxDistance = null;
        for (IntersectionInfo info : infoCollection) {
            Vector toCrossPoint = new Vector(point, info.getPoint());
            if (Vector.scalar(toCrossPoint, directionVector) >= 0) {
                if (maxDistance == null) {
                    furthestInfo = info;
                    maxDistance = toCrossPoint.length();
                } else {
                    double localCrossPointDistance = toCrossPoint.length();
                    if (maxDistance < localCrossPointDistance) {
                        maxDistance = localCrossPointDistance;
                        furthestInfo = info;
                    }
                }
            }
        }
        return furthestInfo;
    }

}
