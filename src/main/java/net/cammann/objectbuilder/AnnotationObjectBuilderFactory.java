package net.cammann.objectbuilder;

import java.lang.annotation.Annotation;

import net.cammann.annotations.Callback;
import net.cammann.annotations.Fixed;
import net.cammann.annotations.Lookup;
import net.cammann.annotations.Range;

public class AnnotationObjectBuilderFactory {

	public ObjectBuilder getBuilder(Annotation annotation, BuildContext ctx) {
		if (annotation.annotationType().equals(Fixed.class)) {
			return new FixedObjectBuilder((Fixed) annotation);
		} else if(annotation.annotationType().equals(Range.class)) {
			return new RangeObjectBuilder((Range) annotation, ctx.getRangeRound());
		} else if (annotation.annotationType().equals(Lookup.class)) {
			return new LookupObjectBuilder((Lookup) annotation, ctx);
		} else if (annotation.annotationType().equals(Callback.class)) {
			return new CallbackObjectBuilder((Callback) annotation, ctx);
		}
		return null;
	}

}
