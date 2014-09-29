package damp.ekeko.aspectj.annotationtests;

import damp.ekeko.aspectj.annotations.*;

/*
 * Fixture for Requires and Excludes
 */
@Requires(aspect = "Aspect1")
@Excludes(label = "Label1")
public aspect REFix1 {

}
