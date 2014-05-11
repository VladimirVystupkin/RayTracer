package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.math.Point;
import com.spbstu.raytracing.math.Ray;
import com.spbstu.raytracing.math.Vector;


import java.util.ArrayList;
import java.util.List;


/**
 * @author vva
 */
public class ModelTriangle {

    final Point v1, v2, v3;
    final Vector n1, n2, n3;
    final Vector v12, v13;

    public ModelTriangle(final Point v1, final Point v2, final Point v3,
                         final Vector n1, final Vector n2, final Vector n3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.n1 = n1.getNormalized();
        this.n2 = n2.getNormalized();
        this.n3 = n3.getNormalized();
        v12 = new Vector(v1, v2);
        v13 = new Vector(v1, v3);
    }

    public ModelTriangle(final Point v1, final Point v2, final Point v3
    ) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.n1 = Vector.cross(new Vector(v1, v2), new Vector(v2, v3)).getNormalized();
        this.n2 = n1;
        this.n3 = n1;
        v12 = new Vector(v1, v2);
        v13 = new Vector(v1, v3);
    }


    public List<IntersectionInfo> getIntersectionInfo(final Ray ray, final Material material) {
        List<IntersectionInfo> intersectionInfoList = new ArrayList<>();
        Vector T = new Vector(v1, ray.getPoint());
        Vector P = Vector.cross(ray.getDirectionVector(), v13);
        Vector Q = Vector.cross(T, v12);
        double c = Vector.scalar(P, v12);
        double u = Vector.scalar(P, T) / c;
        if (Vector.scalar(Q, v13) < 0) {
            return intersectionInfoList;
        }
        if (u > 0 && u < 1) {
            double v = Vector.scalar(Q, ray.getDirectionVector()) / c;
            if (v > 0 && (u + v < 1)) {
                Vector normal = n1;//Vector.tripleLinearCombination(u, v, 1 - u - v, n1, n2, n3).getNormalized();
                Point intersectionPoint = Point.tripleLinearCombination(1 - u - v, u, v, v1, v2, v3);
                intersectionInfoList.add(new IntersectionInfo(intersectionPoint, normal, material));
            }
        }
        return intersectionInfoList;
    }
}
