/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2013-2019 Andres Almiray.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kordamp.gradle.plugin.markdown.tasks

import com.overzealous.remark.Options
import com.overzealous.remark.Remark
import org.pegdown.PegDownProcessor

import java.util.concurrent.locks.ReentrantLock

import static org.pegdown.Extensions.ABBREVIATIONS
import static org.pegdown.Extensions.AUTOLINKS
import static org.pegdown.Extensions.DEFINITIONS
import static org.pegdown.Extensions.FENCED_CODE_BLOCKS
import static org.pegdown.Extensions.HARDWRAPS
import static org.pegdown.Extensions.QUOTES
import static org.pegdown.Extensions.SMARTS
import static org.pegdown.Extensions.SMARTYPANTS
import static org.pegdown.Extensions.SUPPRESS_ALL_HTML
import static org.pegdown.Extensions.TABLES

/**
 * @author Ted Naleid
 * @author Andres Almiray
 */
class MarkdownProcessor {
    private PegDownProcessor processor = null
    private Remark remark = null
    private String baseUri = null

    private static Options remarkOptions = null
    private static int pegdownExtensions = 0

    private final ReentrantLock processorLock = new ReentrantLock()

    /**
     * Converts the provided Markdown into HTML
     *
     * <p>By default this method uses the shared configuration.  However, the default configuration can
     * be overridden by passing in a map or map-like object as the second argument.  With a custom
     * configuration, a new Pegdown processor is created <strong>every call to this method!</strong></p>
     *
     * @param text Markdown-formatted text
     * @param conf If provided, creates a custom pegdown with unique settings just for this instance
     * @return HTML-formatted text
     */
    String markdownToHtml(String text, Map conf = [:]) {
        // lazily created, so we call the method directly
        PegDownProcessor p = getProcessor(conf)
        String result = ''
        // we have to lock, because pegdown is not thread-safe<
        try {
            processorLock.lock()
            result = p.markdownToHtml((String) text, new MarkdownToHtmlLinkRenderer())
        } finally {
            processorLock.unlock()
        }
        result
    }

    /**
     * Converts the provided HTML back to Markdown
     *
     * <p>By default this method uses the shared configuration.  However, the default configuration can
     * be overridden by passing in a map or map-like object as the second argument.  With a custom
     * configuration, a new Remark is created <strong>every call to this method!</strong></p>
     *
     * @param text HTML-formatted text
     * @param conf If provided, creates a custom remark with unique settings just for this instance
     * @return Markdown-formatted text
     */
    @SuppressWarnings('DuplicateStringLiteral')
    String htmlToMarkdown(String text, Map conf = [:]) {
        // lazily created, so we call the method directly
        Remark r = getRemark(conf)
        String customBaseUri = baseUri ?: ''
        if (customBaseUri.size() > 0 && customBaseUri[-1] != '/') {
            customBaseUri += '/'
        }
        r.convertFragment(text, customBaseUri)
    }

    /**
     * Utility method to strip untrusted HTML from markdown input.
     *
     * <p>Works by simply running the text through pegdown and back through remark.</p>
     *
     * <p>By default this method uses the shared configuration.  However, the default configuration can
     * be overridden by passing in a map or map-like object as the second argument.  With a custom
     * configuration, new processing engines are created <strong>every call to this method!</strong></p>
     *
     * @param text Markdown-formatted text
     * @param conf If provided, creates custom remark and pegdown with unique settings for this instance
     * @return Sanitized Markdown-formatted text
     */
    String sanitize(String text, Map conf = [:]) {
        htmlToMarkdown(markdownToHtml(text, conf), conf)
    }

