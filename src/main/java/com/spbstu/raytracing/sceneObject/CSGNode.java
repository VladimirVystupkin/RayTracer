package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.ModelTriangle;
import com.spbstu.raytracing.Relation;
import com.spbstu.raytracing.math.*;

import java.util.List;
import java.util.Map;

/**
 * @author vva
 * @date 04.04.14
 * @description
 */
public class CSGNode extends SceneObject {

    final SceneObject leftNode, rightNode;


    protected CSGNode(SceneObject leftNode, SceneObject rightNode, Material material, Map<Attributes.Attribute, Matrix3D> attributesMap) {
        super(material, attributesMap);
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }


    @Override
    public Vector3D getNormal(Point3D point) {
        return ((Relation<Vector3D, CSGNode>) ((Point3DExt) point).getInfo()).getKey();
    }


    //unused
    @Override
    public Vector3D getStaticNormal(Point3D point) {
        return null;
    }

    @Override
    public List<Point3D> getStaticCrossPoints(Ray3D ray) {
        return null;
    }
}
