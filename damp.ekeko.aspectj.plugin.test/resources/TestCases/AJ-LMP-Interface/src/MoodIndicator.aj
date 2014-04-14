public aspect MoodIndicator {
	
	public interface Moody {};
	
	public class InnerMoodIndicatorClass {};
	
	public void InnerMoodIndicatorClass.foo() {
		return;
	}


	private Mood Moody.mood = Mood.HAPPY;

	public Mood Moody.getMood() {
		return mood;
	}

	declare parents : org.xyz..* implements Moody;
	
	declare parents : org.xyz..* extends InnerMoodIndicatorClass;

	before(Moody m) : execution(* *.*(..)) && this(m) {
		System.out.println("I'm feeling " + m.getMood());
	}
}

