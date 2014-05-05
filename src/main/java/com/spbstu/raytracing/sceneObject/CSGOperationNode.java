package com.spbstu.raytracing.sceneObject;

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
public class CSGOperationNode extends SceneObject {

    final SceneObject leftNode, rightNode;
    final CSGOperation operation;

    public CSGOperationNode(final SceneObject leftNode, final SceneObject rightNode, final CSGOperation operation, final Map<Attributes.AttributeName, Attribute> attributesMap) {
        super(attributesMap);
        this.leftNode = leftNode;
        this.rightNode = rightNode;
        this.operation = operation;
    }


    @Override

    public List<IntersectionInfo> getStaticIntersectionInfo(final Ray ray) {
        return operation.getIntersectionInfo(ray, leftNode, rightNode);
    }


    public static CSGOperationNode fromMap(final CSGOperation csgOperation, final HashMap hashMap, final Map<Integer, Material> materialMap) throws IOException {
        return new CSGOperationNode(CSGNode.fromMap((ArrayList) hashMap.get("left_node"), materialMap), CSGNode.fromMap((ArrayList) hashMap.get("right_node"), materialMap), csgOperation, new HashMap<Attributes.AttributeName, Attribute>());
    }
}
