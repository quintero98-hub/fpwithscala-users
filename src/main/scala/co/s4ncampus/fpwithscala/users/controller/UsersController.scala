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
                    user <- req.as[User]
                    result <- userService.create(user).value
                } yield result
                
                action.flatMap {
                    case Right(saved) => Ok(saved.asJson)
                    case Left(UserAlreadyExistsError(existing)) => Conflict(s"The user with legal id ${existing.legalId} already exists")
                }
        }
    //falta validar si el usuario existe
    private def updateUser(userService: UserService[F]): HttpRoutes[F] = {
        HttpRoutes.of[F] {
            //@GET public String printUserName(@PathParam("userName") String userId)
            case req@ PUT -> Root  =>
                val action2 = for {
                    user   <- req.as[User]
                    result <- userService.upload(user).value
                } yield result

                action2.flatMap {
                    case Right(saved) => Ok(saved.asJson)
                    case Left(UserAlreadyExistsError(existing)) => Conflict(s"The user with legal id ${existing.legalId} already exists")
                }
        }
    }

    private def getUser(userService: UserService[F]): HttpRoutes[F] = {
        HttpRoutes.of[F] {
            case req@ GET -> Root  =>
                val action2 = for {
                    user   <- req.as[User]
                    result <- userService.getUser("103").value
                } yield result

                action2.flatMap {
                    case Right(saved) => Ok(saved.asJson)
                    case Left(UserAlreadyExistsError(existing)) => Conflict(s"The user with legal id ${existing.legalId} already exists")
                }
        }
    }

    /*
    * def get(id: Long): Action[AnyContent] = Action.async { request: Request[AnyContent] =>
    clientsDao.get(id).map(_.getOrElse(throw NotFoundException()))
      .map(x => Ok(Json.toJson(x)))
  }

    * */

    def endpoints(userService: UserService[F]): HttpRoutes[F] = {
        //To convine routes use the function `<+>`
        createUser(userService)<+>updateUser(userService)
        //<+>getUser(userService)
    }

}

object UsersController {
    def endpoints[F[_]: Sync](userService: UserService[F]): HttpRoutes[F] =
        new UsersController[F].endpoints(userService)
}