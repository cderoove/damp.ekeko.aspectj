package damp.ekeko.aspectj.annotations;
/*
 * Fixture for Requires and Excludes
 */
@Requires(aspect = "Aspect4a")
@Requires(aspect = "Aspect4b")
@Requires(label = "Label4a")
@Excludes(aspect = "Aspect4c", label = "Label4b")
public class REFix4 {

}
