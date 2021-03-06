/*******************************************************************************
 * Copyright (c) 2018 Atos
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Ricardo Jose Tejada Garcia (Atos) - main developer
 * Jesús Gorroñogoitia (Atos) - architect
 * Initially developed in the context of STAMP EU project https://www.stamp-project.eu
 *******************************************************************************/
package eu.stamp.wp4.dspot.execution.launch;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

// The plug-in ID
public static final String PLUGIN_ID = "DSpotTest"; //$NON-NLS-1$ 

// The shared instance
private static Activator plugin; 

private ClassLoader urlClassLoader;

/**
 * The constructor
 */
public Activator() {
}

/*
 * (non-Javadoc)
 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
 */
public void start(BundleContext context) throws Exception {
super.start(context);
plugin = this;

}

public ClassLoader getClassLoaderWithDSpotLibrary() {
return urlClassLoader;
}


/*
 * (non-Javadoc)
 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
 */
public void stop(BundleContext context) throws Exception {
plugin = null;
super.stop(context);
}

/**
 * Returns the shared instance
 *
 * @return the shared instance
 */
public static Activator getDefault() {
return plugin;
}

/**
 * Returns an image descriptor for the image file at the given
 * plug-in relative path
 *
 * @param path the path
 * @return the image descriptor
 */
public static ImageDescriptor getImageDescriptor(String path) {
return imageDescriptorFromPlugin(PLUGIN_ID, path);
}
}
