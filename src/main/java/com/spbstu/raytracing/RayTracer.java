package com.spbstu.raytracing;

import com.spbstu.raytracing.lightning.LightSource;
import com.spbstu.raytracing.math.*;
import com.spbstu.raytracing.math.Point;
import com.spbstu.raytracing.math.Vector;
import com.spbstu.raytracing.sceneObject.SceneObject;
import com.sun.istack.internal.Nullable;
import com.sun.javafx.beans.annotations.NonNull;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Simple ray tracer class
 *
 * @author vva
 */
public class RayTracer {


    /**
     * Class  defining ray tracer parameters
     */
    public static class RayTracerInfo {
        Color ambient;
        LightningStyle lightningStyle;
        int depth;

        /**
         * Constructor to  define ray tracer parameters
         *
         * @param ambient        default scene ambient lightning
         * @param lightningStyle {@link com.spbstu.raytracing.RayTracer.RayTracerInfo.LightningStyle}lightning style
         * @param depth          reflected and refraction depth
         */
        public RayTracerInfo(@NonNull final Color ambient, @NonNull final LightningStyle lightningStyle, final int depth) {
            this.ambient = ambient;
            this.lightningStyle = lightningStyle;
            this.depth = depth;
        }

        /**
         * Enumeration for lightning style
         */
        public static enum LightningStyle {
            PHONG,
            PHONG_BLINN
        }
    }

    final RayTracerInfo rayTracerInfo;
    final Camera camera;
    final SceneObject[] sceneObjects;
    final LightSource[] lightSources;


    /**
     * Constructor for ray tracer by ray tracer info, camera and screne parameters
     *
     * @param rayTracerInfo ray tracer info
     * @param camera        camera to emit rays and write results
     * @param sceneObjects  scene objects
     * @param lightSources  lightning sources
     */
    public RayTracer(@NonNull final RayTracerInfo rayTracerInfo, @NonNull Camera camera,
                     @NonNull final Collection<SceneObject> sceneObjects, @NonNull final Collection<LightSource> lightSources) {
        this.rayTracerInfo = rayTracerInfo;
        this.camera = camera;
        this.sceneObjects = new SceneObject[sceneObjects.size()];
        sceneObjects.toArray(this.sceneObjects);
        this.lightSources = new LightSource[lightSources.size()];
        lightSources.toArray(this.lightSources);
    }


