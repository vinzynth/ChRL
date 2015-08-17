/**
 * @author Christian Richard Leopold - ChRL
 * Jul 24, 2015 - 4:01:27 PM
 * chrl-config
 * at.chrl.nutils.configuration.transformers
 */
package at.chrl.nutils.configuration.transformers;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import at.chrl.nutils.configuration.PropertyTransformer;
import at.chrl.nutils.configuration.TransformationException;

/**
 * @author Christian Richard Leopold - ChRL <br>
 * Jul 24, 2015 - 4:01:27 PM
 *
 */
public class DateTransformer implements PropertyTransformer<Date> {

	/**
	 * Shared instance of this transformer. It's thread-safe so no need of
	 * multiple instances
	 */
	public static final DateTransformer SHARED_INSTANCE = new DateTransformer();
	
	/**
	 * Date Formats
	 */
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat DATE_AND_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd kk:mm");
	
	/**
	 * {@inheritDoc}
	 * @see at.chrl.nutils.configuration.PropertyTransformer#transform(java.lang.String, java.lang.reflect.Field)
	 */
	@Override
	public Date transform(String value, Field field) throws TransformationException {
		try {
			return DATE_AND_TIME_FORMAT.parse(value);
		} catch (ParseException e) {
			try {
				return DATE_FORMAT.parse(value);
			} catch (ParseException e1) {
				throw new TransformationException(e1);
			}
		}
	}
}
