package com.spbstu.raytracing;

import com.spbstu.raytracing.light.LightSource;
import com.spbstu.raytracing.math.Ray3D;
import com.spbstu.raytracing.math.Point3D;
import com.spbstu.raytracing.math.Vector3D;
import com.spbstu.raytracing.sceneObject.SceneObject;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author vva
 * @date 01.04.14
 * @description
 */
public class RayTracer {

    public static class RayTracerInfo {
        Color ambient;
        LightningStyle lightningStyle;
        int depth;

        public RayTracerInfo(Color ambient, LightningStyle lightningStyle) {
            this.ambient = ambient;
            this.lightningStyle = lightningStyle;
        }

        public RayTracerInfo(Color ambient, LightningStyle lightningStyle, int depth) {
            this.ambient = ambient;
            this.lightningStyle = lightningStyle;
            this.depth = depth;
        }

        public static enum LightningStyle {
            PHONG,
            PHONG_BLINN
        }
    }


    public static Relation<Point3D, SceneObject> getClosest(Ray3D ray, Collection<SceneObject> sceneObjects) {
        List<Point3D> crossPoints = new ArrayList<Point3D>();
        Collection<Relation<Point3D, SceneObject>> sceneObjectByCrossPoints = new ArrayList<>();
        for (SceneObject sceneObject : sceneObjects) {
            Point3D crossPoint = ray.getClosest(sceneObject.getCrossPoints(ray));
            if (crossPoint != null) {
                crossPoints.add(crossPoint);
                sceneObjectByCrossPoints.add(new Relation<>(crossPoint, sceneObject));
            }
        }
        Point3D closestCrossPoint = ray.getClosest(crossPoints);
        for (Relation<Point3D, SceneObject> sceneObjectWithCrossPoint : sceneObjectByCrossPoints) {
            if (sceneObjectWithCrossPoint.getKey().equals(closestCrossPoint)) {
                return sceneObjectWithCrossPoint;
            }
        }
        return null;
    }

    public static Point3D getFurthest(Ray3D ray, Collection<SceneObject> sceneObjects) {
        List<Point3D> crossPoints = new ArrayList<Point3D>();
        for (SceneObject sceneObject : sceneObjects) {
            crossPoints.addAll(sceneObject.getCrossPoints(ray));
        }
        return ray.getFurthest(crossPoints);
    }


    public static Color getSpecular(double intensityFactor, RayTracerInfo info, Ray3D ray, Relation<Point3D, SceneObject> sceneObjectWithCrossPoint, LightSource lightSource) {
        Point3D crossPoint = sceneObjectWithCrossPoint.getKey();
        SceneObject sceneObject = sceneObjectWithCrossPoint.getValue();
        Vector3D normal = sceneObject.getNormal(crossPoint);
        Vector3D onPointDir = lightSource.getOnPointDirection(crossPoint);
        Vector3D onCameraDir = Vector3D.onNumber(ray.getDirectionVector(), -1);
        onPointDir.normalize();
        onCameraDir.normalize();
        Vector3D r = Vector3D.sub(onPointDir, Vector3D.onNumber(normal, 2 * Vector3D.scalar(onPointDir, normal)));
        r.normalize();
        double c = Math.max(Math.pow(Vector3D.scalar(r, onCameraDir), sceneObject.getSpecularPower()), 0) * intensityFactor;
        Color color = lightSource.getColor();
        return new Color((int) (color.getRed() * c), (int) (color.getGreen() * c), (int) (color.getBlue() * c));
    }

    public static Color getDiffuse(double intensityFactor, Relation<Point3D, SceneObject> sceneObjectWithCrossPoint, LightSource lightSource) {
        Point3D crossPoint = sceneObjectWithCrossPoint.getKey();
        SceneObject sceneObject = sceneObjectWithCrossPoint.getValue();
        Vector3D normal = sceneObject.getNormal(crossPoint);
        Vector3D onLightSourceNormal = Vector3D.onNumber(lightSource.getOnPointDirection(crossPoint), -1);
        onLightSourceNormal.normalize();
        double c = Math.max(Vector3D.scalar(onLightSourceNormal, normal), 0) * intensityFactor;
        Color color = lightSource.getColor();
        return new Color((int) (color.getRed() * c), (int) (color.getGreen() * c), (int) (color.getBlue() * c));
    }

