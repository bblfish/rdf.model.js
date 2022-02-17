/*
 * Copyright 2021 Henry Story
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package run.cosy.rdfjs.model

import utest.*

object TestModel extends TestSuite:

   val bblStr = "https://bblfish.net/#me"
   val df     = EZDataFactory()

   def foaf(att: String)(using df: EZDataFactory): NamedNode =
     df.namedNode("https://xmlns.com/foaf/0.1/" + att)

   def tests = Tests {
     test("the same Named Nodes is  Equal to itself") {
       val bbl  = NamedNode(bblStr)
       val bbl2 = NamedNode(bblStr)
       assert(bbl == bbl2)
     }

     test("the factory") {
       val bbl1 = df.namedNode(bblStr)
       val bbl2 = NamedNode(bblStr)
       assert(bbl1 == bbl2)
       val fname   = df.namedNode("name")
       val bblName = df.literal("Henry")
       val st      = df.quad(bbl1, fname, bblName)
       assert(st.subj == bbl2)
       assert(st.obj == bblName)
       assert(st.rel == fname)
       assert(st.graph == df.defaultGraph())
     }

     test("Factory.id") {
       println(df.namedNode(bblStr))
       println(df.literal("Hello"))
       println(df.defaultGraph())
       println(df.blankNode("l1"))
     }
   }
