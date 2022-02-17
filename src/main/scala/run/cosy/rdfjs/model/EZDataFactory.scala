/*
 * Copyright 2021 Henry Story
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package run.cosy.rdfjs.model

import scala.scalajs.js.undefined

/** DataFactory that is easier to use from Scala
  */
class EZDataFactory(val datafactory: DataFactory):

   import datafactory.Nodes

   inline def namedNode(value: String): NamedNode = datafactory.namedNode(value)

   inline def literal(value: String): Literal = datafactory.literal(value, undefined)

   inline def literal(value: String, lang: String): Literal = datafactory.literal(value, lang)

   inline def literal(value: String, dataType: NamedNode): Literal =
     datafactory.literal(value, dataType)

   inline def variable(value: String): Variable = datafactory.variable(value)

   inline def blankNode(): BlankNode = datafactory.blankNode(undefined)

   inline def blankNode(label: String): BlankNode =
     datafactory.blankNode(label)

   inline def quad(
       subject: Quad.Subject,
       rel: Quad.Predicate,
       obj: Quad.Object
   ): Quad = datafactory.quad(subject, rel, obj, undefined)

   inline def quad(
       subject: Quad.Subject,
       rel: Quad.Predicate,
       obj: Quad.Object,
       graph: Quad.Graph
   ): Quad = datafactory.quad(subject, rel, obj, graph)

   def defaultGraph(): DefaultGraph = datafactory.defaultGraph()

   /** Required by rdflib.js which states: Generates a uniquely identifiably *idempotent* string for
     * the given {term}. Equivalent to "Term.hashString"x§
     *
     * @example
     *   Use this to associate data with a term in an object { obj[id(term)] = "myData" }
     */
   inline def id(term: Nodes): String = datafactory.id(term)

end EZDataFactory

//todo: how do frameworks make sure blank nodes don't end up clashing in their store?
object EZDataFactory:
   val one: BigInt = BigInt(1)

   def apply(): EZDataFactory = new EZDataFactory(new DataFactory(one))
