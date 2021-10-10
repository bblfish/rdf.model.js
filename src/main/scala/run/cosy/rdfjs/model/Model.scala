package run.cosy.rdfjs.model

import java.math.BigInteger
import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportAll, JSExportTopLevel}

trait Term[Type <: Term.TT](@JSExport val termType: Type):
  @JSExport("equals")
  def termEquals(other: js.UndefOr[Term[?]]): Boolean =
    other.map(o => this.equals(o)).getOrElse(false)
end Term

trait ValueTerm[Type <: Term.TT] extends Term[Type]:
  val value: String

object Term:
  type TT =  VTT | Quad.type
  type VTT = NamedNode.type | Literal.type | Variable.type | BlankNode.type | DefaultGraph.type

  val NamedNode: "NamedNode" =  "NamedNode"
  val Literal: "Literal" = "Literal"
  val Variable: "Variable" = "Variable"
  val BlankNode: "BlankNode" = "BlankNode"
  val DefaultGraph: "DefaultGraph" = "DefaultGraph"
  val Quad: "Quad" = "Quad"
end Term

@JSExportTopLevel("NamedNode")
@JSExportAll
case class NamedNode(override val value: String) extends
  ValueTerm[Term.NamedNode.type], Term(Term.NamedNode)

@JSExportAll
case class Literal private (
  override val value: String, val language: String, val datatype: NamedNode
) extends ValueTerm[Term.Literal.type], Term(Term.Literal):
end Literal

object Literal:
  lazy val rdfLangStr = NamedNode( "http://www.w3.org/1999/02/22-rdf-syntax-ns#langString")
  lazy val xsdStr = NamedNode("http://www.w3.org/2001/XMLSchema#string")
  def apply(value: String): Literal = new Literal(value,"",xsdStr)
  def apply(value: String, lang: String) = new Literal(value,lang,rdfLangStr)
  def apply(value: String, dataType: NamedNode) = new Literal(value,"",dataType)
end Literal

//blank nodes should be created via the factory
@JSExportAll
case class BlankNode private[rdfjs] (val value: String)
  extends ValueTerm[Term.BlankNode.type], Term(Term.BlankNode)

@JSExportTopLevel("Variable")
@JSExportAll
case class Variable(val value: String) extends ValueTerm[Term.Variable.type], Term(Term.Variable)

@JSExportTopLevel("DefaultGraph")
@JSExportAll
object DefaultGraph extends ValueTerm[Term.DefaultGraph.type], Term(Term.DefaultGraph):
  //rdflib.js states in default-graph-uri.ts
  //"Prevents circular dependencies between data-factory-internal and statement"
  //Note: 1) I cannot find it being used though 2) it is modelled as a NamedNode
  //todo: remove if not needed
  val value = "chrome:theSession"


@JSExportAll
case class Quad private[rdfjs] (
  @JSExport("subject") val subj: ValueTerm[?],
  @JSExport("predicate") val rel: NamedNode,
  @JSExport("object") val obj: ValueTerm[?],
  val graph: ValueTerm[?]|DefaultGraph.type
) extends Term(Term.Quad)

@JSExportAll
class DataFactory(bnodeStart: BigInt = DataFactory.one):

  private val bnodeCounter: Iterator[BigInt] = new Iterator[BigInt] {
    var nextNode = bnodeStart
    override def hasNext: Boolean = true
    override def next(): BigInt =
      nextNode = nextNode + DataFactory.one
      nextNode
  }
  private val bnode: Iterator[String] = bnodeCounter.map(_.toString(Character.MAX_RADIX))

  def namedNode(value: String): NamedNode = NamedNode(value)
  def literal(value: String): Literal = Literal(value)
  def literal(value: String, lang: String): Literal = Literal(value,lang)
  def literal(value: String, dataType: NamedNode): Literal = Literal(value,dataType)
  def variable(value: String): Variable = Variable(value)
  def blankNode(): BlankNode =
    BlankNode(bnode.next())
  def blankNode(label: String): BlankNode =
    if !label.contains('#') then BlankNode(label)
    else BlankNode(bnode.next())
  def quad(
    subject: ValueTerm[?], rel: NamedNode, obj: ValueTerm[?]
  ): Quad = Quad(subject, rel, obj, DefaultGraph)
  def quad(
    subject: ValueTerm[?], rel: NamedNode, obj: ValueTerm[?],
    graph: ValueTerm[?]|DefaultGraph.type
  ): Quad = Quad(subject, rel, obj, graph)

end DataFactory

//todo: how do frameworks make sure blank nodes don't end up clashing in their store?
@JSExportTopLevel("DataFactory")
object DataFactory:
  val one: BigInt = BigInt(1)
  @JSExport("create")
  def apply(): DataFactory = new DataFactory(one)

object Test:
  def main(args: Array[String]): Unit =
    val bbl = new NamedNode("https://bblfish.net/#me")
    val bbl2 = new NamedNode("https://bblfish.net/#me")
    println(s"$bbl == bbl2 is "+ (bbl == bbl2))



