/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2013-2022 Andres Almiray.
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
package org.kordamp.gradle.plugin.markdown

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
