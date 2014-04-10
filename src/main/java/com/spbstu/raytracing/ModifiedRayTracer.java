package com.spbstu.raytracing;

import com.spbstu.raytracing.light.LightSource;
import com.spbstu.raytracing.math.Point3D;
import com.spbstu.raytracing.math.Ray3D;
import com.spbstu.raytracing.math.Vector3D;
import com.spbstu.raytracing.sceneObject.SceneObject;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author vva
 * @date 04.04.14
 * @description
 */
public class ModifiedRayTracer {

    final RayTracer.RayTracerInfo rayTracerInfo;
    final Camera camera;
    final SceneObject[] sceneObjects;
    final LightSource[] lightSources;

    public ModifiedRayTracer(RayTracer.RayTracerInfo rayTracerInfo, Camera camera, Collection<SceneObject> sceneObjects, Collection<LightSource> lightSources) {
        this.rayTracerInfo = rayTracerInfo;
        this.camera = camera;
        this.sceneObjects = new SceneObject[sceneObjects.size()];
        sceneObjects.toArray(this.sceneObjects);
        this.lightSources = new LightSource[lightSources.size()];
        lightSources.toArray(this.lightSources);
    }


    private Color add(Color... colors) {
        int r = 0;
        int g = 0;
        int b = 0;
        for (Color color : colors) {
            r += color.getRed();
            g += color.getGreen();
            b += color.getBlue();
        }
        return new Color(Math.min(255, r), Math.min(255, g), Math.min(255, b));
    }


    public Point3D traceFurthest(Ray3D ray) {
        java.util.List<Point3D> crossPoints = new ArrayList<Point3D>();
        for (SceneObject sceneObject : sceneObjects) {
            crossPoints.addAll(sceneObject.getCrossPoints(ray));
        }
        return ray.getFurthest(crossPoints);
    }


    public Color getDiffuse(Relation<Point3D, SceneObject> traceResult, LightSource lightSource, double intensityFactor) {
        Point3D crossPoint = traceResult.getKey();
        SceneObject sceneObject = traceResult.getValue();
        Vector3D normal = sceneObject.getNormal(crossPoint);
        double c = Math.max(-Vector3D.scalar(lightSource.getOnPointDirection(crossPoint), normal), 0) * intensityFactor * lightSource.getIntensity(crossPoint);
        Color color = lightSource.getColor();
        return new Color((int) (color.getRed() * c), (int) (color.getGreen() * c), (int) (color.getBlue() * c));
    }

    public Color getSpecular(Relation<Point3D, SceneObject> traceResult, Ray3D ray, LightSource lightSource, double intensityFactor) {
        Point3D crossPoint = traceResult.getKey();
        SceneObject sceneObject = traceResult.getValue();
        Vector3D normal = sceneObject.getNormal(crossPoint);
        Vector3D onPointDir = lightSource.getOnPointDirection(crossPoint);
        double c = intensityFactor;
        switch (rayTracerInfo.lightningStyle) {
            case PHONG:
                Vector3D r = Vector3D.doubleLinearCombination(-1, 2 * Vector3D.scalar(onPointDir, normal), onPointDir, normal);
                r.normalize();
                c *= Math.max(Math.pow(Vector3D.scalar(r, ray.getDirectionVector()), sceneObject.getSpecularPower()), 0);
                break;
            case PHONG_BLINN:
                Vector3D h = Vector3D.add(ray.getDirectionVector(), onPointDir);
                h.normalize();
                c *= Math.max(Math.pow(Vector3D.scalar(h, normal), sceneObject.getSpecularPower()), 0);
                break;
        }
        Color color = lightSource.getColor();
        return new Color((int) (color.getRed() * c), (int) (color.getGreen() * c), (int) (color.getBlue() * c));
    }


    public Color getRefractedColor(Relation<Point3D, SceneObject> traceResult, Ray3D ray, double refractionIndex, double intensity, int deepLevel) {
        if (intensity < 1e-5 || deepLevel == rayTracerInfo.depth) {
            return Color.BLACK;
        }
        double eta = refractionIndex / traceResult.getValue().getRefractionIndex();
        Point3D crossPoint = traceResult.getKey();
        SceneObject sceneObject = traceResult.getValue();
        Vector3D normal = sceneObject.getNormal(crossPoint);
        double cos = -Vector3D.scalar(normal, ray.getDirectionVector());
        if (cos < 0) {
            cos *= -1.0;
            normal = Vector3D.onNumber(normal, -1);
            eta = 1.0 / eta;
        }
        double k = 1.0f - eta * eta * (1.0 - cos * cos);
        if (k < 0) {
            return Color.BLACK;
        } else {
            Vector3D r = Vector3D.doubleLinearCombination(eta, eta * cos - Math.sqrt(k), ray.getDirectionVector(), normal);
            r.normalize();
            Ray3D refractedRay = new Ray3D(Point3D.translate(crossPoint, Vector3D.onNumber(r, 1e-5)), r);
            return getColor(refractedRay, traceResult.getValue().getRefractionIndex(), intensity, deepLevel + 1);
        }
    }

