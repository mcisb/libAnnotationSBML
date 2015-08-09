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

// import java.net.*;
// import java.nio.*;
// import java.nio.channels.*;
import java.util.*;
// import org.mcisb.chem.util.*;
import org.mcisb.ontology.*;
import org.mcisb.util.*;
// import chemaxon.formats.*;
// import chemaxon.marvin.io.*;

/**
 * 
 * @author Neil Swainston
 */
public class KeggCompoundTerm extends KeggReactionParticipantTerm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private static final String ID_PREFIX = "cpd" + OntologyTerm.ENCODED_COLON; //$NON-NLS-1$

	/**
	 * 
	 */
	// private static final String LINE_SEPARATOR = System.getProperty( "line.separator" ); //$NON-NLS-1$

	/**
	 * 
	 */
	private static final String UNDEFINED_SMILES = "UNDEFINED_SMILES"; //$NON-NLS-1$

	/**
	 * 
	 */
	private static final Map<String,String> idToSmiles = new TreeMap<>();

	/**
	 * 
	 */
	private String formula = null;

	/**
	 * 
	 */
	private boolean xrefsInitialised = false;

	/**
	 * 
	 * @param id
	 * @throws Exception
	 */
	public KeggCompoundTerm( final String id ) throws Exception
	{
		super( Ontology.KEGG_COMPOUND, id, ID_PREFIX );
	}

	/**
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String getFormula() throws Exception
	{
		init();
		return formula;
	}

	/**
	 * 
	 * @return String
	 */
	public String getSmiles()
	{
		String smiles = idToSmiles.get( id );

		if( smiles != null && smiles.equals( UNDEFINED_SMILES ) )
		{
			return null;
		}

		if( smiles == null )
		{
			/*
			 * try { smiles = SmilesUtils.getSmiles( getMolFile() );
			 * idToSmiles.put( id, smiles ); } catch( MolFormatException e ) {
			 * idToSmiles.put( id, UNDEFINED_SMILES ); e.printStackTrace(); }
			 * catch( MolExportException e ) { idToSmiles.put( id,
			 * UNDEFINED_SMILES ); e.printStackTrace(); }
			 */
		}

		return smiles;

	}

	/*
	 * 
	 */
	@Override
	public synchronized Map<OntologyTerm,Object[]> getXrefs() throws Exception
	{
		if( !xrefsInitialised )
		{
			final OntologyTerm chebiXref = KeggUtils.getChebiXref( id );
			final OntologyTerm pubchemXref = KeggUtils.getPubChemXref( id );

			if( chebiXref != null )
			{
				addXref( chebiXref );
			}

			if( pubchemXref != null )
			{
				addXref( pubchemXref );
			}

			xrefsInitialised = true;
		}

		return super.getXrefs();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mcisb.ontology.kegg.KeggReactionParticipantTerm#parseProperty(java
	 * .lang.String, java.util.List)
	 */
	@Override
	protected void parseProperty( final String propertyName, final List<String> values ) throws Exception
	{
		final String NAME = "NAME"; //$NON-NLS-1$
		final String FORMULA = "FORMULA"; //$NON-NLS-1$
		final String SEMI_COLON = ";"; //$NON-NLS-1$
		final String EMPTY_STRING = ""; //$NON-NLS-1$

		if( propertyName.equals( NAME ) )
		{
			final List<String> allNames = new ArrayList<>();

			for( Iterator<String> iterator = values.iterator(); iterator.hasNext(); )
			{
				allNames.add( iterator.next() );
			}

			name = CollectionUtils.getFirst( allNames ).replace( SEMI_COLON, EMPTY_STRING ).trim();

			for( int i = 1; i < allNames.size(); i++ )
			{
				addSynonym( allNames.get( i ).replace( SEMI_COLON, EMPTY_STRING ).trim() );
			}
		}
		else if( propertyName.equals( FORMULA ) )
		{
			formula = CollectionUtils.getFirst( values ).trim();
		}
		else
		{
			super.parseProperty( propertyName, values );
		}
	}

	/**
	 * 
	 * @return String
	 * @throws IOException
	 */
	/*
	 * private String getMolFile() throws IOException { final int CAPACITY =
	 * 262144; final URL url = new URL(
	 * "http://www.genome.jp/dbget-bin/www_bget?-f+m+compound+" + id );
	 * //$NON-NLS-1$ final InputStream is = url.openStream();
	 * 
	 * final ReadableByteChannel readChannel = Channels.newChannel( is ); final
	 * ByteBuffer buffer = ByteBuffer.allocate( CAPACITY ); readChannel.read(
	 * buffer );
	 * 
	 * if( is != null ) { is.close(); }
	 * 
	 * readChannel.close();
	 * 
	 * final StringBuffer s = new StringBuffer( "unknown" ); //$NON-NLS-1$
	 * s.append( LINE_SEPARATOR ); s.append( "kegg" ); //$NON-NLS-1$ s.append(
	 * LINE_SEPARATOR ); s.append( LINE_SEPARATOR ); s.append( new String(
	 * buffer.array() ).trim() );
	 * 
	 * return s.toString(); }
	 */
}