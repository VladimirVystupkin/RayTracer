package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.math.*;
import com.spbstu.raytracing.math.Point;
import com.spbstu.raytracing.sceneObject.attributes.Attribute;
import com.spbstu.raytracing.sceneObject.attributes.Attributes;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author vva
 */
public abstract class SceneObject {

    final Material material;
    final Matrix matrix, inverted;

    protected SceneObject(final Material material, final Map<Attributes.AttributeName, Attribute> attributesMap) {
        this.material = material;
        this.matrix = Attributes.getCommonMatrix(attributesMap);
        this.inverted = Matrix.invert(matrix);
    }

    protected SceneObject(final Map<Attributes.AttributeName, Attribute> attributesMap) {
        this.matrix = Attributes.getCommonMatrix(attributesMap);
        this.inverted = Matrix.invert(matrix);
        this.material = null;
    }


    public abstract List<IntersectionInfo> getStaticIntersectionInfo(final Ray ray);


    public List<IntersectionInfo> getIntersectionInfo(final Ray ray) {
        List<IntersectionInfo> intersectionInfoList = new ArrayList<>();
        Point startPoint = ray.getPoint();
        Point endPoint = Point.translate(startPoint, ray.getDirectionVector());
        Ray transformedRay = new Ray(Matrix.multiply(matrix, startPoint),
                Matrix.multiply(matrix, endPoint));
        for (IntersectionInfo info : getStaticIntersectionInfo(transformedRay)) {
            Point intersectionPoint = Matrix.multiply(inverted, info.getPoint());
            Point normalStartPoint = new Point(0, 0, 0);
            Point normalEndPoint = info.getNormal().toPoint3D();
            Vector normal = new Vector(Matrix.multiply(inverted, normalStartPoint),
                    Matrix.multiply(inverted, normalEndPoint)).getNormalized();

            intersectionInfoList.add(new IntersectionInfo(intersectionPoint, normal, info.getMaterial()));
        }
        return intersectionInfoList;
    }
}
