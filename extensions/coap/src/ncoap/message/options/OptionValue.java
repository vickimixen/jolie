package ncoap.message.options;

import com.google.common.net.InetAddresses;
import com.google.common.primitives.Longs;

import io.netty.util.CharsetUtil;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;

public abstract class OptionValue<T> {

    private static final String UNKNOWN_OPTION = "Unknown option no. %d";
    private static final String VALUE_IS_DEFAULT_VALUE = "Given value is "
	    + "default value for option no. %d.";
    private static final String OUT_OF_ALLOWED_RANGE = "Given value length "
	    + "(%d) is out of allowed range "
	    + "for option no. %d (min: %d, max; %d).";
    private static final Charset charset = CharsetUtil.UTF_8;

    public static enum Type {
	EMPTY, STRING, UINT, OPAQUE
    }

    public static final long MAX_AGE_DEFAULT = 60;
    public static final long MAX_AGE_MAX = 0xFFFFFFFFL;
    public static final byte[] ENCODED_MAX_AGE_DEFAULT
	    = new BigInteger(1,
		    Longs.toByteArray(MAX_AGE_DEFAULT)).toByteArray();
    public static final long URI_PORT_DEFAULT = 5683;
    public static final byte[] ENCODED_URI_PORT_DEFAULT
	    = new BigInteger(1,
		    Longs.toByteArray(URI_PORT_DEFAULT)).toByteArray();

    private static class Characteristics {

	private Type type;
	private int minLength;
	private int maxLength;

	private Characteristics(Type type, int minLength, int maxLength) {
	    this.type = type;
	    this.minLength = minLength;
	    this.maxLength = maxLength;
	}

	public Type getType() {
	    return type;
	}

	public int getMinLength() {
	    return minLength;
	}

	public int getMaxLength() {
	    return maxLength;
	}
    }

    private static HashMap<Integer, Characteristics> CHARACTERISTICS
	    = new HashMap<>();

    static {
	CHARACTERISTICS.put(Option.IF_MATCH,
		new Characteristics(OptionValue.Type.OPAQUE, 0, 8));
	CHARACTERISTICS.put(Option.URI_HOST,
		new Characteristics(OptionValue.Type.STRING, 1, 255));
	CHARACTERISTICS.put(Option.ETAG,
		new Characteristics(OptionValue.Type.OPAQUE, 1, 8));
	CHARACTERISTICS.put(Option.IF_NONE_MATCH,
		new Characteristics(OptionValue.Type.EMPTY, 0, 0));
	CHARACTERISTICS.put(Option.URI_PORT,
		new Characteristics(OptionValue.Type.UINT, 0, 2));
	CHARACTERISTICS.put(Option.LOCATION_PATH,
		new Characteristics(OptionValue.Type.STRING, 0, 255));
	CHARACTERISTICS.put(Option.OBSERVE,
		new Characteristics(OptionValue.Type.UINT, 0, 3));
	CHARACTERISTICS.put(Option.URI_PATH,
		new Characteristics(OptionValue.Type.STRING, 0, 255));
	CHARACTERISTICS.put(Option.CONTENT_FORMAT,
		new Characteristics(OptionValue.Type.UINT, 0, 2));
	CHARACTERISTICS.put(Option.MAX_AGE,
		new Characteristics(OptionValue.Type.UINT, 0, 4));
	CHARACTERISTICS.put(Option.URI_QUERY,
		new Characteristics(OptionValue.Type.STRING, 0, 255));
	CHARACTERISTICS.put(Option.ACCEPT,
		new Characteristics(OptionValue.Type.UINT, 0, 2));
	CHARACTERISTICS.put(Option.LOCATION_QUERY,
		new Characteristics(OptionValue.Type.STRING, 0, 255));
	CHARACTERISTICS.put(Option.BLOCK_2,
		new Characteristics(OptionValue.Type.UINT, 0, 3));
	CHARACTERISTICS.put(Option.BLOCK_1,
		new Characteristics(OptionValue.Type.UINT, 0, 3));
	CHARACTERISTICS.put(Option.SIZE_2,
		new Characteristics(OptionValue.Type.UINT, 0, 4));
	CHARACTERISTICS.put(Option.PROXY_URI,
		new Characteristics(OptionValue.Type.STRING, 1, 1034));
	CHARACTERISTICS.put(Option.PROXY_SCHEME,
		new Characteristics(OptionValue.Type.STRING, 1, 255));
	CHARACTERISTICS.put(Option.SIZE_1,
		new Characteristics(OptionValue.Type.UINT, 0, 4));
	CHARACTERISTICS.put(Option.ENDPOINT_ID_1,
		new Characteristics(OptionValue.Type.OPAQUE, 0, 8));
	CHARACTERISTICS.put(Option.ENDPOINT_ID_2,
		new Characteristics(OptionValue.Type.OPAQUE, 0, 8));
    }

    public static Type getType(int optionNumber)
	    throws IllegalArgumentException {

	Characteristics characteristics = CHARACTERISTICS.get(optionNumber);
	if (characteristics == null) {
	    throw new IllegalArgumentException(String.format(UNKNOWN_OPTION,
		    optionNumber));
	} else {
	    return characteristics.getType();
	}
    }

    public static int getMinLength(int optionNumber)
	    throws IllegalArgumentException {

	Characteristics characteristics = CHARACTERISTICS.get(optionNumber);
	if (characteristics == null) {
	    throw new IllegalArgumentException(String.format(UNKNOWN_OPTION,
		    optionNumber));
	} else {
	    return characteristics.getMinLength();
	}
    }

    public static int getMaxLength(int optionNumber)
	    throws IllegalArgumentException {

	Characteristics characteristics = CHARACTERISTICS.get(optionNumber);
	if (characteristics == null) {
	    throw new IllegalArgumentException(String.format(UNKNOWN_OPTION,
		    optionNumber));
	} else {
	    return characteristics.getMaxLength();
	}
    }

    public static boolean isDefaultValue(int optionNumber, byte[] value) {

	if (optionNumber == Option.URI_PORT && Arrays.equals(value,
		ENCODED_URI_PORT_DEFAULT)) {
	    return true;
	} else if (optionNumber == Option.MAX_AGE && Arrays.equals(value,
		ENCODED_MAX_AGE_DEFAULT)) {
	    return true;
	} else if (optionNumber == Option.URI_HOST) {
	    String hostName = new String(value, charset);
	    if (hostName.startsWith("[") && hostName.endsWith("]")) {
		hostName = hostName.substring(1, hostName.length() - 1);
	    }

	    if (InetAddresses.isInetAddress(hostName)) {
		return true;
	    }
	}

	return false;
    }

    protected byte[] value;

    protected OptionValue(int optionNumber, byte[] value, boolean allowDefault)
	    throws IllegalArgumentException {

	if (!allowDefault && OptionValue.isDefaultValue(optionNumber, value)) {
	    throw new IllegalArgumentException(String.format(
		    VALUE_IS_DEFAULT_VALUE, optionNumber));
	}

	if (getMinLength(optionNumber) > value.length
		|| getMaxLength(optionNumber) < value.length) {
	    throw new IllegalArgumentException(String.format(
		    OUT_OF_ALLOWED_RANGE, value.length, optionNumber,
		    getMinLength(optionNumber), getMaxLength(optionNumber)));
	}

	this.value = value;
    }

    public byte[] getValue() {
	return this.value;
    }

    public abstract T getDecodedValue();

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object object);

    @Override
    public String toString() {
	return "" + this.getDecodedValue();
    }

}
