package org.w3.rdfjs

import utest.*

object TestModel extends TestSuite {

	val bblStr = "https://bblfish.net/#me"
	val df = DataFactory()

	def foaf(att: String)(using df: DataFactory): NamedNode =
		df.namedNode("https://xmlns.com/foaf/0.1/" + att)

	def tests = Tests {
		test("the same Named Nodes is  Equal to itself") {
			val bbl = NamedNode(bblStr)
			val bbl2 = NamedNode(bblStr)
			assert(bbl == bbl2)
		}

		test("the factory") {
			val bbl1 = df.namedNode(bblStr)
			val bbl2 = NamedNode(bblStr)
			assert(bbl1 == bbl2)
			val fname = df.namedNode("name")
			val bblName = df.literal("Henry")
			val st = df.quad(bbl1, fname, bblName)
			assert(st.subj == bbl2)
			assert(st.obj == bblName)
			assert(st.rel == fname)
			assert(st.graph == DefaultGraph)
		}
	}
}