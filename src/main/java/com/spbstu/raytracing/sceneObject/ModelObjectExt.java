package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.ModelTriangle;
import com.spbstu.raytracing.Relation;
import com.spbstu.raytracing.math.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author vva
 * @date 02.04.14
 * @description
 */
public class ModelObjectExt extends SceneObject {
    List<ModelTriangle> triangles;
    BoundingBox boundingBox;

    public ModelObjectExt(Relation<BoundingBox, List<ModelTriangle>> relation, Material material, Map<Attributes.Attribute, Matrix3D> attributesMap) {
        super(material, attributesMap);
        this.triangles = relation.getValue();
        this.boundingBox = relation.getKey();
    }

    @Override
    public Vector3D getNormal(Point3D point) {
        return ((Relation<Vector3D, ModelTriangle>) ((Point3DExt) point).getInfo()).getKey();
    }

    @Override
    public List<Point3D> getCrossPoints(Ray3D ray) {
        List<Point3D> crossPoints = new ArrayList<>();
        Point3D startPoint = ray.getPoint();
        Point3D endPoint = Point3D.translate(startPoint, ray.getDirectionVector());
        Ray3D transformedRay = new Ray3D(Matrix3D.multiply(matrix, startPoint),
                Matrix3D.multiply(matrix, endPoint));
        if (!boundingBox.crosses(transformedRay)) {
            return new ArrayList<>();
        }
        for (Point3D staticCrossPoint : getStaticCrossPoints(transformedRay)) {
            crossPoints.add(Matrix3D.multiply(inverted, (Point3DExt)staticCrossPoint));
        }
        return crossPoints;
    }

    @Override
    public Vector3D getStaticNormal(Point3D point) {
        return new Vector3D(1, 0, 0);//unused
    }

    @Override
    public List<Point3D> getStaticCrossPoints(Ray3D ray) {
        List<Point3D> crossPoints = new ArrayList<>();
        for (ModelTriangle triangle : triangles) {
            crossPoints.addAll(triangle.getCrossPoints(ray));
            //if(crossPoints.size()>2){return  crossPoints;}
        }
        return crossPoints;
    }
}
