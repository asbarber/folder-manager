package org.umich.asbarber.fm.file.manip;

import org.umich.asbarber.fm.file.IndexedFile;
import org.umich.asbarber.fm.file.format.ComponentSystem;
import java.util.List;
import org.umich.asbarber.fm.file.Directory;

/**
 *
 * @author Aaron
 */
public class FileManipulator {

    private Directory directory;
    private String delimiter;

    public FileManipulator(Directory dir, String delim) {
        directory = dir;
        delimiter = delim;
    }

    public void setDelimiter(String delim) {
        delimiter = delim;
    }

    public void enumerate() {
        for (Directory subdir : directory.getSubdirectories()) {
            FileManipulator tmp = new FileManipulator(subdir, delimiter);
            tmp.enumerate();
        }

        List<IndexedFile> files = directory.getFiles();
        for (int i = 0; i < files.size(); i++) {
            files.get(i).renameFile("", i + 1);
        }
    }

    public void enumerateTag() {
        for (Directory subdir : directory.getSubdirectories()) {
            FileManipulator tmp = new FileManipulator(subdir, delimiter);
            tmp.enumerateTag();
        }

        List<IndexedFile> files = directory.getFiles();
        for (int i = 0; i < files.size(); i++) {
            files.get(i).renameFile(directory.getName(), i + 1);
        }
    }

    public void denumerate() {
        for (Directory subdir : directory.getSubdirectories()) {
            FileManipulator tmp = new FileManipulator(subdir, delimiter);
            tmp.denumerate();
        }

        List<IndexedFile> files = directory.getFiles();
        for (int i = 0; i < files.size(); i++) {
            files.get(i).denumerate();
        }
    }

    public void implode() {
        for (Directory subdir : directory.getSubdirectories()) {
            //Traverse Subdirectories
            FileManipulator tmp = new FileManipulator(subdir, delimiter);
            tmp.implode();

            //Rename / Movement
            for (IndexedFile file : tmp.directory.getFiles()) {
                file.renameFile(file.getParentDirectory().getName() + delimiter + file.getName());
                file.moveUp();
            }

            //Clean Up
            directory.removeAllSubdirectories();
        }

    }

    public void explode() {
        for (IndexedFile subfile : directory.getFiles()) {
            subfile.explode(delimiter);

            //Renames to original name
            ComponentSystem comps = new ComponentSystem(subfile.getName(), delimiter);
            comps.explode();
            String origName = comps.getPieces().get(comps.getPieces().size() - 1);
            subfile.renameFile(origName.trim());
        }
    }

    public void explodeTag() {
        for (IndexedFile subfile : directory.getFiles()) {
            subfile.explode(delimiter);
        }
    }
}