    /**
     * Adds color limiting each chanel from 0 to 255
     *
     * @param colors color array for addition
     * @return color addition where each chanel from 0 to 255
     */
    @NonNull
    private Color add(@NonNull final Color... colors) {
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


    /**
     * Returns the furthest to ray start point on the ray direction
     * and null if fo every point P in collection angle between ray direction vector
     * and vector {P.x-point.x,P.y-point.y,P.z-point.z} less than 0 where point is ray is start point
     * or no intersection points
     *
     * @param ray ray to get intersection points
     * @return the furthest to ray start point on the ray direction
     */
    @NonNull
    public Point traceFurthest(@NonNull final Ray ray) {
        java.util.List<Point> intersectionPoints = new ArrayList<Point>();
        for (SceneObject sceneObject : sceneObjects) {
            intersectionPoints.addAll(sceneObject.getIntersectionPoints(ray));
        }
        return ray.getFurthest(intersectionPoints);
    }


    /**
     * Returns diffuse color  for specified light source or black color if scene object is in shadow
     *
     * @param traceResult     intersected scene object with intersection point by ray
     * @param lightSource     lightning source
     * @param intensityFactor ray intensity
     * @return diffuse color for specified light source or black color if scene object is in shadow
     */
    @NonNull
    public Color getDiffuse(@NonNull final Relation<Point, SceneObject> traceResult, @NonNull final LightSource lightSource, final double intensityFactor) {
        Point intersectionPoint = traceResult.getKey();
        SceneObject sceneObject = traceResult.getValue();
        Vector normal = sceneObject.getNormal(intersectionPoint);
        double c = Math.max(-Vector.scalar(lightSource.getOnPointDirection(intersectionPoint), normal), 0) * intensityFactor * lightSource.getIntensity(intersectionPoint);
        Color color = lightSource.getColor();
        return new Color((int) (color.getRed() * c), (int) (color.getGreen() * c), (int) (color.getBlue() * c));
    }

    /**
     * Returns specular color  for specified light source depending on lightning style
     *
     * @param traceResult     intersected scene object with intersection point by ray
     * @param ray             intersecting ray
     * @param lightSource     lightning source
     * @param intensityFactor ray intensity
     * @return specular color for specified light source depending on lightning style
     */
    @NonNull
    public Color getSpecular(@NonNull final Relation<Point, SceneObject> traceResult, @NonNull final Ray ray, LightSource lightSource, final double intensityFactor) {
        Point intersectionPoint = traceResult.getKey();
        SceneObject sceneObject = traceResult.getValue();
        Vector normal = sceneObject.getNormal(intersectionPoint);
        Vector onPointDir = lightSource.getOnPointDirection(intersectionPoint);
        double c = intensityFactor;
        switch (rayTracerInfo.lightningStyle) {
            case PHONG:
                Vector r = Vector.doubleLinearCombination(-1, 2 * Vector.scalar(onPointDir, normal), onPointDir, normal).getNormalized();
                c *= Math.max(Math.pow(Vector.scalar(r, ray.getDirectionVector()), sceneObject.getSpecularPower()), 0);
                break;
            case PHONG_BLINN:
                Vector h = Vector.add(ray.getDirectionVector(), onPointDir).getNormalized();
                c *= Math.max(Math.pow(Vector.scalar(h, normal), sceneObject.getSpecularPower()), 0);
                break;
        }
        Color color = lightSource.getColor();
        return new Color((int) (color.getRed() * c), (int) (color.getGreen() * c), (int) (color.getBlue() * c));
    }


    /**
     * Returns color  of the refracted   from scene object ray or black color if deep level is equal to {@link com.spbstu.raytracing.RayTracer.RayTracerInfo#depth}
     * or if total internal reflection detected
     *
     * @param traceResult     intersected scene object with intersection point by ray
     * @param ray             intersecting ray
     * @param refractionIndex thread refraction index to air
     * @param intensity       ray intensity
     * @param deepLevel       ray deep level
     * @return specular color for specified light source depending on lightning style
     */
    @NonNull
    public Color getRefractedColor(@NonNull final Relation<Point, SceneObject> traceResult, @NonNull final Ray ray,
                                   final double refractionIndex, final double intensity, final int deepLevel) {
        if (intensity < 1e-5 || deepLevel == rayTracerInfo.depth) {
            return Color.BLACK;
        }
        double eta = refractionIndex / traceResult.getValue().getRefractionIndex();
        Point intersectionPoint = traceResult.getKey();
        SceneObject sceneObject = traceResult.getValue();
        Vector normal = sceneObject.getNormal(intersectionPoint);
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
            return getColor(refractedRay, traceResult.getValue().getRefractionIndex(), intensity, deepLevel + 1);
        }
    }


    /**
     * Returns color  of the reflected   from scene object ray or black color if deep level is equal to {@link com.spbstu.raytracing.RayTracer.RayTracerInfo#depth}
     *
     * @param traceResult     intersected scene object with intersection point by ray
     * @param ray             intersecting ray
     * @param refractionIndex thread refraction index to air
     * @param intensity       ray intensity
     * @param deepLevel       ray deep level
     * @return specular color for specified light source depending on lightning style
     */
    @NonNull
    public Color getReflectedColor(@NonNull final Relation<Point, SceneObject> traceResult, @NonNull final Ray ray,
                                   final double refractionIndex, final double intensity, final int deepLevel) {
        if (intensity < 1e-5 || deepLevel == rayTracerInfo.depth) {
            return Color.BLACK;
        }
        Point intersectionPoint = traceResult.getKey();
        SceneObject sceneObject = traceResult.getValue();
        Vector normal = sceneObject.getNormal(intersectionPoint);
        Vector onCameraDir = Vector.onNumber(ray.getDirectionVector(), -1).getNormalized();
        Vector directed = ray.getDirectionVector().getNormalized();
        Vector r = Vector.add(directed, Vector.onNumber(normal, 2 * Math.abs(Vector.scalar(directed, normal)))).getNormalized();
        Ray reflectedRay = new Ray(Point.translate(intersectionPoint, Vector.onNumber(r, 1e-5)), r);
        return getColor(reflectedRay, refractionIndex, intensity, deepLevel + 1);
    }

