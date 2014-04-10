package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.Relation;
import com.spbstu.raytracing.math.*;
import com.spbstu.raytracing.sceneObject.attributes.Attribute;
import com.spbstu.raytracing.sceneObject.attributes.Attributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author vva
 * @date 02.04.14
 * @description
 */
public class ModelObject extends SceneObject {
    List<ModelTriangle> triangles;
    BoundingBox boundingBox;

    public ModelObject(Relation<BoundingBox, List<ModelTriangle>> relation, Material material, Map<Attributes.AttributeName, Attribute> attributesMap) {
        super(material, attributesMap);
        this.triangles = relation.getValue();
        this.boundingBox = relation.getKey();
    }

    @Override
    public Vector getNormal(Point3D point) {
        return ((Relation<Vector, ModelTriangle>) ((PointExt) point).getInfo()).getKey();
    }

    @Override
    public List<Point3D> getCrossPoints(Ray ray) {
        List<Point3D> crossPoints = new ArrayList<>();
        Point3D startPoint = ray.getPoint();
        Point3D endPoint = Point3D.translate(startPoint, ray.getDirectionVector());
        Ray transformedRay = new Ray(Matrix.multiply(matrix, startPoint),
                Matrix.multiply(matrix, endPoint));
        if (!boundingBox.crosses(transformedRay)) {
            return crossPoints;
        }
        for (Point3D staticCrossPoint : getStaticCrossPoints(transformedRay)) {
            crossPoints.add(Matrix.multiply(inverted, (PointExt) staticCrossPoint));

        }
        return crossPoints;
    }

    @Override
    public Vector getStaticNormal(Point3D point) {
        return new Vector(1, 0, 0);//unused
    }

    @Override
    public List<Point3D> getStaticCrossPoints(Ray ray) {
        List<Point3D> crossPoints = new ArrayList<>();
        for (ModelTriangle triangle : triangles) {
            crossPoints.addAll(triangle.getCrossPoints(ray));
        }
        return crossPoints;
    }
}
