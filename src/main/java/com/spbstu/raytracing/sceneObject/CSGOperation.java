package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.math.Point;
import com.spbstu.raytracing.math.Ray;

import java.util.List;

/**
 * @author vva
 * @date 04.04.14
 */
public enum CSGOperation {
    CSG_UNION,
    CSG_DIFFERENCE,
    CSG_INTERSECTION;


    private interface CSGOperationResult {
        List<Point> getIntersectionPoints(Ray ray, SceneObject left, SceneObject right);

        Point getNormal(Point point);

    }


//    private class CSGUnionResult implements CSGOperationResult {
//        @Override
//        public List<Point3D> getIntersectionPoints(Ray3D ray, SceneObject left, SceneObject right) {
//            List<Point3D> intersectionPoints = new ArrayList<>(left.getIntersectionPoints(ray));
//            intersectionPoints.addAll(right.getIntersectionPoints(ray));
//            return intersectionPoints;
//        }
//    }
}