    /**
     * Returns or creates the Pegdown processor instance used for conversion
     * @param conf Optional configuration Map to create a custom processor
     * @return PegdownProcessor instance
     */
    PegDownProcessor getProcessor(Map conf = [:]) {
        PegDownProcessor result
        if (conf) {
            Map opts = getConfigurations(conf)
            result = new PegDownProcessor((int) opts.pegdownExtensions, (long) opts.parsingTimeout)
        } else {
            if (processor == null) {
                setupConfigurations()
                processor = new PegDownProcessor(pegdownExtensions, (long) (conf.parsingTimeout ?: 2000L))
            }
            result = processor
        }
        result
    }

    /**
     * Returns or creates the Remark instance used for conversion
     * @param conf Optional configuration Map to create a custom remark.
     * @return Remark instance
     */
    Remark getRemark(Map conf = [:]) {
        Remark result
        if (conf) {
            Map opts = getConfigurations(conf)
            result = new Remark((Options) opts.remarkOptions)
        } else {
            if (remark == null) {
                setupConfigurations()
                remark = new Remark(remarkOptions)
            }
            result = remark
        }
        result
    }

    //------------------------------------------------------------------------

    // sets up the default configuration for markdown and pegdown
    @SuppressWarnings('AssignmentToStaticFieldFromInstanceMethod')
    private void setupConfigurations() {
        if (remarkOptions == null) {
            Map opts = getConfigurations(new ConfigObject())
            remarkOptions = (Options) opts.remarkOptions
            pegdownExtensions = (int) opts.pegdownExtensions
            baseUri = opts.baseUri
        }
    }

    // this is where the configuration actually happens
    // conf can be set via any map-like object
    @SuppressWarnings('Instanceof')
    private static Map getConfigurations(Map conf) {
        Map result = [
            remarkOptions    : Options.pegdownBase(),
            pegdownExtensions: 0,
            baseUri          : null,
            parsingTimeout   : conf.parsingTimeout ?: 2000L]

        if (conf) {
            def all = conf.all as Boolean
            def pdExtension = {
                result.pegdownExtensions = result.pegdownExtensions | it
            }
            def enableIf = { test, rm, pd ->
                if (all || test) {
                    result.remarkOptions[rm] = true
                    pdExtension(pd)
                }
            }
            enableIf(conf.abbreviations, 'abbreviations', ABBREVIATIONS)
            enableIf(conf.hardwraps, 'hardwraps', HARDWRAPS)
            enableIf(conf.definitionLists, 'definitionLists', DEFINITIONS)
            enableIf(conf.autoLinks, 'autoLinks', AUTOLINKS)
            enableIf(conf.smartQuotes, 'reverseSmartQuotes', QUOTES)
            enableIf(conf.smartPunctuation, 'reverseSmartPunctuation', SMARTS)
            enableIf(conf.smart, 'reverseAllSmarts', SMARTYPANTS)

            if (all || conf.fencedCodeBlocks) {
                result.remarkOptions.fencedCodeBlocks = Options.FencedCodeBlocks.ENABLED_TILDE
                pdExtension(FENCED_CODE_BLOCKS)
            }

            if (conf.removeHtml) {
                result.remarkOptions.tables = Options.Tables.REMOVE
                pdExtension(SUPPRESS_ALL_HTML)
            }

            if (all || conf.tables) {
                result.remarkOptions.tables = Options.Tables.MULTI_MARKDOWN
                pdExtension(TABLES)
            } else if (conf.removeTables) {
                result.remarkOptions.tables = Options.Tables.REMOVE
            }

            if (conf.customizeRemark) {
                def opts = conf.customizeRemark(result.remarkOptions)
                if (opts instanceof Options) {
                    result.remarkOptions = opts
                }
            }

            if (conf.customizePegdown) {
                def exts = conf.customizePegdown(result.pegdownExtensions)
                if (exts instanceof Integer) {
                    result.pegdownExtensions = (int) exts
                }
            }

            // only disable baseUri if it is explicitly set to false
            //noinspection GroovyPointlessBoolean
            if (conf.baseUri != false && conf.baseUri) {
                result.baseUri = conf.baseUri
            }
        }

        result
    }
}