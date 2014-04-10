package com.spbstu.raytracing.old;

import com.spbstu.raytracing.old.baseObjects.BaseObject;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author vva
 * @date 10.03.14
 * @description
 */
public class RayTracingAlgorithm {


    private static class CrossPointWithObject {
        Point3D crossPoint;
        BaseObject sceneObject;

        private CrossPointWithObject(Point3D crossPoint, BaseObject sceneObject) {
            this.crossPoint = crossPoint;
            this.sceneObject = sceneObject;
        }
    }

    public void apply(Point3D viewPoint, Screen screen, Collection<BaseObject> sceneObjects) {
        for (int x = 0; x < screen.getWidth(); x++) {
            for (int y = 0; y < screen.getHeight(); y++) {
                Line line = new Line(viewPoint, new Point3D(x - screen.getWidth() / 2, y - screen.getHeight() / 2, 0));
                List<CrossPointWithObject> crossPoints = new ArrayList<CrossPointWithObject>();
                for (BaseObject sceneObject : sceneObjects) {
                    Point3D crossPoint = null;//sceneObject.getCrossPoint(line);
                    if (crossPoint != null) {
                        crossPoints.add(new CrossPointWithObject(crossPoint, sceneObject));
                    }
                }
                Collections.sort(crossPoints, new Comparator<CrossPointWithObject>() {
                    @Override
                    public int compare(CrossPointWithObject o1, CrossPointWithObject o2) {
                        return Double.compare(o1.crossPoint.z, o2.crossPoint.z);
                    }
                });
                if (crossPoints.size() > 0) {
                    //screen.setColor(x, y, crossPoints.get(0).sceneObject.getColor(crossPoints.get(0).crossPoint));
                } else {
                    screen.setColor(x, y, 0);
                }
            }
        }
    }


    public void apply2(Point3D viewPoint, Screen screen, Collection<BaseObject> sceneObjects) {
        for (int x = 0; x < screen.getWidth(); x++) {
            for (int y = 0; y < screen.getHeight(); y++) {
                Line line = new Line(viewPoint, new Point3D(x - screen.getWidth() / 2, y - screen.getHeight() / 2, 0));
                List<CrossPointWithObject> crossPoints = new ArrayList<CrossPointWithObject>();
                for (BaseObject sceneObject : sceneObjects) {
                    Point3D crossPoint = null;//sceneObject.getCrossPoint(line);
                    if (crossPoint != null) {
                        crossPoints.add(new CrossPointWithObject(crossPoint, sceneObject));
                    }
                }
                Collections.sort(crossPoints, new Comparator<CrossPointWithObject>() {
                    @Override
                    public int compare(CrossPointWithObject o1, CrossPointWithObject o2) {
                        return Double.compare(o1.crossPoint.z, o2.crossPoint.z);
                    }
                });
                if (crossPoints.size() > 0) {
                    //screen.setColor(x, y, crossPoints.get(0).sceneObject.getColor(crossPoints.get(0).crossPoint));
                    int color = screen.getColor(x, y);
                    Point3D crossPoint = crossPoints.get(0).crossPoint;
                    Vector3D dist = new Vector3D((0 - crossPoint.x), (0 - crossPoint.y), -crossPoint.z);
                    dist.normalize();
                    double cos = 0;//(Vector3D.scalar(dist, crossPoints.get(0).sceneObject.getNormal(crossPoint)));
                    int r = new Color(color).getRed();
                    int g = new Color(color).getGreen();
                    int b = new Color(color).getBlue();
//                    if (crossPoint.x*crossPoint.x+crossPoint.y*crossPoint.y+(crossPoint.z-600)*(crossPoint.z-600)-300*300>0) {
//                        //throw new IllegalArgumentException("cos<0");
//                        screen.setColor(x, y, 0);
//                        continue;
//                    }
//                    if(cos==1){
//                        color = Color.GREEN.getRGB();
//                        screen.setColor(x, y, color);
//                        continue;
//                    }
                    if(cos<0){
                        screen.setColor(x, y, 0);
                        continue;
                  }
                    color = new Color((int) (r * cos), (int) (g * cos), (int) (b * cos)).getRGB();
                    screen.setColor(x, y, color);
                } else {
                }
            }
        }
    }
}
