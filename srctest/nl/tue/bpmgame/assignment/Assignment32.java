package nl.tue.bpmgame.assignment;

import nl.tue.bpmgame.declarative.DeclarativeModel;
import nl.tue.bpmgame.simulator.TestSimulatorModel;

/**
 * The specification of a concrete assignment: tasks, events, resources, ...
 * The assignment can be obtained through Assignment.getAssignment();
 * Assignment.getAssignment() also contains the invocation of this assignment.
 * This is where the active assignment can be chosen.
 *
 */
public class Assignment32 extends Assignment{

	@Override
	protected void createAssignment() {
		addSkill("SkillA");
		addSkill("SkillB");
		addTask(new Task("TaskA", 60000, 60000, "SkillA", 0));
		addTask(new Task("TaskB", 30000, 30000, "SkillB", 0));
		addEvent(new Event("EventA", 60000, 60000));
		addEvent(new Event("EventB", 60000, 60000));
		addEvent(new Event("Start", 0, 0));
		addEvent(new Event("End", 0, 0));
		addResource(new Resource("ResourceA", new String[] {"SkillA"}, true, 1));
		addResource(new Resource("ResourceB", new String[] {"SkillB"}, true, 1));
		addResource(new Resource(Assignment.GENERAL_MANAGER, new String[] {"SkillA","SkillB"}, true, 1));

		DeclarativeModel behavior = new DeclarativeModel();
		behavior.addCondition("Start", "");
		behavior.addCondition("EventA", "[Start]");
		behavior.addCondition("EventB", "[EventA]");
		behavior.addCondition("End", "[EventB]");
		behavior.addTransitionTouches("EventA", "AttributeA");
		behavior.addTransitionTouches("EventA", "AttributeB");
		behavior.addTransitionTouches("EventB", "AttributeA");
		behavior.addTransitionTouches("EventB", "AttributeB");
		addBehavior(behavior);
		
		CaseType caseType = new CaseType(30000, 30000, "[End]", 1000000, new String[] {"Start","EventA","EventB","End"});
		caseType.addAttribute("AttributeA", "[{firsttime, [{afirst, 100%}, {asecond, 0%}]}, {secondtime, [{afirst, 0%}, {asecond, 100%}]}]");
		caseType.addAttribute("AttributeB", "[{firsttime, [{bfirst, 100%}, {bsecond, 0%}]}, {secondtime, [{bfirst, 0%}, {bsecond, 100%}]}]");
		addCaseType(caseType);
	}

	@Override
	public long getPenaltyTime() {
		//return 30000 + RandomGenerator.generateUniform(600000); //Penalty time is 30-90 seconds
		return 30000;
	}

	@Override
	public boolean executeSkip() {
		//return RandomGenerator.generateUniform(100) <= 80; //There is an 80% chance
		return TestSimulatorModel.executeSkip;
	}

	@Override
	public boolean performUnskilled() {
		//return RandomGenerator.generateUniform(100) <= 80; //There is an 80% chance
		return false;
	}

	@Override
	public int getPenaltySatisfaction() {
		//return 1;
		return 1;
	}
}
