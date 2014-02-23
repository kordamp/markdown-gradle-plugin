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

import spock.lang.Specification

class MarkdownWorkerForMarkdownSpec extends Specification {
    File sourceDir = new File('build/resources/test/src/markdown')
    File outputDir = new File('build/gen-html')

    def setup() {
        if (!outputDir.exists()) {
            outputDir.mkdir()
        }
    }

    def "Renders simple HTML"() {
        when:
        MarkdownWorker worker = new MarkdownWorkerImpl()
        worker.process(Conversion.MARKDOWN, sourceDir, outputDir, [:])

        then:
        !outputDir.list().toList().isEmpty()
        outputDir.list().toList().contains('sample.html')

        File sampleOutput = new File(outputDir, 'sample.html')
        sampleOutput.exists()
        sampleOutput.length() > 0

        !sampleOutput.text.contains('href="http://acme.com"')
    }

    def "Renders simple HTML with custom configuration"() {
        when:
        Map configuration = [
            autoLinks: true
        ]
        MarkdownWorker worker = new MarkdownWorkerImpl()
        worker.process(Conversion.MARKDOWN, sourceDir, outputDir, configuration)

        then:
        !outputDir.list().toList().isEmpty()
        outputDir.list().toList().contains('sample.html')

        File sampleOutput = new File(outputDir, 'sample.html')
        sampleOutput.exists()
        sampleOutput.length() > 0

        sampleOutput.text.contains('href="http://acme.com"')
    }
}
