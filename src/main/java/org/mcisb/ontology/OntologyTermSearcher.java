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
package org.mcisb.ontology;

import java.beans.*;
import java.util.*;

/**
 * 
 * @author Neil Swainston
 */
public class OntologyTermSearcher implements PropertyChangeListener
{
	/**
	 * 
	 */
	public static final String SEARCHING = "SEARCHING"; //$NON-NLS-1$

	/**
	 * 
	 */
	public static final String ONTOLOGY_TERMS = "ONTOLOGY_TERMS"; //$NON-NLS-1$

	/**
	 * 
	 */
	public static final String TEXT = "TEXT"; //$NON-NLS-1$

	/**
	 * 
	 */
	OntologySource ontologySource = null;

	/**
	 * 
	 */
	String searchTerm = null;

	/**
	 * 
	 */
	private final PropertyChangeSupport support = new PropertyChangeSupport( this );

	/**
	 * 
	 */
	private Collection<OntologyTerm> ontologyTerms = null;

	/**
	 * 
	 */
	private boolean searching = false;

	/**
	 * @param ontologySource
	 */
	public void setOntologySource( final OntologySource ontologySource )
	{
		this.ontologySource = ontologySource;
		update();
	}

	/**
	 * 
	 * @param searchTerm
	 */
	public void setSearchTerm( final String searchTerm )
	{
		this.searchTerm = searchTerm;
		update();
	}

	/**
	 * 
	 * @param listener
	 */
	public void addPropertyChangeListener( final PropertyChangeListener listener )
	{
		support.addPropertyChangeListener( listener );
	}

	/**
	 * 
	 * @param listener
	 */
	public void removePropertyChangeListener( final PropertyChangeListener listener )
	{
		support.removePropertyChangeListener( listener );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.
	 * PropertyChangeEvent)
	 */
	@Override
	public void propertyChange( PropertyChangeEvent evt )
	{
		if( evt.getPropertyName().equals( PropertyNames.ONTOLOGY_SOURCE ) && evt.getNewValue() instanceof OntologySource )
		{
			setOntologySource( (OntologySource)evt.getNewValue() );
		}
		else if( evt.getPropertyName().equals( TEXT ) && evt.getNewValue() instanceof String )
		{
			setSearchTerm( (String)evt.getNewValue() );
		}
	}

	/**
	 * 
	 * @param searchTerm
	 */
	private void update()
	{
		final int MIN_SEARCH_LENGTH = 2;

		setOntologyTerms( new ArrayList<OntologyTerm>() );

		if( ontologySource != null && searchTerm != null && searchTerm.length() > MIN_SEARCH_LENGTH )
		{
			new Thread( new Runnable()
			{
				/*
				 * (non-Javadoc)
				 * 
				 * @see java.lang.Runnable#run()
				 */
				@Override
				public void run()
				{
					setSearching( true );
					setOntologyTerms( new ArrayList<OntologyTerm>() );

					try
					{
						setOntologyTerms( ontologySource.search( searchTerm.trim() + ontologySource.getSearchWildcard() ) );
					}
					catch( Exception ex )
					{
						setOntologyTerms( new ArrayList<OntologyTerm>() );
						ex.printStackTrace();
					}
					finally
					{
						setSearching( false );
					}
				}
			} ).start();
		}
	}

	/**
	 * 
	 * @param searching
	 */
	void setSearching( final boolean searching )
	{
		final boolean oldSearching = this.searching;
		this.searching = searching;
		support.firePropertyChange( SEARCHING, oldSearching, searching );
	}

	/**
	 * 
	 * @param ontologyTerms
	 */
	void setOntologyTerms( final Collection<OntologyTerm> ontologyTerms )
	{
		final Collection<OntologyTerm> oldOntologyTerms = this.ontologyTerms;
		this.ontologyTerms = ontologyTerms;
		support.firePropertyChange( ONTOLOGY_TERMS, oldOntologyTerms, ontologyTerms );
	}
}