package com.github.rgmatute;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Autor Ronny Gabriel Matute Granizo 
 * Email: rgmatute91@gmail.com 
 * Whatsapp: +593 981851214
 **/
public class __ {

	public static boolean isString(String value) {
		if (!(value instanceof String)) {
			return false;
		}
		return true;
	}

	public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}

		return true;
	}

	public static boolean isObject(Object obj, String nameObject) {
		try {
			if (!obj.getClass().getName().equals(nameObject)) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean isDateTime_(String ddmmyyyy) {
		if (__.isDateTime(ddmmyyyy, "dd/MM/yyyy")) {
			return true;
		}
		return __.isDateTime(ddmmyyyy, "dd-MM-yyyy");
	}

	public static boolean isDateTime(String dateTime, String dateFromat) {
		if (dateTime == null) {
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
		sdf.setLenient(false);
		try {
			Date date = sdf.parse(dateTime);
			System.out.println(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean isNull(Object value) {
		return value == null;
	}

	/**
	 * Valida si es Nulo y Empty a la vez
	 */
	public static boolean isNullEmpty(Object value) {
		if (value != null) {
			return __.isMatch(value + "", "^$");
		}
		return true;
	}

	/**
	 * Valida si es Nulo y Empty a la vez
	 */
	public static boolean isEqual(Object a, Object b) {
		return (a.equals(b));
	}

	/**
	 * Solo acepta formatos de Correo electronicos
	 */
	public static boolean isEmail(String value) {
		return __.isMatch(value, "[-\\w\\.]+@\\w+\\.\\w+.\\w+");
	}

	/**
	 * sólo puede contener caracteres numéricos.
	 */
	public static boolean isNumeric(String value) {
		return __.isMatch(value, "^[0-9]*$");
	}

	/**
	 * sólo puede contener caracteres numéricos con hasta cuatro decimales separados
	 * por punto.
	 */
	public static boolean isDecimal(String value) {
		return __.isDecimal(value, 4);
	}

	/**
	 * sólo puede contener caracteres numéricos con hasta 'N' decimales separados
	 * por punto.
	 */
	public static boolean isDecimal(String value, int length) {
		return __.isMatch(value, "^[0-9]+(.[0-9]{1," + length + "})?$");
	}

	/**
	 * Retorna true , si encuentra el caracter especificado
	 */
	public static boolean isContaint(String value, String characterContaint) {
		return value.contains(characterContaint);
	}

	/**
	 * sólo puede contener caracteres alfabéticos. No acepta espacios.
	 */
	public static boolean isAlpha(String value) {
		return __.isMatch(value, "^[a-zA-ZáéíóúÁÉÍÑÓÚÜ]*$");
	}

	/**
	 * sólo puede contener caracteres alfabéticos y espacios.
	 */
	public static boolean isAlphaSpace(String value) {
		return __.isMatch(value, "^[a-zA-ZáéíóúÁÉÍÑÓÚÜ\\s]*$");
	}

	/**
	 * sólo puede contener caracteres alfanuméricos.
	 */
	public static boolean isAlphaNum(String value) {
		return __.isMatch(value, "^[a-zA-Z0-9áéíóúÁÉÍÑÓÚÜ]*$");
	}

	/**
	 * sólo puede contener caracteres alfanuméricos y espacios.
	 */
	public static boolean isAlphaNumSpace(String value) {
		return __.isMatch(value, "^[a-zA-Z0-9áéíóúÁÉÍÑÓÚÜ\\s]*$");
	}

	/**
	 * sólo permite letras, espacios, números, punto, numeral, y guión medio o bajo.
	 */
	public static boolean isAnyChar(String value) {
		return __.isMatch(value, "(^[A-Za-z0-9#-_.\\u00C0-\\u017F\\s]*$)+");
	}

	/**
	 * es de tipo placa vehicular y debe constar de 3 letras guion (-) 4 números.
	 */
	public static boolean isPlaca(String value) {
		return __.isMatch(value, "[a-zA-Z]{3}-[0-9]{4}");
	}

	/**
	 * Solo acepta formato de fecha yyyy-mm-dd.
	 */
	public static boolean isDate(String yyyymmdd) {
		return __.isDate(yyyymmdd, "^\\d{4}-\\d{2}-\\d{2}$");
	}

	/**
	 * Acepta cualquier tipo de formato
	 */
	public static boolean isDate(String value, String formatRegEx) {
		return __.isMatch(value, "^" + formatRegEx + "$");
	}

	/**
	 * Acepta cualquier tipo de Expresion regular
	 */
	public static boolean isMatch(String value, String PatternRegEx) {
		Pattern pattern = Pattern.compile(PatternRegEx);
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
		// return value.matches(PatternRegEx);
	}

	/**
	 * Acepta unicamente url validas
	 */
	public static boolean isUrl(String value) {
		return __.isMatch(value, "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
	}

	/**
	 * Acepta unicamente url validas
	 */
	public static boolean isFullname(String value) {
		return __.isMatch(value, "^[a-zA-z ]*$");
	}

	/**
	 * Acepta unicamente Numero de Telefono
	 */
	public static boolean isPhone(String value) {
		return __.isMatch(value, "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}");
	}

	/**
	 * Valida si esta vacio
	 */
	public static boolean isEmpty(Object value) {
		return (value instanceof String) ? isMatch(value.toString(), "^$") : isNull(value);
	}

	/**
	 * Acepta solo DateTime
	 */
	public static boolean isDateTime(String value) {
		return __.isMatch(value, "\\d{4}-[01]\\d-[0-3]\\dT[0-2]\\d:[0-5]\\d:[0-5]\\d(?:\\.\\d+)?Z?");
	}

	/**
	 * Acepta Direccion MAC-Address xx:xx:xx:xx:xx:xx
	 */
	public static boolean isMacAddress(String value) {
		return __.isMatch(value, "^([a-fA-F0-9]{2}[:\\.-]?){5}[a-fA-F0-9]{2}$");
	}

	/**
	 * Acepta solamente direcciones ipv4
	 */
	public static boolean isIpv4(String value) {
		return __.isMatch(value,
				"^((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.){3}(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])$");
	}

	/**
	 * Acepta solamente direcciones Ipv6
	 */
	public static boolean isIpv6(String value) {
		return __.isMatch(value, "^(([0-9a-fA-F]{0,4}:){1,7}[0-9a-fA-F]{0,4})$");
	}

	/**
	 * Acepta solamente Latitudes validas
	 */
	public static boolean isLatitude(String value) {
		return __.isMatch(value, "^(\\+|-)?(?:90(?:(?:\\.0{1,7})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,7})?))$");
	}

	/**
	 * Acepta solamente Longitudes validas
	 */
	public static boolean isLongitude(String value) {
		return __.isMatch(value,
				"^(\\+|-)?(?:180(?:(?:\\.0{1,7})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,7})?))$");
	}

	/**
	 * Acepta solamente vocales
	 */
	public static boolean isVocal(String value) {
		return __.isMatch(value, "^([aeiouAEIOU])+$");
	}

	/**
	 * Acepta solamente consonantes
	 */
	public static boolean isConsonante(String value) {
		return !__.isMatch(value, "^([aeiouAEIOU])+$");
	}

	/**
	 * Acepta solamente Numeros pares
	 */
	public static boolean isPar(String value) {
		return __.isMatch(value, "^[0-9]*[02468]$");
	}

	/**
	 * Acepta solamente Numeros impares
	 */
	public static boolean isImpar(String value) {
		return !__.isPar(value);
	}

	public static String currentDate(String formatt) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern(formatt);
		LocalDateTime localDateTime = LocalDateTime.now();
		// LocalDate localDate = LocalDate.now();
		return (format.format(localDateTime));
	}

	public static String currentDate() {
		return __.currentDate("dd/MM/yyyy");
	}

	public static String currentDateTime() {
		return __.currentDate("dd/MM/yyyy HH:mm:ss");
	}

	public static String currentDateTimeIso() {
		DateTimeFormatter format = DateTimeFormatter.ISO_DATE_TIME;
		LocalDateTime localDateTime = LocalDateTime.now();
		return (format.format(localDateTime) + "Z");
	}

	public static String formattDateTime(String formatt, String value) {
		SimpleDateFormat format = new SimpleDateFormat(formatt);
		LocalDateTime localDateTime = LocalDateTime.parse(value);
		return (format.format(localDateTime));
	}

	public static <E extends Comparable<? super E>> E max(final Collection<E> collection) {
		return Collections.max(collection);
	}

	public static boolean isJson(Object json) {
		try {
			new com.google.gson.Gson().fromJson((json + ""), Object.class);
			return true;
		} catch (com.google.gson.JsonSyntaxException e) {
			return false;
		}
	}

	public static String objectToJson(Object object) {
		return new com.google.gson.Gson().toJson(object);
	}

	public static Object jsonToObject(String json) {
		return new com.google.gson.Gson().fromJson(json, Object.class);
	}

	public static void console(Object message) {
		/*if (!isEmpty(InvokeProperties.getProperty("spring.services.consoleTitle"))) {
			if (isEqual(InvokeProperties.getProperty("spring.services.consoleTitle"), "true")) {*/
				println(characterGenerate("*", 40));
				println(message);
				println(characterGenerate("*", 40));
			/*}
		}*/
	}

	public static void consoleTitle(String title, Object message) {
		/*if (!isEmpty(InvokeProperties.getProperty("spring.services.consoleTitle"))) {
			if (isEqual(InvokeProperties.getProperty("spring.services.consoleTitle"), "true")) {*/
				println(characterGenerate("*", 20) + title + characterGenerate("*", 20));
				println(message);
				println(characterGenerate("*", (40 + title.length())));
			/*}
		}*/
	}

	public static void consoleTitle(String title, Object message, int lengthCharacter) {
		/*if (!isEmpty(InvokeProperties.getProperty("spring.services.consoleTitle"))) {
			if (isEqual(InvokeProperties.getProperty("spring.services.consoleTitle"), "true")) {*/
				println(characterGenerate("*", (lengthCharacter / 2)) + title
						+ characterGenerate("*", (lengthCharacter / 2)));
				println(message);
				println(characterGenerate("*", (lengthCharacter + title.length())));
	/*		}
		}*/
	}

	public static void println(Object message) {
		System.out.println(message);
	}

	public static void print(Object message) {
		System.out.print(message);
	}

	public static String characterGenerate(String character, int length) {
		String r = "";
		for (int i = 0; i < length; i++) {
			r += character;
		}
		return r;
	}

	public static JSONObject json(String jsonString) {
		return new JSONObject(jsonString);
	}

	public static Object findJson(JSONObject jObj, String k) throws JSONException {
		Iterator<?> keys = jObj.keys();
		String val = "";

		while (keys.hasNext()) {
			String key = (String) keys.next();
			// if (jObj.opt(key).equals(k)) {
			if (key.equals(k)) {
				return jObj.get(key);
				// return jObj;// value
				// return key;
			}

			if (jObj.get(key) instanceof JSONObject) {
				return findJson((JSONObject) jObj.get(key), k);
			}

			if (jObj.get(key) instanceof JSONArray) {
				JSONArray jar = (JSONArray) jObj.get(key);
				for (int i = 0; i < jar.length(); i++) {
					JSONObject j = jar.getJSONObject(i);
					val = findJson(j, k).toString();
					if (!val.isEmpty()) {
						// return find(j, k);
						return val;
					}
				}
			}
		}
		return val;
	}
	
	public static String OBTIENE_TELEFONO_DNC(String telefono,String version_telefono) {
		String num_final = "";
		switch(version_telefono) {
		case "INT":
			switch(telefono.length()) {
				case 12:
					//num_final = telefono;
					num_final = "593" + telefono.substring(3,telefono.length());
				break;
				case 10:
					num_final = "593" + telefono.substring(1,telefono.length());
					break;
				case 9:
					num_final = "593" + telefono.substring(0,telefono.length());
					break;
				case 8:
					num_final = "5939" + telefono.substring(0,telefono.length());
				break;
			}
			break;
		case "NAC":
			switch(telefono.length()) {
				case 12:
					num_final = "0" + telefono.substring(3,telefono.length());
				break;
				case 10:
					num_final = "0" + telefono.substring(1,telefono.length());
					break;
				case 9:
					num_final = "0" + telefono.substring(0,telefono.length());
					break;
				case 8:
					num_final = "09" + telefono.substring(0,telefono.length());
					break;
			}
			break;
		case "LOC":
			switch(telefono.length()) {
				case 12:
					num_final = "" + telefono.substring(3,telefono.length());
				break;
				case 10:
					num_final = "" + telefono.substring(1,telefono.length());
					break;
				case 9:
					num_final = "" + telefono.substring(0,telefono.length());
					break;
				case 8:
					num_final = "9" + telefono.substring(0,telefono.length());
					break;
			}
			break;
		case "LEG":
			switch(telefono.length()) {
				case 12:
					num_final = "" + telefono.substring(4,telefono.length());
				break;
				case 10:
					num_final = "" + telefono.substring(2,telefono.length());
					break;
				case 9:
					num_final = "" + telefono.substring(1,telefono.length());
					break;
				case 8:
					num_final = "" + telefono.substring(0,telefono.length());
					break;
			}
			break;
		}
		//num_final = telefono.length() + "";
		return num_final;
	}
	
	public String handle(Object orderEcom,Function<Object,String> fun) {
		return fun.apply(orderEcom);
	}

}