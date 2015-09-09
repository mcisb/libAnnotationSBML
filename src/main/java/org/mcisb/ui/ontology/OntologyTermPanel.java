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

import java.beans.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import org.mcisb.ontology.*;
import org.mcisb.ui.util.*;

/**
 * 
 * @author Neil Swainston
 */
public class OntologyTermPanel extends ComponentPanel implements ListSelectionListener, PropertyChangeListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private OntologyTerm ontologyTerm;

	/**
	 * 
	 * @param message
	 */
	public OntologyTermPanel( final String message )
	{
		super( message, new OntologyTermList() );

		if( component instanceof JList )
		{
			final JList<?> list = (JList<?>)component;
			list.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
			list.addListSelectionListener( this );
		}
	}

	/**
	 * 
	 * @param ontologyTerms
	 */
	public void setOntologyTerms( final Collection<OntologyTerm> ontologyTerms )
	{
		if( component instanceof JList )
		{
			@SuppressWarnings("unchecked")
			final Runnable listDataSetter = new ListDataSetter( (JList<Object>)component, ontologyTerms );
			SwingUtilities.invokeLater( listDataSetter );
		}
	}

	/**
	 * 
	 * @return OntologyTerm
	 */
	public OntologyTerm getOntologyTerm()
	{
		if( component instanceof JList )
		{
			final Object selection = ( (JList<?>)component ).getSelectedValue();
			return ( selection != null ) ? (OntologyTerm)selection : null;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event
	 * .ListSelectionEvent)
	 */
	@Override
	public void valueChanged( ListSelectionEvent e )
	{
		if( !e.getValueIsAdjusting() )
		{
			setOntologyTerm( (OntologyTerm)( (JList<?>)e.getSource() ).getSelectedValue() );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.
	 * PropertyChangeEvent)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void propertyChange( PropertyChangeEvent evt )
	{
		if( evt.getPropertyName().equals( OntologyTermSearcher.ONTOLOGY_TERMS ) && evt.getNewValue() instanceof Collection )
		{
			setOntologyTerms( (Collection<OntologyTerm>)evt.getNewValue() );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mcisb.ui.util.ComponentPanel#dispose()
	 */
	@Override
	public void dispose() throws Exception
	{
		super.dispose();

		if( component instanceof JList )
		{
			( (JList<?>)component ).addListSelectionListener( this );
		}
	}

	/**
	 * 
	 * @param ontologyTerm
	 */
	private void setOntologyTerm( final OntologyTerm ontologyTerm )
	{
		final OntologyTerm oldOntologyTerm = this.ontologyTerm;
		this.ontologyTerm = ontologyTerm;
		firePropertyChange( PropertyNames.ONTOLOGY_TERM, oldOntologyTerm, ontologyTerm );
	}

	/**
	 * 
	 * @author neilswainston
	 */
	private static class ListDataSetter implements Runnable
	{
		/**
		 * 
		 */
		private final Collection<OntologyTerm> ontologyTerms;

		/**
		 * 
		 */
		private final JList<Object> parentComponent;

		/**
		 * 
		 * @param ontologyTerms
		 */
		public ListDataSetter( final JList<Object> parentComponent, final Collection<OntologyTerm> ontologyTerms )
		{
			this.ontologyTerms = ontologyTerms;
			this.parentComponent = parentComponent;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			parentComponent.setListData( ontologyTerms.toArray() );
		}
	}
}