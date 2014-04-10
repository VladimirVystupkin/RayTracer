package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.math.Matrix3D;
import com.spbstu.raytracing.math.Ray3D;
import com.spbstu.raytracing.math.Point3D;
import com.spbstu.raytracing.math.Vector3D;
import com.sun.istack.internal.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author vva
 * @date 10.03.14
 * @description
 */
public abstract class SceneObject {

    //    Matrix3D matrix;
    Material material;

    Matrix3D matrix, inverted;

    protected SceneObject(Material material, Map<Attributes.Attribute, Matrix3D> attributesMap) {
        this.material = material;
        this.matrix = Attributes.getCommonMatrix(attributesMap);
        this.inverted = Matrix3D.invert(matrix);
    }


    public abstract Vector3D getStaticNormal(Point3D point);

    public abstract List<Point3D> getStaticCrossPoints(Ray3D ray);

    public Vector3D getNormal(Point3D point) {
        Point3D transformed = Matrix3D.multiply(matrix, point);

        Vector3D staticNormal = getStaticNormal(transformed);
        Point3D startPoint = new Point3D(0, 0, 0);
        Point3D endPoint = staticNormal.toPoint3D();
        Vector3D normal = new Vector3D(Matrix3D.multiply(inverted, startPoint),
                Matrix3D.multiply(inverted, endPoint));
        normal.normalize();
        return normal;
    }

    public List<Point3D> getCrossPoints(Ray3D ray) {
        List<Point3D> crossPoints = new ArrayList<>();
        Point3D startPoint = ray.getPoint();
        Point3D endPoint = Point3D.translate(startPoint, ray.getDirectionVector());
        Ray3D transformedRay = new Ray3D(Matrix3D.multiply(matrix, startPoint),
                Matrix3D.multiply(matrix, endPoint));
        for (Point3D staticCrossPoint : getStaticCrossPoints(transformedRay)) {
            crossPoints.add(Matrix3D.multiply(inverted, staticCrossPoint));
        }
        return crossPoints;
    }


    public int getColor(int ambientColor, int diffuseColor, int specularColor) {
        return material.getColor(ambientColor, diffuseColor, specularColor);
    }

    public Color getColor(Color ambientColor, Color diffuseColor, Color specularColor) {
        return new Color(material.getColor(ambientColor.getRGB(), diffuseColor.getRGB(), specularColor.getRGB()));
    }

    public int getSpecularPower() {
        return material.specularPower;
    }

    public double getReflectionFactor() {
        return material.reflectionFactor;
    }

    public double getRefractionFactor() {
        return material.refractionFactor;
    }

    public double getRefractionIndex(){
        return   material.refractionIndex;
    }

}
