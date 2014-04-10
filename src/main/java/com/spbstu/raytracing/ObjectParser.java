package com.spbstu.raytracing;

import com.spbstu.raytracing.math.Point3D;
import com.spbstu.raytracing.math.Vector3D;
import com.spbstu.raytracing.sceneObject.BoundingBox;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author vva
 * @date 02.04.14
 * @description
 */
public class ObjectParser {

    public enum Descriptor {
        VERTEX("v"),
        NORMAL("vn"),
        POLYGON("f"),
        UNKNOWN("no matter");

        String value;

        Descriptor(String value) {
            this.value = value;
        }

        public static Descriptor getDescriptor(String value) {
            for (Descriptor descriptor : Descriptor.values()) {
                if (descriptor.value.equals(value)) {
                    return descriptor;
                }
            }
            return UNKNOWN;
        }
    }




    public static Relation<BoundingBox, List<ModelTriangle>> parse(String fileName) throws IOException {
        Point3D min = new Point3D(0, 0, 0), max = new Point3D(0, 0, 0);
        File file = new File(fileName);
        Path path = Paths.get(fileName);
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("no file found by path='" + fileName + "'");
        }
        List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
        List<Point3D> vertexes = new ArrayList<>();
        List<Vector3D> normals = new ArrayList<>();
        List<ModelTriangle> triangles = new ArrayList<>();
        for (String line : lines) {

            int spaceIndex = line.indexOf(" ");
            int tabIndex = line.indexOf("\t");
            int index = Math.min(spaceIndex, tabIndex) == -1 ? Math.max(spaceIndex, tabIndex) : Math.min(spaceIndex, tabIndex);
            if (line.isEmpty() || index == -1) {
                continue;
            }
            String descriptor = line.substring(0, index);
            line = line.substring(index).trim();
            switch (Descriptor.getDescriptor(descriptor)) {
                case VERTEX:
                    parseVertex(line, vertexes, min, max);
                    break;
                case NORMAL:
                    parseNormal(line, normals);
                    break;
                case POLYGON:
                    parsePolygon(line, vertexes, normals, triangles);
            }
        }

        return new Relation<>(new BoundingBox(min, max, 1e-5), triangles/*.subList(0,10)*/);
    }


    public static List<String> split(String line) {
        String partsBySpace[] = line.split(" ");
        List<String> parts = new ArrayList<>();
        for (String partBySpace : partsBySpace) {
            if (partBySpace.isEmpty()) {
                continue;
            }
            for (String partByTab : partBySpace.split("\t")) {
                if (partByTab.isEmpty()) {
                    continue;
                }
                parts.add(partByTab);
            }
        }
        return parts;
    }

    public static void parseVertex(String line, List<Point3D> vertexes, Point3D min, Point3D max) {
        List<String> parts = split(line);
        Point3D point = new Point3D(Double.parseDouble(parts.get(0)), Double.parseDouble(parts.get(1)), Double.parseDouble(parts.get(2)));
        vertexes.add(point);
        if(min.getX() > point.getX()){
            min.setX(point.getX());
        }
        if(min.getY() > point.getY()){
            min.setY(point.getY());
        }
        if(min.getZ() > point.getZ()){
            min.setZ(point.getZ());
        }

        if(max.getX() < point.getX()){
            max.setX(point.getX());
        }
        if(max.getY() < point.getY()){
            max.setY(point.getY());
        }
        if(max.getZ() < point.getZ()){
            max.setZ(point.getZ());
        }
    }

    public static void parseNormal(String line, List<Vector3D> normals) {
        List<String> parts = split(line);
        normals.add(new Vector3D(Double.parseDouble(parts.get(0)), Double.parseDouble(parts.get(1)), Double.parseDouble(parts.get(2))));
    }

    public static void parsePolygon(String line, List<Point3D> vertexes, List<Vector3D> normals, List<ModelTriangle> triangles) {
        List<String> parts = split(line);
        for (int i = 2; i < parts.size(); i++) {
            String v0Parts[] = parts.get(i - 2).split("/");
            String v1Parts[] = parts.get(i - 1).split("/");
            String v2Parts[] = parts.get(i).split("/");
            triangles.add(new ModelTriangle(
                    vertexes.get(Integer.parseInt(v0Parts[0]) - 1), vertexes.get(Integer.parseInt(v1Parts[0]) - 1), vertexes.get(Integer.parseInt(v2Parts[0]) - 1),
                    normals.get(Integer.parseInt(v0Parts[2]) - 1), normals.get(Integer.parseInt(v1Parts[2]) - 1), normals.get(Integer.parseInt(v2Parts[2]) - 1)));
        }
    }

}
