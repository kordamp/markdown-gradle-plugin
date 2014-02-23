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

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author Andres Almiray
 */
class MarkdownPlugin implements Plugin<Project> {
    private static final String DOCUMENTATION = 'Documentation'

    void apply(Project project) {
        project.task('markdownToHtml',
            type: MarkdownToHtmlTask,
            group: DOCUMENTATION,
            description: 'Converts Markdown sources into HTML')

        project.task('htmlToMarkdown',
            type: HtmlToMarkdownTask,
            group: DOCUMENTATION,
            description: 'Converts HTML into Markdown')
    }
}
