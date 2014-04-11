package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.Relation;
import com.spbstu.raytracing.math.*;
import com.spbstu.raytracing.sceneObject.attributes.Attribute;
import com.spbstu.raytracing.sceneObject.attributes.Attributes;

import java.util.List;
import java.util.Map;

/**
 * @author vva
 * @date 04.04.14
 */
public class CSGNode extends SceneObject {

    final SceneObject leftNode, rightNode;


    protected CSGNode(SceneObject leftNode, SceneObject rightNode, Material material, Map<Attributes.AttributeName, Attribute> attributesMap) {
        super(material, attributesMap);
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }


    @Override
    public Vector getNormal(Point point) {
        return ((Relation<Vector, CSGNode>) ((PointExt) point).getInfo()).getKey();
    }


    //unused
    @Override
    public Vector getStaticNormal(Point point) {
        return null;
    }

    @Override
    public List<Point> getStaticIntersectionPoints(Ray ray) {
        return null;
    }
}
