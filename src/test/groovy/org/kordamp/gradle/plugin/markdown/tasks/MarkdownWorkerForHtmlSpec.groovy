/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2013-2021 Andres Almiray.
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

import spock.lang.Specification

class MarkdownWorkerForHtmlSpec extends Specification {
    File sourceDir = new File('build/resources/test/src/html')
    File outputDir = new File('build/gen-markdown')

    def setup() {
        if (!outputDir.exists()) {
            outputDir.mkdir()
        }
    }

    def "Renders simple MD from HTML"() {
        when:
        MarkdownWorker worker = new MarkdownWorkerImpl()
        worker.process(Conversion.HTML, [sourceDir: sourceDir, outputDir: outputDir], [:])

        then:
        !outputDir.list().toList().isEmpty()
        outputDir.list().toList().contains('sample.md')

        File sampleOutput = new File(outputDir, 'sample.md')
        sampleOutput.exists()
        sampleOutput.length() > 0

        sampleOutput.text.contains('''
            ## Hello World ##

            This is a paragraph

             *  item1
             *  item2

            Another paragraph

                code snippet

            **Markdown** is *cool*!

            this is a [link][]

            the following is also a link [http://acme.com][http_acme.com]


            [link]: http://gradle.org
            [http_acme.com]: http://acme.com
         '''.stripIndent(12).trim())
    }
}
