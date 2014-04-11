package com.spbstu.raytracing.sceneObject;

import com.sun.javafx.beans.annotations.NonNull;

import java.awt.*;

/**
 * Material class to identify color
 *
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

    /**
     * Constructor to make material from ambient, diffuse, specular components and specular power,
     * reflection factor equal to 0, refraction factor equal to 0,refraction index equal to 1
     *
     * @param ambient       ambient component
     * @param diffuse       diffuse component
     * @param specular      specular component
     * @param specularPower specular power
     */
    public Material(@NonNull final Component ambient, @NonNull final Component diffuse, @NonNull final Component specular,
                    final int specularPower) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.specularPower = specularPower;
        this.refractionIndex = 1;
        reflectionFactor = 0;
        refractionFactor = 0;
    }


    /**
     * Constructor to make material from ambient, diffuse, specular components ,specular power,
     * and reflection factor, refraction factor equal to 0,refraction index equal to 1
     *
     * @param ambient          ambient component
     * @param diffuse          diffuse component
     * @param specular         specular component
     * @param specularPower    specular power
     * @param reflectionFactor reflection factor
     */
    public Material(@NonNull final Component ambient,@NonNull final Component diffuse,@NonNull final Component specular,
                    final int specularPower, final double reflectionFactor) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.specularPower = specularPower;
        this.reflectionFactor = reflectionFactor;
        this.refractionIndex = 1;
        refractionFactor = 0;
    }


    /**
     * Constructor to make material from ambient, diffuse, specular components ,specular power,
     * reflection factor, refraction factor and refraction index equal to 1
     *
     * @param ambient          ambient component
     * @param diffuse          diffuse component
     * @param specular         specular component
     * @param specularPower    specular power
     * @param reflectionFactor reflection factor
     * @param refractionFactor refraction factor
     */
    public Material(@NonNull final Component ambient,@NonNull final Component diffuse,@NonNull final Component specular,
                    final int specularPower,final double reflectionFactor,final double refractionFactor) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.specularPower = specularPower;
        this.reflectionFactor = reflectionFactor;
        this.refractionFactor = refractionFactor;
        this.refractionIndex = 1;
    }

    /**
     * Constructor to make material from ambient, diffuse, specular components ,specular power,
     * reflection factor, refraction factor and refraction
     *
     * @param ambient          ambient component
     * @param diffuse          diffuse component
     * @param specular         specular component
     * @param specularPower    specular power
     * @param reflectionFactor reflection factor
     * @param refractionFactor refraction factor
     * @param refractionIndex  refraction index to air
     */
    public Material(@NonNull final Component ambient,@NonNull final Component diffuse,@NonNull final Component specular,
                    final int specularPower,final double reflectionFactor,final double refractionFactor,final double refractionIndex) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.specularPower = specularPower;
        this.reflectionFactor = reflectionFactor;
        this.refractionFactor = refractionFactor;
        this.refractionIndex = refractionIndex;
    }

    /**
     * Class to define material property to perceive lightning
     */
    public static class Component {
        final double cR, cG, cB;

        /**
         * Constructor defining material property to perceive lightning
         * @param cR property to perceive red lightning from 0 to 1
         * @param cG property to perceive green lightning from 0 to 1
         * @param cB property to perceive blue lightning  from 0 to 1
         */
        public Component(final double cR,final double cG,final double cB) {
            this.cR = cR;
            this.cG = cG;
            this.cB = cB;
        }
    }


    /**
     * Returns object final color
     * @param ambientColor ambient color
     * @param diffuseColor diffuse color
     * @param specularColor specular color
     * @return object final color
     */
    public int getColor(final int ambientColor,final int diffuseColor,final int specularColor) {
        Color a = new Color(ambientColor);
        Color d = new Color(diffuseColor);
        Color s = new Color(specularColor);
        int r = (int) (a.getRed() * ambient.cR + d.getRed() * diffuse.cR + s.getRed() * specular.cR);
        int g = (int) (a.getGreen() * ambient.cG + d.getGreen() * diffuse.cG + s.getGreen() * specular.cG);
        int b = (int) (a.getBlue() * ambient.cB + d.getBlue() * diffuse.cB + s.getBlue() * specular.cB);
        return new Color(Math.min(255, r), Math.min(255, g), Math.min(255, b)).getRGB();
    }

}
