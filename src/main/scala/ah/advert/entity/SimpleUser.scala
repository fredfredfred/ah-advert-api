package ah.advert.entity

case class SimpleUser(id: Long, name: String, email: String)

object SimpleUser {
  def fromUser(user: User) = new SimpleUser(user.id, user.name, user.email)
}
