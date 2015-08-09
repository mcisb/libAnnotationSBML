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

import java.awt.event.*;
import javax.swing.*;
import org.mcisb.ontology.*;
import org.mcisb.util.*;

/**
 * 
 * @author Neil Swainston
 */
public class OntologyTermList extends JList<Object>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* 
	 * (non-Javadoc)
	 * @see javax.swing.JTable#getToolTipText(java.awt.event.MouseEvent)
	 */
	@Override
	public String getToolTipText( MouseEvent event )
	{
		final int UNDEFINED_INDEX = -1;
		final int index = locationToIndex( event.getPoint() );
			
		if( index != UNDEFINED_INDEX )
		{
		    final Object object = getModel().getElementAt( index );
		    
		    if( object instanceof OntologyTerm )
		    {
		    	try
				{
					return CollectionUtils.toToolTip( ( (OntologyTerm)object ).getSynonyms() );
				}
				catch( Exception e )
				{
					e.printStackTrace();
				}
		    }
		}
		
		return null;
	}
}