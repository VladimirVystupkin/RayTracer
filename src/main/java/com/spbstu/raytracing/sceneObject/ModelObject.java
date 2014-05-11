package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.ModelObjectParserDefault;
import com.spbstu.raytracing.Relation;
import com.spbstu.raytracing.math.*;
import com.spbstu.raytracing.sceneObject.attributes.Attribute;
import com.spbstu.raytracing.sceneObject.attributes.Attributes;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vva
 */
public class ModelObject extends SceneObject {
    final List<ModelTriangle> triangles;
    final BoundingBox boundingBox;

    public ModelObject(final Relation<BoundingBox, List<ModelTriangle>> relation, final Material material, final Map<Attributes.AttributeName, Attribute> attributesMap) {
        super(material, attributesMap);
        this.triangles = relation.getValue();
        this.boundingBox = relation.getKey();
    }

    @Override

    public List<IntersectionInfo> getStaticIntersectionInfo(final Ray ray) {
        List<IntersectionInfo> intersectionInfoList = new ArrayList<>();
        for (ModelTriangle triangle : triangles) {
            intersectionInfoList.addAll(triangle.getIntersectionInfo(ray, material));
        }
        return intersectionInfoList;
    }


    public List<IntersectionInfo> getIntersectionInfo(final Ray ray) {
        List<IntersectionInfo> intersectionInfoList = new ArrayList<>();
        Point startPoint = ray.getPoint();
        Point endPoint = Point.translate(startPoint, ray.getDirectionVector());
        Ray transformedRay = new Ray(Matrix.multiply(matrix, startPoint),
                Matrix.multiply(matrix, endPoint));
        if(!boundingBox.hasIntersection(transformedRay)){return  intersectionInfoList;}
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


    public static ModelObject fromMap(final HashMap hashMap, final Material material, final HashMap<Attributes.AttributeName, Attribute> attributeNameAttributeHashMap) throws IOException {
        return new ModelObject(ModelObjectParserDefault.parse((String) hashMap.get("file_name")), material, attributeNameAttributeHashMap);
    }
}
