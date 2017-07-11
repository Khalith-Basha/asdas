package core.utilities;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * This class contains various utility methods to work with enums.
 */
public class Enums {
	private static Map<Class<? extends Enum<?>>, Map<String, ? extends Enum<?>>> enumData = new HashMap<>();
	private static Map<Class<? extends Enum<?>>, Map<Integer, ? extends Enum<?>>> enumValue = new HashMap<>();

	/**
	 * Returns enum constant by given string. String should be either result of
	 * {@link Enum#toString()} invocation on the constant or string associated
	 * using {@link Symbol} annotation.
	 *
	 * @param enumClass
	 *            class of the enum
	 * @param str
	 *            input string
	 * @param <T>
	 *            class
	 * @return enum constant
	 */
	public static <T extends Enum<T>> T fromString(Class<T> enumClass, String str) {
		if (!enumData.containsKey(enumClass)) {
			enumData.put(enumClass, processEnumSymbols(enumClass));
		}

		Map<String, ?> enumSymbols = enumData.get(enumClass);

		if (!enumSymbols.containsKey(str)) {
			throw new IllegalArgumentException(String.format(
					"There is no enum constant defined in type \"%s\" for string \"%s\"", enumClass.getName(), str));
		}

		return enumClass.cast(enumSymbols.get(str));
	}

	/**
	 * Returns enum constant by given value. Value should be a value associated
	 * using {@link Value} annotation.
	 *
	 * @param enumClass
	 *            class of the enum
	 * @param value
	 *            input value
	 * @param <T>
	 *            class
	 * @return enum constant
	 */
	public static <T extends Enum<T>> T fromInteger(Class<T> enumClass, int value) {
		if (!enumValue.containsKey(enumClass)) {
			enumValue.put(enumClass, processEnumValues(enumClass));
		}

		Map<Integer, ?> enumValues = enumValue.get(enumClass);

		if (!enumValues.containsKey(value)) {
			throw new IllegalArgumentException(String.format(
					"There is no enum constant defined in type \"%s\" for integer \"%s\"", enumClass.getName(), value));
		}

		return enumClass.cast(enumValues.get(value));
	}

	/**
	 * Converts enum constant to string. Result string will be the string
	 * specified in {@link Symbol} annotation for the specified enum constant or
	 * result of invocation {@link Enum#toString()} method.
	 *
	 * @param enumConstant
	 *            enum constant
	 * @return string representation of the enum constant
	 */
	public static String toString(Enum<?> enumConstant) {
		Field field;
		try {
			field = enumConstant.getClass().getDeclaredField(enumConstant.name());
		} catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}

		Symbol representation = field.getAnnotation(Symbol.class);

		return (representation != null) ? representation.value() : enumConstant.toString();
	}

	/**
	 * Converts enum constant to integer. Result value will be the integer
	 * specified in {@link Value} annotation for the specified enum constant.
	 *
	 * @param enumConstant
	 *            enum constant
	 * @return integer representation of the enum constant
	 */
	public static int toInteger(Enum<?> enumConstant) {
		Field field;
		try {
			field = enumConstant.getClass().getDeclaredField(enumConstant.name());
		} catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}

		Value representation = field.getAnnotation(Value.class);

		if (representation == null) {
			throw new RuntimeException("There is no value for specified enum constant");
		}

		return representation.value();
	}

	/**
	 * Processes given enum class and returns map where key is a string
	 * associated with enum which is a value.
	 *
	 * @param enumClass
	 *            class of the enum
	 * @return map object
	 */
	private static <T extends Enum<T>> Map<String, T> processEnumSymbols(Class<T> enumClass) {
		try {
			Map<String, T> map = new HashMap<>();

			for (T enumConstant : enumClass.getEnumConstants()) {
				Field field = enumClass.getDeclaredField(enumConstant.name());
				Symbol representation = field.getAnnotation(Symbol.class);

				if (representation != null) {
					map.put(representation.value(), enumConstant);
				} else {
					map.put(enumConstant.toString(), enumConstant);
				}
			}

			return map;
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Processes given enum class and returns map where key is a integer
	 * associated with enum which is a value.
	 *
	 * @param enumClass
	 *            class of the enum
	 * @return map object
	 */
	private static <T extends Enum<T>> Map<Integer, T> processEnumValues(Class<T> enumClass) {
		try {
			Map<Integer, T> map = new HashMap<>();

			for (T enumConstant : enumClass.getEnumConstants()) {
				Field field = enumClass.getDeclaredField(enumConstant.name());
				Value representation = field.getAnnotation(Value.class);

				if (representation != null) {
					map.put(representation.value(), enumConstant);
				} else {
					throw new NoSuchFieldException("There is no value for specified enum constant");
				}
			}

			return map;
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Annotation that should be applied for enum constant to associate string
	 * with one that differs from name of constant itself.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Symbol {
		String value();
	}

	/**
	 * Annotation that should be applied for enum constant to associate string
	 * with one that differs from name of constant itself.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Value {
		int value();
	}
}
