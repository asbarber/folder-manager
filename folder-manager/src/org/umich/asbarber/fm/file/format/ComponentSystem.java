package org.umich.asbarber.fm.file.format;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author Aaron
 */
public class ComponentSystem {

    private String value;
    private String delimiter;
    private List<String> components;

    public ComponentSystem(String name, String delim) {
        value = name;
        delimiter = delim;
        components = new ArrayList<String>();
    }

    public void explode() {
        StringTokenizer conch = new StringTokenizer(value, delimiter);

        while (conch.hasMoreElements()) {
            components.add((String) conch.nextElement());
        }
    }

    public void reform() {
        value = "";

        //Adds all components in new order with delim in between
        for (String s : components) {
            value += s + delimiter;
        }

        //Trims last delim
        value = value.substring(value.length() - delimiter.length());
    }

    public String export() {
        return value;
    }

    public List<String> getPieces() {
        return components;
    }

    public void reset() {
        components.clear();
        explode();
    }

    public void juggle(int[] order) {
        List<String> copy = new ArrayList(components.size());
        Collections.copy(copy, components);

        components.clear();

        //All of ordered chain
        for (int i = 0; i < order.length; i++) {
            int origIndex = order[i];
            components.add(copy.get(origIndex));
        }
    }

    public void rotate(int rotations) {
        Collections.rotate(components, rotations);
    }

    public void reverse() {
        Collections.reverse(components);
    }

    public void swap(int i1, int i2) {
        Collections.swap(components, i2, i1);
    }

    public void add(String val) {
        components.add(val);
    }

    public void add(int index, String val) {
        components.add(index, val);
    }

    public void delete(int index) {
        components.remove(index);
    }

}
