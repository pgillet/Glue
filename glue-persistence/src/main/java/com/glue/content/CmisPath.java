package com.glue.content;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * An object that may be used to locate a CMIS object in a repository. The
 * methods in this class are inspired from {@link java.nio.file.Path}.
 * 
 * @author pgillet
 */
public class CmisPath implements Comparable<CmisPath>, Iterable<CmisPath> {

    private static final String SEPARATOR = "/";
    private String[] pathElems;
    private boolean absolute;

    /**
     * @throws IllegalArgumentException
     *             if path is whitespace, empty ("") or null.
     */
    public CmisPath(String path) {

	if (StringUtils.isBlank(path)) {
	    throw new IllegalArgumentException(
		    "path is whitespace, empty (\"\") or null");
	}

	this.absolute = path.startsWith(SEPARATOR);
	// We do not use String#split(String) here, as it returns extra empty
	// name elements when the specified path has a leading or trailing
	// separator.
	this.pathElems = StringUtils.split(path, SEPARATOR);
    }

    /**
     * @throws NullPointerException
     *             If the path argument is null
     */
    protected CmisPath(boolean absolute, String... pathElems) {
	pathElems.hashCode();
	this.absolute = absolute;
	this.pathElems = pathElems;
    }

    /**
     * Tells whether or not this path is absolute, i.e. it starts with a leading
     * "/". An absolute path is complete in that it doesn't need to be combined
     * with other path information in order to locate a file.
     * 
     * @return true if, and only if, this path is absolute
     */
    public boolean isAbsolute() {
	return absolute;
    }

    /**
     * Returns the name of this path. The name is the farthest element from the
     * root in the directory hierarchy.
     * 
     * @return the name of this path
     */
    public String getName() {
	return pathElems[pathElems.length - 1];
    }

    /**
     * Returns the parent path, or null if this path does not have a parent.
     * 
     * @return the parent path, or null if this path does not have a parent
     */
    public CmisPath getParent() {
	if (pathElems.length == 1) {
	    return null;
	}

	int len = pathElems.length - 1;
	String[] dest = new String[len];
	System.arraycopy(pathElems, 0, dest, 0, len);
	return new CmisPath(absolute, dest);
    }

    /**
     * Returns the number of name elements in the path.
     * 
     * @return the number of elements in the path, or 0 if this path only
     *         represents a root component
     */
    public int getNameCount() {
	return pathElems.length;
    }

    /**
     * Returns a name element of this path as a CmisPath object.
     * 
     * @param index
     *            the index of the element
     * @return the name element
     * @throws IllegalArgumentException
     *             if index is negative, index is greater than or equal to the
     *             number of elements, or this path has zero name elements
     */
    public CmisPath getName(int index) {
	return new CmisPath(pathElems[index]);
    }

    /**
     * Returns a relative Path that is a subsequence of the name elements of
     * this path.
     * 
     * @param beginIndex
     *            the index of the first element, inclusive
     * @param endIndex
     *            the index of the last element, exclusive
     * @return a new CmisPath object that is a subsequence of the name elements
     *         in this CmisPath
     * @throws IllegalArgumentException
     *             if beginIndex is negative, or greater than or equal to the
     *             number of elements. If endIndex is less than or equal to
     *             beginIndex, or larger than the number of elements.
     */
    public CmisPath subpath(int beginIndex, int endIndex) {
	int len = endIndex - beginIndex;
	String[] dest = new String[len];
	boolean absolute = this.absolute && (beginIndex == 0);
	System.arraycopy(pathElems, beginIndex, dest, 0, len);
	return new CmisPath(absolute, dest);
    }

    /**
     * Tests if this path starts with the given path.
     * 
     * @param other
     *            the given path
     * @return true if this path starts with the given path; otherwise false
     */
    public boolean startsWith(CmisPath other) {
	if (other.pathElems.length > pathElems.length) {
	    return false;
	}

	for (int i = 0; i < other.pathElems.length; i++) {
	    if (!pathElems[i].equals(other.pathElems[i])) {
		return false;
	    }
	}

	return true;
    }

    /**
     * @see #startsWith(CmisPath)
     */
    public boolean startsWith(String other) {
	return startsWith(new CmisPath(other));
    }

    /**
     * Tests if this path ends with the given path.
     * 
     * @param other
     *            the given path
     * @return true if this path ends with the given path; otherwise false
     */
    public boolean endsWith(CmisPath other) {
	if (other.pathElems.length > pathElems.length) {
	    return false;
	}

	for (int i = other.pathElems.length - 1; i >= 0; i--) {
	    if (!pathElems[i].equals(other.pathElems[i])) {
		return false;
	    }
	}

	return true;
    }

    /**
     * @see #endsWith(CmisPath)
     */
    public boolean endsWith(String other) {
	return endsWith(new CmisPath(other));
    }

    /**
     * Resolves the given path against this path. If the other parameter is an
     * absolute path then this method trivially returns other. Otherwise this
     * method considers this path to be a directory and resolves the given path
     * against this path. This method only handles the simplest case, i.e. it
     * simply joins the given path to this path and returns a resulting path
     * that ends with the given path.
     * 
     * @param other
     *            the path to resolve against this path
     * @return the resulting path
     */
    public CmisPath resolve(CmisPath other) {
	if (other.isAbsolute()) {
	    return other;
	}

	String[] newPathElems = new String[pathElems.length
		+ other.pathElems.length];
	System.arraycopy(pathElems, 0, newPathElems, 0, pathElems.length);
	System.arraycopy(other.pathElems, 0, newPathElems, pathElems.length,
		other.pathElems.length);

	return new CmisPath(absolute, newPathElems);
    }

    /**
     * @see #resolve(CmisPath)
     */
    public CmisPath resolve(String other) {
	return resolve(new CmisPath(other));
    }

    /**
     * Not yet implemented.
     * 
     * @return
     */
    public CmisPath normalize() {
	// TODO Auto-generated method stub
	return null;
    }

    /**
     * Not yet implemented.
     * 
     * @return
     */
    public CmisPath relativize(CmisPath other) {
	// TODO Auto-generated method stub
	return null;
    }

    /**
     * Not yet implemented.
     * 
     * @return
     */
    public CmisPath toAbsolutePath() {
	// TODO Auto-generated method stub
	return null;
    }

    public Iterator<CmisPath> iterator() {
	List<CmisPath> l = new ArrayList<CmisPath>(pathElems.length);
	for (String elem : pathElems) {
	    l.add(new CmisPath(elem));
	}
	return l.iterator();
    }

    public int compareTo(CmisPath other) {
	return getPath().compareTo(other.getPath());
    }

    /**
     * Returns this path as a string.
     * 
     * @return
     */
    public String getPath() {

	String path = StringUtils.join(pathElems, SEPARATOR);

	if (absolute) {
	    // Adds the leading slash character
	    path = SEPARATOR + path;
	}

	return path;
    }

    /**
     * Returns the string representation of this path.
     */
    public String toString() {
	return getPath();
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (absolute ? 1231 : 1237);
	result = prime * result + Arrays.hashCode(pathElems);
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	CmisPath other = (CmisPath) obj;
	if (absolute != other.absolute) {
	    return false;
	}
	if (!Arrays.equals(pathElems, other.pathElems)) {
	    return false;
	}
	return true;
    }

}
