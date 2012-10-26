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


package net.imglib2.ops.condition;

import net.imglib2.ops.function.Function;
import net.imglib2.ops.relation.UnaryRelation;


/**
 * A {@link Condition} on a {@link Function}. The Condition is true when
 * a given {@link UnaryRelation} is satisfied on the Function for a
 * specific input.
 * 
 * @author Barry DeZonia
 */
public class UnaryCondition<INPUT, T> implements Condition<INPUT> {

	// -- instance variables --
	
	private final Function<INPUT,T> f1;
	private final T f1Val;
	private final UnaryRelation<T> relation;

	// -- constructor --
	
	public UnaryCondition(Function<INPUT,T> f1, UnaryRelation<T> relation) {
		this.f1 = f1;
		this.f1Val = f1.createOutput();
		this.relation = relation;
	}
	
	// -- Condition methods --
	
	@Override
	public boolean isTrue(INPUT input) {
		f1.compute(input, f1Val);
		return relation.holds(f1Val);
	}
	
	@Override
	public UnaryCondition<INPUT, T> copy() {
		return new UnaryCondition<INPUT, T>(f1.copy(), relation.copy());
	}
}
