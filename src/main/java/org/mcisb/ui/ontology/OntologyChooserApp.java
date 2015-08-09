/*******************************************************************************
 * Manchester Centre for Integrative Systems Biology
 * University of Manchester
 * Manchester M1 7ND
 * United Kingdom
 * 
 * Copyright (C) 2007 University of Manchester
 * 
 * This program is released under the Academic Free License ("AFL") v3.0.
 * (http://www.opensource.org/licenses/academic.php)
 *******************************************************************************/
package org.mcisb.ui.ontology;

import java.util.*;
import javax.swing.*;
import org.mcisb.ui.app.*;
import org.mcisb.ui.util.*;
import org.mcisb.ui.wizard.*;
import org.mcisb.util.*;

/**
 * 
 * @author Neil Swainston
 */
public class OntologyChooserApp extends App
{
	/**
	 * 
	 */
	private final ResourceBundle resourceBundle = ResourceBundle.getBundle( "org.mcisb.ui.ontology.messages" ); //$NON-NLS-1$

	/**
	 * 
	 * @param dialog
	 * @param bean
	 */
	public OntologyChooserApp( final JDialog dialog, final GenericBean bean )
	{
		super( dialog, bean );
		init( resourceBundle.getString( "OntologyChooserApp.title" ), resourceBundle.getString( "OntologyChooserApp.error" ), new ResourceFactory().getImageIcon( resourceBundle.getString( "OntologyChooserApp.icon" ) ).getImage() ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mcisb.ui.app.App#getWizard(org.mcisb.util.GenericBean)
	 */
	@Override
	protected Wizard getWizard() throws Exception
	{
		return new OntologyChooserWizard( bean );
	}
}