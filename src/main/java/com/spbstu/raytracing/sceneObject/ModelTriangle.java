package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.Relation;
import com.spbstu.raytracing.math.Point;
import com.spbstu.raytracing.math.PointExt;
import com.spbstu.raytracing.math.Ray;
import com.spbstu.raytracing.math.Vector;
import com.sun.javafx.beans.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;


/**
 * Class defining *.obj file triangle
 *
 * @author vva
 * @see com.spbstu.raytracing.sceneObject.ModelObject
 */
public class ModelTriangle {

    Point v1, v2, v3;
    Vector n1, n2, n3;
    Vector v12, v13;

    /**
     * Constructor  defining *.obj file triangle by  clockwise  going points and normals
     *
     * @param v1 first triangle point
     * @param v2 second triangle point
     * @param v3 third triangle point
     * @param n1 normal for v1
     * @param n2 normal for v2
     * @param n3 normal for v3
     */
    public ModelTriangle(@NonNull final Point v1, @NonNull final Point v2, @NonNull final Point v3,
                         @NonNull final Vector n1, @NonNull final Vector n2, @NonNull final Vector n3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.n1 = n1.getNormalized();
        this.n2 = n2.getNormalized();
        this.n3 = n3.getNormalized();
        v12 = new Vector(v1, v2);
        v13 = new Vector(v1, v3);
    }


    /**
     * Returns triangle intersection points with normal as inner info in {@link com.spbstu.raytracing.math.PointExt}
     * @param ray ray to get intersection points
     * @return triangle intersection points with normal as inner info in {@link com.spbstu.raytracing.math.PointExt}
     * @see  com.spbstu.raytracing.math.PointExt
     */
    @NonNull
    public List<Point> getIntersectionPoints(@NonNull final Ray ray) {
        List<Point> intersectionPoints = new ArrayList<>();
        Vector T = new Vector(v1, ray.getPoint());
        Vector P = Vector.cross(ray.getDirectionVector(), v13);
        Vector Q = Vector.cross(T, v12);
        double c = Vector.scalar(P, v12);
        double u = Vector.scalar(P, T) / c;
        if (Vector.scalar(Q, v13) < 0) {
            return intersectionPoints;
        }
        if (u > 0 && u < 1) {
            double v = Vector.scalar(Q, ray.getDirectionVector()) / c;
            if (v > 0 && (u + v < 1)) {
                Vector normal = Vector.tripleLinearCombination(u, v, 1 - u - v, n1, n2, n3);
                Point intersectionPoint = new PointExt(Point.tripleLinearCombination(1 - u - v, u, v, v1, v2, v3), new Relation<>(normal, this));
                intersectionPoints.add(intersectionPoint);
            }
        }
        return intersectionPoints;
    }
}
