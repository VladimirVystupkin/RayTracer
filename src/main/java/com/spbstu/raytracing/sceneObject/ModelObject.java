package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.Relation;
import com.spbstu.raytracing.math.*;
import com.spbstu.raytracing.sceneObject.attributes.Attribute;
import com.spbstu.raytracing.sceneObject.attributes.Attributes;
import com.sun.javafx.beans.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class defining *.obj file model
 *
 * @author vva
 */
public class ModelObject extends SceneObject {
    final List<ModelTriangle> triangles;
    final BoundingBox boundingBox;


    /**
     * Constructor which makes  defining *.obj file model from triangle list with bounding box, material and object 3D conversation attributes
     *
     * @param relation      triangle list with bounding box
     * @param material      object material
     * @param attributesMap map from attributes
     * @see com.spbstu.raytracing.sceneObject.SceneObject
     * @see com.spbstu.raytracing.sceneObject.attributes.Attribute
     */
    public ModelObject(@NonNull final Relation<BoundingBox, List<ModelTriangle>> relation,@NonNull final Material material, Map<Attributes.AttributeName, Attribute> attributesMap) {
        super(material, attributesMap);
        this.triangles = relation.getValue();
        this.boundingBox = relation.getKey();
    }

    @Override
    @NonNull
    public Vector getNormal(@NonNull final Point point) {
        return ((Relation<Vector, ModelTriangle>) ((PointExt) point).getInfo()).getKey();
    }

    @Override
    @NonNull
    public List<Point> getIntersectionPoints(@NonNull final Ray ray) {
        List<Point> intersectionPoints = new ArrayList<>();
        Point startPoint = ray.getPoint();
        Point endPoint = Point.translate(startPoint, ray.getDirectionVector());
        Ray transformedRay = new Ray(Matrix.multiply(matrix, startPoint),
                Matrix.multiply(matrix, endPoint));
        if (!boundingBox.hasIntersection(transformedRay)) {
            return intersectionPoints;
        }
        for (Point staticIntersectionPoint : getStaticIntersectionPoints(transformedRay)) {
            intersectionPoints.add(Matrix.multiply(inverted, (PointExt) staticIntersectionPoint));

        }
        return intersectionPoints;
    }

    @Override
    @NonNull
    public Vector getStaticNormal(@NonNull final Point point) {
        return new Vector(1, 0, 0);//unused
    }

    @Override
    @NonNull
    public List<Point> getStaticIntersectionPoints(Ray ray) {
        List<Point> intersectionPoints = new ArrayList<>();
        for (ModelTriangle triangle : triangles) {
            intersectionPoints.addAll(triangle.getIntersectionPoints(ray));
        }
        return intersectionPoints;
    }
}
