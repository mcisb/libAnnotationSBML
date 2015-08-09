package org.mcisb.ontology.kegg;

import java.util.*;
import org.junit.*;
import org.mcisb.ontology.*;

/**
 * 
 * @author Neil Swainston
 */
public class KeggGlycanUtilsTest
{
	/**
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void getXrefs() throws Exception
	{
		final OntologyTerm keggGlycanTerm = KeggGlycanUtils.getInstance().getOntologyTerm( "G10620" ); //$NON-NLS-1$
		Assert.assertTrue( OntologyUtils.getInstance().getXrefs( Arrays.asList( keggGlycanTerm ), Ontology.KEGG_COMPOUND ).contains( OntologyUtils.getInstance().getOntologyTerm( Ontology.KEGG_COMPOUND, "C00035" ) ) ); //$NON-NLS-1$
		Assert.assertTrue( OntologyUtils.getInstance().getXrefs( Arrays.asList( keggGlycanTerm ), Ontology.CHEBI ).contains( OntologyUtils.getInstance().getOntologyTerm( Ontology.CHEBI, "CHEBI:17552" ) ) ); //$NON-NLS-1$
	}
}
