package example

import cats.data.NonEmptyList
import cats.effect.IO
import com.dimafeng.testcontainers.{JdbcDatabaseContainer, PostgreSQLContainer}
import com.dimafeng.testcontainers.scalatest.TestContainerForAll
import doobie.Meta
import doobie.implicits.toSqlInterpolator
import doobie.scalatest.IOChecker
import example.models._
import org.flywaydb.core.Flyway
import org.scalatest.flatspec.AnyFlatSpec
import org.testcontainers.utility.DockerImageName

import java.util.UUID

class PgSpec extends AnyFlatSpec with TestContainerForAll with IOChecker {

  override val containerDef = PostgreSQLContainer.Def(
    dockerImageName = DockerImageName.parse("postgres:15.1"),
    databaseName = "testcontainer-scala",
    username = "scala",
    password = "scala"
  )

  import doobie.postgres.implicits._
  implicit val natMeta: Meta[CustomerId] = Meta[UUID].imap(CustomerId.apply)(_.value)

  val customer = Customer(
    CustomerId(UUID.randomUUID()),
    FirstName("john"),
    LastName("doe"),
    Address(NonEmptyList.one(AddressLine("PO Box 123")), ZipCode("50320"), State.Iowa),
    SSN("333-22-4444")
  )

  def runFlywayMigrations(pgContainer: JdbcDatabaseContainer): Unit = Flyway
    .configure()
    .dataSource(pgContainer.jdbcUrl, pgContainer.username, pgContainer.password)
    .driver(pgContainer.driverClassName)
    .load()
    .migrate(): Unit

  "queries" should "pass checks" in {
    withContainers { pgContainer =>
      runFlywayMigrations(pgContainer)
      check(
        sql"""
              | insert into customers
              | (id, first_name, last_name, ssn)
              | values
              | (${customer.id}, ${customer.firstName}, ${customer.lastName}, ${customer.ssn}) ;
              | """.stripMargin.update
      )
    }
  }

  override def transactor: doobie.Transactor[IO] = withContainers { pgContainer =>
    doobie.util.transactor.Transactor.fromDriverManager(
      pgContainer.driverClassName,
      pgContainer.jdbcUrl,
      pgContainer.username,
      pgContainer.password
    )
  }
}
