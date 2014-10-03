package damp.ekeko.aspectj.annotationtests;

import damp.ekeko.aspectj.annotations.*;

/*
 * Fixture for Requires and Excludes
 */
@Requires(aspect = {"Aspect3a","Aspect3b"}, label = "Label3a")
@Excludes(aspect = "Aspect3c", label = {"Label3b","Label3c"})
public aspect REFix3 {

}
