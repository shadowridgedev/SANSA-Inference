package net.sansa_stack.inference.flink.forwardchaining

import net.sansa_stack.inference.flink.data.RDFGraph
import org.apache.flink.api.scala.{ExecutionEnvironment, _}
import org.apache.jena.vocabulary.{RDF, RDFS}
import net.sansa_stack.inference.data.RDFTriple
import net.sansa_stack.inference.utils.CollectionUtils
import org.slf4j.LoggerFactory

/**
* A forward chaining implementation of the RDFS entailment regime.
*
* @constructor create a new RDFS forward chaining reasoner
* @param env the Apache Flink execution environment
* @author Lorenz Buehmann
*/
class ForwardRuleReasonerRDFS(env: ExecutionEnvironment) extends ForwardRuleReasoner{

  private val logger = com.typesafe.scalalogging.Logger(LoggerFactory.getLogger(this.getClass.getName))

  def apply(graph: RDFGraph): RDFGraph = {
    logger.info("materializing graph...")
    val startTime = System.currentTimeMillis()

    var triplesDS = graph.triples

    // RDFS rules dependency was analyzed in \todo(add references) and the same ordering is used here


    // 1. we first compute the transitive closure of rdfs:subPropertyOf and rdfs:subClassOf

    /**
      * rdfs11	xxx rdfs:subClassOf yyy .
      * yyy rdfs:subClassOf zzz .	  xxx rdfs:subClassOf zzz .
      */
    val subClassOfTriples = extractTriples(triplesDS, RDFS.subClassOf.getURI) // extract rdfs:subClassOf triples
    val subClassOfTriplesTrans = computeTransitiveClosureOpt(subClassOfTriples)

    /*
        rdfs5	xxx rdfs:subPropertyOf yyy .
              yyy rdfs:subPropertyOf zzz .	xxx rdfs:subPropertyOf zzz .
     */
    val subPropertyOfTriples = extractTriples(triplesDS, RDFS.subPropertyOf.getURI) // extract rdfs:subPropertyOf triples
    val subPropertyOfTriplesTrans = computeTransitiveClosureOpt(subPropertyOfTriples)

    // a map structure should be more efficient
    val subClassOfMap = CollectionUtils.toMultiMap(subClassOfTriplesTrans.map(t => (t.subject, t.`object`)).collect)
    val subPropertyMap = CollectionUtils.toMultiMap(subPropertyOfTriplesTrans.map(t => (t.subject, t.`object`)).collect)


    // 2. SubPropertyOf inheritance according to rdfs7 is computed

    /*
      rdfs7	aaa rdfs:subPropertyOf bbb .
            xxx aaa yyy .                   	xxx bbb yyy .
     */
    val triplesRDFS7 =
    triplesDS // all triples (s p1 o)
      .filter(t => subPropertyMap.contains(t.predicate)) // such that p1 has a super property p2
      .flatMap(t => subPropertyMap(t.predicate).map(supProp => RDFTriple(t.subject, supProp, t.`object`))) // create triple (s p2 o)

    // add triples
    triplesDS = triplesDS.union(triplesRDFS7)

    // 3. Domain and Range inheritance according to rdfs2 and rdfs3 is computed

    /*
    rdfs2	aaa rdfs:domain xxx .
          yyy aaa zzz .	          yyy rdf:type xxx .
     */
    val domainTriples = extractTriples(triplesDS, RDFS.domain.getURI)
    val domainMap = domainTriples.map(t => (t.subject, t.`object`)).collect.toMap

    val triplesRDFS2 =
      triplesDS
        .filter(t => domainMap.contains(t.predicate))
        .map(t => RDFTriple(t.subject, RDF.`type`.getURI, domainMap(t.predicate)))

    /*
   rdfs3	aaa rdfs:range xxx .
         yyy aaa zzz .	          zzz rdf:type xxx .
    */
    val rangeTriples = extractTriples(triplesDS, RDFS.range.getURI)
    val rangeMap = rangeTriples.map(t => (t.subject, t.`object`)).collect().toMap

    val triplesRDFS3 =
      triplesDS
        .filter(t => rangeMap.contains(t.predicate))
        .map(t => RDFTriple(t.`object`, RDF.`type`.getURI, rangeMap(t.predicate)))

    val triples23 = triplesRDFS2.union(triplesRDFS3)

    // get rdf:type tuples here as intermediate result
    val typeTriples = triplesDS
      .filter(t => t.predicate == RDF.`type`.getURI)
      .union(triples23)

    // 4. SubClass inheritance according to rdfs9

    /*
    rdfs9	xxx rdfs:subClassOf yyy .
          zzz rdf:type xxx .	        zzz rdf:type yyy .
     */
    val triplesRDFS9 =
    typeTriples // all rdf:type triples (s a A)
      .filter(t => subClassOfMap.contains(t.`object`)) // such that A has a super class B
      .flatMap(t => subClassOfMap(t.`object`).map(supCls => RDFTriple(t.subject, RDF.`type`.getURI, supCls))) // create triple (s a B)

    // 5. merge triples and remove duplicates
    val allTriples = env.union(
      Seq(
        subClassOfTriplesTrans,
        subPropertyOfTriplesTrans,
        triples23,
        triplesRDFS7,
        triplesRDFS9))
      .distinct()

    logger.info("...finished materialization in " + (System.currentTimeMillis() - startTime) + "ms.")
//    val newSize = allTriples.count()
//    logger.info(s"|G_inf|=$newSize")

    // return graph with inferred triples
    RDFGraph(allTriples)
  }
}
