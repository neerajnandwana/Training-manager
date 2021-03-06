package com.mgr.training.view.freemarker;


import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Locale;

import javax.ws.rs.WebApplicationException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mgr.training.util.Const;
import com.mgr.training.view.View;
import com.mgr.training.view.ViewRenderer;
import com.sun.jersey.api.container.MappableContainerException;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

/**
 * A {@link ViewRenderer} which renders Freemarker ({@code .ftl}) templates.
 */
public class FreemarkerViewRenderer implements ViewRenderer {

    private static final Version FREEMARKER_VERSION = Configuration.getVersion();
    
	private static class TemplateLoader extends CacheLoader<Class<?>, Configuration> {
		@Override
		public Configuration load(Class<?> key) throws Exception {
            final Configuration configuration = new Configuration(FREEMARKER_VERSION);
            configuration.setObjectWrapper(new DefaultObjectWrapperBuilder(FREEMARKER_VERSION).build());
			configuration.loadBuiltInEncodingMap();
			configuration.setDefaultEncoding(Const.CHAR_SET.name());
			configuration.setClassForTemplateLoading(key, "/");
			return configuration;
		}
	}

    private final LoadingCache<Class<?>, Configuration> configurationCache;

	public FreemarkerViewRenderer() {
		this.configurationCache = CacheBuilder.newBuilder()
                .concurrencyLevel(128)
                .build(new TemplateLoader());
	}

	@Override
	public void render(View view, Locale locale, OutputStream output) throws IOException, WebApplicationException {
		try {
			final Configuration configuration = configurationCache.getUnchecked(view.getClass());
			final Charset charset = view.getCharset().or(Charset.forName(configuration.getEncoding(locale)));
			final Template template = configuration.getTemplate(view.getTemplateName(), locale, charset.name());
			template.process(view, new OutputStreamWriter(output, template.getEncoding()));
		} catch (TemplateException e) {
			throw new MappableContainerException(e);
		}
	}

}
