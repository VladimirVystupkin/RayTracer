package com.spbstu.raytracing;

import com.spbstu.raytracing.lightning.LightSource;
import com.spbstu.raytracing.math.*;
import com.spbstu.raytracing.math.Point;
import com.spbstu.raytracing.math.Vector;
import com.spbstu.raytracing.sceneObject.IntersectionInfo;
import com.spbstu.raytracing.sceneObject.SceneObject;


import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author vva
 */
public class RayTracer {

    public static class RayTracerInfo {
        final Color ambient;
        final LightningStyle lightningStyle;
        final int depth;

        public RayTracerInfo(final Color ambient, final LightningStyle lightningStyle, final int depth) {
            this.ambient = ambient;
            this.lightningStyle = lightningStyle;
            this.depth = depth;
        }

        public static enum LightningStyle {
            PHONG,
            PHONG_BLINN
        }
    }

    final RayTracerInfo rayTracerInfo;
    final Camera camera;
    final SceneObject[] sceneObjects;
    final LightSource[] lightSources;

    public RayTracer(final RayTracerInfo rayTracerInfo, Camera camera,
                     final Collection<SceneObject> sceneObjects, final Collection<LightSource> lightSources) {
        this.rayTracerInfo = rayTracerInfo;
        this.camera = camera;
        this.sceneObjects = new SceneObject[sceneObjects.size()];
        sceneObjects.toArray(this.sceneObjects);
        this.lightSources = new LightSource[lightSources.size()];
        lightSources.toArray(this.lightSources);
    }


