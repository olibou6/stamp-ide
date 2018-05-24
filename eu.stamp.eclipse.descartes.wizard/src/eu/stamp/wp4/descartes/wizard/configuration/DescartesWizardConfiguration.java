package eu.stamp.wp4.descartes.wizard.configuration;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.m2e.actions.MavenLaunchConstants;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import eu.stamp.wp4.descartes.wizard.utils.*;

@SuppressWarnings("restriction")
public class DescartesWizardConfiguration {

	private IJavaProject jProject;
	
	private String projectPath;
	
	private DescartesWizardPomParser descartesParser;
	
	private ILaunchConfiguration[] configurations;
	private int indexOfCurrentConfiguration = 0;
	
	public DescartesWizardConfiguration(){
		jProject = DescartesWizardPomParser.obtainProject();
		if(jProject != null) { 
			try {
				if(jProject.getProject().hasNature(DescartesWizardConstants.MAVEN_NATURE_ID)) {
         try {
			descartesParser = new DescartesWizardPomParser(jProject);
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
         projectPath = jProject.getProject().getLocation().toString();} 
			} catch (CoreException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
		}
		try { configurations = findConfigurations();
		} catch (CoreException e) { e.printStackTrace(); }
	}
	
	public DescartesWizardConfiguration(IJavaProject jProject) {
		this.jProject = jProject;
		try {
			descartesParser = new DescartesWizardPomParser(jProject);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		projectPath = jProject.getProject().getLocation().toString();
	}
	/*
	 *  getter methods
	 */
	public IJavaProject getProject() {
		return jProject;
	}
	public String getProjectPath() {
		return projectPath;
	}
	public Node[] getMutators() {
		return descartesParser.getMutators();
	}
	public String[] getMutatorsNames() {
		String[] names = {""};
		Node[] mutators = descartesParser.getMutators();
		if(mutators != null) {
	    names = new String[mutators.length];
		for(int i = 0; i < mutators.length; i++) names[i] = mutators[i].getNodeName();}
		return names;
	}
	public String[] getMutatorsTexts() {
		String[] texts = {""};
		Node[] mutators = descartesParser.getMutators();
		if(mutators != null) {
		texts = new String[mutators.length];
		for(int i = 0; i < mutators.length; i++) texts[i] = mutators[i].getTextContent();}
		return texts;
	}
	public DescartesWizardPomParser getDescartesParser() {
		return descartesParser;
	}
	/*
	 *  setter methods
	 */
	public void setProject(IJavaProject jProject) {
		this.jProject = jProject;
		try {
			descartesParser = new DescartesWizardPomParser(jProject);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		projectPath = jProject.getProject().getLocation().toString();	
	}
	public String[] getConfigurationNames() {
		String[] result = new String[configurations.length];
		for(int i = 0; i < configurations.length; i++)result[i] = configurations[i].getName();
		return result;
	}

	public void setCurrentConfiguration(String name) throws CoreException {
		
		configurations = findConfigurations();
		
		ILaunchConfiguration configuration = null;  // local variable to store the new current configuration
		for(int i = 0; i < configurations.length; i++)if(configurations[i]
				.getName().equalsIgnoreCase(name)) {
			configuration = configurations[i];  // set the index and put the new configuration in the local variable
			indexOfCurrentConfiguration = i; break;
		}
		
	  // get the project path and name	
	  projectPath = configuration.getAttribute(MavenLaunchConstants.ATTR_POM_DIR, ""); // the pom is in the project's folder
	  String projectName;  // get the name of the project corresponding to the new configuration
	  if(projectPath.contains("/"))projectName = projectPath.substring(projectPath.lastIndexOf("/"));
	  else projectName = projectPath.substring(projectPath.lastIndexOf("\\")); // windows path separator
	  
	  // now find the project object using the name
	  IProject theProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
	  jProject = new JavaProject(theProject,null); // use the project object to create the IJavaProject
	  String pomName = configuration.getAttribute(MavenLaunchConstants.ATTR_GOALS,"");
	  pomName = pomName.substring(pomName.indexOf("-f ")+3);  // ... -f pomName
	  
	  // parse the pom of the configuration
	  try {
		descartesParser = new DescartesWizardPomParser(jProject,pomName);
	} catch (ParserConfigurationException | SAXException | IOException e) {
		e.printStackTrace();
	}
	  // Now the wizard configuration is updated
	}
	
	public ILaunchConfiguration getCurrentConfiguration() {
		if(configurations.length > 0)return configurations[indexOfCurrentConfiguration];
		return null; 
		}
	
	public String getPomName() {
		String result = descartesParser.getPomName();
		if(result == null && configurations != null) {
			try {
				result = getCurrentConfiguration()
				.getWorkingCopy().getAttribute(DescartesWizardConstants.POM_NAME_LAUNCH_CONSTANT, "");
			} catch (CoreException e) {
				e.printStackTrace();
			}}
		if(result == null) result = "descartes_pom.xml";
		else if(result.isEmpty()) result = "descartes_pom.xml";
		return result;
	}
	
	private ILaunchConfiguration[] findConfigurations() throws CoreException {
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfiguration[] configurations = manager.getLaunchConfigurations(
				manager.getLaunchConfigurationType(DescartesWizardConstants
						.LAUNCH_CONFIGURATION_DESCARTES_ID));
		ILaunchConfiguration[] result = new ILaunchConfiguration[configurations.length];
		for(int i = 0; i < configurations.length; i++) result[i] = configurations[i].getWorkingCopy();
		return result;
	}
}