    public static Color getAmbient(RayTracerInfo rayTracerInfo, double intensityFactor) {
        Color defaut = rayTracerInfo.ambient;
        return new Color((int) (defaut.getRed() * intensityFactor), (int) (defaut.getGreen() * intensityFactor), (int) (defaut.getBlue() * intensityFactor));
    }

    public static Color add(Color... colors) {
        int r = 0;
        int g = 0;
        int b = 0;
        for (Color color : colors) {
            r = Math.min(255, r + color.getRed());
            g = Math.min(255, g + color.getGreen());
            b = Math.min(255, b + color.getBlue());
        }
        return new Color(r, g, b);
    }

    public static void apply(RayTracerInfo rayTracerInfo, Camera camera, Collection<SceneObject> sceneObjects, Collection<LightSource> lightSources) {
        long start = System.currentTimeMillis();
        int all = camera.getScreenWidth() * camera.getScreenHeight();
        int counter = 0;
        int percents = 0;
        Iterator<Relation<Screen.PixelInfo, Ray3D>> relationIterator = camera.getRaysThroughPixelsIterator();
//        while (relationIterator.hasNext()) {
//            Relation<Screen.PixelInfo, Ray3D> rayThroughPixel = relationIterator.next();
//            Color color = Color.BLACK;
//            Screen.PixelInfo pixelInfo = rayThroughPixel.getKey();
//            Ray3D ray = rayThroughPixel.getValue();
//            Relation<Point3D, SceneObject> sceneObjectWithCrossPoint = getClosest(ray, sceneObjects);
//            if (pixelInfo.y == camera.getScreenHeight() / 2 && pixelInfo.x > camera.screen.getWidth() / 3 && sceneObjectWithCrossPoint == null) {
//                camera.screen.setColor(pixelInfo, 0);
//            }
//            if (sceneObjectWithCrossPoint != null) {
//                Point3D crossPoint = sceneObjectWithCrossPoint.getKey();
//                SceneObject sceneObject = sceneObjectWithCrossPoint.getValue();
//                Color ambient = getAmbient(rayTracerInfo);
//                Color diffuse = Color.BLACK;
//                Color specular = Color.BLACK;
//                for (LightSource lightSource : lightSources) {
//                    Ray3D toLightSourceRay = new Ray3D(crossPoint, Vector3D.onNumber(lightSource.getOnPointDirection(crossPoint), -1));
//                    Point3D furthestPoint = traceFurthest(toLightSourceRay, sceneObjects);
//                    if (furthestPoint == null || Point3D.distance(crossPoint, furthestPoint) < 1e-5) {
//                        diffuse = add(diffuse, getDiffuse(1, sceneObjectWithCrossPoint, lightSource));
//                        specular = add(specular, getSpecular(1, rayTracerInfo, ray, sceneObjectWithCrossPoint, lightSource));
//                    }
//                }
//                color = new Color(sceneObject.getColor(ambient.getRGB(), diffuse.getRGB(), specular.getRGB()));
//            }
//            camera.screen.setColor(pixelInfo, color.getRGB());
//            counter++;
//            if (counter % (all / 10) == 0) {
//                percents += 10;
//                System.out.println("progressing:  " + percents + "%");
//            }
//        }
        while (relationIterator.hasNext()) {
            Relation<Screen.PixelInfo, Ray3D> rayWithPixel = relationIterator.next();
            trace(rayWithPixel, rayTracerInfo, camera, sceneObjects, lightSources, 1, 1, null);
            counter++;
            if (counter % (all / 10) == 0) {
                percents += 10;
                System.out.println("progressing:  " + percents + "%");
            }
        }
        System.out.println("time = " + (System.currentTimeMillis() - start) + " millis");
    }


