package com.spbstu.raytracing.sceneObject;

import java.awt.*;

/**
 * @author vva
 * @date 16.03.14
 * @description
 */
public class Material {

    /**
     * свойство материала воспринимать фоновое освещение
     */
    Component ambient;

    /**
     * свойство материала воспринимать рассеянное освещение
     */
    Component diffuse;

    /**
     * свойство материала воспринимать зеркальноe отражения
     */
    Component specular;

    int specularPower;

    double reflectionFactor;

    double refractionFactor;

    double refractionIndex;

    public Material(Component ambient, Component diffuse, Component specular, int specularPower) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.specularPower = specularPower;
        this.refractionIndex = 1;
    }

    public Material(Component ambient, Component diffuse, Component specular, int specularPower, double reflectionFactor) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.specularPower = specularPower;
        this.reflectionFactor = reflectionFactor;
        this.refractionIndex = 1;
    }

    public Material(Component ambient, Component diffuse, Component specular, int specularPower, double reflectionFactor, double refractionFactor) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.specularPower = specularPower;
        this.reflectionFactor = reflectionFactor;
        this.refractionFactor = refractionFactor;
        this.refractionIndex = 1;
    }


    public Material(Component ambient, Component diffuse, Component specular, int specularPower, double reflectionFactor, double refractionFactor, double refractionIndex) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.specularPower = specularPower;
        this.reflectionFactor = reflectionFactor;
        this.refractionFactor = refractionFactor;
        this.refractionIndex = refractionIndex;
    }

    public static class Component {
        double cR, cG, cB;

        public Component(double cR, double cG, double cB) {
            this.cR = cR;
            this.cG = cG;
            this.cB = cB;
        }
    }


    public int getColor(int ambientColor, int diffuseColor, int specularColor) {
        Color a = new Color(ambientColor);
        Color d = new Color(diffuseColor);
        Color s = new Color(specularColor);
        int r = (int) (a.getRed() * ambient.cR + d.getRed() * diffuse.cR + s.getRed() * specular.cR);
        int g = (int) (a.getGreen() * ambient.cG + d.getGreen() * diffuse.cG + s.getGreen() * specular.cG);
        int b = (int) (a.getBlue() * ambient.cB + d.getBlue() * diffuse.cB + s.getBlue() * specular.cB);
        return new Color(Math.min(255, r), Math.min(255, g), Math.min(255, b)).getRGB();
    }

}
