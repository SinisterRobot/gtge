/**
 * 
 */
package com.golden.gamedev.util;

import java.io.Serializable;
import java.util.Comparator;

/**
 * The {@link EqualsComparator} class provides a {@link Serializable}
 * {@link Comparator} implementation that provides the <a
 * href="http://en.wikipedia.org/wiki/Null_object_pattern">Null Object
 * Pattern</a> for a {@link Comparator} by treating both compared {@link Object}
 * instances as being equivalent by returning 0 from
 * {@link #compare(Object, Object)}.
 * 
 * @author MetroidFan2002
 * @version 1.0
 * @since 0.2.4
 * 
 */
public final class EqualsComparator implements Comparator, Serializable {
	
	/**
	 * Serializable ID generated by Eclipse.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new {@link EqualsComparator} instance.
	 */
	public EqualsComparator() {
		super();
	}
	
	public int compare(Object arg0, Object arg1) {
		return 0;
	}
	
}