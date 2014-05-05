package com.spbstu.raytracing.sceneObject;



import java.awt.*;
import java.util.HashMap;

/**
 * @author vva
 */
public class Material {

    final Component ambient;

    final Component diffuse;

    final Component specular;

    final int specularPower;

    final double reflectionFactor;

    final double refractionFactor;

    final double refractionIndex;

    public Material(final Component ambient, final Component diffuse, final Component specular,
                    final int specularPower) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.specularPower = specularPower;
        this.refractionIndex = 1;
        reflectionFactor = 0;
        refractionFactor = 0;
    }

    public Material(final Component ambient, final Component diffuse, final Component specular,
                    final int specularPower, final double reflectionFactor) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.specularPower = specularPower;
        this.reflectionFactor = reflectionFactor;
        this.refractionIndex = 1;
        refractionFactor = 0;
    }

    public Material(final Component ambient, final Component diffuse, final Component specular,
                    final int specularPower, final double reflectionFactor, final double refractionFactor) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.specularPower = specularPower;
        this.reflectionFactor = reflectionFactor;
        this.refractionFactor = refractionFactor;
        this.refractionIndex = 1;
    }

    public Material(final Component ambient, final Component diffuse, final Component specular,
                    final int specularPower, final double reflectionFactor, final double refractionFactor, final double refractionIndex) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.specularPower = specularPower;
        this.reflectionFactor = reflectionFactor;
        this.refractionFactor = refractionFactor;
        this.refractionIndex = refractionIndex;
    }

    public static class Component {
        final double cR, cG, cB;

        public Component(final double cR, final double cG, final double cB) {
            this.cR = cR;
            this.cG = cG;
            this.cB = cB;
        }
    }


    public int getColor(final int ambientColor, final int diffuseColor, final int specularColor) {
        Color a = new Color(ambientColor);
        Color d = new Color(diffuseColor);
        Color s = new Color(specularColor);
        int r = (int) (a.getRed() * ambient.cR + d.getRed() * diffuse.cR + s.getRed() * specular.cR);
        int g = (int) (a.getGreen() * ambient.cG + d.getGreen() * diffuse.cG + s.getGreen() * specular.cG);
        int b = (int) (a.getBlue() * ambient.cB + d.getBlue() * diffuse.cB + s.getBlue() * specular.cB);
        return new Color(Math.min(255, r), Math.min(255, g), Math.min(255, b)).getRGB();
    }


    public static Material fromMap(final HashMap hashMap) {
        Component ambient = new Component(
                Double.parseDouble(((HashMap) hashMap.get("ambient")).get("r").toString()),
                Double.parseDouble(((HashMap) hashMap.get("ambient")).get("g").toString()),
                Double.parseDouble(((HashMap) hashMap.get("ambient")).get("b").toString()));
        Component diffuse = new Component(
                Double.parseDouble(((HashMap) hashMap.get("diffuse")).get("r").toString()),
                Double.parseDouble(((HashMap) hashMap.get("diffuse")).get("g").toString()),
                Double.parseDouble(((HashMap) hashMap.get("diffuse")).get("b").toString()));
        Component specular = new Component(
                Double.parseDouble(((HashMap) hashMap.get("specular")).get("r").toString()),
                Double.parseDouble(((HashMap) hashMap.get("specular")).get("g").toString()),
                Double.parseDouble(((HashMap) hashMap.get("specular")).get("b").toString()));

        int specularPower = Integer.parseInt(hashMap.get("specular_power").toString());
        double reflectionFactor = hashMap.containsKey("reflection_factor") ? Double.parseDouble(hashMap.get("reflection_factor").toString()) : 0;
        double refractionFactor = hashMap.containsKey("refraction_factor") ? Double.parseDouble(hashMap.get("refraction_factor").toString()) : 0;
        double refractionIndex = hashMap.containsKey("refraction_index") ? Double.parseDouble(hashMap.get("refraction_index").toString()) : 1;
        return new Material(ambient, diffuse, specular, specularPower, reflectionFactor, refractionFactor, refractionIndex);
    }

}
