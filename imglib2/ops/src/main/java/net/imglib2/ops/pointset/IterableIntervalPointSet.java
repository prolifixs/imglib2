/*
 * #%L
 * ImgLib2: a general-purpose, multidimensional image processing library.
 * %%
 * Copyright (C) 2009 - 2012 Stephan Preibisch, Stephan Saalfeld, Tobias
 * Pietzsch, Albert Cardona, Barry DeZonia, Curtis Rueden, Lee Kamentsky, Larry
 * Lindsey, Johannes Schindelin, Christian Dietz, Grant Harris, Jean-Yves
 * Tinevez, Steffen Jaensch, Mark Longair, Nick Perry, and Jan Funke.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are
 * those of the authors and should not be interpreted as representing official
 * policies, either expressed or implied, of any organization.
 * #L%
 */


package net.imglib2.ops.pointset;

import net.imglib2.Cursor;
import net.imglib2.IterableInterval;

/**
 * This class is an adapter that allows any {@link IterableInterval} to be
 * treated as a {@link PointSet}. The only limitation is that this PointSet
 * implementation does not support the translate() method.
 * 
 * @author Barry DeZonia
 *
 */
public class IterableIntervalPointSet implements PointSet
{
	// -- instance variables --
	
	private final IterableInterval<?> interval;
	private final long[] boundMin, boundMax;
	private final long size;
	
	// -- constructor --
	
	public IterableIntervalPointSet(IterableInterval<?> interval) {
		this.interval = interval;
		int numDims = interval.numDimensions();
		boundMin = new long[numDims];
		boundMax = new long[numDims];
		interval.min(boundMin);
		interval.max(boundMax);
		long sum = 1;
		for (int i = 0; i < numDims; i++) {
			sum *= 1 + boundMax[i] - boundMin[i];
		}
		size = sum;
	}
	
	// -- PointSet methods --
	
	@Override
	public long[] getOrigin() {
		return boundMin;
	}

	/** Note: this method unsupported! */
	@Override
	public void translate(long[] delta) {
		throw new UnsupportedOperationException(
				"IterableIntervals cannot be moved through space");
	}

	@Override
	public PointSetIterator iterator() {
		return new IntervalIterator();
	}

	@Override
	public int numDimensions() {
		return interval.numDimensions();
	}

	@Override
	public long[] findBoundMin() {
		return boundMin;
	}

	@Override
	public long[] findBoundMax() {
		return boundMax;
	}

	@Override
	public boolean includes(long[] point) {
		for (int i = 0; i < point.length; i++) {
			long val = point[i];
			if (val < boundMin[i] || val > boundMax[i]) return false;
		}
		return true;
	}

	@Override
	public long calcSize() {
		return size;
	}

	@Override
	public IterableIntervalPointSet copy() {
		return new IterableIntervalPointSet(interval);
	}
	
	// -- private helpers --
	
	private class IntervalIterator implements PointSetIterator {
		
		// -- instance variables --
		
		private final Cursor<?> cursor;
		private final long[] pos;
		
		// -- constructor --
		
		public IntervalIterator() {
			cursor = interval.localizingCursor();
			pos = new long[interval.numDimensions()];
		}
		
		// -- PointSetIterator methods --
		
		@Override
		public boolean hasNext() {
			return cursor.hasNext();
		}

		@Override
		public long[] next() {
			cursor.next();
			cursor.localize(pos);
			return pos;
		}

		@Override
		public void reset() {
			cursor.reset();
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}