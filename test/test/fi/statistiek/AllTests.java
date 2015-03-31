package test.fi.statistiek;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.fi.statistiek.descriptives.DescriptivesModelTest;
import test.fi.statistiek.descriptives.DescriptivesViewTest;

@RunWith(Suite.class)

@SuiteClasses({ 
	StatistiekTest.class, 
	StatTableModelTest.class,
	DescriptivesViewTest.class,
	DescriptivesModelTest.class})

public class AllTests
{

}
