package co.s4ncampus.fpwithscala.users.domain

import cats.data.OptionT

trait UserRepositoryAlgebra[F[_]] {
  def create(user: User): F[User]
  def findByLegalId(legalId: String): OptionT[F, User]
  def upload(user: User, legalId:String): F[Boolean]
  def delete(legalId: String):F[Boolean]
  def getAll():F[List[User]]
  //def getUsersByName(name:String):F[List[User]]
}