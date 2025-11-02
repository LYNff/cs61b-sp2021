package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.util.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private Date timestamp;
    private String parent;
    private String mother;
    // Use hashmap to express the file in this snapshot.
    private HashMap<String, String> fileSet;
    // Remember the sha1-name of the commit node.
    private String name;

    /* TODO: fill in the rest of this class. */
    public Commit() {
        message = "initial commit";
        timestamp = new Date(0); // TODO: verify this is the epoch data.
        parent = null;
        mother = null;
        fileSet = new HashMap<>();
        name = null;
    }

    // Get objects from Commit.
    public String getMessage() {
        return message;
    }
    public Date getTimeStamp() {
        return timestamp;
    }
    public String getParent() {
        return parent;
    }
    public String getMother() {
        return mother;
    }
    public HashMap<String, String> getFileset() {
        return fileSet;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setParent(String parent) {
        this.parent = parent;
    }
    public void setMother(String mother) {
        this.mother = mother;
    }

    public static Commit cloneCommit(Commit headPointer, String message) {
        Commit clone = new Commit();
        clone.message = message;
        clone.timestamp = new Date();
        clone.fileSet = headPointer.fileSet;
        return clone;
    }
}
