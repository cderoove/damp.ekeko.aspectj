package damp.ekeko.aspectj.annotationtests;

import damp.ekeko.aspectj.annotations.*;

/*
 * Fixture for Requires and Excludes
 */
@Requires(type = {"Aspect3a","Aspect3b"}, label = "Label3a")
@Excludes(type = "Aspect3c", label = {"Label3b","Label3c"})
public aspect REFix3 {

}
