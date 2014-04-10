package com.spbstu.raytracing;

import com.spbstu.raytracing.math.Point3D;
import com.spbstu.raytracing.math.Ray3D;
import com.spbstu.raytracing.math.Vector3D;
import com.spbstu.raytracing.math.Point3DExt;

import java.util.ArrayList;
import java.util.List;


/**
 * @author vva
 * @date 02.04.14
 * @description
 */
public class ModelTriangle {

    Point3D v1, v2, v3;
    Vector3D n1, n2, n3;
    Vector3D v12, v13;

    public ModelTriangle(Point3D v1, Point3D v2, Point3D v3, Vector3D n1, Vector3D n2, Vector3D n3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.n1 = n1;
        this.n2 = n2;
        this.n3 = n3;
        this.n1.normalize();
        this.n2.normalize();
        this.n3.normalize();
        v12 = new Vector3D(v1, v2);
        v13 = new Vector3D(v1, v3);
    }


    public List<Point3D> getCrossPoints(Ray3D ray) {
        List<Point3D> crossPoints = new ArrayList<>();
        Vector3D T = new Vector3D(v1, ray.getPoint());
        Vector3D P = Vector3D.cross(ray.getDirectionVector(), v13);
        Vector3D Q = Vector3D.cross(T, v12);
        double c = Vector3D.scalar(P, v12);
        double u = Vector3D.scalar(P, T) / c;
        if(Vector3D.scalar(Q,v13)<0){
            return crossPoints;
        }
        if (u > 0 && u < 1) {
            double v = Vector3D.scalar(Q, ray.getDirectionVector()) / c;
            if (v > 0 && (u + v < 1)) {
                Vector3D normal = Vector3D.tripleLinearCombination(u, v, 1 - u - v, n1, n2, n3);
                Point3D crossPoint = new Point3DExt(Point3D.tripleLinearCombination(1 - u - v, u, v, v1, v2, v3), new Relation<>(normal, this));
                crossPoints.add(crossPoint);
            }
        }
        return crossPoints;
    }
}
