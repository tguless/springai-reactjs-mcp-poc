package com.example.mcpclient.tools;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * Date and time utility tools for the AI assistant.
 * These tools are always in scope.
 */
@Configuration
public class DateTimeTools {

    private static final Logger logger = LoggerFactory.getLogger(DateTimeTools.class);
    private static final ZoneId UTC = ZoneId.of("UTC");
    private static final DateTimeFormatter ISO_FORMAT = DateTimeFormatter.ISO_DATE_TIME;
    private static final DateTimeFormatter HUMAN_FORMAT = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a z");

    @Bean(name = "dateTimeToolProviderBean")
    @Qualifier("dateTimeToolProvider")
    public ToolCallbackProvider dateTimeToolProvider() {
        logger.info("Registering date time tools");
        return MethodToolCallbackProvider.builder()
                .toolObjects(new DateTimeUtil())
                .build();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DateTimeResponse {
        private String userLocalTime;
        private String utcTime;
        private String userTimezone;
        private String formattedDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DateTimeDifference {
        private String fromDate;
        private String toDate;
        private long days;
        private long hours;
        private long minutes;
        private long seconds;
        private long totalSeconds;
    }

    public class DateTimeUtil {

        @Tool(description = "Get the current date and time in the user's timezone and UTC")
        public DateTimeResponse getCurrentDateTime(ToolContext context) {
            HttpServletRequest request = (HttpServletRequest) context.getContext().get("httpRequest");
            
            // Determine timezone (either from request or default to system)
            ZoneId userZone = getUserTimeZone(request);
            
            // Get current time in user timezone and UTC
            ZonedDateTime userTime = ZonedDateTime.now(userZone);
            ZonedDateTime utcTime = ZonedDateTime.now(UTC);
            
            return new DateTimeResponse(
                userTime.format(ISO_FORMAT),
                utcTime.format(ISO_FORMAT),
                userZone.toString(),
                userTime.format(HUMAN_FORMAT)
            );
        }

        @Tool(description = "Format a date string according to the specified pattern in the user's timezone")
        public String formatDate(
            @ToolParam(description = "ISO date-time string to format (e.g., '2023-01-01T12:00:00Z')") String dateString,
            @ToolParam(description = "Date format pattern (e.g., 'yyyy-MM-dd', 'MMMM d, yyyy', etc.)") String pattern,
            ToolContext context
        ) {
            try {
                HttpServletRequest request = (HttpServletRequest) context.getContext().get("httpRequest");
                ZoneId userZone = getUserTimeZone(request);
                
                // Parse the input date
                Instant instant = Instant.parse(dateString);
                ZonedDateTime dateTime = instant.atZone(userZone);
                
                // Format using the provided pattern
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                return dateTime.format(formatter);
            } catch (Exception e) {
                logger.error("Error formatting date: {}", e.getMessage());
                return "Error formatting date: " + e.getMessage();
            }
        }

        @Tool(description = "Calculate time between two dates")
        public DateTimeDifference calculateTimeBetween(
            @ToolParam(description = "Start date in ISO format (e.g., '2023-01-01T12:00:00Z')") String fromDate,
            @ToolParam(description = "End date in ISO format (e.g., '2023-01-01T12:00:00Z')") String toDate
        ) {
            try {
                Instant from = Instant.parse(fromDate);
                Instant to = Instant.parse(toDate);
                
                Duration duration = Duration.between(from, to);
                
                long days = duration.toDays();
                long hours = duration.toHoursPart();
                long minutes = duration.toMinutesPart();
                long seconds = duration.toSecondsPart();
                
                return new DateTimeDifference(
                    fromDate,
                    toDate,
                    days,
                    hours,
                    minutes,
                    seconds,
                    duration.toSeconds()
                );
            } catch (Exception e) {
                logger.error("Error calculating time between dates: {}", e.getMessage());
                return new DateTimeDifference(
                    fromDate,
                    toDate,
                    0, 0, 0, 0, 0
                );
            }
        }

        @Tool(description = "Add or subtract time from a date")
        public Map<String, String> modifyDate(
            @ToolParam(description = "Base date in ISO format (e.g., '2023-01-01T12:00:00Z')") String baseDate,
            @ToolParam(description = "Number of units to add (negative to subtract)") long amount,
            @ToolParam(description = "Time unit: DAYS, HOURS, MINUTES, SECONDS, MONTHS, YEARS") String unit,
            ToolContext context
        ) {
            try {
                HttpServletRequest request = (HttpServletRequest) context.getContext().get("httpRequest");
                ZoneId userZone = getUserTimeZone(request);
                
                // Parse the input date
                Instant base = Instant.parse(baseDate);
                ZonedDateTime dateTime = base.atZone(userZone);
                
                // Apply the modification
                ChronoUnit chronoUnit = ChronoUnit.valueOf(unit.toUpperCase());
                ZonedDateTime modified = dateTime.plus(amount, chronoUnit);
                
                // Return the results in multiple formats
                Map<String, String> result = new HashMap<>();
                result.put("isoFormat", modified.format(ISO_FORMAT));
                result.put("humanReadable", modified.format(HUMAN_FORMAT));
                result.put("userTimezone", userZone.toString());
                result.put("utcTime", modified.withZoneSameInstant(UTC).format(ISO_FORMAT));
                
                return result;
            } catch (Exception e) {
                logger.error("Error modifying date: {}", e.getMessage());
                Map<String, String> error = new HashMap<>();
                error.put("error", "Error modifying date: " + e.getMessage());
                return error;
            }
        }
        
        private ZoneId getUserTimeZone(HttpServletRequest request) {
            ZoneId zoneId = ZoneId.systemDefault();
            
            // Try to get timezone from request if available
            if (request != null) {
                String timezone = request.getHeader("X-Timezone");
                if (timezone != null && !timezone.isEmpty()) {
                    try {
                        zoneId = ZoneId.of(timezone);
                    } catch (Exception e) {
                        logger.warn("Invalid timezone in request header: {}", timezone);
                    }
                }
            }
            
            return zoneId;
        }
    }
} 