package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.math.Matrix;
import com.spbstu.raytracing.math.Ray;
import com.spbstu.raytracing.math.Point3D;
import com.spbstu.raytracing.math.Vector;
import com.spbstu.raytracing.sceneObject.attributes.Attribute;
import com.spbstu.raytracing.sceneObject.attributes.Attributes;

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

    Matrix matrix, inverted;

    protected SceneObject(Material material, Map<Attributes.AttributeName, Attribute> attributesMap) {
        this.material = material;
        this.matrix = Attributes.getCommonMatrix(attributesMap);
        this.inverted = Matrix.invert(matrix);
    }


    public abstract Vector getStaticNormal(Point3D point);

    public abstract List<Point3D> getStaticCrossPoints(Ray ray);

    public Vector getNormal(Point3D point) {
        Point3D transformed = Matrix.multiply(matrix, point);

        Vector staticNormal = getStaticNormal(transformed);
        Point3D startPoint = new Point3D(0, 0, 0);
        Point3D endPoint = staticNormal.toPoint3D();
        Vector normal = new Vector(Matrix.multiply(inverted, startPoint),
                Matrix.multiply(inverted, endPoint));
        normal.normalize();
        return normal;
    }

    public List<Point3D> getCrossPoints(Ray ray) {
        List<Point3D> crossPoints = new ArrayList<>();
        Point3D startPoint = ray.getPoint();
        Point3D endPoint = Point3D.translate(startPoint, ray.getDirectionVector());
        Ray transformedRay = new Ray(Matrix.multiply(matrix, startPoint),
                Matrix.multiply(matrix, endPoint));
        for (Point3D staticCrossPoint : getStaticCrossPoints(transformedRay)) {
            crossPoints.add(Matrix.multiply(inverted, staticCrossPoint));
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
