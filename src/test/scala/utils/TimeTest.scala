package utils

import java.sql.Timestamp
import java.time.{LocalDateTime, ZonedDateTime}

trait TimeTest { tt =>
  lazy val now   = LocalDateTime.now().atZone(StockholmClock.EuropeStockholm)
  lazy val nowTs = Timestamp.from(now.toInstant)
  lazy val clock = new Clock {
    override def now(): ZonedDateTime = tt.now
    override def nowTimestamp(): Timestamp = nowTs
  }
}
