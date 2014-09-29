package damp.ekeko.aspectj.annotationtests;

import damp.ekeko.aspectj.annotations.*;

/*
 * Fixture for Requires and Excludes
 */
@Requires(aspect = "Aspect3a", label = "Label3a")
@Excludes(aspect = "Aspect3b")
@Excludes(label = "Label3b")
@Excludes(label = "Label3c")
public aspect REFix3 {

}
