package co.s4ncampus.fpwithscala.users.domain

import cats.data.OptionT

trait UserRepositoryAlgebra[F[_]] {
  def create(user: User): F[User]
  def findByLegalId(legalId: String): OptionT[F, User]
  def getUser(id: String): F[Option[User]]
  def upload(user: User): F[User]
}