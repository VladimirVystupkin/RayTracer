package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.Relation;
import com.spbstu.raytracing.math.Point;
import com.spbstu.raytracing.math.Ray;
import com.spbstu.raytracing.math.Vector;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author vva
 */
public enum CSGOperation {

    CSG_UNION(new CSGUnionResult()),
    CSG_DIFFERENCE(new CSGSubtractionResult()),
    CSG_INTERSECTION(new CSGIntersectionResult());
    CSGOperationResult operationResult;

    CSGOperation(final CSGOperationResult operationResult) {
        this.operationResult = operationResult;
    }

    public List<IntersectionInfo> getIntersectionInfo(final Ray ray, final SceneObject left, final SceneObject right) {
        return operationResult.getIntersectionInfo(ray, left, right);
    }

    private enum CurrentState {
        NONE,
        IN,
        DOUBLE_IN
    }


    private static List<Relation<SceneObject, IntersectionInfo>> sortByDistance(final Ray ray, List<Relation<SceneObject, IntersectionInfo>> relations) {
        if (relations.size() == 0) {
            return new ArrayList<>();
        }
        final Point point = ray.getPoint();
        final Vector directionVector = ray.getDirectionVector();
        Collections.sort(relations, new Comparator<Relation<SceneObject, IntersectionInfo>>() {
            @Override
            public int compare(Relation<SceneObject, IntersectionInfo> rel1, Relation<SceneObject, IntersectionInfo> rel2) {
                double c1 = Vector.scalar(new Vector(point, rel1.getValue().getPoint()), directionVector) >= 0 ? 1 : -1;
                double c2 = Vector.scalar(new Vector(point, rel2.getValue().getPoint()), directionVector) >= 0 ? 1 : -1;
                return new Double(new Vector(point, rel1.getValue().getPoint()).length() * c1).compareTo(new Vector(point, rel2.getValue().getPoint()).length() * c2);
            }
        });
        return relations;
    }

    private interface CSGOperationResult {

        List<IntersectionInfo> getIntersectionInfo(final Ray ray, final SceneObject left, final SceneObject right);

    }


    private static class CSGUnionResult implements CSGOperationResult {

        @Override

        public List<IntersectionInfo> getIntersectionInfo(final Ray ray, final SceneObject left, final SceneObject right) {
            List<IntersectionInfo> leftIntersectionInfoList = left.getIntersectionInfo(ray);
            List<IntersectionInfo> rightIntersectionInfoList = right.getIntersectionInfo(ray);
            List<Relation<SceneObject, IntersectionInfo>> relations = new ArrayList<>();
            for (IntersectionInfo info : leftIntersectionInfoList) {
                relations.add(new Relation<SceneObject, IntersectionInfo>(left, info));
            }
            for (IntersectionInfo info : rightIntersectionInfoList) {
                relations.add(new Relation<SceneObject, IntersectionInfo>(right, info));
            }
            Relation<CurrentState, SceneObject> currentStateWithObject = new Relation<>(CurrentState.NONE, null);
            List<IntersectionInfo> intersectionInfoList = new ArrayList<>();
            for (Relation<SceneObject, IntersectionInfo> relation : sortByDistance(ray, relations)) {
                switch (currentStateWithObject.getKey()) {
                    case NONE:
                        currentStateWithObject = new Relation<>(CurrentState.IN, relation.getKey());
                        intersectionInfoList.add(relation.getValue());
                        break;
                    case IN:
                        if (currentStateWithObject.getValue().equals(relation.getKey())) {
                            intersectionInfoList.add(relation.getValue());
                            currentStateWithObject = new Relation<>(CurrentState.NONE, null);
                        } else {
                            currentStateWithObject = new Relation<>(CurrentState.DOUBLE_IN, relation.getKey());
                        }
                        break;
                    case DOUBLE_IN:
                        currentStateWithObject = new Relation<>(CurrentState.IN, left.equals(relation.getKey()) ? right :
                                left);
                        break;
                }
            }
            return intersectionInfoList;
        }

    }

    private static class CSGIntersectionResult implements CSGOperationResult {

        @Override

