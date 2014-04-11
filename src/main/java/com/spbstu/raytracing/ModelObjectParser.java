package com.spbstu.raytracing;

import com.spbstu.raytracing.math.Point;
import com.spbstu.raytracing.math.Vector;
import com.spbstu.raytracing.sceneObject.BoundingBox;
import com.spbstu.raytracing.sceneObject.ModelTriangle;
import com.sun.istack.internal.Nullable;
import com.sun.javafx.beans.annotations.NonNull;

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
 * Class for *.obj file parsing
 *
 * @author vva
 */
public class ModelObjectParser {

    /**
     * Enumeration for bounding box attributes
     */
    private enum BoundingBoxInfo {
        MIN_X,
        MIN_Y,
        MIN_Z,
        MAX_X,
        MAX_Y,
        MAX_Z
    }

    /**
     * Parsable  *.obj file attributes
     */
    private enum Descriptor {
        VERTEX("v"),
        NORMAL("vn"),
        POLYGON("f"),
        UNKNOWN("no matter");

        String value;

        /**
         * Constructor defining parsable  *.obj file attribute by value
         *
         * @param value parsable  *.obj file attribute value
         */
        Descriptor(@NonNull final String value) {
            this.value = value;
        }

        /**
         * Returns parsble attribute by value and UNKNOWN if no parsable  *.obj file attributes contain specified value
         *
         * @param value parsable  *.obj file attribute value
         * @return parsable  *.obj file attribute
         */
        @NonNull
        public static Descriptor getDescriptor(@Nullable final String value) {
            for (Descriptor descriptor : Descriptor.values()) {
                if (descriptor.value.equals(value)) {
                    return descriptor;
                }
            }
            return UNKNOWN;
        }
    }


    /**
     * Returns triangle list with bounding box parsed from specified file
     *
     * @param fileName filename of file to parse
     * @return triangle list with bounding box parsed from specified file
     * @throws IOException                        if an I/O error occurs reading from the file or a malformed or
     *                                            unmappable byte sequence is read
     * @throws java.lang.IllegalArgumentException if file doesn't exist
     */
    @NonNull
    public static Relation<BoundingBox, List<ModelTriangle>> parse(@NonNull final String fileName) throws IOException {
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


    /**
     * Splits line by space and tab
     * @param line line to split
     * @return list of splitted parts of line by space and tab
     */
    @NonNull
    public static List<String> split(@NonNull final String line) {
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

    /**
     * Parses line to vertex and puts it into vertexes list
     * @param line line to parse
     * @param vertexes vertexes list
     * @param boxInfoMap bounding box attributes map
     */
    public static void parseVertex(@NonNull final String line,@NonNull List<Point> vertexes,@NonNull final Map<BoundingBoxInfo, Double> boxInfoMap) {
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

    /**
     * Parses line to normal and puts it into normals list
     * @param line line to parse
     * @param normals normals list
     */
    public static void parseNormal(@NonNull final String line,@NonNull List<Vector> normals) {
        List<String> parts = split(line);
        normals.add(new Vector(Double.parseDouble(parts.get(0)), Double.parseDouble(parts.get(1)), Double.parseDouble(parts.get(2))));
    }

    /**
     * Parses line to triangles and puts it into  triangles list
     * @param line line to parse
     * @param vertexes  vertexes list
     * @param normals normals list
     * @param triangles triangles list
     */
    public static void parsePolygon(@NonNull final String line,@NonNull final List<Point> vertexes,
                                    @NonNull final List<Vector> normals,@NonNull List<ModelTriangle> triangles) {
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
