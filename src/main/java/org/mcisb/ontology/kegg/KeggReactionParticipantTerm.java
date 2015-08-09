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
package org.mcisb.ontology.kegg;

import java.util.*;
import org.mcisb.util.*;

/**
 * 
 * @author Neil Swainston
 */
public abstract class KeggReactionParticipantTerm extends KeggTerm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private final Collection<String> reactions = new HashSet<>();

	/**
	 * 
	 * @param ontologyName
	 * @param id
	 * @param idPrefix
	 * @throws Exception
	 */
	public KeggReactionParticipantTerm( String ontologyName, String id, String idPrefix ) throws Exception
	{
		super( ontologyName, id, idPrefix );
	}

	/**
	 * 
	 * @return Collection
	 * @throws Exception
	 */
	public Collection<String> getReactions() throws Exception
	{
		init();
		return reactions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mcisb.ontology.kegg.KeggTerm#parseProperty(java.lang.String,
	 * java.util.List)
	 */
	@Override
	protected void parseProperty( final String propertyName, final List<String> values ) throws Exception
	{
		super.parseProperty( propertyName, values );

		final String ALL_REAC = "REACTION|ALL_REAC"; //$NON-NLS-1$
		final String REACTION_PATTERN = "R(\\d{5})"; //$NON-NLS-1$

		if( propertyName.matches( ALL_REAC ) )
		{
			for( int i = 0; i < values.size(); i++ )
			{
				final Collection<String> matches = RegularExpressionUtils.getMatches( values.get( i ), REACTION_PATTERN );

				for( Iterator<String> iterator = matches.iterator(); iterator.hasNext(); )
				{
					reactions.add( iterator.next() );
				}
			}
		}
	}
}