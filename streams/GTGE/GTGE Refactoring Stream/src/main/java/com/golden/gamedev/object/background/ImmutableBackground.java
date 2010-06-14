/**
 * 
 */
package com.golden.gamedev.object.background;

import java.io.Serializable;

/**
 * The {@link ImmutableBackground} class implements the <a
 * href="http://en.wikipedia.org/wiki/Null_object_pattern">Null Object
 * Pattern</a> for the {@link BaseBackground} interface - it is completely
 * immutable after it is constructed and all its operations do not affect any
 * state. The {@link ImmutableBackground} class will always have 0 for its
 * {@link #setBounds(double, double, int, int) bounds}.
 * 
 * @author MetroidFan2002
 * @version 1.0
 * @since 0.2.4
 * @see BaseBackground
 * 
 */
public final class ImmutableBackground extends BaseBackground {
	
	/**
	 * {@link Serializable} id generated by Eclipse.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The singeton instance of the {@link ImmutableBackground} class.
	 */
	public static final Background INSTANCE = new ImmutableBackground();
	
	/**
	 * Creates a new {@link ImmutableBackground} instance, marked private to
	 * avoid instantiation by anything other than the singleton.
	 */
	private ImmutableBackground() {
		super();
	}
	
	public void setLocation(double x, double y) {
		// Intentionally blank - the ImmutableBackground class will not change
		// its location once constructed.
	}
	
	public void setSize(int width, int height) {
		// Intentionally blank - the ImmutableBackground class will not change
		// its size once constructed.
	}
	
	/**
	 * Specialized hidden method defined via the {@link Serializable} interface
	 * documentation - writes only the singleton {@link #INSTANCE} to the
	 * bytestream each time a {@link ImmutableBackground} instance is serialized
	 * out.
	 * @return Returns the singleton {@link #INSTANCE}.
	 */
	private Object writeReplace() {
		return INSTANCE;
	}
	
	/**
	 * Specialized hidden method defined via the {@link Serializable} interface
	 * documentation - reads only the singleton {@link #INSTANCE} from the
	 * bytestream each time a {@link ImmutableBackground} instance is serialized
	 * in.
	 * @return Returns the singleton {@link #INSTANCE}.
	 */
	private Object readResolve() {
		return INSTANCE;
	}
}
