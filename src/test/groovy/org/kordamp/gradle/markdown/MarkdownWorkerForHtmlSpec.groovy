package org.kordamp.gradle.markdown

import spock.lang.Specification

class MarkdownWorkerForHtmlSpec extends Specification {
    File sourceDir = new File('build/resources/test/src/html')
    File outputDir = new File('build/gen-markdown')

    def setup() {
        if (!outputDir.exists()) {
            outputDir.mkdir()
        }
    }

    def "Renders simple HTML"() {
        when:
        MarkdownWorker worker = new MarkdownWorkerImpl()
        worker.process(Conversion.HTML, sourceDir, outputDir, [:])

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
