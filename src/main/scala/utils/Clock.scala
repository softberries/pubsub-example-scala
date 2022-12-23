package utils

import java.sql.Timestamp
import java.time.{ZonedDateTime, ZoneId, ZoneOffset}

trait Clock {
  def now(): ZonedDateTime
  def nowTimestamp(): Timestamp
}

class StockholmClock extends Clock {
  override def now(): ZonedDateTime      = ZonedDateTime.now(StockholmClock.zone)
  override def nowTimestamp(): Timestamp = Timestamp.from(ZonedDateTime.now(StockholmClock.zone).toInstant)
}

object StockholmClock {
  lazy val EuropeStockholm: ZoneId     = ZoneId.of("Europe/Stockholm")
  lazy val zone: ZoneOffset            = ZonedDateTime.now(EuropeStockholm).getOffset
}
