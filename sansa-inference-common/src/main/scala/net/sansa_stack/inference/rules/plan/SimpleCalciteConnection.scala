package net.sansa_stack.inference.rules.plan

import java.lang.reflect.Type
import java.sql.{Blob, CallableStatement, Clob, DatabaseMetaData, NClob, PreparedStatement, SQLWarning, SQLXML, Savepoint, Statement, Struct}
import java.util
import java.util.Properties
import java.util.concurrent.Executor

import org.apache.calcite.adapter.java.JavaTypeFactory
import org.apache.calcite.config.CalciteConnectionConfig
import org.apache.calcite.jdbc.CalciteConnection
import org.apache.calcite.linq4j.tree.Expression
import org.apache.calcite.linq4j.{Enumerator, Queryable}
import org.apache.calcite.schema.SchemaPlus
import org.apache.calcite.tools.Frameworks

/**
  * @author Lorenz Buehmann
  */
class SimpleCalciteConnection extends CalciteConnection{

  val rootSchema = Frameworks.createRootSchema(true)
  var schema: String = ""

  override def config(): CalciteConnectionConfig = null

  override def getSchema: String = schema

  override def getTypeFactory: JavaTypeFactory = null

  override def getRootSchema: SchemaPlus = rootSchema

  override def getProperties: Properties = null

  override def setSchema(schema: String): Unit = {}

  override def setAutoCommit(autoCommit: Boolean): Unit = {}

  override def setHoldability(holdability: Int): Unit = {}

  override def clearWarnings(): Unit = {}

  override def getNetworkTimeout: Int = 1

  override def createBlob(): Blob = null

  override def createSQLXML(): SQLXML = null

  override def setSavepoint(): Savepoint = null

  override def setSavepoint(name: String): Savepoint = null

  override def getTransactionIsolation: Int = 1

  override def createNClob(): NClob = null

  override def getClientInfo(name: String): String = null

  override def getClientInfo: Properties = null

  override def setNetworkTimeout(executor: Executor, milliseconds: Int): Unit = {}

  override def getMetaData: DatabaseMetaData = null

  override def getTypeMap: util.Map[String, Class[_]] = null

  override def rollback(): Unit = {}

  override def rollback(savepoint: Savepoint): Unit = {}

  override def createStatement(): Statement = null

  override def createStatement(resultSetType: Int, resultSetConcurrency: Int): Statement = null

  override def createStatement(resultSetType: Int, resultSetConcurrency: Int, resultSetHoldability: Int): Statement =
    null

  override def getHoldability: Int = 1

  override def setReadOnly(readOnly: Boolean): Unit = {}

  override def setClientInfo(name: String, value: String): Unit = {}

  override def setClientInfo(properties: Properties): Unit = {}

  override def isReadOnly: Boolean = false

  override def setTypeMap(map: util.Map[String, Class[_]]): Unit = {}

  override def getCatalog: String = null

  override def createClob(): Clob = null

  override def setTransactionIsolation(level: Int): Unit = {}

  override def nativeSQL(sql: String): String = null

  override def prepareCall(sql: String): CallableStatement = null

  override def prepareCall(sql: String, resultSetType: Int, resultSetConcurrency: Int): CallableStatement = null

  override def prepareCall(sql: String, resultSetType: Int, resultSetConcurrency: Int, resultSetHoldability: Int):
  CallableStatement = null

  override def createArrayOf(typeName: String, elements: Array[AnyRef]): java.sql.Array = null

  override def setCatalog(catalog: String): Unit = {}

  override def abort(executor: Executor): Unit = {}

  override def close(): Unit = {}

  override def getAutoCommit: Boolean = false

  override def prepareStatement(sql: String): PreparedStatement = null

  override def prepareStatement(sql: String, resultSetType: Int, resultSetConcurrency: Int): PreparedStatement = null

  override def prepareStatement(sql: String, resultSetType: Int, resultSetConcurrency: Int, resultSetHoldability:
  Int): PreparedStatement = null

  override def prepareStatement(sql: String, autoGeneratedKeys: Int): PreparedStatement = null

  override def prepareStatement(sql: String, columnIndexes: Array[Int]): PreparedStatement = null

  override def prepareStatement(sql: String, columnNames: Array[String]): PreparedStatement = null

  override def isValid(timeout: Int): Boolean = false

  override def releaseSavepoint(savepoint: Savepoint): Unit = {}

  override def isClosed: Boolean = false

  override def createStruct(typeName: String, attributes: Array[AnyRef]): Struct = null

  override def getWarnings: SQLWarning = null

  override def commit(): Unit = {}

  override def unwrap[T](iface: Class[T]): T = null.asInstanceOf[T]

  override def isWrapperFor(iface: Class[_]): Boolean = false

  override def createQuery[T](expression: Expression, aClass: Class[T]): Queryable[T] = null

  override def createQuery[T](expression: Expression, `type`: Type): Queryable[T] = null

  override def execute[T](expression: Expression, aClass: Class[T]): T = null.asInstanceOf[T]

  override def execute[T](expression: Expression, `type`: Type): T = null.asInstanceOf[T]

  override def executeQuery[T](queryable: Queryable[T]): Enumerator[T] = null
}
