package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.math.*;
import com.spbstu.raytracing.math.Point;
import com.spbstu.raytracing.sceneObject.attributes.Attribute;
import com.spbstu.raytracing.sceneObject.attributes.Attributes;
import com.sun.javafx.beans.annotations.NonNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Abstract class for ray tracing scene object
 * @author vva
 */
public abstract class SceneObject {

    final Material material;
    final Matrix matrix, inverted;

    /**
     * Scene object constructor by {@link com.spbstu.raytracing.sceneObject.Material} material and
     * map from attributes({@link com.spbstu.raytracing.sceneObject.attributes.Attributes.AttributeName}->{@link com.spbstu.raytracing.sceneObject.attributes.Attribute})
     *
     * @param material      object material
     * @param attributesMap map from attributes
     * @see com.spbstu.raytracing.sceneObject.Material
     * @see com.spbstu.raytracing.sceneObject.attributes.Attribute
     */
    protected SceneObject(@NonNull final Material material, @NonNull final Map<Attributes.AttributeName, Attribute> attributesMap) {
        this.material = material;
        this.matrix = Attributes.getCommonMatrix(attributesMap);
        this.inverted = Matrix.invert(matrix);
    }


    /**
     * Returns object normal from point without 3D matrix conversation
     * @param point point to get normal
     * @return Object normal from point without 3D matrix conversation
     */
    @NonNull
    abstract Vector getStaticNormal(@NonNull final Point point);

    /**
     * Returns object intersection points without 3D matrix conversation
     * @param ray ray to get intersection points
     * @return object intersection points without 3D matrix conversation
     */
    @NonNull
    public abstract List<Point> getStaticIntersectionPoints(@NonNull final Ray ray);


    /**
     * Returns object normal from point with current 3D matrix conversation
     * @param point point to get normal
     * @return Object normal from point with current 3D matrix conversation
     */
    @NonNull
    public Vector getNormal(@NonNull final Point point) {
        Point transformed = Matrix.multiply(matrix, point);

        Vector staticNormal = getStaticNormal(transformed);
        Point startPoint = new Point(0, 0, 0);
        Point endPoint = staticNormal.toPoint3D();
        return new Vector(Matrix.multiply(inverted, startPoint),
                Matrix.multiply(inverted, endPoint)).getNormalized();
    }

    /**
     * Returns object intersection points with current 3D matrix conversation
     * @param ray ray to get intersection points
     * @return object intersection points with current 3D matrix conversation
     */
    @NonNull
    public List<Point> getIntersectionPoints(@NonNull final Ray ray) {
        List<Point> intersectionPoints = new ArrayList<>();
        Point startPoint = ray.getPoint();
        Point endPoint = Point.translate(startPoint, ray.getDirectionVector());
        Ray transformedRay = new Ray(Matrix.multiply(matrix, startPoint),
                Matrix.multiply(matrix, endPoint));
        for (com.spbstu.raytracing.math.Point staticIntersectionPoint : getStaticIntersectionPoints(transformedRay)) {
            intersectionPoints.add(Matrix.multiply(inverted, staticIntersectionPoint));
        }
        return intersectionPoints;
    }


    /**
     * Returns result object color depending on material
     * @param ambientColor  ambient color
     * @param diffuseColor diffuse color
     * @param specularColor specular color
     * @return result object color depending on material
     * @see com.spbstu.raytracing.sceneObject.Material
     */
    @NonNull
    public Color getColor(@NonNull final Color ambientColor,@NonNull final Color diffuseColor,@NonNull final Color specularColor) {
        return new Color(material.getColor(ambientColor.getRGB(), diffuseColor.getRGB(), specularColor.getRGB()));
    }

    /**
     * Returns material specular power
     * @return material specular power
     */
    public int getSpecularPower() {
        return material.specularPower;
    }

    /**
     * Returns material reflection factor
     * @return material reflection factor
     */
    public double getReflectionFactor() {
        return material.reflectionFactor;
    }

    /**
     * Returns material refraction factor
     * @return material refraction factor
     */
    public double getRefractionFactor() {
        return material.refractionFactor;
    }

    /**
     * Returns material refraction index to air
     * @return material refraction index to air
     */
    public double getRefractionIndex() {
        return material.refractionIndex;
    }

}