    public Color getReflectedColor(Relation<Point3D, SceneObject> traceResult, Ray3D ray, double refractionIndex, double intensity, int deepLevel) {
        if (intensity < 1e-5 || deepLevel == rayTracerInfo.depth) {
            return Color.BLACK;
        }
        Point3D crossPoint = traceResult.getKey();
        SceneObject sceneObject = traceResult.getValue();
        Vector3D normal = sceneObject.getNormal(crossPoint);
        Vector3D onCameraDir = Vector3D.onNumber(ray.getDirectionVector(), -1);
        onCameraDir.normalize();
        Vector3D directed = ray.getDirectionVector();
        directed.normalize();
        Vector3D r = Vector3D.add(directed, Vector3D.onNumber(normal, 2 * Math.abs(Vector3D.scalar(directed, normal))));
        r.normalize();
        Ray3D reflectedRay = new Ray3D(Point3D.translate(crossPoint, Vector3D.onNumber(r, 1e-5)), r);
        return getColor(reflectedRay, refractionIndex, intensity, deepLevel + 1);
    }

    public Color getColor(Ray3D ray, double refractionIndex, double intensity, int deepLevel) {
        Color resultColor = Color.BLACK;
        Relation<Point3D, SceneObject> traceResult = traceClosest(ray);
        if (traceResult != null) {
            Color ambient = new Color((int) (intensity * rayTracerInfo.ambient.getRed()), (int) (intensity * rayTracerInfo.ambient.getGreen()), (int) (intensity * rayTracerInfo.ambient.getBlue()));
            Color diffuse = Color.BLACK;
            Color specular = Color.BLACK;
            SceneObject crossedObject = traceResult.getValue();
            for (LightSource lightSource : lightSources) {
                if (!isInShadow(traceResult, lightSource)) {
                    diffuse = add(diffuse, getDiffuse(traceResult, lightSource, intensity));
                    specular = add(specular, getSpecular(traceResult, ray, lightSource, intensity));
                }
            }
            Color reflectedColor = getReflectedColor(traceResult, ray, refractionIndex, crossedObject.getReflectionFactor() * intensity, deepLevel);
            Color refractedColor = getRefractedColor(traceResult, ray, refractionIndex, crossedObject.getRefractionFactor() * intensity, deepLevel);
            //diffuse = add(diffuse, reflectedColor, refractedColor);
            resultColor = crossedObject.getColor(ambient, diffuse, specular);
            resultColor = add(resultColor, crossedObject.getColor(Color.BLACK, reflectedColor, Color.BLACK), crossedObject.getColor(Color.BLACK, refractedColor, Color.BLACK));
        }
        return resultColor;
    }

    boolean isInShadow(Relation<Point3D, SceneObject> traceResult, LightSource lightSource) {
        Point3D crossPoint = traceResult.getKey();
        Ray3D toLightSourceRay = new Ray3D(crossPoint, Vector3D.onNumber(lightSource.getOnPointDirection(crossPoint), -1));
        Point3D furthestPoint = traceFurthest(toLightSourceRay);
        return !(furthestPoint == null || Point3D.distance(crossPoint, furthestPoint) < 1e-5);
    }

    public Relation<Point3D, SceneObject> traceClosest(Ray3D ray) {
        List<Point3D> crossPoints = new ArrayList<Point3D>();
        List<Relation<Point3D, SceneObject>> sceneObjectByCrossPoints = new ArrayList<>();
        for (SceneObject sceneObject : sceneObjects) {
            Point3D crossPoint = ray.getClosest(sceneObject.getCrossPoints(ray));
            if (crossPoint != null) {
                crossPoints.add(crossPoint);
                sceneObjectByCrossPoints.add(new Relation<>(crossPoint, sceneObject));
            }
        }
        Point3D closestCrossPoint = ray.getClosest(crossPoints);
        if (closestCrossPoint == null) {
            return null;
        }
        for (Relation<Point3D, SceneObject> sceneObjectWithCrossPoint : sceneObjectByCrossPoints) {
            if (sceneObjectWithCrossPoint.getKey().equals(closestCrossPoint)) {
                return sceneObjectWithCrossPoint;
            }
        }
        return null;
    }

    public void apply() throws InterruptedException {
        long start = System.currentTimeMillis();
        int threadsNumber = Runtime.getRuntime().availableProcessors();
        Thread threads[] = new Thread[threadsNumber];
        for (int threadNumber = 0; threadNumber < threadsNumber; threadNumber++) {
            threads[threadNumber] = new Thread(new TracerRunnable(camera, threadNumber, threadsNumber));
            threads[threadNumber].start();

        }
        for (int threadNumber = 0; threadNumber < threadsNumber; threadNumber++) {
            threads[threadNumber].join();
        }
        System.out.println("finished ♫");
        System.out.println("elapsed time :" + (System.currentTimeMillis() - start));
    }


    public class TracerRunnable implements Runnable {
        final int threadNumber, threadNumbers;
        Camera camera;

        public TracerRunnable(Camera camera, int threadNumber, int threadNumbers) {
            this.camera = camera;
            this.threadNumber = threadNumber;
            this.threadNumbers = threadNumbers;
        }

        @Override
        public void run() {

            for (int x = threadNumber; x < camera.getScreenWidth(); x += threadNumbers) {
                for (int y = 0; y < camera.getScreenHeight(); y++) {
                    camera.setColor(new Screen.PixelInfo(x, y), getColor(camera.emitRay(x, y), 1, 1, 1));
                }
            }
        }
    }

}
