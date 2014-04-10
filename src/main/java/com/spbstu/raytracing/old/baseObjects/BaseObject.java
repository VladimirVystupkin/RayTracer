package com.spbstu.raytracing.old.baseObjects;

import com.spbstu.raytracing.old.Line;
import com.spbstu.raytracing.old.Matrix3D;
import com.spbstu.raytracing.old.Point3D;
import com.spbstu.raytracing.old.Vector3D;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vva
 * @date 10.03.14
 * @description
 */
public abstract class BaseObject {

    Matrix3D matrix;


//    public Matrix3D getMatrix() {
//        return matrix;
//    }
//
//    public void setMatrix(Matrix3D matrix) {
//        this.matrix = matrix;
//    }
//
//    public abstract Vector3D getStaticNormal(Point3D point);
//
//    public abstract List<Point3D> getStaticCrossPoints(Line line);
//
//    public Vector3D getNormal(Point3D point) {
//        return Matrix3D.invert(matrix).multiply(getStaticNormal(point));
//    }
//
//    public List<Point3D> getCrossPoint(Line line) {
//        Vector3D localLineVector = matrix.multiply(line.getDirectionVector());
//        Point3D localLinePoint = matrix.multiply(line.getPoint().toVector3D()).toPoint3D();
//        List<Point3D> crossPoints = new ArrayList<Point3D>();
//        for (Point3D localCrossPoint : getStaticCrossPoints(new Line(localLinePoint, localLineVector))) {
//            crossPoints.add(matrix.multiply(localCrossPoint.toVector3D()).toPoint3D());
//        }
//        return crossPoints;
//    }
//
//    public abstract int getColor(Point3D point);

}
