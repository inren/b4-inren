import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.filter.ThresholdFilter
import ch.qos.logback.core.ConsoleAppender

import static ch.qos.logback.classic.Level.INFO
import static ch.qos.logback.classic.Level.ERROR
import static ch.qos.logback.classic.Level.DEBUG

appender("RootConsoleAppender", ConsoleAppender) {
	encoder(PatternLayoutEncoder) { pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n" }
}

logger("org.bricket.b4", DEBUG)

// logger("org.apache.wicket", DEBUG)

root(ERROR, ["RootConsoleAppender"])