    /**
     * Returns color from direct ray, reflected and refracted rays
     *
     * @param ray             direct ray
     * @param refractionIndex thread refraction index to air
     * @param intensity       ray intensity
     * @param deepLevel       ray deep level
     * @return color from direct ray, reflected and refracted rays
     */
    @NonNull
    public Color getColor(@NonNull final Ray ray, final double refractionIndex, final double intensity, final int deepLevel) {
        Color resultColor = Color.BLACK;
        Relation<Point, SceneObject> traceResult = traceClosest(ray);
        if (traceResult != null) {
            Color ambient = new Color((int) (intensity * rayTracerInfo.ambient.getRed()), (int) (intensity * rayTracerInfo.ambient.getGreen()), (int) (intensity * rayTracerInfo.ambient.getBlue()));
            Color diffuse = Color.BLACK;
            Color specular = Color.BLACK;
            SceneObject intersectionedObject = traceResult.getValue();
            for (LightSource lightSource : lightSources) {
                if (!isInShadow(traceResult, lightSource)) {
                    diffuse = add(diffuse, getDiffuse(traceResult, lightSource, intensity));
                    specular = add(specular, getSpecular(traceResult, ray, lightSource, intensity));
                }
            }
            Color reflectedColor = getReflectedColor(traceResult, ray, refractionIndex, intersectionedObject.getReflectionFactor() * intensity, deepLevel);
            Color refractedColor = getRefractedColor(traceResult, ray, refractionIndex, intersectionedObject.getRefractionFactor() * intensity, deepLevel);
            //diffuse = add(diffuse, reflectedColor, refractedColor);
            resultColor = intersectionedObject.getColor(ambient, diffuse, specular);
            resultColor = add(resultColor, intersectionedObject.getColor(Color.BLACK, reflectedColor, Color.BLACK), intersectionedObject.getColor(Color.BLACK, refractedColor, Color.BLACK));
        }
        return resultColor;
    }

    /**
     * Detects if scene object is in shadow
     *
     * @param traceResult intersected scene object with intersection point by ray
     * @param lightSource lightning source
     * @return true if scene object is in shadow, else - false
     */
    boolean isInShadow(@NonNull final Relation<Point, SceneObject> traceResult, @NonNull final LightSource lightSource) {
        Point intersectionPoint = traceResult.getKey();
        Ray toLightSourceRay = new Ray(intersectionPoint, com.spbstu.raytracing.math.Vector.onNumber(lightSource.getOnPointDirection(intersectionPoint), -1));
        Point furthestPoint = traceFurthest(toLightSourceRay);
        return !(furthestPoint == null || Point.distance(intersectionPoint, furthestPoint) < 1e-5);
    }

    /**
     * Returns intersected scene object with  the closes intersection point by ray, null if no intersections
     * and null if fo every point P in collection angle between ray direction vector
     * and vector {P.x-point.x,P.y-point.y,P.z-point.z} less than 0 where point is ray is start point
     * or no intersection points
     * @param ray ray to get intersection
     * @return intersected scene object with  the closes intersection point by ray
     */
    @Nullable
    public Relation<Point, SceneObject> traceClosest(@NonNull final Ray ray) {
        List<Point> intersectionPoints = new ArrayList<Point>();
        List<Relation<Point, SceneObject>> sceneObjectByIntersectionPoints = new ArrayList<>();
        for (SceneObject sceneObject : sceneObjects) {
            Point intersectionPoint = ray.getClosest(sceneObject.getIntersectionPoints(ray));
            if (intersectionPoint != null) {
                intersectionPoints.add(intersectionPoint);
                sceneObjectByIntersectionPoints.add(new Relation<>(intersectionPoint, sceneObject));
            }
        }
        Point closestIntersectionPoint = ray.getClosest(intersectionPoints);
        if (closestIntersectionPoint == null) {
            return null;
        }
        for (Relation<Point, SceneObject> sceneObjectWithIntersectionPoint : sceneObjectByIntersectionPoints) {
            if (sceneObjectWithIntersectionPoint.getKey().equals(closestIntersectionPoint)) {
                return sceneObjectWithIntersectionPoint;
            }
        }
        return null;
    }

    /**
     * Applies ray tracing result to camera screen
     * @throws InterruptedException if error occurs while threads are running
     */
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


    /**
     * Runnable for parallel ray tracer work
     */
    public class TracerRunnable implements Runnable {
        final int threadNumber, threadNumbers;
        Camera camera;

        /**
         * Constructors defines which rays to process by thread number, camera and aviable threads number
         * @param camera camera to emit rays and write results
         * @param threadNumber thead number to  define which rays to process
         * @param threadNumbers aviable threads number
         */
        public TracerRunnable(@NonNull Camera camera,final int threadNumber,final int threadNumbers) {
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
