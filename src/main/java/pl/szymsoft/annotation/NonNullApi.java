package pl.szymsoft.annotation;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(value = PACKAGE)
@Retention(value = RUNTIME)
@Documented
@Nonnull
@TypeQualifierDefault(value = {METHOD, PARAMETER})
public @interface NonNullApi {
}
