package org.kordamp.gradle.markdown

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class MarkdownPluginSpec extends Specification {
    Project project

    def setup() {
        project = ProjectBuilder.builder().build()
    }

    def "Applies plugin and checks default setup"() {
        expect:
        project.tasks.findByName('markdown') == null

        when:
        project.apply plugin: MarkdownPlugin

        then:
        Task markdownToHtmlTask = project.tasks.findByName('markdownToHtml')
        markdownToHtmlTask != null
        markdownToHtmlTask.group == 'Documentation'
        markdownToHtmlTask.sourceDir == project.file('src/markdown')
        markdownToHtmlTask.outputDir == new File(project.buildDir, 'gen-html')

        Task htmlToMarkdownTask = project.tasks.findByName('htmlToMarkdown')
        htmlToMarkdownTask != null
        htmlToMarkdownTask.group == 'Documentation'
        htmlToMarkdownTask.sourceDir == project.file('src/html')
        htmlToMarkdownTask.outputDir == new File(project.buildDir, 'gen-markdown')
    }
}
