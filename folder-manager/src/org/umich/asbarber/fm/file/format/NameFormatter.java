package org.umich.asbarber.fm.file.format;

/**
 *
 * @author Aaron
 */
public class NameFormatter {

    private String key;

    public NameFormatter(String name) {
        key = name;
    }

    public void trimExtension() {
        int i = key.lastIndexOf(".");

        if (i != -1) {
            key = key.substring(0, i);
        }
    }

    public void trimNumber() {
        int open = key.lastIndexOf("(");
        int close = key.lastIndexOf(")");

        if (open != -1 && close != -1 && close > open) {

            key = key.substring(0, open).trim() + key.substring(close + 1).trim();
        }
    }

    public String getNumber() {
        int open = key.lastIndexOf("(");
        int close = key.lastIndexOf(")");

        if (open != -1 && close != -1 && close > open) {
            return key.substring(open, close + 1);
        } else {
            return "";
        }
    }

    public void trimLastDirectory() {
        int i = key.lastIndexOf("\\");

        if (i != -1) {
            key = key.substring(0, i);
        }
    }

    public String getName() {
        return key;
    }
}