    private static Ray3D getReflectedRay(Ray3D ray, Relation<Point3D, SceneObject> sceneObjectWithCrossPoint) {
        Point3D crossPoint = sceneObjectWithCrossPoint.getKey();
        SceneObject sceneObject = sceneObjectWithCrossPoint.getValue();
        Vector3D normal = sceneObject.getNormal(crossPoint);
        Vector3D onCameraDir = Vector3D.onNumber(ray.getDirectionVector(), -1);
        onCameraDir.normalize();
        Vector3D directed = ray.getDirectionVector();
        directed.normalize();
        Vector3D r = Vector3D.add(directed, Vector3D.onNumber(normal, 2 * Math.abs(Vector3D.scalar(directed, normal))));
        r.normalize();
        return new Ray3D(Point3D.translate(crossPoint, Vector3D.onNumber(r, 1e-5)), r);

    }


    public static void trace(Relation<Screen.PixelInfo, Ray3D> rayThroughPixel, RayTracerInfo rayTracerInfo, Camera camera, Collection<SceneObject> sceneObjects, Collection<LightSource> lightSources, int deepLevel, double intensityFactor, SceneObject parent) {
        Screen.PixelInfo pixelInfo = rayThroughPixel.getKey();
        Color color = new Color(camera.screen.getImage().getRGB(pixelInfo.x, pixelInfo.y));
        Ray3D ray = rayThroughPixel.getValue();

        Relation<Point3D, SceneObject> sceneObjectWithCrossPoint = getClosest(ray, sceneObjects);

        if (sceneObjectWithCrossPoint != null) {

            Point3D crossPoint = sceneObjectWithCrossPoint.getKey();
            SceneObject sceneObject = sceneObjectWithCrossPoint.getValue();
            Color ambient = getAmbient(rayTracerInfo, intensityFactor);
            Color diffuse = Color.BLACK;
            Color specular = Color.BLACK;
            for (LightSource lightSource : lightSources) {
                Ray3D toLightSourceRay = new Ray3D(crossPoint, Vector3D.onNumber(lightSource.getOnPointDirection(crossPoint), -1));
                Point3D furthestPoint = getFurthest(toLightSourceRay, sceneObjects);
                if (furthestPoint == null || Point3D.distance(crossPoint, furthestPoint) < 1e-5) {

                    diffuse = add(diffuse, getDiffuse(intensityFactor, sceneObjectWithCrossPoint, lightSource));
                    specular = add(specular, getSpecular(intensityFactor, rayTracerInfo, ray, sceneObjectWithCrossPoint, lightSource));
                }
            }

            if (deepLevel == 1) {
                color = new Color(sceneObject.getColor(ambient.getRGB(), diffuse.getRGB(), specular.getRGB()));
            } else {
                Color reflectedColor = new Color(sceneObject.getColor(ambient.getRGB(), diffuse.getRGB(), specular.getRGB()));
                color = add(color, new Color(sceneObject.getColor(0, reflectedColor.getRGB(), 0)));
                System.out.println("reflectedColor = " + reflectedColor);
            }

            camera.screen.setColor(pixelInfo, color.getRGB());
            if (deepLevel < rayTracerInfo.depth && sceneObject.getReflectionFactor() > 0) {
                Relation<Screen.PixelInfo, Ray3D> reflectedRayWithPixel = new Relation<>(pixelInfo, getReflectedRay(ray, sceneObjectWithCrossPoint));
                trace(reflectedRayWithPixel, rayTracerInfo, camera, sceneObjects, lightSources, deepLevel + 1, sceneObject.getReflectionFactor() * intensityFactor, sceneObject);
            }
        }else {
            if(pixelInfo.y==camera.getScreenHeight()/3){
                System.out.println("x = " + pixelInfo.x);
            }
        }
    }

}
