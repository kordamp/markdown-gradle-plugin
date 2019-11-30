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

import org.pegdown.ast.AutoLinkNode
import spock.lang.Specification

/**
 * @author Patrick Reimers
 * @author Markus Schlichting
 */
class MarkdownToHtmlLinkRendererSpec extends Specification {

    def "AutoLinkNode suffix .md is replaced with .html"() {
        when:
        def renderer = new MarkdownToHtmlLinkRenderer();
        def result = renderer.render(new AutoLinkNode("test.md"))

        then:
        result.href.equals("test.html")
    }

    def "AutoLinkNode suffix .markdown is replaced with .html"() {
        when:
        def renderer = new MarkdownToHtmlLinkRenderer();
        def result = renderer.render(new AutoLinkNode("test.markdown"))

        then:
        result.href.equals("test.html")
    }

    def "AutoLinkNode suffix is replaced when an anchor is present"() {
        when:
        def renderer = new MarkdownToHtmlLinkRenderer()
        def result = renderer.render(new AutoLinkNode("test.md#anchor"))

        then:
        result.href.equals("test.html#anchor")
    }

    def "AutoLinkNode suffix .mde is not replaced"() {
        when:
        def renderer = new MarkdownToHtmlLinkRenderer()
        def result = renderer.render(new AutoLinkNode("test.mde"))

        then:
        result.href.equals("test.mde")
    }
}
