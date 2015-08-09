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
package org.mcisb.ontology.chebi;

import java.util.*;
import libchebi.*;
import org.mcisb.ontology.*;
import org.mcisb.util.*;
import org.sbml.jsbml.*;

/**
 * 
 * @author Neil Swainston
 */
public class ChebiTerm extends OntologyTerm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public static final String IS_A = "is_a"; //$NON-NLS-1$

	/**
	 * 
	 */
	public static final String IS_CONJUGATE_BASE_OF = "is_conjugate_base_of"; //$NON-NLS-1$

	/**
	 * 
	 */
	public static final String IS_CONJUGATE_ACID_OF = "is_conjugate_acid_of"; //$NON-NLS-1$

	/**
	 * 
	 */
	public static final String IS_TAUTOMER_OF = "is_tautomer_of"; //$NON-NLS-1$

	/**
	 * 
	 */
	public static final double UNDEFINED_MASS = ChebiEntity.UNDEFINED_VALUE;

	/**
	 * 
	 */
	private ChebiEntity chebiEntity;

	/**
	 * 
	 * @param id
	 * @throws Exception
	 */
	public ChebiTerm( final String id ) throws Exception
	{
		super( OntologyFactory.getOntology( Ontology.CHEBI ), normaliseId( id ) );

		try
		{
			chebiEntity = new ChebiEntity( Integer.parseInt( normaliseId( id ).replace( ChebiUtils.CHEBI_PREFIX, "" ) ) ); //$NON-NLS-1$
		}
		catch( ChebiException e )
		{
			chebiEntity = null;
			valid = false;
		}
	}

	/**
	 * 
	 * @param id
	 * @throws Exception
	 */
	public ChebiTerm( final int id ) throws Exception
	{
		this( ChebiUtils.CHEBI_PREFIX + id );
	}

	/**
	 * 
	 * @return String
	 * @throws Exception
	 */
	public synchronized String getFormula() throws Exception
	{
		return chebiEntity == null ? null : chebiEntity.getFormula();
	}

	/**
	 * 
	 * @return double
	 * @throws Exception
	 */
	public synchronized double getMass() throws Exception
	{
		return chebiEntity == null ? ChebiEntity.UNDEFINED_VALUE : chebiEntity.getMass();
	}

	/**
	 * 
	 * @return int
	 * @throws Exception
	 */
	public synchronized int getCharge() throws Exception
	{
		final int charge = chebiEntity == null ? ChebiEntity.UNDEFINED_VALUE : chebiEntity.getCharge();
		return charge == ChebiEntity.UNDEFINED_VALUE ? NumberUtils.UNDEFINED : charge;
	}

	/**
	 * 
	 * @return String
	 * @throws Exception
	 */
	public synchronized String getSmiles() throws Exception
	{
		return chebiEntity == null ? null : chebiEntity.getSmiles();
	}

	/**
	 * 
	 * @return String
	 * @throws Exception
	 */
	public synchronized String getInchi() throws Exception
	{
		return chebiEntity == null ? null : chebiEntity.getInchi();
	}

	/**
	 * 
	 * @return String
	 * @throws Exception
	 */
	public synchronized Map<ChebiTerm,String> getParents() throws Exception
	{
		final Map<ChebiTerm,String> parents = new LinkedHashMap<>();

		if( chebiEntity != null )
		{
			for( Relation relation : chebiEntity.getOutgoings() )
			{
				parents.put( new ChebiTerm( relation.getTargetChebiId() ), relation.getType().toString() );
			}
		}

		return parents;
	}

	/**
	 * 
	 * @return String
	 * @throws Exception
	 */
	public synchronized Map<ChebiTerm,String> getChildren() throws Exception
	{
		final Map<ChebiTerm,String> children = new LinkedHashMap<>();

		if( chebiEntity != null )
		{
			for( Relation relation : chebiEntity.getIncomings() )
			{
				children.put( new ChebiTerm( relation.getTargetChebiId() ), relation.getType().toString() );
			}
		}

		return children;
	}

	/**
	 * 
	 * @param types
	 * @return String
	 * @throws Exception
	 */
	public synchronized Collection<ChebiTerm> getParents( final Collection<String> types ) throws Exception
	{
		final Collection<ChebiTerm> typedParents = new HashSet<>();

		for( Map.Entry<ChebiTerm,String> entry : getParents().entrySet() )
		{
			if( types.contains( entry.getValue() ) )
			{
				typedParents.add( entry.getKey() );
			}
		}

		return typedParents;
	}

	/**
	 * 
	 * @param types
	 * @return String
	 * @throws Exception
	 */
	public synchronized Collection<ChebiTerm> getChildren( final Collection<String> types ) throws Exception
	{
		final Collection<ChebiTerm> typedChildren = new HashSet<>();

		for( Map.Entry<ChebiTerm,String> entry : getChildren().entrySet() )
		{
			if( types.contains( entry.getValue() ) )
			{
				typedChildren.add( entry.getKey() );
			}
		}

		return typedChildren;
	}

	/**
	 * 
	 * @param type
	 * @return String
	 * @throws Exception
	 */
	public synchronized Collection<ChebiTerm> getParents( final String type ) throws Exception
	{
		return getParents( Arrays.asList( type ) );
	}

	/**
	 * 
	 * @param type
	 * @return String
	 * @throws Exception
	 */
	public synchronized Collection<ChebiTerm> getChildren( final String type ) throws Exception
	{
		return getChildren( Arrays.asList( type ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mcisb.ontology.OntologyTerm#doInitialise()
	 */
	@Override
	protected void doInitialise() throws Exception
	{
		if( chebiEntity != null )
		{
			name = chebiEntity.getName();

			final String inchi = getInchi();

			if( inchi != null )
			{
				addXref( OntologyUtils.getInstance().getOntologyTerm( Ontology.INCHI, inchi ), CVTerm.Type.BIOLOGICAL_QUALIFIER, CVTerm.Qualifier.BQB_IS );
			}

			for( Name chebiEntityName : chebiEntity.getNames() )
			{
				synonyms.add( chebiEntityName.getName() );
			}

			for( DatabaseAccession databaseAccession : chebiEntity.getDatabaseAccessions() )
			{
				if( databaseAccession.getType().equals( "KEGG COMPOUND accession" ) ) //$NON-NLS-1$
				{
					addXref( OntologyUtils.getInstance().getOntologyTerm( Ontology.KEGG_COMPOUND, databaseAccession.getAccessionNumber() ), CVTerm.Type.BIOLOGICAL_QUALIFIER, CVTerm.Qualifier.BQB_IS );
				}
			}
		}
	}

	/**
	 * 
	 * @param id
	 * @return String
	 */
	private static String normaliseId( final String id )
	{
		return id.contains( ENCODED_COLON ) ? id.replace( ENCODED_COLON, COLON ) : ( id.startsWith( ChebiUtils.CHEBI_PREFIX ) ? id : ChebiUtils.CHEBI_PREFIX + id );
	}
}