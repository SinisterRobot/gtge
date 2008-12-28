/*
 * Copyright (c) 2008 Golden T Studios.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.golden.gamedev.object.sprite;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * The <tt>VolatileSprite</tt> class is a {@link AdvanceSprite} extension that
 * is designed to be displayed once and then disappear from view, for example,
 * an {@link AdvanceSprite} representing an explosion. The
 * {@link #restoreSprite()} method has been added such that a
 * <tt>VolatileSprite</tt> instance may be re-used without creating multiple
 * (and effectively identical) <tt>VolatileSprite</tt> instances. This is
 * useful again for explosions, if the same type of explosion needs to happen at
 * a different place in a different time, the instance may be restored and
 * effectively re-played in a different location. However, if the same type of
 * <tt>VolatileSprite</tt> instance needs to occur in two or more different
 * places at relatively the same time, there will have to be multiple instances
 * of <tt>VolatileSprite</tt> created to handle this situation.
 * @since 0.2.3
 * @version 1.1
 * @see AdvanceSprite
 */
public class VolatileSprite extends AdvanceSprite {
	
	/**
	 * A serialVersionUID for the <tt>VolatileSprite</tt> class.
	 * @see Serializable
	 */
	private static final long serialVersionUID = 3599186946035433218L;
	
	/**
	 * Creates a new <tt>VolatileSprite</tt> instance with the
	 * {@link AdvanceSprite#AdvanceSprite()} constructor. This
	 * <tt>VolatileSprite</tt> instance will be {@link #isAnimate() animated}
	 * by default.
	 */
	public VolatileSprite() {
		super();
		this.setAnimate(true);
	}
	
	/**
	 * Creates a new <tt>VolatileSprite</tt> instance with the
	 * {@link AdvanceSprite#AdvanceSprite(BufferedImage[])} constructor. This
	 * <tt>VolatileSprite</tt> instance will be {@link #isAnimate() animated}
	 * by default.
	 * @param image The {@link #getImages() images} to use in this
	 *        <tt>VolatileSprite</tt> instance's animation sequence.
	 */
	public VolatileSprite(BufferedImage[] image) {
		super(image);
		this.setAnimate(true);
	}
	
	/**
	 * Creates a new <tt>VolatileSprite</tt> instance with the given array of
	 * {@link BufferedImage} instances and the position specified, using the
	 * {@link AdvanceSprite#AdvanceSprite(BufferedImage[], double, double)}
	 * constructor. This <tt>VolatileSprite</tt> instance will be
	 * {@link #isAnimate() animated} by default.
	 * @param image The {@link #getImages() images} to use in this
	 *        <tt>VolatileSprite</tt> instance's animation sequence.
	 * @param x The {@link #getX() x-coordinate position} of this
	 *        <tt>VolatileSprite</tt> instance.
	 * @param y The {@link #getY() y-coordinate position} of this
	 *        <tt>VolatileSprite</tt> instance.
	 */
	public VolatileSprite(BufferedImage[] image, double x, double y) {
		super(image, x, y);
		this.setAnimate(true);
	}
	
	/**
	 * Creates a new <tt>VolatileSprite</tt> instance with the position
	 * specified, using the {@link AdvanceSprite#AdvanceSprite(double, double)}
	 * constructor. This <tt>VolatileSprite</tt> instance will be
	 * {@link #isAnimate() animated} by default.
	 * @param x The {@link #getX() x-coordinate position} of this
	 *        <tt>VolatileSprite</tt> instance.
	 * @param y The {@link #getY() y-coordinate position} of this
	 *        <tt>VolatileSprite</tt> instance.
	 */
	public VolatileSprite(double x, double y) {
		super(x, y);
		this.setAnimate(true);
	}
	
	/**
	 * Restores a <tt>VolatileSprite</tt> instance's animation sequence to the
	 * beginning, so that it may be rendered again from the first
	 * {@link #getAnimationFrame() animation frame}.
	 */
	public void restoreSprite() {
		this.setFrame(0);
		this.setAnimate(true);
		this.setActive(true);
	}
	
	public void update(long elapsedTime) {
		super.update(elapsedTime);
		
		if (!this.isAnimate()) {
			this.setActive(false);
		}
	}
	
}