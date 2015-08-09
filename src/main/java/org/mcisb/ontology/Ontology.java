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

import java.io.*;
import java.util.*;

/**
 * 
 * @author Neil Swainston
 */
public class Ontology implements Serializable, Comparable<Object>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public static final String ID_SEPARATOR = ":"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String CATH = "CATH superfamily"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String CHEBI = "ChEBI"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String DRUGBANK = "DrugBank"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String EC = "Enzyme Nomenclature"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String ENSEMBL = "Ensembl"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String NCBI_GENE = "NCBI Gene"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String EVIDENCE_CODE = "Evidence Code Ontology"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String GO = "Gene Ontology"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String KEGG_DRUG = "KEGG Drug"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String KEGG_GENES = "KEGG Genes"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String KEGG_GENOME = "KEGG Genome"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String KEGG_GLYCAN = "KEGG Glycan"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String KEGG_COMPOUND = "KEGG Compound"; //$NON-NLS-1$

	/**
	 * 
	 */
	public static final String KEGG_PATHWAY = "KEGG Pathway"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String KEGG_REACTION = "KEGG Reaction"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String PUBCHEM_COMPOUND = "PubChem-compound"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String PUBCHEM_SUBSTANCE = "PubChem-substance"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String PUBMED = "PubMed"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String SBO = "Systems Biology Ontology"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String SGD = "SGD"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String TAXONOMY = "Taxonomy"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String TAIR_LOCUS = "TAIR Locus"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String TCDB = "Transport Classification Database"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String UNIPROT = "UniProt Knowledgebase"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String HMDB = "HMDB"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String INCHI = "InChI"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String WILDCARD = "$id"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	static final String URL_SEPARATOR = "#"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	static final String URI_SEPARATOR = ":"; //$NON-NLS-1$

	/**
	 * 
	 */
	private final String name;
	
	/**
	 * 
	 */
	private final String urlIdentifier;
	
	/**
	 * 
	 */
	private final String urnIdentifier;
	
	/**
	 * 
	 */
	private final Collection<String> uriIdentifiers;
	
	/**
	 * 
	 */
	private final String linkTemplate;
	
	/**
	 * 
	 */
	private final String regularExpression;
	
	/**
	 *
	 * @param name
	 * @param urlIdentifier
	 * @param urnIdentifier
	 * @param uriIdentifiers
	 * @param linkTemplate
	 * @param regularExpression
	 */
	public Ontology( final String name, final String urlIdentifier, final String urnIdentifier, final Collection<String> uriIdentifiers, final String linkTemplate, final String regularExpression )
	{
		this.name = name;
		this.urlIdentifier = urlIdentifier;
		this.urnIdentifier = urnIdentifier;
		this.uriIdentifiers = uriIdentifiers;
		this.linkTemplate = linkTemplate;
		this.regularExpression = regularExpression;
	}
	
	/**
	 * 
	 * @return name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * 
	 * @return urnIdentifier
	 */
	public String getUrnIdentifier()
	{
		return urnIdentifier;
	}
	
	/**
	 * 
	 * @return urlIdentifier
	 */
	public String getUrlIdentifier()
	{
		return urlIdentifier;
	}
	
	/**
	 *
	 * @return Collection
	 */
	public Collection<String> getUriIdentifiers()
	{
		return uriIdentifiers;
	}
	
	/**
	 * 
	 * @param deprecatedUriIdentifier
	 */
	public void addDeprecatedUriIdentifier( final String deprecatedUriIdentifier )
	{
		uriIdentifiers.add( deprecatedUriIdentifier );
	}
	
	/**
	 * 
	 * @return linkTemplate
	 */
	public String getLinkTemplate()
	{
		return linkTemplate;
	}
	
	/**
	 * 
	 * @return regularExpression
	 */
	public String getRegularExpression()
	{
		return name.equals( INCHI ) ? "^InChI\\=1S(\\/.+)+$" : regularExpression; //$NON-NLS-1$
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return getName();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo( Object o )
	{
		if( o instanceof Ontology )
		{
			final Ontology objOntology = (Ontology)o;
			
			if( name.equals( INCHI ) && objOntology.name.equals( INCHI ) )
    		{
    			return 0;
    		}
			else if( name.equals( INCHI ) )
    		{
    			return -1;
    		}
    		else if( objOntology.name.equals( INCHI ) )
    		{
    			return 1;
    		}
			
			return name.compareTo( ( (Ontology)o ).name );
		}
		
		return -1;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( Object o )
	{
		if( o instanceof Ontology )
		{
			return name.equals( ( (Ontology)o ).name );
		}
		
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return name.hashCode();
	}
}