    private Color add(final Color... colors) {
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


    public IntersectionInfo traceFurthest(final Ray ray) {
        List<IntersectionInfo> intersectionInfoList = new ArrayList<>();
        for (SceneObject sceneObject : sceneObjects) {
            intersectionInfoList.addAll(sceneObject.getIntersectionInfo(ray));
        }
        return ray.getFurthest(intersectionInfoList);
    }


    public Color getDiffuse(final IntersectionInfo intersectionInfo, final LightSource lightSource, final double intensityFactor) {
        Point intersectionPoint = intersectionInfo.getPoint();
        Vector normal = intersectionInfo.getNormal();
        double c = Math.max(-Vector.scalar(lightSource.getOnPointDirection(intersectionPoint), normal), 0) * intensityFactor * lightSource.getIntensity(intersectionPoint);
        Color color = lightSource.getColor();
        try {

            return new Color((int) (color.getRed() * c), (int) (color.getGreen() * c), (int) (color.getBlue() * c));

        } catch (Exception e) {
            System.out.println("e = " + e);
            throw new RuntimeException(e);
        }
    }


    public Color getSpecular(final IntersectionInfo intersectionInfo, final Ray ray, LightSource lightSource, final double intensityFactor) {
        Point intersectionPoint = intersectionInfo.getPoint();
        Vector normal = intersectionInfo.getNormal();
        Vector onPointDir = lightSource.getOnPointDirection(intersectionPoint);
        double c = intensityFactor;
        switch (rayTracerInfo.lightningStyle) {
            case PHONG:
                Vector r = Vector.doubleLinearCombination(-1, 2 * Vector.scalar(onPointDir, normal), onPointDir, normal).getNormalized();
                c *= Math.max(Math.pow(Vector.scalar(r, ray.getDirectionVector()), intersectionInfo.getSpecularPower()), 0);
                break;
            case PHONG_BLINN:
                Vector h = Vector.add(ray.getDirectionVector(), onPointDir).getNormalized();
                c *= Math.max(Math.pow(Vector.scalar(h, normal), intersectionInfo.getSpecularPower()), 0);
                break;
        }
        Color color = lightSource.getColor();
        return new Color((int) (color.getRed() * c), (int) (color.getGreen() * c), (int) (color.getBlue() * c));
    }


    public Color getRefractedColor(final IntersectionInfo intersectionInfo, final Ray ray,
                                   final double refractionIndex, final double intensity, final int deepLevel) {
        if (intensity < 1e-5 || deepLevel == rayTracerInfo.depth) {
            return Color.BLACK;
        }
        double eta = refractionIndex / intersectionInfo.getRefractionIndex();
        Point intersectionPoint = intersectionInfo.getPoint();
        Vector normal = intersectionInfo.getNormal();
        double cos = -Vector.scalar(normal, ray.getDirectionVector());
        if (cos < 0) {
            cos *= -1.0;
            normal = Vector.onNumber(normal, -1);
            eta = 1.0 / eta;
        }
        double k = 1.0f - eta * eta * (1.0 - cos * cos);
        if (k < 0) {
            return Color.BLACK;
        } else {
            Vector r = Vector.doubleLinearCombination(eta, eta * cos - Math.sqrt(k), ray.getDirectionVector(), normal).getNormalized();
            Ray refractedRay = new Ray(Point.translate(intersectionPoint, Vector.onNumber(r, 1e-5)), r);
            return getColor(refractedRay, intersectionInfo.getRefractionIndex(), intensity, deepLevel + 1);
        }
    }


    public Color getReflectedColor(final IntersectionInfo intersectionInfo, final Ray ray,
                                   final double refractionIndex, final double intensity, final int deepLevel) {
        if (intensity < 1e-5 || deepLevel == rayTracerInfo.depth) {
            return Color.BLACK;
        }
        Point intersectionPoint = intersectionInfo.getPoint();
        Vector normal = intersectionInfo.getNormal();
        Vector onCameraDir = Vector.onNumber(ray.getDirectionVector(), -1).getNormalized();
        Vector directed = ray.getDirectionVector().getNormalized();
        Vector r = Vector.add(directed, Vector.onNumber(normal, 2 * Math.abs(Vector.scalar(directed, normal)))).getNormalized();
        Ray reflectedRay = new Ray(Point.translate(intersectionPoint, Vector.onNumber(r, 1e-5)), r);
        return getColor(reflectedRay, refractionIndex, intensity, deepLevel + 1);
    }


    public Color getColor(final Ray ray, final double refractionIndex, final double intensity, final int deepLevel) {
        Color resultColor = Color.BLACK;
        IntersectionInfo intersectionInfo = traceClosest(ray);
        if (intersectionInfo != null) {
            Color ambient = new Color((int) (intensity * rayTracerInfo.ambient.getRed()), (int) (intensity * rayTracerInfo.ambient.getGreen()), (int) (intensity * rayTracerInfo.ambient.getBlue()));
            Color diffuse = Color.BLACK;
            Color specular = Color.BLACK;
            for (LightSource lightSource : lightSources) {
                if (!isInShadow(intersectionInfo, lightSource)) {
                    diffuse = add(diffuse, getDiffuse(intersectionInfo, lightSource, intensity));
                    specular = add(specular, getSpecular(intersectionInfo, ray, lightSource, intensity));
                }
            }
            Color reflectedColor = getReflectedColor(intersectionInfo, ray, refractionIndex, intersectionInfo.getReflectionFactor() * intensity, deepLevel);
            Color refractedColor = getRefractedColor(intersectionInfo, ray, refractionIndex, intersectionInfo.getRefractionFactor() * intensity, deepLevel);
            resultColor = intersectionInfo.getColor(ambient, diffuse, specular);
            resultColor = add(resultColor, intersectionInfo.getColor(Color.BLACK, reflectedColor, Color.BLACK), intersectionInfo.getColor(Color.BLACK, refractedColor, Color.BLACK));
        }
        return resultColor;
    }

    boolean isInShadow(final IntersectionInfo intersectionInfo, final LightSource lightSource) {
        Point intersectionPoint = intersectionInfo.getPoint();
        Ray toLightSourceRay = new Ray(intersectionPoint, com.spbstu.raytracing.math.Vector.onNumber(lightSource.getOnPointDirection(intersectionPoint), -1));
        IntersectionInfo furthestInfo = traceFurthest(toLightSourceRay);
        return !(furthestInfo == null || Point.distance(intersectionPoint, furthestInfo.getPoint()) < 1e-5);
    }


    public IntersectionInfo traceClosest(final Ray ray) {
        List<IntersectionInfo> intersectionInfoList = new ArrayList<>();
        for (SceneObject sceneObject : sceneObjects) {
            intersectionInfoList.addAll(sceneObject.getIntersectionInfo(ray));
        }
        return ray.getClosest(intersectionInfoList);
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
        // System.out.println("finished ♫");
        //System.out.println("elapsed time :" + (System.currentTimeMillis() - start));
    }


    public class TracerRunnable implements Runnable {
        final int threadNumber, threadNumbers;
        Camera camera;

        public TracerRunnable(Camera camera, final int threadNumber, final int threadNumbers) {
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
