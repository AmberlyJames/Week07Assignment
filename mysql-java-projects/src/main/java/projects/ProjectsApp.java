package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;
/*
 * This class is a menu driven application that accepts user input from the console, 
 * then performs CRUD operations on the project tables. 
 */
public class ProjectsApp {

	private Scanner scanner = new Scanner(System.in);
	private ProjectService projectService = new ProjectService();
	private Project curProject;

	//@formatter:off
	private List<String> operations = List.of(
			"1) Add a Project",
			"2) List projects",
			"3) Select a project",
			"4) Update project details",
			"5) Delete a project"
			
		);
	//@formatter:on

	public static void main(String[] args) {

		new ProjectsApp().processUserSelections();
	}

	private void processUserSelections() {				//switch statement to process each of the user selections
		boolean done = false;							

		while (!done) {
			try {
				int selection = getUserSelection();

				switch (selection) {

				case -1:
					done = exitMenu();
					break;

				case 1:
					createProject();
					break;

				case 2:
					listProjects();
					break;

				case 3:
					selectProject();
					break;

				case 4:
					updateProjectDetails();
					break;
					
				case 5:
					deleteProject();
					break;

				default:
					System.out.print("\n" + selection + " is not a valid selection. Try again.");
					break;

				}
			} catch (Exception e) {
				System.out.println("\nError: " + e + " Try again.");
			}
		}
	}

	private void deleteProject() {
		listProjects();
		Integer projectId = getIntInput("Enter the project Id for the project you would like to delete");
		
		projectService.deleteProject(projectId);
		System.out.println("Project " + projectId + " was successfully deleted.");
		
		if(Objects.nonNull(curProject) && curProject.getProjectId().equals(projectId)) {
			curProject = null;
		}
	}

	private void updateProjectDetails() {
		if (Objects.isNull(curProject)) {
			System.out.println("\nPlease select a project.");
			return;
		}
		String projectName = getStringInput("Enter the project name [" + curProject.getProjectName() + "]");
		
		BigDecimal estimatedHours = getDecimalInput(
				"Enter the estimated hours to complete [" + curProject.getEstimatedHours() + "]");
		
		BigDecimal actualHours = getDecimalInput(
				"Enter the actual hours to complete [" + curProject.getActualHours() + "]");
		
		Integer difficulty = getIntInput("Enter the difficulty [" + curProject.getDifficulty() + "]");
		
		String notes = getStringInput("Enter the notes [" + curProject.getNotes() + "]");

		Project project = new Project();
		
		project.setProjectId(curProject.getProjectId());
		project.setProjectName(Objects.isNull(projectName) ? curProject.getProjectName() : projectName);
		project.setEstimatedHours(Objects.isNull(estimatedHours) ? curProject.getEstimatedHours() : estimatedHours);
		project.setActualHours(Objects.isNull(actualHours) ? curProject.getActualHours() : actualHours);
		project.setDifficulty(Objects.isNull(difficulty) ? curProject.getDifficulty() : difficulty);
		project.setNotes(Objects.isNull(notes) ? curProject.getNotes() : notes);
		
		projectService.modifyProjectDetails(project);
		
		curProject = projectService.fetchProjectById(curProject.getProjectId());

	}

	private void selectProject() {
		listProjects();
		Integer projectId = getIntInput("Enter a project ID to select a project");

		curProject = null;											// unselects a project, if one is already selected
		curProject = projectService.fetchProjectById(projectId);	 // throws exception if project id entered is invalid

	}

	private void listProjects() {
		List<Project> projects = projectService.fetchAllProjects();	//prints a list of all projects, with their project Id

		System.out.println("\nProjects: ");

		projects.forEach(
				project -> System.out.println("   " + project.getProjectId() + ": " + project.getProjectName()));

	}

	private void createProject() {
		String projectName = getStringInput("Enter project name");
		BigDecimal estimatedHours = getDecimalInput("Enter the estmated hours");	//obtains user input for project fields 
		BigDecimal actualHours = getDecimalInput("Enter actual hours");
		Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
		String notes = getStringInput("Enter the project notes");

		Project project = new Project();

		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);								//sets user input into new project fields
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);

		Project dbProject = projectService.addProject(project);
		System.out.println("you have successfully created project: " + dbProject);

	}

	private BigDecimal getDecimalInput(String prompt) {				//converts string to big decimal class from user input
		String input = getStringInput(prompt);
		if (Objects.isNull(input)) {
			return null;
		}
		try {
			return new BigDecimal(input).setScale(2);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid decimal number.");
		}
	}

	private boolean exitMenu() {
		System.out.println("Exiting the menu.");
		return true;
	}

	private int getUserSelection() {							//Method to print operations, then returns user input as an integer
		printOperations();										//If input is null, -1 is returned

		Integer input = getIntInput("Enter a menu selection");

		return Objects.isNull(input) ? -1 : input;
	}

	private Integer getIntInput(String prompt) {				//Prompts the user with a string and returns an integer unless the input is invalid
		String input = getStringInput(prompt);
		if (Objects.isNull(input)) {
			return null;
		}
		try {													//Converts the string input to an integer value to return an int
			return Integer.valueOf(input);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid number.");
		}
	}

	private String getStringInput(String prompt) {				//Method to actually print the prompt to obtain user input and returns user input as a string.
		System.out.print(prompt + ": ");
		String input = scanner.nextLine();
		return input.isBlank() ? null : input.trim();
	}

	private void printOperations() {
		System.out.println("\nThese are the available selections. Press the Enter key to quit:");		//prints each of the operations on a separate line, and a message indicating which project you are working with
		operations.forEach(line -> System.out.println("   " + line));
		if (Objects.isNull(curProject)) {
			System.out.println("\nYou are not working with a project.");
		} else {
			System.out.println("\nYou are working with project: " + curProject);
		}
	}
}