        public List<IntersectionInfo> getIntersectionInfo(final Ray ray, final SceneObject left, final SceneObject right) {
            List<IntersectionInfo> leftIntersectionInfoList = left.getIntersectionInfo(ray);
            List<IntersectionInfo> rightIntersectionInfoList = right.getIntersectionInfo(ray);
            List<Relation<SceneObject, IntersectionInfo>> relations = new ArrayList<>();
            for (IntersectionInfo info : leftIntersectionInfoList) {
                relations.add(new Relation<SceneObject, IntersectionInfo>(left, info));
            }
            for (IntersectionInfo info : rightIntersectionInfoList) {
                relations.add(new Relation<SceneObject, IntersectionInfo>(right, info));
            }
            Relation<CurrentState, SceneObject> currentStateWithObject = new Relation<>(CurrentState.NONE, null);
            List<IntersectionInfo> intersectionInfoList = new ArrayList<>();
            for (Relation<SceneObject, IntersectionInfo> relation : sortByDistance(ray, relations)) {
                switch (currentStateWithObject.getKey()) {
                    case NONE:
                        currentStateWithObject = new Relation<>(CurrentState.IN, relation.getKey());
                        break;
                    case IN:
                        if (currentStateWithObject.getValue().equals(relation.getKey())) {
                            currentStateWithObject = new Relation<>(CurrentState.NONE, null);
                        } else {
                            currentStateWithObject = new Relation<>(CurrentState.DOUBLE_IN, relation.getKey());
                            intersectionInfoList.add(relation.getValue());
                        }
                        break;
                    case DOUBLE_IN:
                        currentStateWithObject = new Relation<>(CurrentState.IN, left.equals(relation.getKey()) ? right :
                                left);
                        intersectionInfoList.add(relation.getValue());
                        break;
                }
            }
            return intersectionInfoList;
        }

    }

    private static class CSGSubtractionResult implements CSGOperationResult {

        @Override

        public List<IntersectionInfo> getIntersectionInfo(final Ray ray, final SceneObject left, final SceneObject right) {
            List<IntersectionInfo> leftIntersectionInfoList = left.getIntersectionInfo(ray);
            List<IntersectionInfo> rightIntersectionInfoList = right.getIntersectionInfo(ray);
            List<Relation<SceneObject, IntersectionInfo>> relations = new ArrayList<>();
            for (IntersectionInfo info : leftIntersectionInfoList) {
                relations.add(new Relation<SceneObject, IntersectionInfo>(left, info));
            }
            for (IntersectionInfo info : rightIntersectionInfoList) {
                relations.add(new Relation<SceneObject, IntersectionInfo>(right, info));
            }
            Relation<CurrentState, SceneObject> currentStateWithObject = new Relation<>(CurrentState.NONE, null);
            List<IntersectionInfo> intersectionInfoList = new ArrayList<>();
            for (Relation<SceneObject, IntersectionInfo> relation : sortByDistance(ray, relations)) {
                switch (currentStateWithObject.getKey()) {
                    case NONE:
                        currentStateWithObject = new Relation<>(CurrentState.IN, relation.getKey());
                        if (relation.getKey() == left) {
                            intersectionInfoList.add(relation.getValue());
                        }
                        break;
                    case IN:
                        if (currentStateWithObject.getValue().equals(relation.getKey())) {
                            if (relation.getKey() == left) {
                                IntersectionInfo info = relation.getValue();
                                intersectionInfoList.add(relation.getValue());
                                //intersectionInfoList.add(new IntersectionInfo(info.getPoint(), Vector.onNumber(info.getNormal(), -1), info.getMaterial()));
                            } else {
                                // intersectionInfoList.add(relation.getValue());
                            }
                            currentStateWithObject = new Relation<>(CurrentState.NONE, null);
                        } else {
                            currentStateWithObject = new Relation<>(CurrentState.DOUBLE_IN, relation.getKey());
                            if (relation.getKey() == right) {
                                intersectionInfoList.add(relation.getValue());
                            }
                        }
                        break;
                    case DOUBLE_IN:
                        currentStateWithObject = new Relation<>(CurrentState.IN, left.equals(relation.getKey()) ? right :
                                left);
                        if (relation.getKey() == right) {
                            IntersectionInfo info = relation.getValue();
                            intersectionInfoList.add(new IntersectionInfo(info.getPoint(), Vector.onNumber(info.getNormal(), -1), info.getMaterial()));
                        }
                        break;
                }
            }
            if (intersectionInfoList.size() > 0 && intersectionInfoList.size() % 2 == 1) {
                System.out.println("intersectionInfoList = " + intersectionInfoList);
            }
            return intersectionInfoList;
        }
    }
}
