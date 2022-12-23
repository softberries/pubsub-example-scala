package object core {
  final case class RequestError(error: String) extends Exception(error)

  type ClientResponse[T]   = Either[RequestError, T]
  type ClientResponses[T]  = ClientResponse[List[T]]
}
