/*
 * Copyright 2021 Henry Story
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package run.cosy.rdfjs.model

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportAll, JSExportTopLevel}

/** sjrd said on Discord: > If you're implementing a JS interface, it's better to declare the
  * interface as a trait Foo extends js.Object and then extend that trait. Instead of using exports.
  */
trait DataFactoryI extends js.Object:
   val namedNode: js.Function1[String, NamedNode]
   val literal: js.Function2[String, js.UndefOr[String | NamedNode], Literal]
   val variable: js.Function1[String, Variable]
   val blankNode: js.Function1[js.UndefOr[String], BlankNode]

   import Quad.{Graph, Object, Predicate, Subject}

   val quad: js.Function4[Subject, Predicate, Object, js.UndefOr[Graph], Quad]
   val defaultGraph: js.Function0[DefaultGraph]
   type Nodes = BlankNode | NamedNode | Literal | DefaultGraph | Variable

   /** Required by rdflib.js which states: Generates a uniquely identifiably *idempotent* string for
     * the given {term}. Equivalent to "Term.hashString"x§
     *
     * @example
     *   Use this to associate data with a term in an object { obj[id(term)] = "myData" }
     */
   val id: js.Function1[Term[?], String]
end DataFactoryI

/** Data Factory Functions as defined by rdf.js
  *
  * Class to use when one wants to pass to JS function objects
  * @see
  *   https://rdf.js.org/data-model-spec/#datafactory-interface
  */
class DataFactory(bnodeStart: BigInt = EZDataFactory.one) extends DataFactoryI:

   private val bnodeCounter: Iterator[BigInt] = new Iterator[BigInt]:
      var nextNode = bnodeStart

      override def hasNext: Boolean = true

      override def next(): BigInt =
         nextNode = nextNode + EZDataFactory.one
         nextNode
   private val bnode: Iterator[String] = bnodeCounter.map(_.toString(Character.MAX_RADIX))

   override val namedNode: js.Function1[String, NamedNode] =
     value => NamedNode(value)

   override val literal: js.Function2[String, js.UndefOr[String | NamedNode], Literal] =
     (value, tp) =>
       tp.map {
         case lang: String   => Literal(value, lang)
         case uri: NamedNode => Literal(value, uri)
       }.getOrElse(Literal(value))

   override val variable: js.Function1[String, Variable] =
     (name) => Variable(name)

   override val blankNode: js.Function1[js.UndefOr[String], BlankNode] =
     label =>
       label.map(lstr =>
         if !lstr.contains('#') then BlankNode(lstr)
         else BlankNode(bnode.next())
       ).getOrElse(BlankNode(bnode.next()))

   import Quad.{Subject, Predicate, Object, Graph}
   override val quad: js.Function4[Subject, Predicate, Object, js.UndefOr[Graph], Quad] =
     (subj, pred, obj, graph) =>
       Quad(subj, pred, obj, graph.getOrElse(defaultGraph()))

   override val defaultGraph: js.Function0[DefaultGraph] = () => new DefaultGraph()

   override val id: js.Function1[Term[?], String] = term => term.toString

end DataFactory

//todo: how do frameworks make sure blank nodes don't end up clashing in their store?
@JSExportTopLevel("DataFactory")
object DataFactory:
   val one: BigInt = BigInt(1)

   @JSExport("create")
   def apply(): DataFactory = new DataFactory(one)
