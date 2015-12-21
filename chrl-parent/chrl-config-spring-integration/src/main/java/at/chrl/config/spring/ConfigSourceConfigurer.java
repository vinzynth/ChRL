package at.chrl.config.spring;

import at.chrl.nutils.configuration.ConfigUtil;
import at.chrl.nutils.configuration.Property;
import at.chrl.nutils.configuration.listener.ConfigEventListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * Custom ConfigSourceConfigurer
 *
 * Created by vinzynth on 21.12.15.
 */
@Component
public final class ConfigSourceConfigurer extends PropertySourcesPlaceholderConfigurer implements EnvironmentAware, InitializingBean{

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        super.setEnvironment(environment);
    }

    private Map<String, Object> parseConfig(final Class<?> targetClass, final Object instance){
        Map<String, Object> config = new HashMap<>();
        Arrays.stream(targetClass.getFields()).filter(f -> f.isAnnotationPresent(Property.class)).forEach(f -> {
            Property annotation = f.getAnnotation(Property.class);
            java.lang.String key = annotation.key();
            java.lang.Object value = null;
            try {
                value = f.get(instance);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return;
            }
            config.putIfAbsent(key,value);
        });
        return config;
    }

    private void analyzeConfig(MutablePropertySources propSource, Object object){
        propSource.addFirst(new MapPropertySource(object.getClass().getName() + "@" + object.hashCode(), parseConfig(object.getClass(), object)));
    }

    private void analyzeConfig(MutablePropertySources propSource, Class<?> targetClass){
        propSource.addFirst(new MapPropertySource(targetClass.getName(), parseConfig(targetClass, null)));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final MutablePropertySources envPropSources = ((ConfigurableEnvironment)environment).getPropertySources();
        ConfigUtil.addConfigEventListener(new ConfigEventListener() {
            @Override
            public void onLoadedConfigClass(Class<?> targetClass) {
                analyzeConfig(envPropSources, targetClass);
            }

            @Override
            public void onLoadedConfigObject(Object obj) {
                analyzeConfig(envPropSources, obj);
            }
        });
        ConfigUtil.reload();
    }
}
