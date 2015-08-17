/**
 * This file is part of aion-lightning <aion-lightning.org>.
 * 
 * aion-lightning is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * aion-lightning is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * aion-lightning. If not, see <http://www.gnu.org/licenses/>.
 */
package at.chrl.rebellion.metadata;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that can be applied on Runnable classes in scripts.<br>
 * All the scheduled tasks will be automatically cancelled in case of unloading
 * script context<br>
 * Please note that loading/unloading is controlled by
 * {@link at.chrl.rebellion.classlistener.ScheduledTaskClassListener}
 *
 * @author SoulKeeper
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Scheduled {

	/**
	 * Array of cron expressions. Each one should be valid.
	 *
	 * @return Array of cron expressions that will be used to schedule runnable
	 */
	String[] value();

	/**
	 * Effective only in case if {@link #value()} has more than 1 element.<br>
	 * If true - a new instance of the runnable will be created for each cron
	 * expression.<br>
	 * If false - single instance will be triggered multiple times
	 */
	boolean instancePerCronExpression() default false;

	/**
	 * If this scheduler should be disabled ignored
	 * 
	 * @return disabled or not
	 */
	boolean disabled() default false;

	/**
	 * Indicates if this task is long-running task or not.<br>
	 * 
	 * @return true if is long-running task
	 */
	boolean longRunningTask() default false;
}
