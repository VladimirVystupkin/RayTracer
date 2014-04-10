package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.math.Point3D;
import com.spbstu.raytracing.math.Point3DExt;
import com.spbstu.raytracing.math.Ray3D;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vva
 * @date 04.04.14
 * @description
 */
public enum CSGOperation {
    CSG_UNION,
    CSG_DIFFERENCE,
    CSG_INTERSECTION;


    private interface CSGOperationResult {
        List<Point3D> getCrossPoints(Ray3D ray, SceneObject left, SceneObject right);

        Point3D getNormal(Point3D point);

    }


//    private class CSGUnionResult implements CSGOperationResult {
//        @Override
//        public List<Point3D> getCrossPoints(Ray3D ray, SceneObject left, SceneObject right) {
//            List<Point3D> crossPoints = new ArrayList<>(left.getCrossPoints(ray));
//            crossPoints.addAll(right.getCrossPoints(ray));
//            return crossPoints;
//        }
//    }
}
