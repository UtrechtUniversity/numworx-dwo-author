package org.cbook.cbookif.rm;

/**
 * The resource manager
 * @author wim
 *
 */
public interface ResourceManager {

	/**
	 * The images container.
	 */
	String IMAGES = "images";
	
	/**
	 * The global container. Read-only.
	 * @return a container or resources
	 */
	ResourceContainer getWidgetContainer();

	/**
	 * The local container. Read-write for designer, read-only for student
	 * @return a container of resources
	 */
	ResourceContainer getInstanceContainer();

	/**
	 * The local per student container. 
	 * Read-write for student, non existent for designer
	 * @return a container of resources
	 */
	ResourceContainer getStudentContainer();

}
