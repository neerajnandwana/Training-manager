package com.mgr.training.view;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import javax.ws.rs.WebApplicationException;

/**
 * The rendering engine for a type of view.
 */
public interface ViewRenderer {

    /**
     * Renders the given {@link View} for the given {@link Locale} to the given {@link
     * OutputStream}.
     *
     * @param view   a view
     * @param locale the locale in which the view should be rendered
     * @param output the output stream
     * @throws IOException             if there is an error writing to {@code output}
     * @throws WebApplicationException if there is an error rendering the template
     */
    void render(View view,
                Locale locale,
                OutputStream output) throws IOException, WebApplicationException;
}
