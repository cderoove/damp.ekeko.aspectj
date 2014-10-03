package damp.ekeko.aspectj.annotationtests;

import damp.ekeko.aspectj.annotations.Excludes;
import damp.ekeko.aspectj.annotations.Requires;

/*
 * Fixture for Requires and Excludes
 */
@Requires(type = {"Aspect4a","Aspect4b"}, label = "Label4a")
@Excludes(type = "Aspect4c", label = "Label4b")
public class REFix4 {

}
