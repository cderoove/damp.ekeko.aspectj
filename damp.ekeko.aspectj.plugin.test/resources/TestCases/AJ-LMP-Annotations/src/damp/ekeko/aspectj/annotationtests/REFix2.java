package damp.ekeko.aspectj.annotationtests;

import damp.ekeko.aspectj.annotations.Excludes;
import damp.ekeko.aspectj.annotations.Requires;

/*
 * Fixture for Requires and Excludes
 */
@Requires(label = "Label2")
@Excludes(aspect = "Aspect2")
public class REFix2 {

}
