package com.barneyb.aoc;

import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@DisabledIfEnvironmentVariable(
        named = "CI",
        matches = "true")
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface NotInCI {
}
