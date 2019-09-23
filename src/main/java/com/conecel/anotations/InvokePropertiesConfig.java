package com.conecel.anotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.rgmatute.InvokeProperties;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface InvokePropertiesConfig {
	String value() default "";

	String pathFile() default "";

	boolean autorefresh() default false;

	Class<?> classHandler() default InvokeProperties.class;

	String methodHandler() default "";
}