package com.github.cookingbackend.elide.converter;

import com.yahoo.elide.core.utils.coerce.converters.ElideTypeConverter;
import com.yahoo.elide.core.utils.coerce.converters.Serde;
import java.time.Duration;
import java.time.format.DateTimeParseException;

@ElideTypeConverter(type = Duration.class, name = "Duration")
public class DurationSerde implements Serde<String, Duration> {
	@Override
	public Duration deserialize(String val) {
		try {
			return Duration.parse(val);
		} catch (final DateTimeParseException ex) {
			throw new IllegalArgumentException(ex);
		}
	}

	@Override
	public String serialize(Duration val) {
		return val.toString();
	}
}
