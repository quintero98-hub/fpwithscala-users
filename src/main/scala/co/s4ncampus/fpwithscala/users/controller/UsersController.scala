package co.s4ncampus.fpwithscala.users.controller

import co.s4ncampus.fpwithscala.users.domain._
import cats.effect.Sync
import cats.syntax.all._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, HttpRoutes}
import co.s4ncampus.fpwithscala.users.domain.User

class UsersController[F[_]: Sync] extends Http4sDsl[F] {

    implicit val userDecoder: EntityDecoder[F, User] = jsonOf

    private def createUser(userService: UserService[F]): HttpRoutes[F] = 
        HttpRoutes.of[F] {
            case req @ POST -> Root =>
                val action = for {
                    user   <- req.as[User]
                    result <- userService.create(user).value
                } yield result
                println("create" + action)
                action.flatMap {
                    case Right(saved) => Ok(saved.asJson)
                    case Left(UserAlreadyExistsError(existing)) => Conflict(s"The user with legal id ${existing.legalId} already exists")
                }
        }
    //falta validar si el usuario existe
    private def updateUser(userService: UserService[F]): HttpRoutes[F] = {
        HttpRoutes.of[F] {
            case req@ PUT -> Root /legalId =>
                val action = for {
                    user   <- req.as[User]
                    result <- userService.upload(user, legalId).value
                } yield result
                println("update" + action)
                action.flatMap {
                    case Right(existing) => Conflict(s"The user with legal id ${existing.legalId} doesn't exist")
                    case Left(UserDoesntExistError(saved)) => Ok(saved.asJson)
                }
        }
    }

    private def getUser(userService: UserService[F]): HttpRoutes[F] = {
        HttpRoutes.of[F] {
            case GET -> Root /id =>
                userService.get(id).value.flatMap {
                    case Some(user) => Ok(user.asJson)
                    case None       => Conflict(s"Couldn't find the user with legal id $id")
                }
        }
    }

    private def getAll(userService: UserService[F]): HttpRoutes[F] = {
        HttpRoutes.of[F] {
            case GET -> Root =>
                userService.getAll().flatMap {
                    case Nil     => Conflict("There is no users created")
                    case list    => Ok(list.asJson)
                }
        }
    }

    private def deleteUser(userService: UserService[F]): HttpRoutes[F] ={
        HttpRoutes.of[F] {
            case DELETE -> Root /id =>
            userService.delete(id).flatMap{
                case true   => Ok("User deleted")
                case false  => Conflict(s"Couldn't find the user with legal id $id")
            }
        }
    }

    def endpoints(userService: UserService[F]): HttpRoutes[F] = {
        //To convine routes use the function `<+>`
        createUser(userService)<+>updateUser(userService)<+>getUser(userService)<+>deleteUser(userService)<+>getAll(userService)
    }

}

object UsersController {
    def endpoints[F[_]: Sync](userService: UserService[F]): HttpRoutes[F] =
        new UsersController[F].endpoints(userService)
}