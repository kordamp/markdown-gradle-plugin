/*
 * Copyright 2012-2014 the original author or authors.
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
package org.kordamp.gradle.markdown

import org.pegdown.LinkRenderer
import org.pegdown.ast.AutoLinkNode
import org.pegdown.ast.ExpLinkNode
import org.pegdown.ast.RefLinkNode
import org.pegdown.ast.WikiLinkNode

import static org.pegdown.LinkRenderer.Rendering

/**
 * @author Ed MacDonald
 */
class MarkdownToHtmlLinkRenderer extends LinkRenderer {
    private final Conversion conversion = Conversion.MARKDOWN

    @Override
    Rendering render(AutoLinkNode node) {
        adjustHref(super.render(node))
    }

    @Override
    Rendering render(ExpLinkNode node, String text) {
        adjustHref(super.render(node, text))
    }

    @Override
    Rendering render(RefLinkNode node, String url, String title, String text) {
        adjustHref(super.render(node, url, title, text))
    }

    @Override
    Rendering render(WikiLinkNode node) {
        adjustHref(super.render(node))
    }

    // Assumes each extension starts with "."
    private Rendering adjustHref( Rendering rendering ) {
        if(conversion.extensions().size() > 0) {
            def regex =  conversion.extensions().join('|\\')
            regex = '(\\' + regex + ')\$'
            rendering.href = rendering.href.replaceFirst(regex, conversion.targetExtension())
        }
        rendering
    }
}
