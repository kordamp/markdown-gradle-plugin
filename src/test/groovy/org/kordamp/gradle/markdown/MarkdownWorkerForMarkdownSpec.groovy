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
