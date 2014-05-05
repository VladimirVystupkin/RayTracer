package com.spbstu.raytracing;

import com.spbstu.raytracing.math.Point;
import com.spbstu.raytracing.math.Vector;
import com.spbstu.raytracing.sceneObject.BoundingBox;
import com.spbstu.raytracing.sceneObject.Material;
import com.spbstu.raytracing.sceneObject.ModelTriangle;
import com.spbstu.raytracing.sceneObject.Triangle;
import com.spbstu.raytracing.sceneObject.attributes.Attribute;
import com.spbstu.raytracing.sceneObject.attributes.Attributes;



import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vva
 */
public class ModelObjectParserDefault {

    private enum BoundingBoxInfo {
        MIN_X,
        MIN_Y,
        MIN_Z,
        MAX_X,
        MAX_Y,
        MAX_Z
    }

    private enum Descriptor {
        VERTEX("v"),
        NORMAL("vn"),
        POLYGON("f"),
        UNKNOWN("no matter");

        String value;

        Descriptor(final String value) {
            this.value = value;
        }


        public static Descriptor getDescriptor(final String value) {
            for (Descriptor descriptor : Descriptor.values()) {
                if (descriptor.value.equals(value)) {
                    return descriptor;
                }
            }
            return UNKNOWN;
        }
    }


    public static Relation<BoundingBox, List<ModelTriangle>> parse(final String fileName) throws IOException {
        File file = new File(fileName);
        Path path = Paths.get(fileName);
        Map<BoundingBoxInfo, Double> boxInfoMap = new HashMap<>();
        boxInfoMap.put(BoundingBoxInfo.MIN_X, 0.0);
        boxInfoMap.put(BoundingBoxInfo.MIN_Y, 0.0);
        boxInfoMap.put(BoundingBoxInfo.MIN_Z, 0.0);
        boxInfoMap.put(BoundingBoxInfo.MAX_X, 0.0);
        boxInfoMap.put(BoundingBoxInfo.MAX_Y, 0.0);
        boxInfoMap.put(BoundingBoxInfo.MAX_Z, 0.0);
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("no file found by path='" + fileName + "'");
        }
        List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
        List<Point> vertexes = new ArrayList<>();
        List<Vector> normals = new ArrayList<>();
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
                    parseVertex(line, vertexes, boxInfoMap);
                    break;
                case NORMAL:
                    parseNormal(line, normals);
                    break;
                case POLYGON:
                    parsePolygon(line, vertexes, normals, triangles);
            }
        }

        return new Relation<>(new BoundingBox(
                new Point(boxInfoMap.get(BoundingBoxInfo.MIN_X), boxInfoMap.get(BoundingBoxInfo.MIN_Y), boxInfoMap.get(BoundingBoxInfo.MIN_Z)),
                new Point(boxInfoMap.get(BoundingBoxInfo.MAX_X), boxInfoMap.get(BoundingBoxInfo.MAX_Y), boxInfoMap.get(BoundingBoxInfo.MAX_Z)), 1e-5), triangles);
    }


    public static List<String> split(final String line) {
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

    public static void parseVertex(final String line, List<Point> vertexes, final Map<BoundingBoxInfo, Double> boxInfoMap) {
        List<String> parts = split(line);
        Point point = new Point(Double.parseDouble(parts.get(0)), Double.parseDouble(parts.get(1)), Double.parseDouble(parts.get(2)));
        vertexes.add(point);
        if (boxInfoMap.get(BoundingBoxInfo.MIN_X) > point.getX()) {
            boxInfoMap.put(BoundingBoxInfo.MIN_X, point.getX());
        }
        if (boxInfoMap.get(BoundingBoxInfo.MIN_Y) > point.getY()) {
            boxInfoMap.put(BoundingBoxInfo.MIN_Y, point.getY());
        }
        if (boxInfoMap.get(BoundingBoxInfo.MIN_Z) > point.getZ()) {
            boxInfoMap.put(BoundingBoxInfo.MIN_Z, point.getZ());
        }

        if (boxInfoMap.get(BoundingBoxInfo.MAX_X) < point.getX()) {
            boxInfoMap.put(BoundingBoxInfo.MAX_X, point.getX());
        }
        if (boxInfoMap.get(BoundingBoxInfo.MAX_Y) < point.getY()) {
            boxInfoMap.put(BoundingBoxInfo.MAX_Y, point.getY());
        }
        if (boxInfoMap.get(BoundingBoxInfo.MAX_Z) < point.getZ()) {
            boxInfoMap.put(BoundingBoxInfo.MAX_Z, point.getZ());
        }

    }

    public static void parseNormal(final String line, List<Vector> normals) {
        List<String> parts = split(line);
        normals.add(new Vector(Double.parseDouble(parts.get(0)), Double.parseDouble(parts.get(1)), Double.parseDouble(parts.get(2))));
    }

    public static void parsePolygon(final String line, final List<Point> vertexes,
                                    final List<Vector> normals, List<ModelTriangle> triangles) {
        List<String> parts = split(line);
        Triangle triangle = new Triangle(vertexes.get(-1 + Integer.parseInt(parts.get(0))),
                vertexes.get(-1 + Integer.parseInt(parts.get(1))), vertexes.get(-1 + Integer.parseInt(parts.get(2)))
                , new Material(new Material.Component(0, 0, 0), new Material.Component(0, 0, 0), new Material.Component(0, 0, 0), 0), new HashMap<Attributes.AttributeName, Attribute>());
        triangles.add(new ModelTriangle(vertexes.get(-1 + Integer.parseInt(parts.get(0))), vertexes.get(-1 + Integer.parseInt(parts.get(1))), vertexes.get(-1 + Integer.parseInt(parts.get(2)))
                , triangle.getNormal(), triangle.getNormal(), triangle.getNormal()));
    }

}
